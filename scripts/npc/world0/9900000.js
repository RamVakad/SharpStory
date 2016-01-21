var status; 
var slot;
var newpot;
var newact;

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) { 
            cm.sendSimple("#eWelcome, I'm the #rHotkey NPC#k, and which hotkey action would you like to change?\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
" + cm.getPlayer().showPotKeys()); 
        } else if (status == 1) {
            if (selection == 5) {
                cm.sendOk(cm.getPlayer().resetPotKeys());
                cm.dispose();
            } else {
                slot = selection;
                String = "#ePlease select the item you wish to replace as your hotkey -\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                if (cm.UseList(cm.getClient()) != "") {
                    cm.sendSimple(String+cm.UseList(cm.getClient()));
                } else {
                    cm.sendOk("#e#rYou must have an item in your USE inventory!");
                }
            }
        } else if (status == 2) {
            newpot = cm.getPlayer().getUseId(selection);
            cm.sendSimple("#ePlease select the behaviour of the hotkey -\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0##b\r\n\
#L1092095#NPC Network Manager\r\n\
#L13337#General Shop\r\n\
#L9040011#Bulletin Board\r\n\
#L9030000#All-In-One-Shop\r\n\
#L9000021#Job Advancer\r\n\
#L9001100#Skill Maxer\r\n\
#L9000020#Transportation Manager\r\n\
#L9000019#Reward NPC\r\n\
#L9900001#Stylist\r\n\
#L2013001#Mesos Converter\r\n\
#L9201000#Currency Exchange\r\n\
#L1052015#NX Seller\r\n\
#L9101000#Chair Seller\r\n\
#L1204020#Godly Timeless Trader\r\n\
#L1052014#Slave Trader\r\n\
#L1052013#Item Upgrader\r\n\
#L9000036#Vote Point Trader\r\n\
#L9270033#Super Stat Item Maker\r\n\
#L9201049#Hero Items\r\n\
#L2042007#Godly Crystal System\r\n\
#L9040008#Ranking\r\n\
#L9201024#Item Auction NPC\r\n\
#L9201026#Equipment Auction NPC\r\n\
#L9201025#Item Potential NPC\r\n\
#L9201023#Auction Income Dropbox NPC\r\n\
#L9201142#Guild Headquarters NPC\r\n\
#L9201027#Rebirth System\r\n\
#L9900000#Hotkey NPC\r\n\
#L9201082#Event Trophy NPC\r\n\
#L1043001#Abandon Pet\r\n\
#L1022101#Max Stat Item Maker\r\n\
#L1002003#Max Stat Item v2\r\n\
#L1092097#Event NPC");
        } else if (status == 3) {
            newact = selection;
            cm.getPlayer().setPotKey(slot, newpot, newact);
            cm.sendOk("#eThank you for using my services!");
            cm.dispose();
        }
    }  
}