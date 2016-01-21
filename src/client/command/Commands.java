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
License.te

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.command;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import client.IItem;
import client.ISkill;
import client.Item;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleStat;
import client.SkillFactory;
import constants.ItemConstants;
import constants.skills.Aran;
import constants.skills.Archer;
import constants.skills.Beginner;
import constants.skills.Bowmaster;
import constants.skills.ChiefBandit;
import constants.skills.Cleric;
import constants.skills.Crossbowman;
import constants.skills.Crusader;
import constants.skills.DarkKnight;
import constants.skills.DragonKnight;
import constants.skills.Fighter;
import constants.skills.Hermit;
import constants.skills.Hero;
import constants.skills.Hunter;
import constants.skills.ILArchMage;
import constants.skills.ILWizard;
import constants.skills.Magician;
import constants.skills.NightLord;
import constants.skills.Page;
import constants.skills.Paladin;
import constants.skills.Spearman;
import constants.skills.SuperGM;
import constants.skills.Swordsman;
import constants.skills.WhiteKnight;
import java.awt.Point;
import java.io.File;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import tools.DatabaseConnection;
import net.server.Channel;
import net.server.Server;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.AbstractPlayerInteraction;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShopFactory;
import server.TimerManager;
import server.events.gm.MapleEvent;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.life.MapleNPC;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.Pair;

public class Commands {

    public static boolean executePlayerCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        if (heading == '!' && player.gmLevel() == 0) {
            c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Try using @" + sub[0] + "!", 280, 5));
            return false;
        }
        if (heading == '@') {
            long curTime = Calendar.getInstance().getTimeInMillis();
            if (c.getPlayer().getDelay("COMMAND_SPAM") > curTime) {
                c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Please don't spam commands!", 280, 5));
                return true;
            } else {
                Calendar futureCal = Calendar.getInstance();
                futureCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 2);
                c.getPlayer().addDelay("COMMAND_SPAM", futureCal.getTimeInMillis());
            }
            if (player.getMap().getId() == 922020000 || player.getMap().getId() == 222020111 || player.getMap().getId() == 970030020) {
                c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: The command is not avaiable in this map.", 280, 5));
                return true;
            } else if (sub[0].equals("help") || sub[0].equals("commands")) {
                player.message("============================================================");
                player.message("~..::|SharpStory Player Commands|::.. ~");
                player.message("============================================================");
                player.message("@stat                                  -- Displays stat related commands.");
                player.message("@mail                                  -- Displays offline mail commands.");
                player.message("@trade                                 -- Displays trade commands.");
                player.message("@rebirth                               -- Displays rebirth commands.");
                player.message("@npc                                   -- Displays the NPC/Shop commands.");
                player.message("@map                                   -- Displays the warping commands.");
                player.message("@donor                                 -- Displays donator commands.");
                player.message("@clearinv [slot]                       -- Deletes all items in the selected slot.");
                player.message("@unstuck                               -- Unstuck's you from being unable to click NPC's");
                player.message("@afk                                   -- Indicates that you are AFK to other players.");
                player.message("@strip                                 -- Takes off everything.");
                player.message("@callgm [message]                      -- Sends message to online GMs.");
                player.message("@save                                  -- Saves your character.");
                player.message("@uptime                                -- Shows time elasped since server has been last restarted.");
                player.message("@resetpotkeys                          -- Resets your hotkeys.");
                player.message("@song                                  -- Plays your custom song.");
                player.message("@dpstest                               -- Test's your DAMAGE PER SECOND (DPS)");
            } else if (sub[0].equals("ranks")) {
                player.message("~..::|SharpStory RANKING Commands|::.. ~");
                player.message("@dpsranks                              -- Displays DPS Ranking.");
                player.message("@overallranks                          -- Displays Overall Ranking");
                player.message("@pvpranks                              -- Comming soon");
                player.message("@expeditionranks                       -- Comming soon");
            } else if (sub[0].equals("stat")) {
                player.message("~..::|SharpStory STAT RELATED Commands|::.. ~");
                player.message("@checkstats                            -- Displays your stats.");
                player.message("@spy [name]                            -- Spies on someone else's stats.");
                player.message("@[stat] [number]                       -- Adds [number] to [str/dex/int/luk].");
                player.message("@[stat] -[number]                      -- Removes [number] from [str/dex/int/luk].");
            } else if (sub[0].equals("mail")) {
                player.message("~..::|SharpStory OFFLINE MAIL Commands|::.. ~");
                player.message("@checkinbox                            -- Opens your inbox.");
                player.message("@clearinbox                            -- Deletes all messages in your inbox.");
                player.message("@sendmail [player name] [message]      -- Sends player mail for 500,000,000 mesos.");
            } else if (sub[0].equals("trade")) {
                player.message("~..::|SharpStory TRADE Commands|::.. ~");
                player.message("@getcloud                              -- Trades mesos for cloud piece at the current transfer rate.");
                player.message("@getmesos                              -- Trades cloud piece for mesos at the current transfer rate.");
                player.message("@transferrate                          -- Informs you of the current meso to cloud transfer rate.");
                player.message("@crystaltoice                          -- Trades 1000 Crystal Shards for a ice piece.");
            } else if (sub[0].equals("rebirth")) {
                player.message("~..::|SharpStory REBIRTH Commands|::.. ~");
                player.message("@rebirthJOB                            -- Rebirths you without changing job.");
                player.message("@rebirthEXP                            -- Rebirths you into an explorer.");
                player.message("@rebirthCYG                            -- Rebirths you into an cygnus.");
                player.message("@rebirthARA                            -- Rebirths you into an aran.");
                player.message("@autorebirth                           -- Toggles Auto Rebirth System ON/OFF");
            } else if (sub[0].equals("npc")) {
                player.message("~..::|SharpStory NPC/SHOP Commands|::.. ~");
                player.message("@exchange                              -- Opens the crystal exchanger.");
                player.message("@shop                                  -- Opens the general shop.");
                player.message("@stylist                               -- Opens the stylist NPC.");
                player.message("@job                                   -- Opens the Job-Advancer NPC.");
                player.message("@nnc                                   -- Opens the NPC-Network-Center.");
                player.message("@transport                             -- Opens the Transportation NPC.");
            } else if (sub[0].equals("map")) {
                player.message("~..::|SharpStory WARPING Commands|::.. ~");
                player.message("@tele [place]                          -- Teleports you to the desired location.");
                player.message("@henesys                               -- Teleports you to henesys.");
                player.message("@fm                                    -- Warps you to the Free Market Entrance.");
                player.message("@joinevent                             -- Joins an event in progress.");
                player.message("@hangout                               -- Takes you to the hangout map.");
                player.message("@1337place                             -- Warps your to that 1337 Player NPC place.");
                player.message("@guildhq                               -- Takes you to your guild hideout.");
            } else if (sub[0].equals("donor")) {
                player.message("~..::|SharpStory DONOR Commands|::.. ~");
                player.message("@dnotice [message]                     -- Sends notice message to everyone.");
                player.message("@dmax                                  -- Maxes special skills.");
                player.message("@djob [jobid]                          -- Transforms you into job selection.");
                player.message("@djobhelp                              -- Displays job IDs.");
                player.message("@dtransform [id]                       -- Transforms you into selection.");
                player.message("@dtranshelp                            -- Displays the transformation IDs");
                player.message("@dbuffme                               -- Buffs you.");
                player.message("@dchalkboard <message>                  -- Sets chalkboard with custom message.");
                player.message("@dsmega <message>                       -- Super megaphones message.");
            } else if (sub[0].equals("clearinv")) {
                String type = sub[1];
                if (type.equals("equip")) {
                    for (int i = 0; i < 201; i++) {
                        IItem tempItem = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) i);
                        if (tempItem == null) {
                            continue;
                        }
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, (byte) i, tempItem.getQuantity(), false, true);
                    }
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Equipment Slot Cleared.", 280, 5));
                } else if (type.equals("use")) {
                    for (int i = 0; i < 201; i++) {
                        IItem tempItem = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((byte) i);
                        if (tempItem == null) {
                            continue;
                        }
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (byte) i, tempItem.getQuantity(), false, true);
                    }
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Use Slot Cleared.", 280, 5));
                } else if (type.equals("setup")) {
                    for (int i = 0; i < 101; i++) {
                        IItem tempItem = c.getPlayer().getInventory(MapleInventoryType.SETUP).getItem((byte) i);
                        if (tempItem == null) {
                            continue;
                        }
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.SETUP, (byte) i, tempItem.getQuantity(), false, true);
                    }
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Set-Up Slot Cleared.", 280, 5));
                } else if (type.equals("etc")) {
                    for (int i = 0; i < 201; i++) {
                        IItem tempItem = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem((byte) i);
                        if (tempItem == null || player.isClearInvException(tempItem.getItemId())) {
                            continue;
                        }
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, (byte) i, tempItem.getQuantity(), false, true);
                    }
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: ETC Slot Cleared.", 280, 5));
                } else if (type.equals("cash")) {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Cash Slot cannot be cleared.", 280, 5));
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Slot " + type + " does not exist! Try etc/use...", 280, 5));
                }
            } else if (sub[0].equals("unstuck")) {
                NPCScriptManager.getInstance().dispose(c);
                c.announce(MaplePacketCreator.enableActions());
                c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You're Fixed.", 280, 5));
            } else if (sub[0].equals("afk")) {
                player.setChalkboard("I'm AFK! SharpStory Rocks!");
                player.getMap().broadcastMessage(MaplePacketCreator.useChalkboard(player, false));
                player.getClient().announce(MaplePacketCreator.enableActions());
            } else if (sub[0].equals("strip")) {
                player.unequipEverything();
            } else if (sub[0].equals("callgm")) {
                int minimumTimeToWait = 5;
                long ts2 = player.getDelay("COMMAND_CALLGM");
                long ts1 = System.currentTimeMillis();
                int timeDif = (int) (ts1 - ts2) / 60000;
                if (timeDif >= minimumTimeToWait || timeDif < 0) {
                    c.getWorldServer().broadcastGMPacket(MaplePacketCreator.serverNotice(1, "[@callgm]" + player.getName() + ": " + joinStringFrom(sub, 1)));
                    player.addDelay("COMMAND_CALLGM", ts1);
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Message Sent!", 280, 5));
                } else {
                    c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                }
            } else if (sub[0].equals("save")) {
                int minimumTimeToWait = 5;
                long ts2 = player.getDelay("COMMAND_SAVE");
                long ts1 = System.currentTimeMillis();
                int timeDif = (int) (ts1 - ts2) / 60000;
                if (timeDif >= minimumTimeToWait || timeDif < 0) {
                    player.saveToDB(true);
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Your character has been saved! ", 280, 5));
                    player.addDelay("COMMAND_SAVE", ts1);
                } else {
                    c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                }
            } else if (sub[0].equals("uptime")) {
                player.dropMessage(Server.getInstance().getUptime());
            } else if (sub[0].equals("resetpotkeys")) {
                c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: " + player.resetPotKeys(), 280, 5));
            } else if (sub[0].equals("song")) {
                if (sub.length <= 1) {
                    player.dropMessage("To use this, enter @song followed by do, re, mi, pa, sol, la, or si.\r\nExample: @song do re mi");
                } else {
                    final String notes[] = sub;
                    final MapleCharacter musician = player;
                    int delay = 0;
                    for (int i = 1; i <= sub.length + 1; i++) {
                        final int j = i;
                        TimerManager.getInstance().schedule(new Runnable() {

                            public void run() {
                                if (notes[j].compareTo("do") == 0 || notes[j].compareTo("re") == 0 || notes[j].compareTo("mi") == 0 || notes[j].compareTo("pa") == 0 || notes[j].compareTo("sol") == 0 || notes[j].compareTo("la") == 0 || notes[j].compareTo("si") == 0) {
                                    musician.getClient().announce(MaplePacketCreator.playSound("orbis/" + notes[j]));
                                }
                            }
                        }, delay);

                        delay += 500;
                    }
                }
            } else if (sub[0].equals("dpstest")) {
                final int duration = 30;
                final MapleCharacter chr = player;
                if (!chr.isTestingDPS()) {
                    chr.toggleTestingDPS();
                    chr.getClient().getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Keep attacking the scarecrow for the next " + duration + " seconds.", 280, 5));
                    final MapleMonster monster = new MapleMonster(MapleLifeFactory.getMonster(9001007));
                    int distance;
                    int job = chr.getJob().getId();
                    if ((job >= 300 && job < 413) || (job >= 1300 && job < 1500) || (job >= 520 && job < 600)) {
                        distance = 125;
                    } else {
                        distance = 50;
                    }
                    Point p = new Point((int) chr.getPosition().getX() - distance, (int) chr.getPosition().getY());
                    monster.setBelongTo(chr);
                    final int newhp = Integer.MAX_VALUE;
                    MapleMonsterStats newstats = new MapleMonsterStats(monster.getStats());
                    newstats.setHp(newhp);
                    monster.setStats(newstats);
                    chr.getMap().spawnMonsterOnGroudBelow(monster, p);
                    TimerManager.getInstance().schedule(new Runnable() {

                        public void run() {
                            int health = monster.getHp();
                            chr.getMap().killMonster(monster, chr, false);
                            int dps = (newhp - health) / duration;
                            if (dps > chr.getDPS()) {
                                chr.getClient().getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: NEW RECORD: You damage per second is now " + dps + "!", 280, 5));
                                Server.getInstance().broadcastMessage(chr.getWorld(), MaplePacketCreator.serverNotice(6, "[Sharp]:: " + chr.getName() + " has set his new DPS record of " + dps + "!"));
                                try {
                                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET dps = ? WHERE id = ?");
                                    ps.setInt(1, dps);
                                    ps.setInt(2, chr.getId());
                                    ps.executeUpdate();
                                    ps.close();
                                } catch (Exception e) {
                                    System.out.println("Exception while saving DPS of " + chr.getName() + ": " + e);
                                }
                                chr.setDPS(dps);
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You damage per second is " + dps + "!", 280, 5));
                                chr.message("[Sharp]:: No new record has been set, not updating.");
                            }
                            chr.toggleTestingDPS();
                        }
                    }, duration * 1000);
                } else {
                    chr.getClient().getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You last DPS test is not yet complete!!", 280, 5));
                }
            } else if (sub[0].equals("dpsranks")) {
                int minimumTimeToWait = 1;
                long ts2 = player.getDelay("COMMAND_DPSRANKS");
                long ts1 = System.currentTimeMillis();
                int timeDif = (int) (ts1 - ts2) / 60000;
                if (timeDif >= minimumTimeToWait || timeDif < 0) {
                    player.getClient().getWorldServer().DPSRanking(player);
                    player.addDelay("COMMAND_DPSRANKS", ts1);
                } else {
                    c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                }
            } else if (sub[0].equals("overallranks")) {
                int minimumTimeToWait = 1;
                long ts2 = player.getDelay("COMMAND_ALLRANKS");
                long ts1 = System.currentTimeMillis();
                int timeDif = (int) (ts1 - ts2) / 60000;
                if (timeDif >= minimumTimeToWait || timeDif < 0) {
                    player.getClient().getWorldServer().AllRanking(player);
                    player.addDelay("COMMAND_ALLRANKS", ts1);
                } else {
                    c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                }
            } else if (sub[0].equals("checkstats")) {
                player.message("Your stats are:");
                player.message("Level: " + player.getLevel() + "  ||  Reborns: " + player.getReborns());
                player.message("Occupation: " + player.getOccupation() + " || Occupation Level: " + player.getOccupationLevel());
                player.message("WATT: " + player.getTotalWatk() + "  ||  MAGIC: " + player.getTotalMagic() + " ");
                player.message("Fame: " + player.getFame() + " || Vote Points: " + player.getVotePT());
                player.message("Str: " + player.getTotalStr() + "  ||  Dex: " + player.getTotalDex() + "  ||  Int: " + player.getTotalInt() + "  ||  Luk: " + player.getTotalLuk());
                player.message("Player has " + player.getMeso() + " mesos.");
                player.message("Crystal Shards: " + player.shardCount());
                player.message("EXP Rate: " + player.getExpRate() + " || MESO Rate: " + player.getMesoRate());
            } else if (sub[0].equals("spy")) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
                if (victim != null && victim.gmLevel() < 1) {
                    player.message(victim.getName() + "'s stats are:");
                    player.message("Level: " + victim.getLevel() + "  ||  Reborns: " + victim.getReborns());
                    player.message("Occupation: " + victim.getOccupation() + " || Occupation Level: " + victim.getOccupationLevel());
                    player.message("WATT: " + victim.getTotalWatk() + "  ||  MAGIC: " + victim.getTotalMagic() + " ");
                    player.message("Fame: " + victim.getFame() + " || Vote Points: " + victim.getVotePT());
                    player.message("Str: " + victim.getTotalStr() + "  ||  Dex: " + victim.getTotalDex() + "  ||  Int: " + victim.getTotalInt() + "  ||  Luk: " + victim.getTotalLuk());
                    player.message("Player has " + victim.getMeso() + " mesos.");
                    player.message("Crystal Shards: " + victim.shardCount());
                    player.message("EXP Rate: " + victim.getExpRate() + " || MESO Rate: " + victim.getMesoRate());
                } else {
                    if (victim.gmLevel() > 1) {
                        player.message("You can't spy on a GM.");
                    } else {
                        player.message("Player not found.");
                    }
                }
            } else if (sub[0].equalsIgnoreCase("str") || sub[0].equalsIgnoreCase("int") || sub[0].equalsIgnoreCase("luk") || sub[0].equalsIgnoreCase("dex")) {
                if (sub.length < 2) {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Please provide the amount you would like to add/remove!", 280, 5));
                }
                int amount = Integer.parseInt(sub[1]);
                boolean str = sub[0].equalsIgnoreCase("str");
                boolean Int = sub[0].equalsIgnoreCase("int");
                boolean luk = sub[0].equalsIgnoreCase("luk");
                boolean dex = sub[0].equalsIgnoreCase("dex");
                if (amount > 0 && amount <= player.getRemainingAp() && amount <= 32763 || amount < 0 && amount >= -32763 && Math.abs(amount) + player.getRemainingAp() <= 32767) {
                    if (str && amount + player.getStr() <= 32767 && amount + player.getStr() >= 4) {
                        player.setStr(player.getStr() + amount);
                        player.updateSingleStat(MapleStat.STR, player.getStr());
                        player.setRemainingAp(player.getRemainingAp() - amount);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have added #b " + amount + "#k to#r STR#k. ", 280, 5));
                    } else if (Int && amount + player.getInt() <= 32767 && amount + player.getInt() >= 4) {
                        player.setInt(player.getInt() + amount);
                        player.updateSingleStat(MapleStat.INT, player.getInt());
                        player.setRemainingAp(player.getRemainingAp() - amount);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have added #b " + amount + "#k to#r INT#k. ", 280, 5));
                    } else if (luk && amount + player.getLuk() <= 32767 && amount + player.getLuk() >= 4) {
                        player.setLuk(player.getLuk() + amount);
                        player.updateSingleStat(MapleStat.LUK, player.getLuk());
                        player.setRemainingAp(player.getRemainingAp() - amount);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have added #b " + amount + "#k to#r LUK#k. ", 280, 5));
                    } else if (dex && amount + player.getDex() <= 32767 && amount + player.getDex() >= 4) {
                        player.setDex(player.getDex() + amount);
                        player.updateSingleStat(MapleStat.DEX, player.getDex());
                        player.setRemainingAp(player.getRemainingAp() - amount);
                        player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have added #b " + amount + "#k to#r DEX#k. ", 280, 5));
                    } else {
                        player.dropMessage(1, "[Sharp]:: Please make sure the stat you are trying to raise is not over 32,767 or under 4.");
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Please make sure your AP is not over 32,767 and you have enough to distribute.", 280, 5));
                }
            } else if (sub[0].equals("checkinbox")) {
                player.showNotes();
            } else if (sub[0].equals("clearinbox")) {
                try {
                    int minimumTimeToWait = 5;
                    long ts2 = player.getDelay("COMMAND_CLEARINBOX");
                    long ts1 = System.currentTimeMillis();
                    int timeDif = (int) (ts1 - ts2) / 60000;
                    if (timeDif >= minimumTimeToWait || timeDif < 0) {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE notes SET `deleted` = 1 WHERE `to`=?");
                        ps.setString(1, player.getName());
                        ps.executeUpdate();
                        ps.close();
                        player.addDelay("COMMAND_CLEARINBOX", ts1);
                    } else {
                        c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                    }
                } catch (Exception e) {
                    System.out.println("Exception at clearinbox player command: " + e);
                }
            } else if (sub[0].equals("sendmail")) {
                String to = sub[1];
                if (to.equals(player.getName())) {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You can't send mail to yourself! ", 280, 5));
                } else {
                    String msg = joinStringFrom(sub, 2);
                    if (MapleCharacter.getIdByName(to) == -1) {
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Player does not exist!", 280, 5));
                    } else if (player.getMeso() < 500000000) {
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You do not have enough money!", 280, 5));
                    } else {
                        try {
                            player.gainMeso(-500000000, false);
                            c.getSession().write(MaplePacketCreator.sendHint(MapleCharacter.sendNoteStat(to, msg, player.getName(), (byte) 0), 280, 5));
                        } catch (Exception e) {
                        }
                        MapleCharacter receiver = player.getClient().getWorldServer().getPlayerStorage().getCharacterByName(to);
                        if (receiver != null) {
                            receiver.showNotes();
                        }
                    }
                }
            } else if (sub[0].equals("getcloud")) {
                if (player.getMeso() >= player.getClient().getChannelServer().getRate()) {
                    player.gainMeso(-1 * player.getClient().getChannelServer().getRate(), false);
                    player.gainItem(4001063, (short) 1, false);
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You don't have enough mesos! ", 280, 5));
                }
            } else if (sub[0].equals("getmeso")) {
                if (player.getItemQuantity(4001063, false) > 0 && player.getMeso() < (2147000000 - (player.getClient().getChannelServer().getRate()))) {
                    player.gainMeso(player.getClient().getChannelServer().getRate(), false);
                    player.gainItem(4001063, (short) -1, false);
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You don't have clouds or you have too much mesos already! ", 280, 5));
                }
            } else if (sub[0].equals("transferrate")) {
                c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Transfer Rate - " + player.getClient().getChannelServer().getRate(), 280, 5));
            } else if (sub[0].equals("crystaltoice")) {
                if (player.getItemQuantity(4031917, false) >= 1000) {
                    player.gainItem(4031917, (short) -1000, false);
                    player.gainItem(4000150, (short) 1, false);
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You don't have enough crystals! ", 280, 5));
                }
            } else if (sub[0].equals("rebirthjob")) {
                if (player.getLevel() > 199) {
                    player.doReborn();
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have to be level 200 to rebirth.", 280, 5));
                }
            } else if (sub[0].equals("rebirthexp")) {
                if (player.getLevel() > 199) {
                    player.doExpReborn();
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have to be level 200 to rebirth.", 280, 5));
                }
            } else if (sub[0].equals("rebirthcyg")) {
                if (player.getLevel() > 199) {
                    player.doCygReborn();
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have to be level 200 to rebirth.", 280, 5));
                }
            } else if (sub[0].equals("rebirthara")) {
                if (player.getLevel() > 199) {
                    player.doAranReborn();
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You have to be level 200 to rebirth.", 280, 5));
                }
            } else if (sub[0].equals("autorebirth")) {
                player.toggleAutoRebirth();
            } else if (sub[0].equals("exchange")) {
                NPCScriptManager.getInstance().start(c, 2013001, null, null, true);
            } else if (sub[0].equals("shop")) {
                MapleShopFactory.getInstance().getShopForNPC(9201011).sendShop(c);
            } else if (sub[0].equals("stylist")) {
                NPCScriptManager.getInstance().start(c, 9900001, null, null, true);
            } else if (sub[0].equals("job")) {
                NPCScriptManager.getInstance().start(c, 9000021, null, null, true);
            } else if (sub[0].equals("nnc")) {
                NPCScriptManager.getInstance().start(c, 1092095, null, null, true);
            } else if (sub[0].equals("transport")) {
                NPCScriptManager.getInstance().start(c, 9000020, null, null, true);
            } else if (sub[0].equalsIgnoreCase("tele") || sub[0].equalsIgnoreCase("goto") || sub[0].equalsIgnoreCase("go")) {
                HashMap<String, Integer> maps = new HashMap<String, Integer>();
                maps.put("henesys", 100000100);
                maps.put("ellinia", 101000000);
                maps.put("perion", 102000000);
                maps.put("kerning", 103000000);
                maps.put("lith", 104000000);
                maps.put("sleepywood", 105040300);
                maps.put("florina", 110000000);
                maps.put("orbis", 200000000);
                maps.put("happy", 209000000);
                maps.put("elnath", 211000000);
                maps.put("ereve", 130000000);
                maps.put("ludi", 220000000);
                maps.put("omega", 221000000);
                maps.put("korean", 222000000);
                maps.put("aqua", 230000000);
                maps.put("leafre", 240000000);
                maps.put("mulung", 250000000);
                maps.put("herb", 251000000);
                maps.put("nlc", 600000000);
                maps.put("shrine", 800000000);
                maps.put("showa", 801000000);
                maps.put("fm", 910000000);
                maps.put("fm1", 910000001);
                maps.put("fm2", 910000002);
                maps.put("fm3", 910000003);
                maps.put("fm4", 910000004);
                maps.put("fm5", 910000005); //Wow inefficient shit.
                maps.put("fm6", 910000006);
                maps.put("fm7", 910000007);
                maps.put("fm8", 910000008);
                maps.put("fm9", 910000009);
                maps.put("fm10", 910000010);
                maps.put("fm11", 910000011);
                maps.put("fm12", 910000012);
                maps.put("guild", 200000301);
                maps.put("fog", 105040306);
                if (sub.length != 2) {
                    player.dropMessage("Syntax: @tele <map name> or @tele help");
                } else if (maps.containsKey(sub[1])) {
                    int map = maps.get(sub[1]);
                    if (map / 910000000 == 1) {
                        player.saveLocation("FREE_MARKET");
                    }
                    player.changeMap(map);
                } else {
                    player.message("========================================================================");
                    player.message("                ..::| SharpStory Map Selections |::..                   ");
                    player.message("========================================================================");
                    player.message("| henesys | ellinia | perion | kerning | lith   | sleepywood | florina |");
                    player.message("| fog     | orbis   | happy  | elnath  | ereve  | ludi       | omega   |");
                    player.message("| korean  | aqua    | leafre | mulung  | herb   | nlc        | shrine  |");
                    player.message("| shower  | fm      | guild  | fm1, fm2, fm3...fm12 |");
                }
                maps.clear();

            } else if (sub[0].equals("henesys")) {
                player.changeMap(100000100, 0);
            } else if (sub[0].equals("fm")) {
                player.changeMap(910000000, 0);
            } else if (sub[0].equals("joinevent")) {
                if (player.getClient().getChannelServer().getCustomEventStatus() == true) {
                    player.changeMap(player.getClient().getChannelServer().getCustomEventMap(), 0);
                } else {
                    player.dropMessage(1, "There's no event in this channel at the moment to warp you to. Try another channel.");
                }
            } else if (sub[0].equals("hangout")) {
                player.changeMap(100000001, 0);
            } else if (sub[0].equals("1337place") || sub[0].equals("propeople")) {
                player.changeMap(109010200, 0);
            } else if (sub[0].equals("guildhq")) {
                if (player.getGuildId() > 0) {
                    int hqid = player.getGuildHQ();
                    if (hqid == 0) {
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Your guild does not have a headquarter!", 280, 5));
                    } else {
                        player.changeMap(hqid);
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Enjoy!~", 280, 5));
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: You are not in a guild!", 280, 5));
                }
            } else if (sub[0].equals("dnotice")) {
                if (player.isDonor()) {
                    Server.getInstance().broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(6, "[Donor][" + player.getName() + "] " + joinStringFrom(sub, 1)));
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else if (sub[0].equals("dmax")) {
                if (player.isDonor()) {
                    int minimumTimeToWait = 5;
                    long ts2 = player.getDelay("COMMAND_DONORMAX");
                    long ts1 = System.currentTimeMillis();
                    int timeDif = (int) (ts1 - ts2) / 60000;
                    if (timeDif >= minimumTimeToWait || timeDif < 0) {
                        int[] skills = {1013, 1017, 1019, 1031, 8001000, 8001001};
                        for (int i = 0; i < skills.length; i++) {
                            ISkill skill = SkillFactory.getSkill(skills[i]);
                            player.changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
                        }
                        c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Donor skills maxed! ", 280, 5));
                        player.addDelay("COMMAND_DONORMAX", ts1);
                    } else {
                        c.announce(MaplePacketCreator.serverNotice(1, "You cannot use this command for " + (minimumTimeToWait - timeDif) + " more minutes."));
                    }

                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else if (sub[0].equals("djob")) {
                if (player.isDonor()) {
                    if (!sub[1].equals("910") && !sub[1].equals("900")) {
                        player.changeJob(MapleJob.getById(Integer.parseInt(sub[1])));
                        player.equipChanged();
                    } else {
                        c.getSession().write(MaplePacketCreator.sendHint("You cannot have this job.", 280, 5));
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else if (sub[0].equals("djobhelp")) {
                String[] messagesToDrop = {"BEGINNER(0), DONOR JOB(800)",
                    "---------------------------------------------------------------------------",
                    "WARRIOR(100)",
                    "FIGHTER(110), CRUSADER(111), HERO(112)",
                    "PAGE(120), WHITEKNIGHT(121), PALADIN(122)",
                    "SPEARMAN(130), DRAGONKNIGHT(131), DARKKNIGHT(132)",
                    "---------------------------------------------------------------------------",
                    "MAGICIAN(200)",
                    "FP_WIZARD(210), FP_MAGE(211), FP_ARCHMAGE(212)",
                    "IL_WIZARD(220), IL_MAGE(221), IL_ARCHMAGE(222)",
                    "CLERIC(230), PRIEST(231), BISHOP(232)",
                    "---------------------------------------------------------------------------",
                    "BOWMAN(300)",
                    "HUNTER(310), RANGER(311), BOWMASTER(312)",
                    "CROSSBOWMAN(320), SNIPER(321), MARKSMAN(322)",
                    "---------------------------------------------------------------------------",
                    "THIEF(400)",
                    "ASSASSIN(410), HERMIT(411), NIGHTLORD(412)",
                    "BANDIT(420), CHIEFBANDIT(421), SHADOWER(422)",
                    "---------------------------------------------------------------------------",
                    "PIRATE(500)",
                    "BRAWLER(510), MARAUDER(511), BUCCANEER(512)",
                    "GUNSLINGER(520), OUTLAW(521), CORSAIR(522)",
                    "---------------------------------------------------------------------------",
                    "NOBLESSE(1000)",
                    "DAWNWARRIOR(1100), JOB2(1110), JOB3(1111), JOB4(1112)",
                    "BLAZEWIZARD(1200), JOB2(1210), JOB3(1211), JOB4(1212)",
                    "WINDARCHER(1300), JOB2(1310), JOB3(1311), JOB4(1312)",
                    "NIGHTWALKER(1400), JOB2(1410), JOB3(1411), JOB4(1412)",
                    "THUNDERBREAKER(1500), JOB2(1510), JOB3(1511), JOB4(1512)",
                    "---------------------------------------------------------------------------",
                    "LEGEND(2000)",
                    "ARAN1(2100), ARAN2(2110), ARAN3(2111), ARAN4(2112)"};
                for (int i = 0; i < messagesToDrop.length; i++) {
                    player.message(messagesToDrop[i]);
                }
            } else if (sub[0].equals("dtransform")) {
                if (player.isDonor()) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    ii.getItemEffect(Integer.parseInt("22100" + sub[1])).applyTo(player);
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }

            } else if (sub[0].equals("dtranshelp")) {
                String[] messagesToDrop = {"00 - Orange Mushroom Piece", "01 - Ribbon Pig Piece", "02 - Grey Piece", "03 - Dragon Elixir", "05 - Tigun Transformation Bundle", "06 - Rainbow-Colored Snail Shell", "07 - Ghost", "08 - Ghost Candy", "09 - Sophillia's Abandoned Doll", "10 - Potion of Transformation", "11 - Potion of Transformation ", "12 - Change to Mouse", "16 - Mini Draco Transformation", "17 - Moon", "18 - Moon Bunny", "21 - Gaga", "22 - Old Guy", "30 - Very Old Guy", "32 - Cody's Picture", "33 - Cake Picture", "34 - Alien Gray", "35 - Pissed Off Penguin", "36 - Smart Ass Penguin", "37 - Big Blade Penguin", "38 - Big Blade Penguin v2.0", "39 - Stupid Penguin", "43 - Freaky Worm"};
                for (int i = 0; i < messagesToDrop.length; i++) {
                    player.message(messagesToDrop[i]);
                }
            } else if (sub[0].equals("dbuffme")) {
                if (player.isDonor()) {
                    int minimumTimeToWait = 5;
                    long ts2 = player.getDelay("COMMAND_DONORBUFF");
                    long ts1 = System.currentTimeMillis();
                    int timeDif = (int) (ts1 - ts2) / 60000;
                    if (timeDif >= minimumTimeToWait || timeDif < 0) {
                        int[] skills = {Beginner.ECHO_OF_HERO, SuperGM.BLESS, SuperGM.HASTE, SuperGM.HYPER_BODY, Aran.HEROS_WILL, Aran.MAPLE_WARRIOR, Swordsman.IRON_BODY, Fighter.POWER_GUARD, Fighter.RAGE, Spearman.IRON_WILL, Page.POWER_GUARD, Crusader.ARMOR_CRASH, WhiteKnight.MAGIC_CRASH, DragonKnight.POWER_CRASH, Hero.ENRAGE, Paladin.STANCE, DarkKnight.HEX_OF_BEHOLDER, Magician.MAGIC_ARMOR, Magician.MAGIC_GUARD, ILWizard.MEDITATION, Cleric.INVINCIBLE, ILArchMage.INFINITY, ILArchMage.MANA_REFLECTION, Archer.FOCUS, Hunter.SOUL_ARROW, Crossbowman.SOUL_ARROW, Bowmaster.CONCENTRATE, Bowmaster.SHARP_EYES, NightLord.SHADOW_STARS, Hermit.SHADOW_PARTNER, ChiefBandit.MESO_GUARD};
                        for (int i = 0; i < skills.length; i++) {
                            try {
                                player.giveSkill(skills[i]);
                            } catch (Exception e) {
                                continue;
                            }
                        }
                        player.addDelay("COMMAND_DONORBUFF", ts1);
                    } else {
                        player.dropMessage(6, "You may not buff yourself for " + (minimumTimeToWait - timeDif) + " more minutes.");
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else if (sub[0].equals("dchalkboard")) {
                if (player.isDonor()) {
                    player.setChalkboard(joinStringFrom(sub, 1));
                    player.getMap().broadcastMessage(MaplePacketCreator.useChalkboard(player, false));
                    player.getClient().announce(MaplePacketCreator.enableActions());
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else if (sub[0].equals("dsmega")) {
                if (player.isDonor()) {
                    int minimumTimeToWait = 2; //seconds
                    long ts2 = player.getDelay("COMMAND_DSMEGA");
                    long ts1 = System.currentTimeMillis();
                    int timeDif = (int) (ts1 - ts2) / 1000;
                    if (timeDif >= minimumTimeToWait || timeDif < 0) {
                        for (MapleCharacter online : player.getClient().getWorldServer().getPlayerStorage().getAllCharacters()) {
                            online.getClient().getSession().write(MaplePacketCreator.serverNotice(3, c.getChannel(), "" + c.getPlayer().getName() + " : " + joinStringFrom(sub, 1)));
                        }
                        player.addDelay("COMMAND_DSMEGA", ts1);
                    } else {
                        player.dropMessage(6, "You may smega for " + (minimumTimeToWait - timeDif) + " more minutes.");
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Command only avaliable to Donors!", 280, 5));
                }
            } else {
                if (player.gmLevel() == 0) {
                    c.getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: The command " + heading + sub[0] + " does not exist.", 280, 5));
                }
                return false;
            }// If it comes out of all the if statements and does not reach the false statement, return true.
            // It's confusing when you have lots of commands so I thought i needed to add this in.
            return true;
        }

        return false;
    }

    public static boolean executeGMCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        Channel cserv = c.getChannelServer();
        Server srv = Server.getInstance();
        if (sub[0].equals("commands")) {
            player.message("~-~-~-~-~-~-~-");
            player.message("GM Commands : ");
            player.message("~-~-~-~-~-~-~-");
            player.message("NOTICE - NOT ALL WON'T COMMANDS WILL WORK IF YOU ARE NOT THE ADMIN!");
            player.message("!item [itemid] [amt]       -- Gives you [amt] [itemid].");
            player.message("!buffme                    -- Gives you a series of buffs.");
            player.message("!ap [amt]                  -- Gives you [amt] AP");
            player.message("!sp [amt]                  -- Gives you [amt] SP");
            player.message("!warpallhere               -- Warps everyone in the channel to your location.");
            player.message("!warphere [player]         -- Warps [player] to your location. ");
            player.message("/c    [player]             -- Warps you to [player].");
            player.message("/m    [mapID/Name]         -- Warps you to [map].");
            player.message("!online                    -- Displays a list of all online characters");
            player.message("!heal                      -- Restore you HP/MP to your maxHP/maxMP");
            player.message("!warpoxtop                 -- Warps players on the TOP area out of the OXMap");
            player.message("!warpoxmiddle              -- Warps players on the MIDDLE area out of the OXMap");
            player.message("!warpoxright               -- Warps players on the RIGHT area out of the OXMap");
            player.message("!warpoxleft                -- Warps players on the LEFT area out of the OXMap");
            player.message("!killall                   -- Kills all mobs in your map.");
            player.message("!search [category] [name]  -- Searches the id of [name] under [category. E.g: !search npc snail");
            player.message("!dc [player]               -- Disconnects [player] from the server.");
            player.message("!clock [time]              -- Creates a clock with [time] seconds left.");
            player.message("!watch [ign]               -- Watch's the player's chat.");
            player.message("!watch [ign] stop          -- Stops watching player.");
            player.message("!mute [ign]                -- Mutes the player");
            player.message("!unmute [ign]              -- Unmutes the player.");
            player.message("!speakall [message]        -- Makes players in map speak given message");
            player.message("!mutemap [ign]             -- Mutes map.");
            player.message("!unmutemap                 -- Unmutes map.");
            player.message("!transhelp                 -- Gives you transformation IDs");
            player.message("!trans  [id ]              -- Transforms you.");
            player.message("!watch all                 -- Watch's everyone.");
            player.message("!watch clear               -- Stops watching everyone.");
            player.message("!meso [amt]                -- Gives you [amt] mesos.");
            player.message("!mesoperson [person] [amt] -- Gives [player] [amt] mesos.");
            player.message("!jail [player]             -- Sends [player] to jail.");
            player.message("!unjail [player]           -- Releases [player] from jail.");
            player.message("!killmap                   -- Kills every player on your map.");
            player.message("!spawn [mobid] [amt]       -- Spawns [amt] number of mob [mobid]");
            player.message("!drop [itemid] [amt]       -- Drops [amt] number of [itemid].");
            player.message("!npc [npcid]               -- Creates a temporary NPC [npcid] on your location. Disappears when server restarts.");
            player.message("!setall [amt]              -- Sets all your stats to [amt].");
            player.message("!maxstat                   -- Maxes all your stats.");
            player.message("!event                     -- Starts an event on yout map.");
            player.message("!startevent                -- Do not use this for custom events. Use !eventhelp for more info.");
            player.message("!level [amt]               -- Changes your level to [amt].");
            player.message("!levelperson [player] [amt]-- Changes [player]'s level to [amt]");
            player.message("!job [jobid]               -- Changes your job to [jobid].");
            player.message("!jobperson [player] [jobid]-- Changes [player]'s job to [jobid].");
            player.message("!servermessage [msg]       -- Sets the banner on top to [msg]");
            player.message("!notice [notice]           -- Sends a notice [notice] to all players.");
            player.message("!ban [player] [reason]     -- Bans [player] and IP from connection to the game. Works online and offline.");
            player.message("!unban [player]            -- Unbans [player].");
            player.message("!customitem                -- itemid, watt, matt, str, dex, int, luk, line1, line2, line3.");
            player.message("!cheaters                  -- Displays potential hackers.");
            player.message("!rape [ign]                -- LoL. Don't do it..");
            player.message("!eventhelp                 -- All the event help you need.");
            player.message("!warptoevent               -- Warps the map to the event map. Do not use for custom events.");
            player.message("!gmlog [limit]             -- Retrives GM Log.");
            player.message("!gmchat [message]          -- GMServer use, unnecessary for SharpStory");
            player.message("!deleteskill [skillid]     -- Deletes skillid");
            player.message("!clearlog [limit]          -- Clears GM Log");
            player.message("!deletechar [name]         -- Deletes character");
            player.message("!terminal [command]        -- Runs terminal command");
            player.message("!spymail [player]          -- Spies on player's inbox");
            player.message("!dividemap                 -- Divides players in the map into two teams.");
            player.message("!supermob [mobid] [n]      -- Spawns mob that is n times stronger.");
        } else if (sub[0].equals("pos")) {
            player.message("X:" + player.getPosition().getX());
            player.message("Y:" + player.getPosition().getY());
        } else if (sub[0].equals("supermob")) {
            int multiplier = Integer.parseInt(sub[1]);
            if (multiplier < 0) {
                multiplier = 1;
            }
            MapleMonster monster = new MapleMonster(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])));
            MapleMonsterStats newstats = new MapleMonsterStats(monster.getStats());  
            newstats.setHp(monster.getHp() * multiplier);
            newstats.setMp(monster.getMp() * multiplier);
            newstats.setExp(monster.getExp() * multiplier);
            monster.setStats(newstats);
            player.getMap().spawnMonsterOnGroudBelow(monster, player.getPosition());
        } else if (sub[0].equals("dividemap")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.setIntoTeam();
            }
        } else if (sub[0].equals("deleteskill")) {

            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM notes WHERE `to`=? AND `deleted` = 0", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ps.setString(1, sub[1]);
                ResultSet rs = ps.executeQuery();
                rs.last();
                int count = rs.getRow();
                if (count == 0) {
                    player.getClient().getSession().write(MaplePacketCreator.sendHint("#e[#rSharp#k]:: Player's inbox is empty! ", 280, 5));
                }
                rs.first();
                player.getClient().announce(MaplePacketCreator.showNotes(rs, count));
                rs.close();
                ps.close();
            } catch (SQLException e) {
            }
        } else if (sub[0].equals("deleteskill")) {
            ISkill skill = SkillFactory.getSkill(Integer.parseInt(sub[1]));
            player.changeSkillLevel(skill, (byte) -1, skill.getMaxLevel(), -1);
        } else if (sub[0].equals("gmchat")) {
            //String message = joinStringFrom(sub, 1);
            //Server.getInstance().gmChat(player.getName() + " : " + message, null); //LOL GMServer
        } else if (sub[0].equals("eventhelp")) {
            player.message("===~EVENT HELP~===");
            player.message("For a event like snowball, use '!event snowball' in any map of choice.");
            player.message("After everyone gathers, you use '!dividemap' to divide the map into teams.");
            player.message("Then, use '!warptoevent' to warp the players to the event, and finally '!startevent' to start the event.");
            player.message("For custom events, just use !event without any additional words and you can host any event you want.");
            player.message("!event snowball/fitness/ola/coconut");
        } else if (sub[0].equals("ap")) {
            player.setRemainingAp(Integer.parseInt(sub[1]));
        } else if (sub[0].equals("clock")) {
            player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(sub, 1, 60)));
        } else if (sub[0].equals("buffme")) {
            int[] skills = {Beginner.ECHO_OF_HERO, SuperGM.BLESS, SuperGM.HASTE, SuperGM.HYPER_BODY, Aran.HEROS_WILL, Aran.MAPLE_WARRIOR, Swordsman.IRON_BODY, Fighter.POWER_GUARD, Fighter.RAGE, Spearman.IRON_WILL, Page.POWER_GUARD, Crusader.ARMOR_CRASH, WhiteKnight.MAGIC_CRASH, DragonKnight.POWER_CRASH, Hero.ENRAGE, Paladin.STANCE, DarkKnight.HEX_OF_BEHOLDER, Magician.MAGIC_ARMOR, Magician.MAGIC_GUARD, ILWizard.MEDITATION, Cleric.INVINCIBLE, ILArchMage.INFINITY, ILArchMage.MANA_REFLECTION, Archer.FOCUS, Hunter.SOUL_ARROW, Crossbowman.SOUL_ARROW, Bowmaster.CONCENTRATE, Bowmaster.SHARP_EYES, NightLord.SHADOW_STARS, Hermit.SHADOW_PARTNER, ChiefBandit.MESO_GUARD};
            for (int i = 0; i < skills.length; i++) {
                try {
                    player.giveSkill(skills[i]);
                } catch (Exception e) {
                    continue;
                }
            }
        } else if (sub[0].equals("mute") || sub[0].equals("unmute")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            if (victim != null) {
                if (sub[0].equals("mute")) {
                    victim.shutUp();
                } else {
                    victim.allowChat();
                }
            } else {
                player.message("Player not found.");
            }
        } else if (sub[0].equals("mutemap")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                if (map.gmLevel() < 1) {
                    map.shutUp();
                }
            }
        } else if (sub[0].equals("unmutemap")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.allowChat();
            }
        } else if (sub[0].equals("speakall")) {
            String text = joinStringFrom(sub, 1);
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                mch.getMap().broadcastMessage(MaplePacketCreator.getChatText(mch.getId(), text, false, 0));
            }
        } else if (sub[0].equals("spawn")) {
            if (sub.length > 2) {
                for (int i = 0; i < Integer.parseInt(sub[2]); i++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])), player.getPosition());
                }
            } else {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])), player.getPosition());
            }
        } else if (sub[0].equals("unjail")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int mapid = 100000100; //Henesys 
            if (sub.length > 2 && sub[1].equals("2")) {
                mapid = 100000100;
                victim = cserv.getPlayerStorage().getCharacterByName(sub[2]);
            }
            if (victim != null) {
                MapleMap target = cserv.getMapFactory().getMap(mapid);
                MaplePortal targetPortal = target.getPortal(0);
                victim.changeMap(target, targetPortal);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Jail]" + victim.getName() + " was un-jailed. Let's hope he/she had learnt his lesson."));

            } else {
                player.message(sub[1] + " not found!");
            }
        } else if (sub[0].equals("killmap")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                if (map.isGM() == false) {
                    map.setHp(0);
                    map.updateSingleStat(MapleStat.HP, 0);
                }
            }
        } else if (sub[0].equals("jail")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int mapid = 922020000; //Forgotten Darkness
            if (sub.length > 2 && sub[1].equals("2")) {
                mapid = 922020000;
                victim = cserv.getPlayerStorage().getCharacterByName(sub[2]);
            }
            if (victim != null) {
                MapleMap target = cserv.getMapFactory().getMap(mapid);
                MaplePortal targetPortal = target.getPortal(0);
                victim.changeMap(target, targetPortal);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Jail]" + victim.getName() + " was jailed, the player will be there until future notice."));
                victim.message("You gotz own'd");
            } else {
                player.message(sub[1] + " not found!");
            }
        } else if (sub[0].equals("cleardrops")) {
            player.getMap().clearDrops(player);
        } else if (sub[0].equals("dc")) {
            MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);;
            if (player.gmLevel() > target.gmLevel()) {
                target.getClient().disconnect();
            } else {
                player.message("Ass.");
            }
        } else if (sub[0].equals("warpoxtop") || sub[0].equals("warpoxleft") || sub[0].equals("warpoxright") || sub[0].equals("warpoxmiddle")) {
            if (player.getMap().getId() == 109020001) {
                if (sub[0].equals("warpoxtop")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y <= -206 && !person.isGM()) {
                            person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                        }
                    }
                    player.message("Top Warpped Out.");
                } else if (sub[0].equals("warpoxleft")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -952 && person.getPosition().x <= -308 && !person.isGM()) {
                            person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                        }
                    }
                    player.message("Left Warpped Out.");
                } else if (sub[0].equals("warpoxright")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -142 && person.getPosition().x <= 502 && !person.isGM()) {
                            person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                        }
                    }
                    player.message("Right Warpped Out.");
                } else if (sub[0].equals("warpoxmiddle")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 274 && person.getPosition().x >= -308 && person.getPosition().x <= -142 && !person.isGM()) {
                            person.changeMap(person.getMap().getReturnMap(), person.getMap().getReturnMap().getPortal(0));
                        }
                    }
                    player.message("Middle Warpped Out.");
                }
            } else {
                player.message("These commands can only be used in the OX Map.");
            }
        } else if (sub[0].equals("warpallhere")) {
            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() != player.getMapId()) {
                    mch.changeMap(player.getMap(), player.getPosition());
                }
            }
        } else if (sub[0].equals("rape")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            List<Pair<MapleBuffStat, Integer>> list = new ArrayList<Pair<MapleBuffStat, Integer>>();
            list.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MORPH, 8));
            list.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.CONFUSE, 1));
            victim.announce(MaplePacketCreator.giveBuff(0, 0, list));
            victim.getMap().broadcastMessage(player, MaplePacketCreator.giveForeignBuff(victim.getId(), list), false);
        } else if (sub[0].equals("warphere")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).changeMap(player.getMap(), player.getMap().findClosestSpawnpoint(player.getPosition()));
        } else if (sub[0].equalsIgnoreCase("warpmap")) {

            try {
                for (MapleCharacter tobewarped : player.getMap().getCharacters()) {
                    tobewarped.changeMap(c.getChannelServer().getMapFactory().getMap(Integer.valueOf(sub[1])));
                }
            } catch (Exception e) {
                System.out.println("Failed to warp map [" + player.getName() + "]");
            }
        } else if (sub[0].equals("fame")) {
            MapleCharacter famevictim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            famevictim.setFame(Integer.parseInt(sub[2]));
            famevictim.updateSingleStat(MapleStat.FAME, famevictim.getFame());
        } else if (sub[0].equals("gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        } else if (sub[0].equals("heal")) {
            player.setHpMp(30000);
        } else if (sub[0].equals("watch")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            if (sub.length == 3) {
                if (victim != null) {
                    victim.clearWatcher();
                    player.message("You stopped watching " + victim.getName());
                } else {
                    player.message("This player isn't in the same channel as you or logged off");
                }
            } else if (sub.length == 2) {
                if (sub[1].equalsIgnoreCase("clear")) {
                    for (MapleCharacter chars : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chars.getWatcher() == player) {
                            chars.clearWatcher();
                        }
                    }
                    player.message("You stopped watching everyone in your channel.");
                } else if (sub[1].equalsIgnoreCase("all")) {
                    for (MapleCharacter chars : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chars.getWatcher() == null && chars != player) {
                            chars.setWatcher(player);
                        }
                    }
                    player.message("You started watching everyone in your channel. Use '!watch clear' to stop.");
                } else {
                    if (victim != null) {
                        if (victim.getWatcher() == null) {
                            victim.setWatcher(player);
                            player.message("You started watching " + victim.getName());
                        } else {
                            if (victim.getWatcher().getName() != null) {
                                player.message(victim.getWatcher().getName() + " is already watching this player. Use !watch " + victim.getName() + " clear so you can watch this player instead");
                            } else {
                                player.message("Someone is already watching this player. Use !watch " + victim.getName() + " clear so you can watch this player instead");
                            }
                        }
                    } else {
                        player.message("This player isn't in the same channel as you or logged off");
                    }
                }
            } else {
                player.message("Syntax: !watch <ign> / !watch <ign> stop / !watch all / !watch clear");
            }
        } else if (sub[0].equals("dtranshelp")) {
            String[] messagesToDrop = {"00 - Orange Mushroom Piece", "01 - Ribbon Pig Piece", "02 - Grey Piece", "03 - Dragon Elixir", "05 - Tigun Transformation Bundle", "06 - Rainbow-Colored Snail Shell", "07 - Ghost", "08 - Ghost Candy", "09 - Sophillia's Abandoned Doll", "10 - Potion of Transformation", "11 - Potion of Transformation ", "12 - Change to Mouse", "16 - Mini Draco Transformation", "17 - Moon", "18 - Moon Bunny", "21 - Gaga", "22 - Old Guy", "30 - Very Old Guy", "32 - Cody's Picture", "33 - Cake Picture", "34 - Alien Gray", "35 - Pissed Off Penguin", "36 - Smart Ass Penguin", "37 - Big Blade Penguin", "38 - Big Blade Penguin v2.0", "39 - Stupid Penguin", "43 - Freaky Worm"};
            for (int i = 0; i < messagesToDrop.length; i++) {
                player.message(messagesToDrop[i]);
            }
        } else if (sub[0].equals("transform")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            ii.getItemEffect(Integer.parseInt("22100" + sub[1])).applyTo(player);
        } else if (sub[0].equalsIgnoreCase("search")) {
            StringBuilder sb = new StringBuilder();
            sb.append("#e");
            if (sub.length > 2) {
                String search = joinStringFrom(sub, 2);
                long start = System.currentTimeMillis();//for the lulz 
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz"));
                if (!sub[1].equalsIgnoreCase("ITEM")) {
                    if (sub[1].equalsIgnoreCase("NPC")) {
                        data = dataProvider.getData("Npc.img");
                    } else if (sub[1].equalsIgnoreCase("MOB") || sub[1].equalsIgnoreCase("MONSTER")) {
                        data = dataProvider.getData("Mob.img");
                    } else if (sub[1].equalsIgnoreCase("SKILL")) {
                        data = dataProvider.getData("Skill.img");
                    } else if (sub[1].equalsIgnoreCase("MAP")) {
                        sb.append("#e#rUse /m <map name> to search for maps!");
                    } else {
                        sb.append("#bInvalid search.\r\nSyntax: '!search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
                    }
                    if (data != null) {
                        String name;
                        for (MapleData searchData : data.getChildren()) {
                            name = MapleDataTool.getString(searchData.getChildByPath("name"), "NO-NAME");
                            if (name.toLowerCase().contains(search.toLowerCase())) {
                                sb.append("#b").append(Integer.parseInt(searchData.getName())).append("#k - #r").append(name).append("\r\n");
                            }
                        }
                    }
                } else {
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (sb.length() < 32654) {//ohlol 
                            if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                                //#v").append(id).append("# #k-  
                                sb.append("#b").append(itemPair.getLeft()).append("#k - #r").append(itemPair.getRight()).append("\r\n");
                            }
                        } else {
                            sb.append("#bCouldn't load all items, there are too many results.\r\n");
                            break;
                        }
                    }
                }
                if (sb.length() == 0) {
                    sb.append("#bNo ").append(sub[1].toLowerCase()).append("s found.\r\n");
                }

                sb.append("\r\n#kLoaded within ").append((double) (System.currentTimeMillis() - start) / 1000).append(" seconds.");//because I can, and it's free 

            } else {
                sb.append("#bInvalid search.\r\nSyntax: '!search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
            }
            c.announce(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, sb.toString(), "00 00", (byte) 0));
        } else if (sub[0].equals("item") || sub[0].equals("drop")) {
            int itemId = Integer.parseInt(sub[1]);
            short quantity = 1;
            try {
                quantity = Short.parseShort(sub[2]);
            } catch (Exception e) {
            }
            if (sub[0].equals("item")) {
                int petid = -1;
                if (ItemConstants.isPet(itemId)) {
                    petid = MaplePet.createPet(itemId);
                }
                MapleInventoryManipulator.addById(c, itemId, quantity, " " + player.getName(), petid, -1);
            } else {
                IItem toDrop;
                if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = MapleItemInformationProvider.getInstance().getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (byte) 0, quantity);
                }
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
        } else if (sub[0].equals("job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(sub[1])));
            player.equipChanged();
        } else if (sub[0].equals("kill")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).setHpMp(0);
        } else if (sub[0].equals("killall")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                player.getMap().killMonster(monster, player, true);
                //monster.giveExpToCharacter(player, monster.getExp() * c.getPlayer().getExpRate(), true, 1);
                monster.giveExpToCharacter(player, monster.getExp(), true, 1);
            }
            player.message("Killed " + monsters.size() + " monsters.");
        } else if (sub[0].equals("unbug")) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.enableActions());
        } else if (sub[0].equals("level")) {
            player.setLevel(Integer.parseInt(sub[1]));
            player.gainExp(-player.getExp(), false, false);
            player.updateSingleStat(MapleStat.LEVEL, player.getLevel());
        } else if (sub[0].equals("levelperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setLevel(Integer.parseInt(sub[2]));
            victim.gainExp(-victim.getExp(), false, false);
            victim.updateSingleStat(MapleStat.LEVEL, victim.getLevel());
        } else if (sub[0].equals("levelpro")) {
            while (player.getLevel() < Math.min(255, Integer.parseInt(sub[1]))) {
                player.levelUp(false);
            }
        } else if (sub[0].equals("levelup")) {
            player.levelUp(false);
        } else if (sub[0].equals("maxstat")) {
            final String[] s = {"setall", String.valueOf(Short.MAX_VALUE)};
            executeGMCommand(c, s, heading);
            player.setLevel(255);
            player.setFame(13337);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.setOccLevel(100);
            player.setReborns(1337);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.FAME, 13337);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
        } else if (sub[0].equals("mesoperson")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).gainMeso(Integer.parseInt(sub[2]), true);
        } else if (sub[0].equals("mesos")) {
            player.gainMeso(Integer.parseInt(sub[1]), true);
        } else if (sub[0].equals("notice")) {
            Server.getInstance().broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(6, "[" + player.getName() + "] " + joinStringFrom(sub, 1)));
        } else if (sub[0].equals("openportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(true);
        } else if (sub[0].equals("closeportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(false);
        } else if (sub[0].equals("startevent")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.getMap().startEvent(map);
            }
        } else if (sub[0].equals("occupation")) {
            player.setOccupation(Integer.parseInt(sub[1]));
        } else if (sub[0].equals("event")) {
            if (player.getClient().getChannelServer().getCustomEventStatus() == false) {
                if (sub[1].equals("ola")) {
                    c.getChannelServer().setEvent(new MapleEvent(109030101, 50)); // Wrong map but still Ola Ola
                } else if (sub[1].equals("fitness")) {
                    c.getChannelServer().setEvent(new MapleEvent(109040000, 50));
                } else if (sub[1].equals("snowball")) {
                    c.getChannelServer().setEvent(new MapleEvent(109060001, 50));
                } else if (sub[1].equals("coconut")) {
                    c.getChannelServer().setEvent(new MapleEvent(109080000, 50));
                }
                int mapid = getOptionalIntArg(sub, 1, c.getPlayer().getMapId());
                player.getClient().getChannelServer().setCustomEventStatus(true);
                player.getClient().getChannelServer().setCustomEventMap(mapid);
                try {
                    Server.getInstance().broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(6, "[Event] An event has started in Channel " + c.getChannel() + " at " + player.getMap().getMapName() + "! Use @joinevent to join it. Note: You must be in Channel " + c.getChannel() + " for it to work!"));
                } catch (Exception e) {
                }
            } else {
                player.getClient().getChannelServer().setCustomEventStatus(false);
                try {
                    Server.getInstance().broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(6, "[Event] The event has ended. Thanks to all of those who participated."));
                } catch (Exception e) {
                }
            }

            c.getChannelServer().setEvent(null);
        } else if (sub[0].equals("online")) {
            for (Channel ch : srv.getChannelsFromWorld(player.getWorld())) {
                String s = "Characters online (Channel " + ch.getId() + " Online: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                    for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                        s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                    }
                    player.dropMessage(s.substring(0, s.length() - 2));
                }
            }
        } else if (sub[0].equals("cheaters")) {
            String nibs = "Cheaters : ";
            for (MapleCharacter mpchr : player.getClient().getWorldServer().getNoobStorage().getAllCharacters()) {
                nibs += MapleCharacter.makeMapleReadable(mpchr.getName()) + ", ";
            }
            player.dropMessage(nibs.substring(0, nibs.length() - 2));
        } else if (sub[0].equals("jobperson")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.changeJob(MapleJob.getById(Integer.parseInt(sub[2])));
            player.equipChanged();
        } else if (sub[0].equals("pap")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8500001), player.getPosition());
        } else if (sub[0].equals("pianus")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
        } else if (sub[0].equals("servermessage")) {
            c.getWorldServer().setServerMessage(joinStringFrom(sub, 1));
        } else if (sub[0].equals("warptoevent")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.changeMap(c.getChannelServer().getEvent().getMapId(), player.getTeam());
            }
        } else if (sub[0].equals("warpola")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.changeMap(109030101, player.getTeam());
            }
        } else if (sub[0].equals("warpfitness")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.changeMap(109040000, player.getTeam());
            }
        } else if (sub[0].equals("warpcoconut")) {
            for (MapleCharacter map : player.getMap().getCharacters()) {
                map.changeMap(109080000, player.getTeam());
            }
        } else if (sub[0].equals("setall")) {
            final int x = Short.parseShort(sub[1]);
            player.setStr(x);
            player.setDex(x);
            player.setInt(x);
            player.setLuk(x);
            player.updateSingleStat(MapleStat.STR, x);
            player.updateSingleStat(MapleStat.DEX, x);
            player.updateSingleStat(MapleStat.INT, x);
            player.updateSingleStat(MapleStat.LUK, x);
        } else if (sub[0].equals("gmlog")) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * from gmlog LIMIT " + sub[1]);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    player.message("No logs to display!");
                } else {
                    player.message(rs.getString("name") + " used command \"" + rs.getString("command") + "\" on " + rs.getTimestamp("when") + " ");
                }
                while (rs.next()) {
                    player.message(rs.getString("name") + " used command \"" + rs.getString("command") + "\" on " + rs.getTimestamp("when") + " ");
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                System.out.println("Exception - " + e);
            }
        } else if (sub[0].equals("sp")) {
            player.setRemainingSp(Integer.parseInt(sub[1]));
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (sub[0].equals("unban")) {
            try {
                int accid = MapleCharacter.getAccIdByName(sub[1]);

                PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET banned = -1 WHERE id = " + accid);
                p.executeUpdate();
                p.close();

                PreparedStatement psbip = DatabaseConnection.getConnection().prepareStatement("SELECT ip FROM iplog WHERE accountid = ?");
                psbip.setInt(1, accid);
                ResultSet rsbip = psbip.executeQuery();
                String ip = "";
                if (rsbip.next()) {
                    ip = rsbip.getString("ip");
                }
                rsbip.close();
                psbip.close();


                p = DatabaseConnection.getConnection().prepareStatement("DELETE FROM ipbans WHERE ip = " + ip);
                p.executeUpdate();
                p.close();

                player.message("Unbanned Account ID: " + accid);

            } catch (Exception e) {
                player.message("Failed to unban " + sub[1]);
                return true;
            }
            player.message("Unbanned Account - " + sub[1]);
        } else if (sub[0].equals("ban")) {
            if (sub[1].equals(player.getName())) {
                player.dropMessage("You're so stupid, don't ban yourself!");
            }
            boolean banned = MapleCharacter.ban(sub[1], joinStringFrom(sub, 2), false);
            if (banned) {
                player.message("Successfully banned player, make sure you post proof on the forums!");
            } else {
                player.message("Problem with banning player, check your input and make sure you have proof!");
            }
        } else {
            return false;
        }
        return true;
    }

    public static void executeAdminCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        if (player.gmLevel() <= 4) {
            player.message("You may not use these commands.");
        } else if (sub[0].equals("terminal")) {
            try {
                Runtime.getRuntime().exec(joinStringFrom(sub, 1));
            } catch (Exception e) {
                player.message("" + e);
            }
        } else if (sub[0].equals("customitem")) {
            int id = Integer.parseInt(sub[1]);
            int wa = Integer.parseInt(sub[2]);
            int matt = Integer.parseInt(sub[3]);
            int str = Integer.parseInt(sub[4]);
            int dex = Integer.parseInt(sub[5]);
            int intel = Integer.parseInt(sub[6]);
            int luk = Integer.parseInt(sub[7]);
            int line1 = Integer.parseInt(sub[8]);
            int line2 = Integer.parseInt(sub[9]);
            int line3 = Integer.parseInt(sub[10]);
            AbstractPlayerInteraction ab = new AbstractPlayerInteraction(c);
            ab.gainEqWithStats(id, wa, matt, str, dex, intel, luk, line1, line2, line3);
        } else if (sub[0].equals("horntail")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8810026), player.getPosition());
        } else if (sub[0].equals("pmob")) {
            int npcId = Integer.parseInt(sub[1]);
            int mobTime = Integer.parseInt(sub[2]);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (sub[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equals("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(ypos);
                mob.setRx0(xpos + 50);
                mob.setRx1(xpos - 50);
                mob.setFh(fh);
                try {
                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    player.message("Failed to save MOB to the database");
                }
                player.getMap().addMonsterSpawn(mob, mobTime, 0);
            } else {
                player.message("You have entered an invalid Mob-Id");
            }

        } else if (sub[0].equals("deletechar")) {
            boolean lol = MapleClient.deleteChar(MapleCharacter.getIdByName(sub[1]));
            if (lol) {
                player.message("Deleted character.");
            } else {
                player.message("Character did not get deleted. There was a problem.");
            }
        } else if (sub[0].equals("pnpc")) {
            int npcId = Integer.parseInt(sub[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(ypos);
                npc.setRx0(xpos + 50);
                npc.setRx1(xpos - 50);
                npc.setFh(fh);
                try {
                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "n");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    player.message("Failed to save NPC to the database");
                }
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                player.message("You have entered an invalid Npc-Id");
            }
        } else if (sub[0].equals("packet")) {
            player.getMap().broadcastMessage(MaplePacketCreator.customPacket(joinStringFrom(sub, 1)));
        } else if (sub[0].equals("warpworld")) {
            Server server = Server.getInstance();
            byte world = Byte.parseByte(sub[1]);
            if (world <= (server.getWorlds().size() - 1)) {
                try {
                    String[] socket = server.getIP(world, c.getChannel()).split(":");
                    c.getWorldServer().removePlayer(player);
                    player.getMap().removePlayer(player);//LOL FORGOT THIS ><
                    c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                    player.setWorld(world);
                    player.saveToDB(true);//To set the new world :O (true because else 2 player instances are created, one in both worlds)
                    c.announce(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
                } catch (Exception ex) {
                    player.message("Error when trying to change worlds, are you sure the world you are trying to warp to has the same amount of channels.");
                }

            } else {
                player.message("Invalid world; highest number available: " + (server.getWorlds().size() - 1));
            }

        } else if (sub[0].equals("npc")) {
            MapleNPC npc = MapleLifeFactory.getNPC(Integer.parseInt(sub[1]));
            if (npc != null) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            }
        } else if (sub[0].equals("pinkbean")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8820009), player.getPosition());
        } else if (sub[0].equals("playernpc")) {
            player.playerNPC(c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]), Integer.parseInt(sub[2]));
        } else if (sub[0].equals("setgmlevel")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.setGM(Integer.parseInt(sub[2]));
            player.message("Done.");
            victim.getClient().disconnect();
        } else if (sub[0].equals("setdonor")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
            victim.setDonor(Integer.parseInt(sub[2]));
            player.message("Done.");
            victim.getClient().disconnect();
        } else if (sub[0].equals("shutdown")) {
            Server.getInstance().shutdown(false).run();
        } else if (sub[0].equals("restart")) {
            Server.getInstance().shutdown(true).run();
        } else if (sub[0].equals("exprate")) {
            c.getWorldServer().setExpRate(((Integer.parseInt(sub[1]))));
            for (MapleCharacter mc : c.getWorldServer().getPlayerStorage().getAllCharacters()) {
                mc.setRates();
            }
        } else if (sub[0].equals("mesorate")) {
            c.getWorldServer().setMesoRate(((Integer.parseInt(sub[1]))));
            for (MapleCharacter mc : c.getWorldServer().getPlayerStorage().getAllCharacters()) {
                mc.setRates();
            }
        } else if (sub[0].equals("clearlog")) {
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("delete from gmlog LIMIT " + sub[1]);
                ps.executeUpdate();
                ps.close();
                player.message("Cleared the first " + sub[1] + " matches");
            } catch (Exception e) {
                System.out.println("Exception - " + e);
            }
        } else if (sub[0].equals("sql")) {
            final String query = Commands.joinStringFrom(sub, 1);
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
                ps.executeUpdate();
                ps.close();
                player.message("Done " + query);
            } catch (SQLException e) {
                player.message("Query Failed: " + query);
            }
        } else if (sub[0].equals("sqlwithresult")) {
            String name = sub[1];
            final String query = Commands.joinStringFrom(sub, 2);
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    player.message(String.valueOf(rs.getObject(name)));
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                player.message("Query Failed: " + query);
            }
        } else if (sub[0].equals("zakum")) {
            player.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            for (int x = 8800003; x < 8800011; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition());
            }
        } else if (sub[0].equals("itemvac")) {
            List<MapleMapObject> items = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject item : items) {
                MapleMapItem mapitem = (MapleMapItem) item;
                if (!MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                    continue;
                }
                mapitem.setPickedUp(true);
                player.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, player.getId()), mapitem.getPosition());
                player.getMap().removeMapObject(item);
                player.getMap().nullifyObject(item);

            }
        } else {
            player.yellowMessage("Command " + heading + sub[0] + " does not exist.");
        }
    }

    private static String joinStringFrom(String arr[], int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != arr.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    private static int getOptionalIntArg(String sub[], int position, int def) {
        if (sub.length > position) {
            try {
                return Integer.parseInt(sub[position]);
            } catch (NumberFormatException nfe) {
                return def;
            }
        }
        return def;
    }
}
