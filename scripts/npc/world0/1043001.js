var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    var abandonedpets = cm.getDeletedPetsString();
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
            cm.sendSimple("#eWould you like to abandon one of your pet in this bush? \r\n\
#d\t\t\t\t\t    Total Abandoned Pets - "+abandonedpets+"\r\n\
\r\n\
#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L1337#Yes.\r\n\
#L0#No."); 
        } else if (status == 1) { 
            if (selection == 0) {
                cm.sendOk("#eGood. You're doing the right thing.");
                cm.dispose();
            } else {
                var plist = cm.PetList(cm.getClient());
                if (plist != "") {
                    var intro = "#ePlease choose the pet you want to abandon :(\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                    cm.sendSimple(intro+plist);
                } else {
                    cm.sendOk("#e#rYou don't even have any pets.");
                    cm.dispose();
                }
            }
        } else if (status == 2) {
            cm.deletePet(selection);
            cm.sendOk("#e#rAbandoning pets is not the right thing to do. Just saying.");
            cm.dispose();
        }
    }  
}