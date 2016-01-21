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
            cm.sendSimple("#eWelcome to the #rCurrency Exchange#k NPC! What would you like to exchange? \r\n\
\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#Exchange 500 #v4031917# for a #v4031050#\r\n\
#L1#Exchange 750 #v4031917# for a #v4000415#\r\n\
#L2#Exchange 1000 #v4031917# for a #v4000150#"); 
        } else if (status == 1) { 
            if (selection==0) {
                if (cm.itemQuantity(4031917) > 499) {
                    cm.gainItem(4031917, -500);
                    cm.gainItem(4031050, 1);
                    cm.sendOk("#e#bThank you for using my service!");
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rYou don't have enough #v4031917#");
                    cm.dispose();
                }
            } else if (selection==1) {
                if (cm.itemQuantity(4031917) > 749) {
                    cm.gainItem(4031917, -750);
                    cm.gainItem(4000415, 1);
                    cm.sendOk("#e#bThank you for using my service!");
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rYou don't have enough #v4031917#");
                    cm.dispose();
                }
            } else if (selection==2) {
                if (cm.itemQuantity(4031917) > 999) {
                    cm.gainItem(4031917, -1000);
                    cm.gainItem(4000150, 1);
                    cm.sendOk("#e#bThank you for using my service!");
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rYou don't have enough #v4031917#");
                    cm.dispose();
                }
            }
        }
    }
}