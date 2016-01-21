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
        status
    } else if (status == 1) {
        cm.sendSimple("#eHello, I'm the #rSharpStory#k NX Seller!\r\n\#bWhat package would you like?#k \r\n\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\ #L0# 10,000 NX Cash - 200,000,000 Mesos #l \r\n #L1# 30,000 NX Cash - 500,000,000 Mesos#l");
    } else if (status == 2) {
        if (selection == 0) {
           if (cm.getMeso() > 199999999) {
            cm.sendOk("#eThank you for using my service!");
	    cm.gainMeso(-200000000);
            cm.getPlayer().getCashShop().gainCash(4, 10000);
            cm.dispose();
	} else {
		cm.sendOk("#eYou do not have enough mesos!");
		cm.dispose();
	}
        } else if (selection == 1) {
             if (cm.getMeso() > 499999999) {
            cm.sendOk("#eThank you for using my service!");
	    cm.gainMeso(-500000000);
	    cm.getPlayer().getCashShop().gainCash(4, 30000);
            cm.dispose();
	} else {
		cm.sendOk("#eYou do not have enough mesos!");
		cm.dispose();
	}
        }
    }
}  