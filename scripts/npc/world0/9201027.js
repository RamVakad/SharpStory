var status = 0;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {       
    if (mode == -1) {
        cm.dispose();
    
    }else if (mode == 0){
        cm.sendOk ("#eOkay, talk to me when you want to earn some belts!");
        cm.dispose();

    }else{             
        if (mode == 1)
            status++;
        else
            status--;
        
        if (status == 0) {
        cm.sendNext("#eHello, I'm the Belt System of #rSharpStory#k.");
        }else if (status == 1) { 
          cm.sendSimple ("#eIf you want to spend your rebirths, pick one -#e#d" +
                 "#k\r\n#L0##b#v1132000# - [50 Rebirths] All stats + 1000, 500 WA/MA" +
                 "#k\r\n#L1##b#v1132001# - [200 Rebirths] All Stats + 3500, 1000 WA/MA" +
                 "#k\r\n#L2##b#v1132002# - [400 Rebirths] All Stats + 7000, 1500 WA/MA" +
                 "#k\r\n#L3##b#v1132003# - [500 Rebirths] All Stats + 10000, 2000 WA/MA" +
                 "#k\r\n#L4##b#v1132004# - [750 Rebirths] All Stats + 15000, 3000 WA/MA");

            } else if (selection == 0) {  
            if(cm.getPlayer().getReborns() > 49) {
            cm.gainEqWithStat(1132000, 500, 500, 1000, 1000, 1000, 1000, 0, 0, 0);
	    cm.getPlayer().setReborns(cm.getPlayer().getReborns() - 50);
            cm.dispose();
               } else {
             cm.sendOk ("#eYou must have #r5 Rebirths#k to get this belt!");
             cm.dispose();
             }
            } else if (selection == 1) {  
            if(cm.getPlayer().getReborns() > 199) {
	    cm.gainEqWithStat(1132001, 1000, 1000, 3500, 3500, 3500, 3500, 0, 0, 0);
	    cm.getPlayer().setReborns(cm.getPlayer().getReborns() - 200);
            cm.dispose();
               } else {
             cm.sendOk ("#eYou must have #r20 Rebirths#k to get this belt!");
             cm.dispose();
             }
            } else if (selection == 2) {  
            if(cm.getPlayer().getReborns() > 399) {
            cm.gainEqWithStat(1132002, 1500, 1500, 7000, 7000, 7000, 7000, 0, 0, 0);
	    cm.getPlayer().setReborns(cm.getPlayer().getReborns() - 400);
            cm.dispose();
               } else {
             cm.sendOk ("#eYou must have #r50 Rebirths#k to get this belt!");
             cm.dispose();
             }
            } else if (selection == 3) {  
            if(cm.getPlayer().getReborns() > 499) {
            cm.gainEqWithStat(1132003, 2000, 2000, 10000, 10000, 10000, 10000, 0, 0, 0);
	    cm.getPlayer().setReborns(cm.getPlayer().getReborns() - 500);
            cm.dispose();
               } else {
             cm.sendOk ("#eYou must have #r50 Rebirths#k to get this belt!");
             cm.dispose();
             }
            } else if (selection == 4) {  
            if(cm.getPlayer().getReborns() > 749) {
            cm.gainEqWithStat(1132004, 3000, 3000, 15000, 15000, 15000, 15000, 0, 0, 0);
	    cm.getPlayer().setReborns(cm.getPlayer().getReborns() - 750);
            cm.dispose();
               } else {
             cm.sendOk ("#eYou must have #r75 Rebirths#k to get this belt!");
             cm.dispose();
             }
            }
}
}