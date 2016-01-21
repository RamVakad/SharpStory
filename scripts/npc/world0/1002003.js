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
            cm.sendAcceptDecline("#eHello, I'm the #bSharpStory#k Max Stat Item NPC v2!#k\r\n#rPlease Meet these Requirements: \r\n\r\n#b1 #v1112401#\r\n1 #v1112000#\r\n\r\n#rYou must have an equipment in your inventory!");
        } else if (status == 1) {
            if (cm.itemQuantity(1112000) > 0 && cm.itemQuantity(1112401) > 0 && cm.EquipList(cm.getClient()) != ""){
                var String = "#e#rPlease choose the item you want to make your new MSI, it will be edited to have 30,000 STR, DEX, INT, LUK - #k\r\n\r\n#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                cm.sendSimple(String+cm.EquipList(cm.getClient()));
            } else  {
                cm.sendOk ("#e#rSorry, but you don't meet the requirements to do this procedure!");
                cm.dispose(); 
            }
        } else if (status == 2) { 
            if (cm.getPlayer().getEquipId(selection) != 1112000) {
                cm.gainItem(1112000, -1);
            }
            if (cm.getPlayer().getEquipId(selection) != 1112401) {
                cm.gainItem(1112401, -1);
            }
            cm.editEq(selection, 0, 0, 30000, 30000, 30000, 30000, true);
            cm.sendOk("#e#rAlright, I hope you come back!");
            cm.dispose();	
        }			
    }
}