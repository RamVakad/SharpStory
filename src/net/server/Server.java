/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server;

import client.MapleCharacter;
import client.SkillFactory;
import gm.GMPacketCreator;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import net.MaplePacket;
import tools.DatabaseConnection;
import net.MapleServerHandler;
import net.PacketProcessor;
import net.mina.MapleCodecFactory;
import net.server.guild.MapleAlliance;
import net.server.guild.MapleGuild;
import net.server.guild.MapleGuildCharacter;
import gm.server.GMServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import server.TimerManager;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import server.CashShop.CashItemFactory;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;
import tools.Pair;

public class Server implements Runnable {

    private IoAcceptor acceptor;
    private List<Map<Byte, String>> channels = new LinkedList<Map<Byte, String>>();
    private List<World> worlds = new ArrayList<World>();
    private Properties subnetInfo = new Properties();
    private static Server instance = null;
    private List<Pair<Byte, String>> worldRecommendedList = new LinkedList<Pair<Byte, String>>();
    private Map<Integer, MapleGuild> guilds = new LinkedHashMap<Integer, MapleGuild>();
    private PlayerBuffStorage buffStorage = new PlayerBuffStorage();
    private Map<Integer, MapleAlliance> alliances = new LinkedHashMap<Integer, MapleAlliance>();
    private boolean online = false;
    public final Object SAVEQUEUE = new Object();
    public final Object DISCONNECTQUEUE = new Object();
    public final Object AUCTIONQUEUE = new Object();
    public final Object MACLISTQUEUE = new Object();
    private Calendar startTime;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public boolean isOnline() {
        return online;
    }

    public List<Pair<Byte, String>> worldRecommendedList() {
        return worldRecommendedList;
    }

    public void removeChannel(byte worldid, byte channel) {
        channels.remove(channel);
        World world = worlds.get(worldid);
        if (world != null) {
            world.removeChannel(channel);
        }
    }

    public Channel getChannel(byte world, byte channel) {
        return worlds.get(world).getChannel(channel);
    }

    public List<Channel> getChannelsFromWorld(byte world) {
        return worlds.get(world).getChannels();
    }

    public List<Channel> getAllChannels() {
        List<Channel> channelz = new ArrayList<Channel>();
        for (World world : worlds) {
            for (Channel ch : world.getChannels()) {
                channelz.add(ch);
            }
        }

        return channelz;
    }

    public String getIP(byte world, byte channel) {
        return channels.get(world).get(channel);
    }

    @Override
    public void run() {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("sharp.ini"));
            System.setProperty("wzpath", p.getProperty("wzpath"));
            System.setProperty("gmserver", p.getProperty("gmserver"));
        } catch (Exception e) {
            System.out.println("Please start CreateINI.bat");
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(shutdown(true)));
        DatabaseConnection.getConnection();
        IoBuffer.setUseDirectBuffer(false); //explain ploxx
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", (IoFilter) new ProtocolCodecFilter(new MapleCodecFactory()));
        TimerManager tMan = TimerManager.getInstance();
        tMan.start();
        tMan.register(tMan.purge(), 300000);//Purging ftw...
        //tMan.register(new RankingWorker(), ServerConstants.RANKING_INTERVAL);

        try {
            for (byte i = 0; i < Byte.parseByte(p.getProperty("worlds")); i++) {
                System.out.println("Starting world " + i);
                World world = new World(i,
                        Byte.parseByte(p.getProperty("flag" + i)),
                        p.getProperty("eventmessage" + i),
                        Integer.parseInt(p.getProperty("exprate" + i)),
                        Byte.parseByte(p.getProperty("droprate" + i)),
                        Integer.parseInt(p.getProperty("mesorate" + i)),
                        Byte.parseByte(p.getProperty("bossdroprate" + i)));

                worldRecommendedList.add(new Pair<Byte, String>(i, p.getProperty("whyamirecommended" + i)));
                worlds.add(world);
                channels.add(new LinkedHashMap<Byte, String>());
                for (byte j = 0; j < Byte.parseByte(p.getProperty("channels" + i)); j++) {
                    byte channelid = (byte) (j + 1);
                    Channel channel = new Channel(i, channelid);
                    world.addChannel(channel);
                    channels.get(i).put(channelid, channel.getIP());
                }
                world.setServerMessage(p.getProperty("servermessage" + i));
                System.out.println("");
                System.out.println("Finished loading world " + i + "\r\n");
            }
        } catch (Exception e) {
            System.out.println("Error in sharp.ini, start CreateINI.bat to re-make the file.");
            e.printStackTrace();//For those who get errors
            System.exit(0);
        }

        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        acceptor.setHandler(new MapleServerHandler(PacketProcessor.getProcessor()));
        try {
            acceptor.bind(new InetSocketAddress(8484));
        } catch (IOException ex) {
        }
        System.out.println("Listening on port 8484\r\n");
        System.out.print("Loading server");

        ScheduledFuture<?> loldot = null;//rofl //i get this one, lolcats. yes. that was a lame pun. and you prolly won't get it either
        loldot = tMan.register(new Runnable() {

            @Override
            public void run() {
                System.out.print(".");
            }
        }, 500, 500);
        SkillFactory.loadAllSkills();
        CashItemFactory.getSpecialCashItems();//just load who cares o.o //I do.
        MapleItemInformationProvider.getInstance().getAllItems();
        if (System.getProperty("gmserver").equals("true")) {
            GMServer.getInstance();
        }
        loldot.cancel(true); //lolcats.cancel()...i wanna code the server in lolcode now.
        this.startTime = Calendar.getInstance();
        try {
            Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ps = c.prepareStatement("UPDATE accounts SET loggedin = 0");
            ps.executeUpdate();
            ps.close();

            ps = c.prepareStatement("UPDATE characters SET HasMerchant = 0");
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            System.out.println("Exception while running startup queries: " + e);
        }
        System.out.println("\r\nServer is now online.");
        System.out.println("");
        online = true;
    }

    public void shutdown() {
        TimerManager.getInstance().stop();
        acceptor.unbind();
        System.out.println("Server offline.");
        System.exit(0);// BOEIEND :D
    }

    public void restart() {
        TimerManager.getInstance().stop();
        acceptor.unbind();
        System.out.println("Server restarting.");
        try {
            Runtime.getRuntime().exec("reboot -n");
        } catch (IOException ex) {
            System.out.println("Exception restarting server with reboot -n command.");
        }
        System.exit(0);// BOEIEND :D
    }

    public static void main(String args[]) {
        Server.getInstance().run();
    }

    public Properties getSubnetInfo() {
        return subnetInfo;
    }

    public MapleAlliance getAlliance(int id) {
        synchronized (alliances) {
            if (alliances.containsKey(id)) {
                return alliances.get(id);
            }
            return null;
        }
    }

    public void addAlliance(int id, MapleAlliance alliance) {
        synchronized (alliances) {
            if (!alliances.containsKey(id)) {
                alliances.put(id, alliance);
            }
        }
    }

    public void disbandAlliance(int id) {
        synchronized (alliances) {
            MapleAlliance alliance = alliances.get(id);
            if (alliance != null) {
                for (Integer gid : alliance.getGuilds()) {
                    guilds.get(gid).setAllianceId(0);
                }
                alliances.remove(id);
            }
        }
    }

    public void allianceMessage(int id, MaplePacket packet, int exception, int guildex) {
        MapleAlliance alliance = alliances.get(id);
        if (alliance != null) {
            for (Integer gid : alliance.getGuilds()) {
                if (guildex == gid) {
                    continue;
                }
                MapleGuild guild = guilds.get(gid);
                if (guild != null) {
                    guild.broadcast(packet, exception);
                }
            }
        }
    }

    public boolean addGuildtoAlliance(int aId, int guildId) {
        MapleAlliance alliance = alliances.get(aId);
        if (alliance != null) {
            alliance.addGuild(guildId);
            return true;
        }
        return false;
    }

    public boolean removeGuildFromAlliance(int aId, int guildId) {
        MapleAlliance alliance = alliances.get(aId);
        if (alliance != null) {
            alliance.removeGuild(guildId);
            return true;
        }
        return false;
    }

    public boolean setAllianceRanks(int aId, String[] ranks) {
        MapleAlliance alliance = alliances.get(aId);
        if (alliance != null) {
            alliance.setRankTitle(ranks);
            return true;
        }
        return false;
    }

    public boolean setAllianceNotice(int aId, String notice) {
        MapleAlliance alliance = alliances.get(aId);
        if (alliance != null) {
            alliance.setNotice(notice);
            return true;
        }
        return false;
    }

    public boolean increaseAllianceCapacity(int aId, int inc) {
        MapleAlliance alliance = alliances.get(aId);
        if (alliance != null) {
            alliance.increaseCapacity(inc);
            return true;
        }
        return false;
    }

    public Set<Byte> getChannelServer(byte world) {
        return new HashSet<Byte>(channels.get(world).keySet());
    }

    public byte getHighestChannelId() {
        byte highest = 0;
        for (Byte channel : channels.get(0).keySet()) {
            if (channel != null && channel.intValue() > highest) {
                highest = channel.byteValue();
            }
        }
        return highest;
    }

    public int createGuild(int leaderId, String name) {
        return MapleGuild.createGuild(leaderId, name);
    }

    public MapleGuild getGuild(int id, MapleGuildCharacter mgc) {
        synchronized (guilds) {
            if (guilds.get(id) != null) {
                return guilds.get(id);
            }
            if (mgc == null) {
                return null;
            }
            MapleGuild g = new MapleGuild(mgc);
            if (g.getId() == -1) {
                return null;
            }
            guilds.put(id, g);
            return g;
        }
    }

    public void clearGuilds() {//remake
        synchronized (guilds) {
            guilds.clear();
        }
        //for (List<Channel> world : worlds.values()) {
        //reloadGuildCharacters();

    }

    public void setGuildMemberOnline(MapleGuildCharacter mgc, boolean bOnline, byte channel) {
        MapleGuild g = getGuild(mgc.getGuildId(), mgc);
        g.setOnline(mgc.getId(), bOnline, channel);
    }

    public int addGuildMember(MapleGuildCharacter mgc) {
        MapleGuild g = guilds.get(mgc.getGuildId());
        if (g != null) {
            return g.addGuildMember(mgc);
        }
        return 0;
    }

    public boolean setGuildAllianceId(int gId, int aId) {
        MapleGuild guild = guilds.get(gId);
        if (guild != null) {
            guild.setAllianceId(aId);
            return true;
        }
        return false;
    }

    public void leaveGuild(MapleGuildCharacter mgc) {
        MapleGuild g = guilds.get(mgc.getGuildId());
        if (g != null) {
            g.leaveGuild(mgc);
        }
    }

    public void guildChat(int gid, String name, int cid, String msg) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.guildChat(name, cid, msg);
        }
    }

    public void changeRank(int gid, int cid, int newRank) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.changeRank(cid, newRank);
        }
    }

    public void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        MapleGuild g = guilds.get(initiator.getGuildId());
        if (g != null) {
            g.expelMember(initiator, name, cid);
        }
    }

    public void setGuildNotice(int gid, String notice) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.setGuildNotice(notice);
        }
    }

    public void memberLevelJobUpdate(MapleGuildCharacter mgc) {
        MapleGuild g = guilds.get(mgc.getGuildId());
        if (g != null) {
            g.memberLevelJobUpdate(mgc);
        }
    }

    public void changeRankTitle(int gid, String[] ranks) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.changeRankTitle(ranks);
        }
    }

    public void setGuildEmblem(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.setGuildEmblem(bg, bgcolor, logo, logocolor);
        }
    }

    public void disbandGuild(int gid) {
        synchronized (guilds) {
            MapleGuild g = guilds.get(gid);
            g.disbandGuild();
            guilds.remove(gid);
        }
    }

    public boolean increaseGuildCapacity(int gid) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            return g.increaseCapacity();
        }
        return false;
    }

    public void gainGP(int gid, int amount) {
        MapleGuild g = guilds.get(gid);
        if (g != null) {
            g.gainGP(amount);
        }
    }

    public PlayerBuffStorage getPlayerBuffStorage() {
        return buffStorage;
    }

    public void deleteGuildCharacter(MapleGuildCharacter mgc) {
        setGuildMemberOnline(mgc, false, (byte) -1);
        if (mgc.getGuildRank() > 1) {
            leaveGuild(mgc);
        } else {
            disbandGuild(mgc.getGuildId());
        }
    }

    public void reloadGuildCharacters(byte world) {
        World worlda = getWorld(world);
        for (MapleCharacter mc : worlda.getPlayerStorage().getAllCharacters()) {
            if (mc.getGuildId() > 0) {
                setGuildMemberOnline(mc.getMGC(), true, worlda.getId());
                memberLevelJobUpdate(mc.getMGC());
            }
        }
        worlda.reloadGuildSummary();
    }

    public void broadcastMessage(byte world, MaplePacket packet) {
        for (Channel ch : getChannelsFromWorld(world)) {
            ch.broadcastPacket(packet);
        }
    }

    public World getWorld(int id) {
        return worlds.get(id);
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public void removePlayer(MapleCharacter chr) {
        for (World wrld : getWorlds()) {
            wrld.removePlayer(chr);
        }
    }

    public void gmChat(String message, String exclude) {
        GMServer server = GMServer.getInstance();
        server.broadcastInGame(MaplePacketCreator.serverNotice(6, message));
        server.broadcastOutGame(GMPacketCreator.chat(message), exclude);
    }

    public String getUptime() {
        String y = "";
        long uptime = Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis();
        uptime = uptime / 1000;
        int seconds = (int) (uptime % 60);
        int minutes = (int) (uptime % 3600) / 60;
        int hours = (int) (uptime / 3600);
        y += "The server has been online for ";
        if (hours > 0) {
            if (hours != 1) {
                y += hours + " hours,";
            } else {
                y += hours + " hour,";
            }
        }
        if (minutes > 0) {
            if (minutes != 1) {
                y += minutes + " minutes,";
            } else {
                y += minutes + " minute,";
            }
        }
        if (seconds > 0) {
            if (seconds != 1) {
                y += seconds + " seconds,";
            } else {
                y += seconds + " second,";
            }
        }
        return y.substring(0, y.length() - 1) + ".";
    }

    public final Runnable shutdown(final boolean restart) {//only once :D
        //This is fkin stupid, why would you make everything null when JVM will exit anyway?
        //idc about players not saving its their dam problem.
        return new Runnable() {

            @Override
            public void run() {
                if (restart) {
                    restart();
                } else {
                    shutdown();
                }
                System.out.println((restart ? "Restarting" : "Shutting down") + " the server!\r\n");
                for (World w : getWorlds()) {
                    w.shutdown();
                }
                for (World w : getWorlds()) {
                    while (w.getPlayerStorage().getAllCharacters().size() > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            System.err.println("FUCK MY LIFE"); // yes...fuk ur life
                        }
                    }
                }
                for (Channel ch : getAllChannels()) {
                    while (ch.getConnectedClients() > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            System.err.println("FUCK MY LIFE"); // so sad, crying over a game
                        }
                    }
                }

                TimerManager.getInstance().purge();
                TimerManager.getInstance().stop();

                for (Channel ch : getAllChannels()) {
                    while (!ch.finishedShutdown()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            System.err.println("FUCK MY LIFE"); // yes, we get it, you are one sad man.
                        }
                    }
                }
                worlds.clear();
                worlds = null;
                channels.clear();
                channels = null;
                worldRecommendedList.clear();
                worldRecommendedList = null;
                System.out.println("Worlds + Channels are offline.");
                acceptor.unbind();
                acceptor = null;
                if (!restart) {
                    System.exit(0);
                } else {
                    System.out.println("\r\nRestarting the server....\r\n");
                    try {
                        instance.finalize();//FUU I CAN AND IT'S FREE
                    } catch (Throwable ex) {
                    }
                    instance = null;
                    System.gc();
                    try {
                        Runtime.getRuntime().exec("reboot -n");// So much easier, set startup scripts
                    } catch (Exception e) {
                    }
                }
            }
        };
    }
}