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
        cm.sendSimple("#e#kHello, I'm the #rSharpStory#k Skill Maxer!\r\n\#bWould you like me to max your skills?#k \r\n\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\ #L0# Yes. #l \r\n #L1# No. #l");
    } else if (status == 2) {
        if (selection == 0) {
            cm.sendOk(cm.getPlayer().maxMastery());
            cm.dispose();
        } else if (selection == 1) {
            cm.sendOk("#eHmph.");
            cm.dispose();
        }
    }
}  













