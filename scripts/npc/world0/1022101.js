/*
	By Mac
	Max Stat Item NPC
        AIM:darkriuxd MSN:darkriuxd@hotmail.com
*/

importPackage(net.sf.odinms.client);

var status = 0;
var selected = 1;
var wui = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendAcceptDecline("#eHello, I'm the #rSharpStory#k Max Stat Item NPC!#k\r\n#rPlease Meet these Requirements: \r\n\r\n#bSTR - 30,000\r\n\DEX - 30,000\r\n\LUK - 30,000\r\n\INT - 30,000#k\r\n#b1x #v4031759#\r\n#b5 Vote Point\r\n");
        } else if (status == 1) {
            if (cm.getPlayer().getStr() > 29999 && cm.getPlayer().getDex() > 29999 && cm.getPlayer().getInt() > 29999 && cm.getPlayer().getLuk() > 29999 && cm.itemQuantity(4031759) > 0 && cm.getPlayer().getVotePT() > 4){
                cm.sendSimple("#e#rPlease choose what MSI you want -#k\r\n\
\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L1112401#NON-NX Ring - #v1112401#\r\n\
#L1112000#NX Ring - #v1112000#");
            } else  {
                cm.sendOk ("#e#dI'm sorry, but you don't meet the requirements to use my service. You can get the Subani Ankh from the Reward NPC!");
                cm.dispose(); 
            }
        } else if (status == 2) { 
            cm.gainEqWithStat(selection, 0, 0, 30000, 30000, 30000, 30000, 0, 0, 0);
            cm.getPlayer().setStr((cm.getPlayer().getStr() - 30000));
            cm.getPlayer().setDex((cm.getPlayer().getDex() - 30000));
            cm.getPlayer().setLuk((cm.getPlayer().getLuk() - 30000));
            cm.getPlayer().setInt((cm.getPlayer().getInt() - 30000));
            if (cm.getPlayer().getStr() < 4) cm.getPlayer().setStr(4);
            if (cm.getPlayer().getDex() < 4) cm.getPlayer().setDex(4);
            if (cm.getPlayer().getLuk() < 4) cm.getPlayer().setLuk(4);
            if (cm.getPlayer().getInt() < 4) cm.getPlayer().setInt(4);
            cm.gainItem(4031759, -1);
            cm.getPlayer().takeVotePT(5);	
            cm.sendOk("#e#rAlright, I hope you come back!");
            cm.dispose();
        }
    }
}


