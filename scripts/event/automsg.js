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
var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis())
        nextTime += 60 * 1000 * 2;
    setupTask = em.scheduleAtTimestamp("start", nextTime);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    scheduleNew();
    var Message = new Array("Type @help in the chat bar to view all the avaliable player commands.", "Typing @joinevent when an event is in progress will take you to the event location.", "The NPC-Network-Manager will give you access to almost all other NPC's.", "Player command @nnc will open the NPC-Network-Manager.", "You can trade your Mesos/Crystals using commands instead of NPCs.", "A list of RARE NX items can be found in the forums.", "Vote-Points can be gained online by voting, or playing for five hours straight.", "You cannot use the auction system as storage!", "Auctions expire if left unrenewed for more than a week.", "The character information box a special section that displays information of rebirths, crystal shards, ETC.", "You can type in @shop to open the general shop.", "You can always change your default Hotkeys by visiting the Hotkey NPC!", "Hotkeys can also be called POTKEYS, since they use potions as identifiers!");
    em.getChannelServer().yellowWorldMessage("[Sharp]:: " + Message[Math.floor(Math.random() * Message.length)]);
}