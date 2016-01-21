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
        cm.sendYesNo("#eDon't you want to get out of here?"); 
    } else if (status == 1) { 
        if (cm.getPlayer().getLevel() > 19) {
	cm.sendYesNo("#eDo you have what I need?");
} else {
	cm.sendOk("#e#rGet to level 20 first!");
	cm.dispose();
}
    } else if (status == 2) { 
        if(cm.itemQuantity(4031917) > 14) {
	cm.sendOk("#eI just need to tell you one last thing!\r\n\r\n\
#bWhen you get out, type in #r@help#b into your chat box. It is a must!\r\n\r\n\
#dThat's it, talk to the job advancer and skill maxer as soon as you get out of here and check out what #rSharpStory#d is all about.\r\n\r\n\
#rGood luck on your journey!!#k");
} else {
	cm.sendOk("#eYou don't have the 15 #rCrystal Shards#k!");
cm.dispose();
}
    } else if (status == 3) {
	cm.gainItem(4031917, -15);
	cm.gainMeso(100000000);
	cm.getPlayer().changeMap(100000100, 0);
	cm.dispose();
}
}