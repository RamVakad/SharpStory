var opt;
var status;

function start() {
    status = 0;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    }else{
        status--;
    }
    if (status == 0) {
        cm.dispose();
    } else if (status == 1) {
        cm.sendSimple("#eHello! Would you like to change your occupation to a Casher? \r\n\
#rNOTE#k - #bYour occupation level must be 100 and will be RESET!#k\r\n\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\ #L0# Yes. #l \r\n #L1# No. #l");
    } else if (status == 2) {
        opt = selection;
cm.sendYesNo("#e#r\t\t\t\t\t\t\t\t\t\t\t  Casher\r\n\
#bBenefit#k - As a Casher, you will have a chance to gain #rMAPLE POINTS#k when you kill a monster.\r\nEach occupation level will increase the quantity of benefit gained.");
    } else if (status == 3) {
        if (opt == 0) {
            cm.getPlayer().setOccupation(1); 
            cm.sendOk("#eYou won't regret this!");
            cm.dispose();
        } else {
            cm.sendOk("#eToo bad for you then.");
            cm.dispose();
        }
    }
}  