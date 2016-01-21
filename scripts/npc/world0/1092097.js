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
            if (cm.getClient().getChannelServer().isEventAvaliable()) {
                cm.sendSimple("#eWould you like to participate in the event? \r\n\
\r\n\
#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L1337#Yes.\r\n\
#L0#No."); 
            } else {
                cm.sendOk("#eNo event is not avaliable at the moment, try again later.");
                cm.dispose();
            }
        } else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("#eYou're missing out on all the rewards!!!");
                cm.dispose();
            } else {
                cm.sendOk(cm.participateInEvent()); //will dispose in function if already participated
            }
        } else if (status == 2) {
            cm.warpToRandomJQ();
            cm.dispose();
        }
    }  
}