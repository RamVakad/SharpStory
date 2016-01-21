var status = 0;
var selectedequip = 1;
var wui = 0;
var incSTR=0;
var incLUK=0;
var incDEX=0;
var incINT=0;
var incWatt=0;
var incMatt=0;
var starCost=0;
var mesoCost=0;
var stattype;

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
            cm.sendSimple("#eWelcome to the Item Upgrader NPC of #rSharpStory#k! \r\n\
What Would you like to do? \r\n\ \r\n\
\r\n\
#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L0#Upgrade my Equipment. \r\n\
#L1#Purchase Upgrade Tickets -#k 5 Ice Pieces");
        }else if (status == 1){
            wui=selection;
            if (wui==0){
                if (cm.EquipList(cm.getClient())==""){
                    cm.sendOk("#e#rYou do not have any equipment in your inventory!");
                    cm.dispose();
                }else{
                    var String = "#ePlease select the equipment that you wish to upgrade.\r\nBelow is a list of equipment in your inventory. \r\n \r\n#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                    cm.sendSimple(String+cm.EquipList(cm.getClient()));
                }
            }else if (wui==1){
                if(cm.itemQuantity(4000150) > 4) {
                    cm.gainItem(4000150, -5);
                    cm.gainItem(4031544, 1);
                    cm.sendOk("#eYou can also gain these tickets through voting!");
                } else {
                    cm.sendOk("#eYou do not have 5 Ice Pieces!");
                    cm.dispose();
                }
            }
        } else if (status==2){
            selectedequip=selection;
            if (wui==0){
                cm.sendSimple("#eWhich stat would you like to increase for the selected Equipment? \r\n\
#rPlease note that the selected stat will not exceed 30,000. \r\n\
\r\n\
#k#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L2#Strength (STR) - #r+500#b\r\n\
#L3#Dexterity (DEX) - #r+500#b\r\n\
#L4#Intelligence (INT) - #r+500#b\r\n\
#L5#Luck (LUK) - #r+500#b\r\n\
#L6#Weapon Attack (WATT) - #r+100#b\r\n\
#L7#Magic Attack (MATT) - #r+100");
            }else if (wui==1){
                cm.dispose();
            }
        }else if (status==3){
            stattype = selection;
            cm.sendYesNo("#eAre you ready to use your #v4031544# to upgrade your equipment?");
        }else if (status == 4){
                    
            if (cm.haveItem(4031544,1)){
                cm.gainItem(4031544,-1);
                if (stattype==2){
                    incSTR = 500;
                }else if (stattype==3){
                    incDEX = 500;
                }else if (stattype==4){
                    incINT = 500;
                }else if (stattype==5){
                    incLUK = 500;
                } else if (stattype==6){
                    incWatt=100;
                } else if (stattype==7){
                    incMatt=120;
                }
                cm.editEq(selectedequip, incWatt, incMatt, incSTR, incDEX, incINT, incLUK, false);
                if (cm.getGuild() != null) cm.getGuild().gainGP(5);
                cm.sendOk("#eYour equipment has been upgraded, Thank you for using my service!");
                cm.dispose();
            }else{
                cm.sendOk("#eSorry, you do not have any #v4031544#, you must first purchase this or gain them via voting.");
                cm.dispose();
                        
            }
        }
    }
}
