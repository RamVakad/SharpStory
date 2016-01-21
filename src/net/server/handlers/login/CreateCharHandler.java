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
package net.server.handlers.login;

import client.IItem;
import client.Item;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventory;
import client.MapleInventoryType;
import client.MapleSkinColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.AbstractMaplePacketHandler;
import net.server.Server;
import server.MapleItemInformationProvider;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class CreateCharHandler extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String name = slea.readMapleAsciiString();
        if (!MapleCharacter.canCreateChar(name)) {
            return;
        }


        MapleCharacter newchar = MapleCharacter.getDefault(c);
        newchar.setWorld(c.getWorld());
        int job = slea.readInt();
        int face = slea.readInt();
        newchar.setFace(face);
        newchar.setHair(slea.readInt() + slea.readInt());
        int skincolor = slea.readInt();
        if (skincolor > 3) {
            return;
        }
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT votepoints FROM accounts WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newchar.getAccountID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                newchar.setVotePT(rs.getInt("votepoints"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            //lazy to handle
        }
        newchar.setSkinColor(MapleSkinColor.getById(skincolor));
        int top = slea.readInt();
        int bottom = slea.readInt();
        int shoes = slea.readInt();
        int weapon = slea.readInt();
        newchar.setGender(slea.readByte());
        newchar.setName(name);
        if (!newchar.isGM()) {
            if (job == 0) { // Knights of Cygnus
                //newchar.setJob(MapleJob.NOBLESSE);
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161047, (byte) 0, (short) 1));
            } else if (job == 1) { // Adventurer
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1));
            } else if (job == 2) { // Aran
                //newchar.setJob(MapleJob.LEGEND);
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161048, (byte) 0, (short) 1));
            } else {
                c.disconnect(); //How the fuck is this even possible? Dam packet editors.
                //i used to do this...
                System.out.println("[CHAR CREATION] A new job ID has been found: " + job + "::: SUSPECTING HAXX"); //I should IP ban for packet editing!
                return;
            }
        }
        //CHECK FOR EQUIPS
        MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        if (newchar.getGender() == 0) {
            IItem eq_hat = MapleItemInformationProvider.getInstance().getEquipById(1003047);
            eq_hat.setPosition((byte) -101);
            equip.addFromDB(eq_hat);
            IItem eq_cape = MapleItemInformationProvider.getInstance().getEquipById(1102229);
            eq_cape.setPosition((byte) -109);
            equip.addFromDB(eq_cape);
            IItem eq_top = MapleItemInformationProvider.getInstance().getEquipById(1042072);
            eq_top.setPosition((byte) -105);
            equip.addFromDB(eq_top);
            IItem eq_bottom = MapleItemInformationProvider.getInstance().getEquipById(1060002);
            eq_bottom.setPosition((byte) -6);
            equip.addFromDB(eq_bottom);
            IItem eq_shoes = MapleItemInformationProvider.getInstance().getEquipById(1072323);
            eq_shoes.setPosition((byte) -107);
            equip.addFromDB(eq_shoes);
            IItem eq_weapon = MapleItemInformationProvider.getInstance().getEquipById(1302000);
            eq_weapon.setPosition((byte) -11);
            equip.addFromDB(eq_weapon.copy());
            newchar.saveToDB(false);
            c.announce(MaplePacketCreator.addNewCharEntry(newchar));
        } else {
            IItem eq_hat = MapleItemInformationProvider.getInstance().getEquipById(1003047);
            eq_hat.setPosition((byte) -101);
            equip.addFromDB(eq_hat);
            IItem eq_cape = MapleItemInformationProvider.getInstance().getEquipById(1102229);
            eq_cape.setPosition((byte) -109);
            equip.addFromDB(eq_cape);
            IItem eq_top = MapleItemInformationProvider.getInstance().getEquipById(1042001);
            eq_top.setPosition((byte) -105);
            equip.addFromDB(eq_top);
            IItem eq_bottom = MapleItemInformationProvider.getInstance().getEquipById(1062052);
            eq_bottom.setPosition((byte) -106);
            equip.addFromDB(eq_bottom);
            IItem eq_shoes = MapleItemInformationProvider.getInstance().getEquipById(1072323);
            eq_shoes.setPosition((byte) -107);
            equip.addFromDB(eq_shoes);
            IItem eq_weapon = MapleItemInformationProvider.getInstance().getEquipById(1302000);
            eq_weapon.setPosition((byte) -11);
            equip.addFromDB(eq_weapon.copy());
            newchar.saveToDB(false);
            c.announce(MaplePacketCreator.addNewCharEntry(newchar));
        }
        Server.getInstance().broadcastMessage(newchar.getWorld(), MaplePacketCreator.serverNotice(6, "[Sharp]:: Welcome our latest created character, " + newchar.getName() + "!"));
        int[] defaultKeys = {2002024, 2002023, 2002022, 2002025, 2002021};
        int[] defaultActions = {1092095, 13337, 9030000, 9000021, 9000020};
        for (int i = 0; i < 5; i++) {
            try {
                ps = con.prepareStatement("INSERT INTO potkeys (charid, pot, action, slot) VALUES (?, ?, ?, ?)");
                ps.setInt(1, MapleCharacter.getIdByName(newchar.getName()));
                ps.setInt(2, defaultKeys[i]);
                ps.setInt(3, defaultActions[i]);
                ps.setInt(4, i);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
            }
        }

    }
}