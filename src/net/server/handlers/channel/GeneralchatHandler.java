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
package net.server.handlers.channel;

import client.MapleCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;
import client.command.Commands;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import tools.DatabaseConnection;

public final class GeneralchatHandler extends net.AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String s = slea.readMapleAsciiString();
        MapleCharacter chr = c.getPlayer();
        char heading = s.charAt(0);
        if (heading == '/' || heading == '!' || heading == '@') {
            String[] sp = s.split(" ");
            sp[0] = sp[0].toLowerCase().substring(1);
            if (!Commands.executePlayerCommand(c, sp, heading)) {
                if (chr.isGM()) {
                    if (Commands.executeGMCommand(c, sp, heading)) {
                        if (!sp[0].equals("online")) {
                            try {
                                Connection con = DatabaseConnection.getConnection();
                                PreparedStatement ps = con.prepareStatement("INSERT INTO gmlog (`name`, `command`) VALUES (?, ?)");
                                ps.setString(1, chr.getName());
                                ps.setString(2, s);
                                ps.executeUpdate();
                                ps.close();
                            } catch (SQLException e) {
                                //System.out.println(e);
                            }
                        }
                    } else {
                        Commands.executeAdminCommand(c, sp, heading);
                    }
                }
            }
        } else {
            if (chr.canTalk() == false) {
                if (chr.isDonor()) {
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.sendYellowTip("[Donor]" + c.getPlayer().getName() + ": " + s));
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), s, false, 1));
                } else {
                    if (!chr.isHidden()) {
                        chr.getMap().broadcastMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.isGM(), slea.readByte()));
                    } else {
                        chr.getMap().broadcastGMMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.isGM(), slea.readByte()));
                    }
                }
                if (c.getPlayer().getWatcher() != null) {
                    c.getPlayer().getWatcher().message("[Watch][" + c.getPlayer().getName() + "][All] : " + s);
                }
            }
        }
    }
}
