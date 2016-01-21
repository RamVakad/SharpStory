var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) { 
            cm.sendSimple("#eWhat rankings would you like to view?\r\n\
#b\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#Guild Rankings\r\n\
#L1#Character Rankings"); 
        } else if (status == 1) { 
            if (selection == 0) { 
                cm.displayGuildRanks();
                cm.dispose();
            } else if (selection == 1) { 
                cm.sendOk(cm.ranking()); 
                cm.dispose(); 
            } 
        }
    }  
}