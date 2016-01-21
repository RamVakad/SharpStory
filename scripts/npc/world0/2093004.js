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
            if (cm.getClient().getWorldServer().getParticipation(cm.getPlayer().getName())) {
                cm.sendOk("#eYou made it this far! Press ok to recieve your reward.");
            } else {
                cm.getPlayer().changeMap(100000100);
                cm.sendOk("#e#rYour name is no longer in the event participants list, you cannot take longer than two hours on this jump quest.");
                cm.dispose();
            }
        } else if (status == 1) {
            cm.getPlayer().changeMap(100000100);
            cm.sendOk(cm.eventReward());
            cm.dispose();
        }
    }  
}