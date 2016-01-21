function start() { 
var transrate = cm.getWithCommas(cm.getTransRate());
    cm.sendSimple("#eWelcome to the #rMesos Converter#k NPC! I can store your mesos here and I will let you hold on to a piece of me until you retrive your mesos.\r\n\
#r\t\t\t  NOTE - The Transfer Rate keeps changing.\r\n\
#d\t\t\t\t\t    Transfer Rate - "+transrate+"\r\n\
\r\n\
#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L1#Store Mesos.\r\n\
#L0#Retrive Mesos."); 
}
function action(mode, type, selection){ 
    cm.dispose(); 
    if (mode == 1) { 
        if (selection == 0) { 
            if(cm.itemQuantity(4001063) > 0 && cm.getMeso() < (2147000000 -(cm.getTransRate()))) { 
                cm.gainMeso(cm.getTransRate()); 
                cm.gainItem(4001063,-1);  
                cm.sendOk("#eThank you for using my service, I hope to see you again!"); 
            } else { 
                cm.sendOk("#eSorry, you either don't have a #kCrystal Shard#k or you have too much mesos in your inventory!"); 
            } 

            } else if (selection == 1) { 
            if (cm.getMeso() >= cm.getTransRate()) { 
                cm.gainMeso(-1 * cm.getTransRate()); 
                cm.gainItem(4001063,1); 
                cm.sendOk("#eThank you for using my service, I hope to see you again!"); 
            } else { 
                cm.sendOk("#eSorry, you don't have enough mesos!"); 
            } 
        } 
}
    } 
  