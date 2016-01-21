/*
	By Mac
	Max Stat Item NPC
        AIM:darkriuxd MSN:darkriuxd@hotmail.com
*/


var status = 0;
var selected = 1;
var wui = 0;
var tf = false;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
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
            cm.sendSimple("#eHello, I'm the #rSharpStory#k #bItem Potential#k NPC!\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0##b\r\n\
#L0#Change PoTential on My Equipment - 1 Vote Point\r\n\
#L1#Explain Potential - #r MUST READ BEFORE TRYING\r\n\
#L2##bDisplay my stats\r\n\
#L3##bCheck Potential on my Equipment");
        } else if (status == 1) {
            if (selection == 0) {
                if (cm.getPlayer().getVotePT() > 0 && cm.EquipList(cm.getClient())!=""){
                    var String = "#ePlease Choose your desired #rEQUIP #kor #rNX #kyou want to apply discover potential upon.\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                    if (cm.EquipList(cm.getClient()) != "") {
                        cm.sendSimple(String+cm.EquipList(cm.getClient()));
                    } else {
                        sendOk("#e#rYou must have an item in your EQUIPMENT inventory!");
                        cm.dispose();
                    }
                    status = 3;
                } else  {
                    cm.sendOk ("#e#dI'm sorry, but you don't meet the requirements to use my service or you don't have an equipment in your inventory.");
                    cm.dispose(); 
                }
            } else if (selection == 1) {
                cm.sendNext("#e#bItem Potential#k Boosts the stats of an item. On top of every item, there are #dpotential values#k.\r\n\
#b1.#r The number of values determines how many 'potentials' an item has.\r\n\
#b2.#r You cannot have more than 3 potentials\r\n\
#b3.#r Items usually have no potential/2 potentials\r\n\
#b4.#r Gaining 3 potentials on a item is unlikely, but possible.\r\n\\n\
#b5.#r If you already discovered potential on your item, it is safe from getting destroyed.\r\n\
#kPress next to find out what each potential stands for value is -");
            } else if (selection == 2) {
                cm.sendOk("#eHere are your stats -\r\n\
#rSTR - #b"+cm.getPlayer().getTotalStr()+"\r\n\
#rDEX - #b"+cm.getPlayer().getTotalDex()+"\r\n\
#rINT - #b"+cm.getPlayer().getTotalInt()+"\r\n\
#rLUK - #b"+cm.getPlayer().getTotalLuk()+"");
                cm.dispose();
            } else if (selection == 3) {
                if (cm.getPlayer().getVotePT() > 0 && cm.EquipList(cm.getClient())!=""){
                    var String = "#ePlease choose your equipment to check what potential it has -\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
                    cm.sendSimple(String+cm.EquipList(cm.getClient()));
                    status = 4;
                } else {
                    cm.sendOk("#eYou have no items in equipment inventory.");
                    cm.dispose();
                }
            }
        } else if (status ==2) { 
            cm.sendNext("#eHere are the potential values -\r\n\
#b1/2/3/4#b - #d2% STR/DEX/INT/LUK\r\n\
#b5/6/7/8#b - #g4% STR/DEX/INT/LUK\r\n\
#b9/10/11/12#b - #r6% STR/DEX/INT/LUK\r\n\
#b13 - #r82 M.Att\r\n\
#b14 - #r76 W.Att\r\n\
#b15 - #r6% Total Damage\r\n\
#kPress Next to know important info -");
        } else if (status == 3) { 
            cm.sendOk("#eHere is the IMPORTANT INFORMATION -\r\n\
#r1. #bThe added stats do not show when you open your status window, you have to use @stats\r\n\
#r2. #bThis system has been tested and works, do not say 'I don't see a difference!'\r\n\
#b3.#r The FIRST TIME you potential an item, it has a 20% chance of losing all potential power and cannot be potential'd again.\r\n\n\
#b4.#r On top of that, it has a chance 50% chance of getting totally destroyed if it loses its potential power.");
            cm.dispose();
        } else if (status == 4) {
            cm.sendOk(cm.potEq(selected));
            cm.dispose();
        } else if (status == 5) {
            cm.sendOk("#e#bHere is the potential on the equipment - " + cm.showPoT(selected));
            cm.dispose();
        }		
        if (selection == 1) {

    }	
    }
}


