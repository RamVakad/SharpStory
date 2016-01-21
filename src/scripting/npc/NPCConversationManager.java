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
package scripting.npc;

import client.AuctionSystemHandler;
import client.Equip;
import client.IItem;
import client.ISkill;
import client.ItemFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import constants.ExpTable;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventory;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleSkinColor;
import client.MapleStat;
import client.SkillFactory;
import constants.ItemConstants;
import tools.Randomizer;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import net.server.Channel;
import tools.DatabaseConnection;
import net.server.MapleParty;
import net.server.MaplePartyCharacter;
import net.server.Server;
import net.server.guild.MapleAlliance;
import net.server.guild.MapleGuild;
import scripting.AbstractPlayerInteraction;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleShopFactory;
import server.MapleStatEffect;
import server.events.gm.MapleEvent;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapFactory;
import server.partyquest.Pyramid;
import server.partyquest.Pyramid.PyramidMode;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;

/**
 *
 * @author Matze
 */
public class NPCConversationManager extends AbstractPlayerInteraction {

    private int npc;
    private String getText;

    public NPCConversationManager(MapleClient c, int npc) {
        super(c);
        this.npc = npc;
    }

    public int getNpc() {
        return npc;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(this);
    }

    public void sendNext(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
    }

    public void sendPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
    }

    public void sendNextPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
    }

    public void sendOk(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
    }

    public void sendYesNo(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
    }

    public void sendAcceptDecline(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
    }

    public void sendSimple(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
    }

    public void sendNext(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", speaker));
    }

    public void sendPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", speaker));
    }

    public void sendNextPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", speaker));
    }

    public void sendOk(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", speaker));
    }

    public void sendYesNo(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", speaker));
    }

    public void sendAcceptDecline(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", speaker));
    }

    public void sendSimple(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", speaker));
    }

    public void sendStyle(String text, int styles[]) {
        getClient().announce(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        getClient().announce(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
    }

    public void sendGetText(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalkText(npc, text, ""));
    }

    /*
     * 0 = ariant colliseum
     * 1 = Dojo
     * 2 = Carnival 1
     * 3 = Carnival 2
     * 4 = Ghost Ship PQ?
     * 5 = Pyramid PQ
     * 6 = Kerning Subway
     */
    public void sendDimensionalMirror(String text) {
        getClient().announce(MaplePacketCreator.getDimensionalMirror(text));
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return this.getText;
    }

    public int getJobId() {
        return getPlayer().getJob().getId();
    }

    public void startQuest(short id) {
        try {
            MapleQuest.getInstance(id).forceStart(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void completeQuest(short id) {
        try {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainMeso(int gain) {
        getPlayer().gainMeso(gain, true, false, true);
    }

    public void gainExp(int gain) {
        getPlayer().gainExp(gain, true, true);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public void showEffect(String effect) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(effect, 3));
    }

    public void playSound(String sound) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(sound, 4));
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor(MapleSkinColor.getById(color));
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public int itemQuantity(int itemid) {
        return getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid);
    }

    public int itemQuantityX10(int itemid) {
        return (getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid)) * 10;
    }

    public void displayGuildRanks() {
        MapleGuild.displayGuildRanks(getClient(), npc);
    }

    @Override
    public MapleParty getParty() {
        return getPlayer().getParty();
    }

    @Override
    public void resetMap(int mapid) {
        getClient().getChannelServer().getMapFactory().getMap(mapid).resetReactors();
    }

    public void gainCloseness(int closeness) {
        for (MaplePet pet : getPlayer().getPets()) {
            if (pet.getCloseness() > 30000) {
                pet.setCloseness(30000);
                return;
            }
            pet.gainCloseness(closeness);
            while (pet.getCloseness() > ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                pet.setLevel((byte) (pet.getLevel() + 1));
                byte index = getPlayer().getPetIndex(pet);
                getClient().announce(MaplePacketCreator.showOwnPetLevelUp(index));
                getPlayer().getMap().broadcastMessage(getPlayer(), MaplePacketCreator.showPetLevelUp(getPlayer(), index));
            }
            IItem petz = getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getPosition());
            getPlayer().getClient().announce(MaplePacketCreator.updateSlot(petz));
        }
    }

    public String getName() {
        return getPlayer().getName();
    }

    public int getGender() {
        return getPlayer().getGender();
    }

    public void changeJobById(int a) {
        getPlayer().changeJob(MapleJob.getById(a));
    }

    public void addRandomItem(int id) {
        MapleItemInformationProvider i = MapleItemInformationProvider.getInstance();
        MapleInventoryManipulator.addFromDrop(getClient(), i.randomizeStats((Equip) i.getEquipById(id)), true);
    }

    public MapleJob getJobName(int id) {
        return MapleJob.getById(id);
    }

    public MapleStatEffect getItemEffect(int itemId) {
        return MapleItemInformationProvider.getInstance().getItemEffect(itemId);
    }

    public void resetStats() {
        //getPlayer().resetStats();
    }

    public void processGachapon(int[] id, boolean remote) {
        int[] gacMap = {100000000, 101000000, 102000000, 103000000, 105040300, 800000000, 809000101, 809000201, 600000000, 120000000};
        int itemid = id[Randomizer.nextInt(id.length)];
        addRandomItem(itemid);
        if (!remote) {
            gainItem(5220000, (short) -1);
        }
        sendNext("You have obtained a #b#t" + itemid + "##k.");
        getClient().getChannelServer().broadcastPacket(MaplePacketCreator.gachaponMessage(getPlayer().getInventory(MapleInventoryType.getByType((byte) (itemid / 1000000))).findById(itemid), c.getChannelServer().getMapFactory().getMap(gacMap[(getNpc() != 9100117 && getNpc() != 9100109) ? (getNpc() - 9100100) : getNpc() == 9100109 ? 8 : 9]).getMapName(), getPlayer()));
    }

    public void disbandAlliance(MapleClient c, int allianceId) {
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
            Server.getInstance().allianceMessage(c.getPlayer().getGuild().getAllianceId(), MaplePacketCreator.disbandAlliance(allianceId), -1, -1);
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

    public boolean canBeUsedAllianceName(String name) {
        if (name.contains(" ") || name.length() > 12) {
            return false;
        }
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM alliance WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                rs.close();
                return false;
            }
            ps.close();
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MapleAlliance createAlliance(MapleCharacter chr1, MapleCharacter chr2, String name) {
        int id = 0;
        int guild1 = chr1.getGuildId();
        int guild2 = chr2.getGuildId();
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `alliance` (`name`, `guild1`, `guild2`) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, guild1);
            ps.setInt(3, guild2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        MapleAlliance alliance = new MapleAlliance(name, id, guild1, guild2);
        try {
            Server.getInstance().setGuildAllianceId(guild1, id);
            Server.getInstance().setGuildAllianceId(guild2, id);
            chr1.setAllianceRank(1);
            chr1.saveGuildStatus();
            chr2.setAllianceRank(2);
            chr2.saveGuildStatus();
            Server.getInstance().addAlliance(id, alliance);
            Server.getInstance().allianceMessage(id, MaplePacketCreator.makeNewAlliance(alliance, chr1.getClient()), -1, -1);
        } catch (Exception e) {
            return null;
        }
        return alliance;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>();
        for (Channel channel : Server.getInstance().getChannelsFromWorld(getPlayer().getWorld())) {
            for (MapleCharacter chr : channel.getPartyMembers(getPlayer().getParty())) {
                if (chr != null) {
                    chars.add(chr);
                }
            }
        }
        return chars;
    }

    public void warpParty(int id) {
        for (MapleCharacter mc : getPartyMembers()) {
            if (id == 925020100) {
                mc.setDojoParty(true);
            }
            mc.changeMap(getWarpMap(id));
        }
    }

    public boolean hasMerchant() {
        return getPlayer().hasMerchant();
    }

    public boolean hasMerchantItems() {
        try {
            if (!ItemFactory.MERCHANT.loadItems(getPlayer().getId(), false).isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        if (getPlayer().getMerchantMeso() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void showFredrick() {
        c.announce(MaplePacketCreator.getFredrick(getPlayer()));
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharacters()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public MapleEvent getEvent() {
        return c.getChannelServer().getEvent();
    }

    public void divideTeams() {
        if (getEvent() != null) {
            getPlayer().setTeam(getEvent().getLimit() % 2); //muhaha :D
        }
    }

    public boolean createPyramid(String mode, boolean party) {//lol
        PyramidMode mod = PyramidMode.valueOf(mode);

        MapleParty partyz = getPlayer().getParty();
        MapleMapFactory mf = c.getChannelServer().getMapFactory();

        MapleMap map = null;
        int mapid = 926010100;
        if (party) {
            mapid += 10000;
        }
        mapid += (mod.getMode() * 1000);

        for (byte b = 0; b < 5; b++) {//They cannot warp to the next map before the timer ends (:
            map = mf.getMap(mapid + b);
            if (map.getCharacters().size() > 0) {
                map = null;
                continue;
            } else {
                break;
            }
        }

        if (map == null) {
            return false;
        }

        if (!party) {
            partyz = new MapleParty(-1, new MaplePartyCharacter(getPlayer()));
        }
        Pyramid py = new Pyramid(partyz, mod, map.getId());
        getPlayer().setPartyQuest(py);
        py.warp(mapid);
        dispose();
        return true;
    }

    public String PetList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<String>();
        for (IItem item : cash.list()) {
            if (ItemConstants.isPet(item.getItemId())) {
                stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
            }
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : equip.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<String>();
        for (IItem item : use.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<String>();
        for (IItem item : cash.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<String>();
        for (IItem item : etc.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : setup.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String getFullConnected() {
        int x = 0;
        for (Channel ch : Server.getInstance().getChannelsFromWorld(getPlayer().getWorld())) {
            x += ch.getConnectedClients();
        }
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(x);
    }

    public String getWithCommas(int x) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(x);
    }

    public int getTransRate() {
        return c.getChannelServer().getRate();
    }

    public void gainEqWithStat(int id, int wa, int matt, int str, int dex, int intel, int luk, int line1, int line2, int line3) {
        gainEqWithStats(id, wa, matt, str, dex, intel, luk, line1, line2, line3);
    }

    public boolean editEq(byte slot, short incWA, short incMA, short incStr, short incDex, short incInt, short incLuk, boolean set) {
        MapleInventory equipinv = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eqp = (Equip) equipinv.getItem(slot).copy();

        int nStr = eqp.getStr() + incStr;
        int nDex = eqp.getDex() + incDex;
        int nInt = eqp.getInt() + incInt;
        int nLuk = eqp.getLuk() + incLuk;
        int nWatk = eqp.getWatk() + incWA;
        int nMatk = eqp.getMatk() + incMA;

        if (set) {
            if (nStr > 0) {
                nStr = incStr;
            }
            if (nDex > 0) {
                nDex = incDex;
            }
            if (nInt > 0) {
                nInt = incInt;
            }
            if (nLuk > 0) {
                nLuk = incLuk;
            }
            if (incWA > 0) {
                nWatk = incWA;
            }
            if (incMA > 0) {
                nMatk = incMA;
            }
        }

        if (nStr > 30000) {
            nStr = 30000;
        }
        if (nDex > 30000) {
            nDex = 30000;
        }
        if (nInt > 30000) {
            nInt = 30000;
        }
        if (nLuk > 30000) {
            nLuk = 30000;
        }
        if (nWatk > 30000) {
            nWatk = 30000;
        }
        if (nMatk > 30000) {
            nMatk = 30000;
        }

        eqp.setStr((short) nStr); // STR
        eqp.setDex((short) nDex); // DEX
        eqp.setInt((short) nInt); // INT
        eqp.setLuk((short) nLuk); //LUK
        eqp.setWatk((short) nWatk); // Watk
        eqp.setMatk((short) nMatk); // Matk

        equipinv.removeItem(slot);
        equipinv.addItem(eqp);
        getPlayer().reload();
        return true;
    }

    public String potEq(byte slot) {
        String ret = "";
        tryblock:
        try {
            MapleInventory equipinv = getPlayer().getInventory(MapleInventoryType.EQUIP);
            Equip eqp = (Equip) equipinv.getItem(slot).copy();

            if (eqp.getLines().equals("Destroyed")) {
                ret = "#e#d#rDestroyed items lose potential abilities, therefore cannot be potentialed.";
                break tryblock;
            }


            if (eqp.getLines().equals("No Potential")) {
                int destroychance = Randomizer.nextInt(9);
                if (destroychance > 7) {
                    int gonerand = Randomizer.nextInt(9);
                    if (gonerand < 5) {
                        getPlayer().takeVotePT(1);
                        MapleInventoryManipulator.removeFromSlot(c, equipinv.getType(), slot, (short) 1, false);
                        ret = "#e#d#rSorry, your item has been totally destroyed.";
                        break tryblock;
                    } else {
                        eqp.setLine1(99);
                        eqp.setLine2(99);
                        eqp.setLine3(99);
                        equipinv.removeItem(slot);
                        equipinv.addItem(eqp);
                        getPlayer().takeVotePT(1);
                        getPlayer().reload();
                        ret = "#e#d#rSorry, your item's potential ablility has been destroyed.";
                        break tryblock;
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                int lineno = i + 1;
                if (lineno == 3) {
                    if (eqp.getLine3() == 0) {
                        int linerandom = Randomizer.nextInt(100);
                        if (linerandom > 10) {
                            break;
                        }
                    }
                }
                eqp = randomizePotential(eqp, lineno);
            }
            equipinv.removeItem(slot);
            equipinv.addItem(eqp);
            getPlayer().takeVotePT(1);
            getPlayer().reload();
            ret = "#e#rSuccess!";
            break tryblock;
        } catch (Exception e) {
            System.out.println("Exception at potEq() - " + e);
        }
        return ret;
    }

    public Equip randomizePotential(Equip eqp, int line) {
        int potrand = Randomizer.nextInt(26);
        int statrand = Randomizer.nextInt(4);
        statrand += 1;
        if (potrand < 12) {
            switch (line) {
                case 1:
                    eqp.setLine1(statrand);
                    break;
                case 2:
                    eqp.setLine2(statrand);
                    break;
                case 3:
                    eqp.setLine3(statrand);
                    break;
            }
        }
        if (potrand > 12 && potrand < 21) {
            switch (line) {
                case 1:
                    eqp.setLine1(statrand + 4);
                    break;
                case 2:
                    eqp.setLine2(statrand + 4);
                    break;
                case 3:
                    eqp.setLine3(statrand + 4);
                    break;
            }
        }
        if (potrand > 20 && potrand < 25) {
            switch (line) {
                case 1:
                    eqp.setLine1(statrand + 8);
                    break;
                case 2:
                    eqp.setLine2(statrand + 8);
                    break;
                case 3:
                    eqp.setLine3(statrand + 8);
                    break;
            }
        }
        if (potrand == 25) {
            switch (line) {
                case 1:
                    eqp.setLine1(13);
                    break;
                case 2:
                    eqp.setLine2(13);
                    break;
                case 3:
                    eqp.setLine3(13);
                    break;
            }
        }
        if (potrand == 26) {
            switch (line) {
                case 1:
                    eqp.setLine1(14);
                    break;
                case 2:
                    eqp.setLine2(14);
                    break;
                case 3:
                    eqp.setLine3(14);
                    break;
            }
        }
        if (potrand == 27) {
            switch (line) {
                case 1:
                    eqp.setLine1(15);
                    break;
                case 2:
                    eqp.setLine2(15);
                    break;
                case 3:
                    eqp.setLine3(15);
                    break;
            }
        }
        return eqp;
    }

    public void openShopById(int id) {
        MapleShopFactory.getInstance().getShopById(id).sendShop(getClient());
    }

    public void openNpcShop(int id) {
        MapleShopFactory.getInstance().getShopForNPC(id).sendShop(getClient());
    }

    public int getBossLog(String bossid) {
        return getPlayer().getBossLog(bossid);
    }

    public int getGiftLog(String bossid) {
        return getPlayer().getGiftLog(bossid);
    }

    public void setBossLog(String bossid) {
        getPlayer().setBossLog(bossid);
    }

    public String ranking() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT characters.name, characters.reborns, characters.dps, guilds.name AS guildname FROM characters, guilds WHERE characters.guildid = guilds.guildid AND characters.gm = 0 AND characters.banned = 0 ORDER BY characters.reborns DESC, characters.level DESC LIMIT 50");
            ResultSet rs = ps.executeQuery();
            int i = 0;
            String rank = "";
            while (rs.next()) {
                i++;
                if (rs.getString("guildname") != null) {
                    rank += (i + ".#r" + rs.getString("name") + "#n#k, #e#bReborns: #d" + rs.getInt("reborns") + "#k, #e#bDPS: #d" + rs.getInt("dps") + "#n#k, #e#bGuild: #d" + rs.getString("guildname") + "#k\r\n");
                } else {
                    rank += (i + ".#r" + rs.getString("name") + "#n#k, #e#bReborns: #d" + rs.getInt("reborns") + "#k, #e#bDPS: #d" + rs.getInt("dps") + "#n#k\r\n");
                }
            }
            ps.close();
            rs.close();
            return rank;
        } catch (SQLException e) {
            return "Unknown error, please report this to the admin";
        }
    }

    public void maxSkill(int id, int level, int masterlevel) {
        maxSkill(id);
    }

    public void maxSkill(int id) {
        ISkill skill = SkillFactory.getSkill(id);
        getPlayer().changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
    }

    public boolean addEquipAuction(byte slot, int ice, int black, int tear, int cloud, int meso, int vp, int shards) {
        MapleInventory equipinv = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equipinv.getItem(slot);
        int item = equipinv.getItem(slot).getItemId();
        int str = eu.getStr();
        int dex = eu.getDex();
        int int_ = eu.getInt();
        int luk = eu.getLuk();
        int hp = eu.getHp();
        int mp = eu.getMp();
        int watk = eu.getWatk();
        int matk = eu.getMatk();
        int wdef = eu.getWdef();
        int mdef = eu.getMdef();
        int acc = eu.getAcc();
        int avoid = eu.getAvoid();
        int hands = eu.getHands();
        int speed = eu.getSpeed();
        int jump = eu.getJump();
        int locked = 0;
        int vicious = eu.getVicious();
        int itemlevel = eu.getItemLevel();
        int itemexp = eu.getItemExp();
        int ringid = eu.getRingId();
        int upgradeslots = eu.getUpgradeSlots();
        int line1 = eu.getLine1();
        int line2 = eu.getLine2();
        int line3 = eu.getLine3();
        if (eu.getOwner().equals("")) {
            MapleInventoryManipulator.removeFromSlot(c, equipinv.getType(), slot, (short) 1, false);
            return AuctionSystemHandler.addEquipAuction(item, str, dex, int_, luk, hp, mp, watk, matk, wdef, mdef, acc, avoid, hands, speed, jump, locked, vicious, itemlevel, itemexp, ringid, upgradeslots, line1, line2, line3, 0, ice, black, tear, cloud, meso, vp, shards, getClient());
        } else {
            return false;
        }
    }

    public boolean addItemAuction(int inven, byte slot, int ice, int black, int tear, int cloud, int meso, int vp, int shards, int quantity) {
        MapleInventory inventory = null;
        if (inven == 1) {
            inventory = getPlayer().getInventory(MapleInventoryType.USE);
        }
        if (inven == 2) {
            inventory = getPlayer().getInventory(MapleInventoryType.SETUP);
        }
        if (inven == 3) {
            inventory = getPlayer().getInventory(MapleInventoryType.ETC);
        }
        if (inven == 4) {
            inventory = getPlayer().getInventory(MapleInventoryType.CASH);
        }
        int itemid = inventory.getItem(slot).getItemId();
        if (inventory.getItem((byte) slot).getOwner().equals("") && !ItemConstants.isPet(itemid)) {
            MapleInventoryManipulator.removeFromSlot(getClient(), inventory.getType(), slot, (short) quantity, false);
            return AuctionSystemHandler.addItemAuction(itemid, quantity, ice, black, tear, cloud, meso, vp, shards, c);
        } else {
            return false;
        }
    }

    public String getEquipAuctions() {
        return AuctionSystemHandler.getEquipAuctions();
    }

    public String getEquipInfo(int id) {
        if (id == 0) {
            dispose();
            return "";
        }
        if (AuctionSystemHandler.isExpired(id)) {
            AuctionSystemHandler.deleteAuction(id);
            dispose();
            return "#rThe auction has expired. Therefore, it has been deleted.";
        }
        return AuctionSystemHandler.getEquipInfo(id);
    }

    public String getItemAuctions() {
        return AuctionSystemHandler.getItemAuctions();
    }

    public String getItemInfo(int id) {
        if (id == 0) {
            dispose();
            return "";
        }
        if (AuctionSystemHandler.isExpired(id)) {
            AuctionSystemHandler.deleteAuction(id);
            dispose();
            return "#rThe auction has expired. Therefore, it has been deleted.";
        }
        return AuctionSystemHandler.getItemInfo(id);
    }

    public String getPrice(int id) {
        return AuctionSystemHandler.getPrice(id);
    }

    public String showPoT(byte slot) {
        String y = "";
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int[] lines = {eu.getLine1(), eu.getLine2(), eu.getLine3()};
        for (int i = 0; i < 3; i++) {
            switch (lines[i]) {
                case 0:
                    break;
                case 1:
                    y += "\r\n2% STR";
                    break;
                case 2:
                    y += "\r\n2% DEX";
                    break;
                case 3:
                    y += "\r\n2% LUK";
                    break;
                case 4:
                    y += "\r\n2% INT";
                    break;
                case 5:
                    y += "\r\n4% STR";
                    break;
                case 6:
                    y += "\r\n4% DEX";
                    break;
                case 7:
                    y += "\r\n4% LUK";
                    break;
                case 8:
                    y += "\r\n4% INT";
                    break;
                case 9:
                    y += "\r\n6% STR";
                    break;
                case 10:
                    y += "\r\n6% DEX";
                    break;
                case 11:
                    y += "\r\n6% LUK";
                    break;
                case 12:
                    y += "\r\n6% INT";
                    break;
                case 13:
                    y += "\r\n82 M.ATT";
                    break;
                case 14:
                    y += "\r\n76 W.ATT";
                    break;
                case 15:
                    y += "\r\n6% TOTAL DAMAGE";
                    break;
                default:
                    break;
            }
        }
        if (y.equals("")) {
            y = "#r" + eu.getLines();
        }
        return y;
    }

    public String getDropboxInfo(int auctionid) {
        return AuctionSystemHandler.getDropboxInfo(auctionid);
    }

    public String retriveDropbox(int auctionid) {
        String ret = "#eThere was an error. Please report this to the administrator.";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM dropbox WHERE auctionid = ?");
            ps.setInt(1, auctionid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ice = rs.getInt("ice");
                int black = rs.getInt("black");
                int tear = rs.getInt("tear");
                int cloud = rs.getInt("cloud");
                int meso = rs.getInt("meso");
                int vp = rs.getInt("vp");
                int shards = rs.getInt("shards");
                if (ice > 0) {
                    gainItem(4000150, (short) ice);
                }
                if (black > 0) {
                    gainItem(4031050, (short) black);
                }
                if (tear > 0) {
                    gainItem(4000415, (short) tear);
                }
                if (cloud > 0) {
                    gainItem(4001063, (short) cloud);
                }
                if (shards > 0) {
                    gainItem(4031917, (short) shards);
                }
                if (meso > 0) {
                    gainMeso(meso);
                }
                if (vp > 0) {
                    getPlayer().addVotePT(vp);
                }
            }
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM dropbox WHERE auctionid = ?");
            ps1.setInt(1, auctionid);
            ps1.executeUpdate();
            ps1.close();
            ps.close();
            rs.close();
            ret = "#eThank you for using my service!";
        } catch (Exception e) {
            System.out.println("Exception at retriveDropbox(): " + e);
        }
        return ret;
    }

    public boolean retriveEquipAuction(int id, boolean addToDropbox) {
        try {
            if (AuctionSystemHandler.isExpired(id)) {
                AuctionSystemHandler.deleteAuction(id);
                dispose();
                getPlayer().dropMessage(1, "The auction has expired, and therefore deleted.");
                return false;
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM auctions WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (addToDropbox) {
                    AuctionSystemHandler.addToDropbox(id);
                }
                AuctionSystemHandler.deleteAuction(id);
                gainFromAuction(rs.getInt("itemid"), rs.getInt("str"), rs.getInt("dex"), rs.getInt("int"), rs.getInt("luk"), rs.getInt("hp"), rs.getInt("mp"), rs.getInt("watk"), rs.getInt("matk"), rs.getInt("wdef"), rs.getInt("mdef"), rs.getInt("acc"), rs.getInt("avoid"), rs.getInt("hands"), rs.getInt("speed"), rs.getInt("jump"), rs.getInt("vicious"), rs.getInt("itemlevel"), rs.getInt("itemexp"), rs.getInt("ringid"), rs.getInt("upgradeslots"), rs.getInt("line1"), rs.getInt("line2"), rs.getInt("line3"));
            }
            ps.close();
            rs.close();
            return true;
        } catch (Exception e) {
            System.out.println("Exception at retriveEquipAuction(): " + e);
        }
        return false;
    }

    public String buyEquipAuction(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM auctions WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            String name = "";
            while (rs.next()) {
                name = MapleCharacter.getNameById(rs.getInt("cid"));
                if (rs.getInt("cid") != getPlayer().getId()) {
                    if (rs.getInt("type") == 1) {
                        if (rs.getInt("ice") <= itemQuantity(4000150) && rs.getInt("shards") <= itemQuantity(4031917) && rs.getInt("black") <= itemQuantity(4031050) && rs.getInt("tear") <= itemQuantity(4000415) && rs.getInt("cloud") <= itemQuantity(4001063) && rs.getInt("meso") <= getMeso() && rs.getInt("vp") <= getPlayer().getVotePT()) {
                            gainItem(4000150, (short) (-1 * rs.getInt("ice")));
                            gainItem(4031050, (short) (-1 * rs.getInt("black")));
                            gainItem(4000415, (short) (-1 * rs.getInt("tear")));
                            gainItem(4001063, (short) (-1 * rs.getInt("cloud")));
                            gainItem(4031917, (short) (-1 * rs.getInt("shards")));
                            gainMeso((-1 * rs.getInt("meso")));
                            getPlayer().takeVotePT(rs.getInt("vp"));
                            retriveEquipAuction(id, true);
                            getPlayer().sendNoteStat(name, "You auction (Auction ID - " + id + ") has been baught by " + getPlayer().getName() + "! Collect your income at the auction income dropbox NPC!", "Sharp", (byte) 0);
                        } else {
                            ps.close();
                            rs.close();
                            return "#r#eYou do not have enough wealth to purchase this item.";
                        }
                    }
                } else {
                    ps.close();
                    rs.close();
                    return "#e#rYou can't buy your own item.";
                }
            }
            ps.close();
            rs.close();
            return "#b#eThank you for your purchase.";
        } catch (Exception e) {
            System.out.println("Exception at buyEquipAuction(): " + e);
        }
        return "Error";
    }

    public String getOwnedEquipAuctions() {
        return AuctionSystemHandler.getOwnedEquipAuctions(c);
    }

    public String getOwnedItemAuctions() {
        return AuctionSystemHandler.getOwnedItemAuctions(c);
    }

    public boolean retriveItemAuction(int id, boolean addToDropbox) {
        try {
            if (AuctionSystemHandler.isExpired(id)) {
                AuctionSystemHandler.deleteAuction(id);
                dispose();
                getPlayer().dropMessage(1, "The auction has expired, and therefore deleted.");
                return false;
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid, quantity FROM auctions WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (addToDropbox) {
                    AuctionSystemHandler.addToDropbox(id);
                }
                gainItem(rs.getInt("itemid"), (short) rs.getInt("quantity"));
                AuctionSystemHandler.deleteAuction(id);
            }
            ps.close();
            rs.close();
            return true;
        } catch (Exception e) {
            System.out.println("Exception at retriveItemAuction(): " + e);
        }
        return false;
    }

    public String buyItemAuction(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT ice, black, tear, cloud, meso, type, vp, shards, cid FROM auctions WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            String name = "";
            while (rs.next()) {
                name = MapleCharacter.getNameById(rs.getInt("cid"));
                if (rs.getInt("cid") != getPlayer().getId()) {
                    if (rs.getInt("type") == 2) {
                        if (rs.getInt("ice") <= itemQuantity(4000150) && rs.getInt("shards") <= itemQuantity(4031917) && rs.getInt("black") <= itemQuantity(4031050) && rs.getInt("tear") <= itemQuantity(4000415) && rs.getInt("cloud") <= itemQuantity(4001063) && rs.getInt("meso") <= getMeso() && rs.getInt("vp") <= getPlayer().getVotePT()) {
                            gainItem(4000150, (short) (-1 * rs.getInt("ice")));
                            gainItem(4031050, (short) (-1 * rs.getInt("black")));
                            gainItem(4000415, (short) (-1 * rs.getInt("tear")));
                            gainItem(4001063, (short) (-1 * rs.getInt("cloud")));
                            gainItem(4031917, (short) (-1 * rs.getInt("shards")));
                            gainMeso((-1 * rs.getInt("meso")));
                            getPlayer().takeVotePT(rs.getInt("vp"));
                            retriveItemAuction(id, true);
                            getPlayer().sendNoteStat(name, "You auction (Auction ID - " + id + ") has been baught by " + getPlayer().getName() + "!", "Sharp", (byte) 0);
                        } else {
                            ps.close();
                            rs.close();
                            return "#b#eYou do not have enough wealth to purchase this item.";
                        }
                    }
                } else {
                    ps.close();
                    rs.close();
                    return "#e#rYou can't buy your own item.";
                }
            }
            ps.close();
            rs.close();
            return "#b#eThank you for your purchase.";
        } catch (Exception e) {
            System.out.println("Exception at retriveItemAuction(): " + e);
        }
        return "#r#eYou do not have enough wealth to purchase this item.";
    }

    public void addDeletedPet() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE custom set value = ? where id = 1");
            ps.setInt(1, getDeletedPets() + 1);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            System.out.println("Exception at addDeletedPets() -" + e);
        }
    }

    public int getDeletedPets() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from custom where id = 1");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int ret = rs.getInt("value");
            rs.close();
            ps.close();
            return ret;
        } catch (Exception e) {
            System.out.println("Exception at getDeletedPets() -" + e);
        }
        return 0;
    }

    public String getDeletedPetsString() {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df.format(getDeletedPets());
    }

    public void deletePet(byte slot) {
        getPlayer().unequipAllPets();
        try {
            int id = getPlayer().getInventory(MapleInventoryType.CASH).getItem(slot).getPet().getUniqueId();
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE from pets WHERE `petid` = ?");
            ps.setInt(1, id);
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }
        addDeletedPet();
    }

    public int slotQuantity(byte slot, int inven) {
        MapleInventory inventory = null;
        if (inven == 1) {
            inventory = getPlayer().getInventory(MapleInventoryType.USE);
        }
        if (inven == 2) {
            inventory = getPlayer().getInventory(MapleInventoryType.SETUP);
        }
        if (inven == 3) {
            inventory = getPlayer().getInventory(MapleInventoryType.ETC);
        }
        if (inven == 4) {
            inventory = getPlayer().getInventory(MapleInventoryType.CASH);
        }
        int itemid = inventory.getItem(slot).getItemId();
        return getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid);
    }

    public String getAvaliableHQS() {
        return MapleGuild.getAvaliableHQS();
    }

    public String searchItemAuction(int id) {
        String str = AuctionSystemHandler.searchItemAuction(id);
        if (str.startsWith("#e#rAuction")) {
            dispose();
        }
        return str;
    }

    public String searchEquipAuction(int id) {
        String str = AuctionSystemHandler.searchEquipAuction(id);
        if (str.startsWith("#e#rAuction")) {
            dispose();
        }
        return str;
    }

    public void worldMessage(String x) {
        Server.getInstance().broadcastMessage(getPlayer().getWorld(), MaplePacketCreator.serverNotice(6, x));
    }

    public void warpToRandomJQ() {
        int jqmaps[] = {100000202, 220000006};
        int rand = Randomizer.nextInt(jqmaps.length);
        getPlayer().changeMap(jqmaps[rand]);
    }

    public String participateInEvent() {
        if (getClient().getWorldServer().getParticipation(getPlayer().getName())) {
            dispose();
            return "#e#rYou have already participated in the event!!!";
        } else {
            getClient().getWorldServer().addEventParticipants(getPlayer().getName());
            return "#e#rEnjoy!!";
        }
    }

    public String eventReward() {
        int jqmaps[] = {100000202, 220000006};
        int rewards[] = {4, 4};
        //respective to each other.

        int index = 1337; // i thought intarray.indexOf(int value) existed, but then again, there can be duplicates.
        for (int i = 0; i < jqmaps.length; i++) {
            if (jqmaps[i] == getPlayer().getMap().getId()) {
                index = i;
            }
        }
        if (index == 1337) {
            return "#eNice try, but you have to #rcomplete#k the #revent#k fairly to gain your reward!";
        }
        short trophies = (short) rewards[index];
        gainItem(4000038, trophies);
        return "#eYou gained #r" + trophies + "#k #bEvent Trophies#k! Come back next time!";
    }

    public String getDropbox() {
        return AuctionSystemHandler.getDropbox(c);
    }

    public String spawnSuperMob(int mobid, int hpmpmultiplier, int expmultiplier) {
        String ret = "";
        if (getPlayer().getMap().getMonstersOnMap() > 9) {
            ret = "#eThere cannot be more than 10 monsters in the guild hideout.";
        } else {
            MapleMonster monster = new MapleMonster(MapleLifeFactory.getMonster(mobid));
            MapleMonsterStats newstats = new MapleMonsterStats(monster.getStats());
            newstats.setHp(monster.getHp() * hpmpmultiplier);
            newstats.setMp(monster.getMp() * hpmpmultiplier);
            newstats.setExp(monster.getExp() * expmultiplier);
            monster.setStats(newstats);
            //getPlayer().getMap().spawnMonsterOnGroudBelow(monster, getPlayer().getMap().getNPCObject(this.getNpc()).getPosition());
            getPlayer().getMap().spawnMonsterOnGroudBelow(monster, getPlayer().getPosition());
            ret = "#e#rThank you for using my service.";
        }
        return ret;
    }

    public void killAllMonsters(int mapid) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        map.killAllMonsters(); // No drop.
    }

    public void clearDrops() {
        getPlayer().getMap().clearDrops(getPlayer());
    }

    public String getMobName(int id) {
        return MapleLifeFactory.getMonster(id).getName();
    }
}