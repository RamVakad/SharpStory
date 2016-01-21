var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) { 
            cm.sendSimple("#eWelcome to the #rSharpStory#k Mount Trader!\r\n\
#dPick your slave -#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0# Exchange 4 vote points for #v9102065#\r\n\
#L1# Exchange 4 vote points for #v9102066#\r\n\
#L2# Exchange 4 vote points for #v9101673#\r\n\
#L3# Exchange 4 vote points for #v9101753#\r\n\
#L4# Exchange 4 vote points for #v9101925#\r\n\
#L5# Exchange 4 vote points for #v9101747#\r\n\
#L6# Exchange 4 vote points for #v9102106#\r\n\
#b#L7# Exchange 8 vote points for a Maple Racing Kart\r\n\
#b#L8# Exchange 8 vote points for a Robot"); 
        } else if (status == 1) { 
            if (selection > 6) {
                if (cm.getPlayer().getVotePT() > 7) {
                    cm.gainItem(4031050, -1);
                    cm.gainItem(4000415, -1);
                    cm.gainItem(4000150, -1);
                    cm.getPlayer().takeVotePT(8);
                    if (selection == 7) {
                        cm.gainItem(1902036,1);
                        cm.gainItem(1912029,1);
                    } else if (selection == 8) {
                        cm.gainItem(1902021,1);
                        cm.gainItem(1912014,1);
                    }
                    cm.sendOk("#e#rEnjoy your slave~!");
                    cm.dispose();
                }
            } else {
                if (cm.getPlayer().getVotePT() > 3) {
                    cm.gainItem(4031050, -1);
                    cm.gainItem(4000415, -1);
                    cm.gainItem(4000150, -1);
                    cm.getPlayer().takeVotePT(4);
                    if (selection == 0) {
                        cm.gainItem(1902038,1);
                        cm.gainItem(1912031,1);
                    } else if (selection == 1) {
                        cm.gainItem(1902039,1);
                        cm.gainItem(1912032,1);
                    } else if (selection == 2) {
                        cm.gainItem(1902009,1);
                        cm.gainItem(1912004,1);
                    } else if (selection == 3) {
                        cm.gainItem(1902012,1);
                        cm.gainItem(1912008,1);
                    } else if (selection == 4) {
                        cm.gainItem(1902020,1);
                        cm.gainItem(1912013,1);
                    } else if (selection == 5) {
                        cm.gainItem(1902011,1);
                        cm.gainItem(1912007,1);
                    } else if (selection == 6) {
                        cm.gainItem(1902008,1);
                        cm.gainItem(1912003,1);
                    }
                    cm.sendOk("#e#rEnjoy your slave~!");
                    if (cm.getGuild() != null) cm.getGuild().gainGP(5);
                    cm.dispose();
                } else {
                    cm.sendOk("#eYou do not have the required items or enough vote points.");
                    cm.dispose();
                }
            }
        }
    }  
}
