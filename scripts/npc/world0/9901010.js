var status = 0;

function start() {
    cm.sendNext("#eHey, I'm giving out #rfree smegas#k to players everyday!");
}

function action(mode, type, selection) {
    if (mode == 0) {
        cm.sendOk("#e#bFine, Your loss is someone else's gain...")
        cm.dispose();
    }else {
        if(mode > 0)
            status++;
        else if(mode < 0)
            cm.dispose();
        if (status == 1) {
            if (cm.getGiftLog('FreeGift') >= 1) {
                cm.sendOk("#eI'm sorry, you have already received your gift today! Please come back later!");
                cm.dispose();
            }else
                cm.sendYesNo("#eYou haven't received your #rFREE Megaphones#k today, do you want to get your free reward for playing now?");
        }else if (status == 2) {
            cm.gainItem(5072000, 10);
            cm.setBossLog('FreeGift');
            cm.sendOk("#eI hope you enjoy!");
            cm.dispose();
        } else
            cm.sendOk("#e#bFine, Your loss is someone else's gain...")
    }
}