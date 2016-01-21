var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == 1) { 
        status++; 
    }else{ 
        status--; 
    } if (status == -1) {
cm.dispose();
}
    if (status == 0) { 
        cm.sendNext("#eHey #r#h ##k, did Sharp send you here?");
    } else if (status == 1) { 
        cm.sendNextPrev("#e#rMore like forced me here!#k...Who are you?", 2); 
    } else if (status == 2) { 
        cm.sendNextPrev("#eYou mean Sharp didn't tell you? I'm going to be your fitness trainer!\r\nI'm going to help you get get #rstrong#k, for a price...");
    } else if (status == 3) {
	cm.sendNextPrev("#eWhat? I have to pay now? #rHell no#k.", 2);
    } else if (status == 4) {
	cm.sendNextPrev("#e#bLet me give you some tips#k:\r\n\r\n\
If you're going to survive out there, you have to be #rstrong#klike me, ok?\r\n\r\n\
Every monster you kill will drop #rCrystal Shards#k.\r\n\
Those shards are the the currency of #bSharpStory#k.\r\n\r\n\
#dIf you have them, you get what you want. If you don't, then you don't.");		
    } else if (status == 5) {
	cm.sendNextPrev("#eAlright, what do you need me to do?\r\nIt doesn't seem like I have a choice anyway...",2);
    } else if (status == 6) {
	cm.sendOk("#eFirst, I want you to reach level 20 right here in this room, then I want you to talk to my agent, #bP#k, and give her 15\r\n#rCrystal Shards#k.\r\n\r\n\
She will guide you out, and then you're free to do whatever you want.\r\n\r\n\
#dTalk to me again to hear my prepared speech!");
	cm.dispose();
    }
}