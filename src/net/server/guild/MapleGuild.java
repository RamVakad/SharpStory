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
package net.server.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import client.MapleCharacter;
import client.MapleClient;
import java.util.Calendar;
import java.util.LinkedList;
import tools.DatabaseConnection;
import net.MaplePacket;
import net.server.Channel;
import net.server.Server;
import tools.MaplePacketCreator;
import tools.PrintError;

public class MapleGuild {

    public final static int CREATE_GUILD_COST = 2000000000;
    public final static int CHANGE_EMBLEM_COST = 2000000000;
    public final static int INCREASE_GUILD_COST = 2000000000;

    private enum BCOp {

        NONE, DISBAND, EMBELMCHANGE
    }
    private List<MapleGuildCharacter> members;
    private String rankTitles[] = new String[5]; // 1 = master, 2 = jr, 5 = lowest member
    private String name, notice;
    private int id, gp, logo, logoColor, leader, capacity, logoBG, logoBGColor, signature, allianceId;
    private byte world;
    private Map<Byte, List<Integer>> notifications = new LinkedHashMap<Byte, List<Integer>>();
    private boolean bDirty = true;

    public MapleGuild(MapleGuildCharacter initiator) {
        int guildid = initiator.getGuildId();
        world = initiator.getWorld();
        members = new ArrayList<MapleGuildCharacter>();
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = " + guildid);
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {
                id = -1;
                ps.close();
                rs.close();
                return;
            }
            id = guildid;
            name = rs.getString("name");
            gp = rs.getInt("GP");
            logo = rs.getInt("logo");
            logoColor = rs.getInt("logoColor");
            logoBG = rs.getInt("logoBG");
            logoBGColor = rs.getInt("logoBGColor");
            capacity = rs.getInt("capacity");
            for (int i = 1; i <= 5; i++) {
                rankTitles[i - 1] = rs.getString("rank" + i + "title");
            }
            leader = rs.getInt("leader");
            notice = rs.getString("notice");
            signature = rs.getInt("signature");
            allianceId = rs.getInt("allianceId");
            ps.close();
            rs.close();
            ps = con.prepareStatement("SELECT id, name, level, job, guildrank, allianceRank FROM characters WHERE guildid = ? ORDER BY guildrank ASC, name ASC");
            ps.setInt(1, guildid);
            rs = ps.executeQuery();
            if (!rs.first()) {
                rs.close();
                ps.close();
                return;
            }
            do {
                members.add(new MapleGuildCharacter(rs.getInt("id"), rs.getInt("level"), rs.getString("name"), (byte) -1, world, rs.getInt("job"), rs.getInt("guildrank"), guildid, false, rs.getInt("allianceRank")));
            } while (rs.next());
            setOnline(initiator.getId(), true, initiator.getChannel());
            ps.close();
            rs.close();
        } catch (SQLException se) {
            System.out.println("unable to read guild information from sql" + se);
            return;
        }
    }

    public void buildNotifications() {
        if (!bDirty) {
            return;
        }
        Set<Byte> chs = Server.getInstance().getChannelServer(world);
        if (notifications.keySet().size() != chs.size()) {
            notifications.clear();
            for (Byte ch : chs) {
                notifications.put(ch, new LinkedList<Integer>());
            }
        } else {
            for (List<Integer> l : notifications.values()) {
                l.clear();
            }
        }
        synchronized (members) {
            for (MapleGuildCharacter mgc : members) {
                if (!mgc.isOnline()) {
                    continue;
                }
                List<Integer> ch = notifications.get(mgc.getChannel());
                if (ch != null) {
                    ch.add(mgc.getId());
                }
                //Unable to connect to Channel... error was here
            }
        }
        bDirty = false;
    }

    public void writeToDB(boolean bDisband) {
        try {
            Connection con = DatabaseConnection.getConnection();
            if (!bDisband) {
                StringBuilder builder = new StringBuilder();
                builder.append("UPDATE guilds SET GP = ?, logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ?, ");
                for (int i = 0; i < 5; i++) {
                    builder.append("rank").append(i + 1).append("title = ?, ");
                }
                builder.append("capacity = ?, notice = ? WHERE guildid = ?");
                PreparedStatement ps = con.prepareStatement(builder.toString());
                ps.setInt(1, gp);
                ps.setInt(2, logo);
                ps.setInt(3, logoColor);
                ps.setInt(4, logoBG);
                ps.setInt(5, logoBGColor);
                for (int i = 6; i < 11; i++) {
                    ps.setString(i, rankTitles[i - 6]);
                }
                ps.setInt(11, capacity);
                ps.setString(12, notice);
                ps.setInt(13, this.id);
                ps.execute();
                ps.close();
            } else {
                this.checkAlliances();
                PreparedStatement ps = con.prepareStatement("UPDATE characters SET guildid = 0, guildrank = 5 WHERE guildid = ?");
                ps.setInt(1, this.id);
                ps.execute();
                ps.close();
                ps = con.prepareStatement("DELETE FROM guilds WHERE guildid = ?");
                ps.setInt(1, this.id);
                ps.execute();
                ps.close();
                this.broadcast(MaplePacketCreator.guildDisband(this.id));
            }
        } catch (SQLException se) {
        }
    }

    public void checkAlliances() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guild where allianceId = ?");
            ps.setInt(1, this.allianceId);
            ResultSet rs = ps.executeQuery();

            int allianceGuilds = 0;
            while (rs.next()) {
                allianceGuilds++;
            }

            if (allianceGuilds <= 2) {
                this.disbandAllianceExt(this.allianceId);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Exception at checkAlliances():" + e);
        }
    }

    public int getId() {
        return id;
    }

    public int getLeaderId() {
        return leader;
    }

    public int getGP() {
        return gp;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int l) {
        logo = l;
    }

    public int getLogoColor() {
        return logoColor;
    }

    public void setLogoColor(int c) {
        logoColor = c;
    }

    public int getLogoBG() {
        return logoBG;
    }

    public void setLogoBG(int bg) {
        logoBG = bg;
    }

    public int getLogoBGColor() {
        return logoBGColor;
    }

    public void setLogoBGColor(int c) {
        logoBGColor = c;
    }

    public String getNotice() {
        if (notice == null) {
            return "";
        }
        return notice;
    }

    public String getName() {
        return name;
    }

    public java.util.Collection<MapleGuildCharacter> getMembers() {
        return java.util.Collections.unmodifiableCollection(members);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSignature() {
        return signature;
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, BCOp.NONE);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, BCOp.NONE);
    }

    public void broadcast(MaplePacket packet, int exceptionId, BCOp bcop) {
        synchronized (notifications) {
            if (bDirty) {
                buildNotifications();
            }
            try {
                for (Byte b : Server.getInstance().getChannelServer(world)) {
                    if (notifications.get(b).size() > 0) {
                        if (bcop == BCOp.DISBAND) {
                            Server.getInstance().getWorld(world).setGuildAndRank(notifications.get(b), 0, 5, exceptionId);
                        } else if (bcop == BCOp.EMBELMCHANGE) {
                            Server.getInstance().getWorld(world).changeEmblem(this.id, notifications.get(b), new MapleGuildSummary(this));
                        } else {
                            Server.getInstance().getWorld(world).sendPacket(notifications.get(b), packet, exceptionId);
                        }
                    }
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.getName()).append(" guild failed to broadcast!.\r\n");
                sb.append("----------------------------------------------------------------------------\r\n");
                sb.append(tools.StringUtil.stackTraceToString(e)).append("\r\n");
                sb.append("----------------------------------------------------------------------------\r\n");
                PrintError.print(PrintError.GUILD_BROADCAST, sb.toString());
                System.out.println("Failed to contact channel(s) for broadcast.");
            }
        }
    }

    public void guildMessage(MaplePacket serverNotice) {
        for (MapleGuildCharacter mgc : members) {
            for (Channel cs : Server.getInstance().getChannelsFromWorld(world)) {
                if (cs.getPlayerStorage().getCharacterById(mgc.getId()) != null) {
                    cs.getPlayerStorage().getCharacterById(mgc.getId()).getClient().getSession().write(serverNotice);
                    break;
                }
            }
        }
    }

    public final void setOnline(int cid, boolean online, byte channel) {
        boolean bBroadcast = true;
        for (MapleGuildCharacter mgc : members) {
            if (mgc.getId() == cid) {
                if (mgc.isOnline() && online) {
                    bBroadcast = false;
                }
                mgc.setOnline(online);
                mgc.setChannel(channel);
                break;
            }
        }
        if (bBroadcast) {
            this.broadcast(MaplePacketCreator.guildMemberOnline(id, cid, online), cid);
        }
        bDirty = true;
    }

    public void guildChat(String name, int cid, String msg) {
        this.broadcast(MaplePacketCreator.multiChat(name, msg, 2), cid);
    }

    public String getRankTitle(int rank) {
        return rankTitles[rank - 1];
    }

    public static int createGuild(int leaderId, String name) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT guildid FROM guilds WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                ps.close();
                rs.close();
                return 0;
            }
            ps.close();
            rs.close();
            ps = con.prepareStatement("INSERT INTO guilds (`leader`, `name`, `signature`) VALUES (?, ?, ?)");
            ps.setInt(1, leaderId);
            ps.setString(2, name);
            ps.setInt(3, (int) System.currentTimeMillis());
            ps.execute();
            ps.close();
            ps = con.prepareStatement("SELECT guildid FROM guilds WHERE leader = ?");
            ps.setInt(1, leaderId);
            rs = ps.executeQuery();
            rs.first();
            int guildid = rs.getInt("guildid");
            rs.close();
            ps.close();
            return guildid;
        } catch (Exception e) {
            return 0;
        }
    }

    public int addGuildMember(MapleGuildCharacter mgc) {
        synchronized (members) {
            if (members.size() >= capacity) {
                return 0;
            }
            for (int i = members.size() - 1; i >= 0; i--) {
                if (members.get(i).getGuildRank() < 5 || members.get(i).getName().compareTo(mgc.getName()) < 0) {
                    members.add(i + 1, mgc);
                    bDirty = true;
                    break;
                }
            }
        }
        this.broadcast(MaplePacketCreator.newGuildMember(mgc));
        return 1;
    }

    public void leaveGuild(MapleGuildCharacter mgc) {
        this.broadcast(MaplePacketCreator.memberLeft(mgc, false));
        synchronized (members) {
            members.remove(mgc);
            bDirty = true;
        }
    }

    public void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        synchronized (members) {
            java.util.Iterator<MapleGuildCharacter> itr = members.iterator();
            MapleGuildCharacter mgc;
            while (itr.hasNext()) {
                mgc = itr.next();
                if (mgc.getId() == cid && initiator.getGuildRank() < mgc.getGuildRank()) {
                    this.broadcast(MaplePacketCreator.memberLeft(mgc, true));
                    itr.remove();
                    bDirty = true;
                    try {
                        if (mgc.isOnline()) {
                            Server.getInstance().getWorld(mgc.getWorld()).setGuildAndRank(cid, 0, 5);
                        } else {
                            try {
                                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`) VALUES (?, ?, ?, ?)");
                                ps.setString(1, mgc.getName());
                                ps.setString(2, initiator.getName());
                                ps.setString(3, "You have been expelled from the guild.");
                                ps.setLong(4, System.currentTimeMillis());
                                ps.executeUpdate();
                                ps.close();
                            } catch (SQLException e) {
                                System.out.println("expelMember - MapleGuild " + e);
                            }
                            Server.getInstance().getWorld(mgc.getWorld()).setOfflineGuildStatus((short) 0, (byte) 5, cid);
                        }
                    } catch (Exception re) {
                        re.printStackTrace();
                        return;
                    }
                    return;
                }
            }
            System.out.println("Unable to find member with name " + name + " and id " + cid);
        }
    }

    public void changeRank(int cid, int newRank) {
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.getId()) {
                try {
                    if (mgc.isOnline()) {
                        Server.getInstance().getWorld(mgc.getWorld()).setGuildAndRank(cid, this.id, newRank);
                    } else {
                        Server.getInstance().getWorld(mgc.getWorld()).setOfflineGuildStatus((short) this.id, (byte) newRank, cid);
                    }
                } catch (Exception re) {
                    re.printStackTrace();
                    return;
                }
                mgc.setGuildRank(newRank);
                this.broadcast(MaplePacketCreator.changeRank(mgc));
                return;
            }
        }
    }

    public void setGuildNotice(String notice) {
        this.notice = notice;
        writeToDB(false);
        this.broadcast(MaplePacketCreator.guildNotice(this.id, notice));
    }

    public void memberLevelJobUpdate(MapleGuildCharacter mgc) {
        for (MapleGuildCharacter member : members) {
            if (mgc.equals(member)) {
                member.setJobId(mgc.getJobId());
                member.setLevel(mgc.getLevel());
                this.broadcast(MaplePacketCreator.guildMemberLevelJobUpdate(mgc));
                break;
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MapleGuildCharacter)) {
            return false;
        }
        MapleGuildCharacter o = (MapleGuildCharacter) other;
        return (o.getId() == id && o.getName().equals(name));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + this.id;
        return hash;
    }

    public void changeRankTitle(String[] ranks) {
        for (int i = 0; i < 5; i++) {
            rankTitles[i] = ranks[i];
        }
        this.broadcast(MaplePacketCreator.rankTitleChange(this.id, ranks));
        this.writeToDB(false);
    }

    public void disbandGuild() {
        this.writeToDB(true);
        this.broadcast(null, -1, BCOp.DISBAND);
    }

    public void setGuildEmblem(short bg, byte bgcolor, short logo, byte logocolor) {
        this.logoBG = bg;
        this.logoBGColor = bgcolor;
        this.logo = logo;
        this.logoColor = logocolor;
        this.writeToDB(false);
        this.broadcast(null, -1, BCOp.EMBELMCHANGE);
    }

    public MapleGuildCharacter getMGC(int cid) {
        for (MapleGuildCharacter mgc : members) {
            if (mgc.getId() == cid) {
                return mgc;
            }
        }
        return null;
    }

    public boolean increaseCapacity() {
        if (capacity > 99) {
            return false;
        }
        capacity += 5;
        this.writeToDB(false);
        this.broadcast(MaplePacketCreator.guildCapacityChange(this.id, this.capacity));
        return true;
    }

    public void gainGP(int amount) {
        this.gp += amount;
        this.writeToDB(false);
        this.guildMessage(MaplePacketCreator.updateGP(this.id, this.gp));
        for (MapleGuildCharacter mgc : members) {
            for (Channel cs : Server.getInstance().getChannelsFromWorld(world)) {
                if (cs.getPlayerStorage().getCharacterById(mgc.getId()) != null) {
                    cs.getPlayerStorage().getCharacterById(mgc.getId()).dropMessage(6, "[Guild]:: GP Update - " + this.gp + " Total Point(s) || (" + amount + ") Guild Point(s) have been gained!");
                }
            }
        }

    }

    public static MapleGuildResponse sendInvite(MapleClient c, String targetName) {
        MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterByName(targetName);
        if (mc == null) {
            return MapleGuildResponse.NOT_IN_CHANNEL;
        }
        if (mc.getGuildId() > 0) {
            return MapleGuildResponse.ALREADY_IN_GUILD;
        }
        mc.getClient().getSession().write(MaplePacketCreator.guildInvite(c.getPlayer().getGuildId(), c.getPlayer().getName()));
        return null;
    }

    public static void displayGuildRanks(MapleClient c, int npcid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `name`, `GP`, `logoBG`, `logoBGColor`, " + "`logo`, `logoColor` FROM guilds ORDER BY `GP` DESC LIMIT 50");
            ResultSet rs = ps.executeQuery();
            c.getSession().write(MaplePacketCreator.showGuildRanks(npcid, rs));
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("failed to display guild ranks. " + e);
        }
    }

    public int getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(int aid) {
        this.allianceId = aid;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE guilds SET allianceId = ? WHERE guildid = ?");
            ps.setInt(1, aid);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
        }
    }

    public int getIncreaseGuildCost() {
        return INCREASE_GUILD_COST;
    }

    public void dropMessage(String x) {
        for (MapleGuildCharacter mgc : members) {
            for (Channel cs : Server.getInstance().getChannelsFromWorld(world)) {
                if (cs.getPlayerStorage().getCharacterById(mgc.getId()) != null) {
                    cs.getPlayerStorage().getCharacterById(mgc.getId()).dropMessage(6, x);
                }
            }
        }
    }

    public int getHideoutCost(int hqid) {
        int cost = 9999;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from guildhq where hqid = ?");
            ps.setInt(1, hqid);
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                cost = rs.getInt("cost");
            }
            rs.close();
            ps.close();
            return cost;
        } catch (Exception e) {
            System.out.println("GETCost Exception -" + e);
        }
        return cost;
    }

    public String leaseHideout(int hqid) {
        try {
            if (getHideout() == 0 && isAvaliable(hqid)) {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE guildhq set guildid = ?, expirestamp = ? WHERE hqid = ?");
                ps.setInt(1, id);
                ps.setLong(2, (Calendar.getInstance().getTimeInMillis() + (604800000)));
                ps.setInt(3, hqid);
                ps.executeUpdate();
                ps.close();
                for (MapleGuildCharacter mgc : members) {
                    MapleCharacter.sendNoteStat(MapleCharacter.getNameById(mgc.getId()), "The guild leader has baught a headquarter for the guild. You should check it out by using command @guildhq.", "Sharp", (byte) 0);
                }
                return "#eThank you for renting your guild hideout! It will expire in one week!";
            } else {
                return "#eYou can't lease a hideout either because your guild already has one or you are trying to lease the same hideout that you leased before!";
            }
        } catch (Exception e) {
        }
        return "#eThank you for renting your guild hideout! It will expire in one week!";
    }

    public static String getAvaliableHQS() {
        String x = "";
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from guildhq");
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                int hqid = rs.getInt("hqid");
                if (isAvaliable(hqid)) {
                    if (hqid >= 910000013 && hqid <= 910000022) {
                        x += "#L" + hqid + "##rFree Market " + (hqid - 910000000) + "#k - #b" + rs.getInt("cost") + " Guild Points #k- #d7 Days\r\n";
                    } else if (hqid == 910050000) {
                        x += "#r#L" + hqid + "#God Quarters#k - #b " + rs.getInt("cost") + " Guild Points\r\n";
                    } else {
                        x += "#r#L" + hqid + "#King Quarters#k - #b " + rs.getInt("cost") + " Guild Points\r\n";
                    }
                }
            }
            rs.close();
            ps.close();
            return x;
        } catch (Exception e) {
            System.out.println("Ohlol " + e);
        }
        return x;
    }

    public static boolean isAvaliable(int hqid) {
        boolean avaliable = false;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from guildhq where hqid = ?");
            ps.setInt(1, hqid);
            ResultSet rs = ps.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                if (Calendar.getInstance().getTimeInMillis() >= rs.getLong("expirestamp")) {
                    avaliable = true;
                }
            }
            rs.close();
            ps.close();
            return avaliable;
        } catch (Exception e) {
            System.out.println("Exception isAvaliable -" + e);
        }
        return avaliable;
    }

    public int getHideout() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from guildhq where guildid = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            int hq = 0;
            while (rs.next()) {
                if (!isAvaliable(rs.getInt("hqid"))) {
                    hq = rs.getInt("hqid");
                }
            }
            rs.close();
            ps.close();
            return hq;
        } catch (Exception e) {
            System.out.println("gethideout Error" + e);
        }
        return 0;
    }

    public void disbandAllianceExt(int allianceId) {
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE guilds SET allianceId = ? where allianceId = ?");
            ps.setInt(1, allianceId);
            ps.setInt(2, 0);
            ps.execute();
            ps.close();

            ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `alliance` WHERE id = ?");
            ps.setInt(1, allianceId);
            ps.executeUpdate();
            ps.close();
            Server.getInstance().allianceMessage(this.getAllianceId(), MaplePacketCreator.disbandAlliance(allianceId), -1, -1);
            Server.getInstance().disbandAlliance(allianceId);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }
}