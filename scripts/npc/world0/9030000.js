/*
Created by Lee Yiyuan of NakedStory83 edited by Coffee
*/

var status = 0;
var wui = 0;

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
        if (status == -1) cm.dispose();
        else if (status ==0){
            cm.sendSimple("\t\t\t#e#rWelcome to the All In One Shop of #dSharpStory!\r\n\
#r\t\t\t\t\t\t\tWhat would you like to buy?\r\n\
\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L13337##rGeneral Shop#b\r\n\
#L0#Weapons\r\n\
#L1050#Overalls\r\n\
#L1082#Gloves\r\n\
#L1002#Hats\r\n\
#L1092#Shields\r\n\
#L1040#Tops\r\n\
#L1060#Bottoms\r\n\
#L1102#Capes\r\n\
#L1072#Shoes\r\n\
#L1032#Earrings\r\n\
#L1#Scrolls\r\n\
#L2002#Potions (Page 1)\r\n\
#L2050#Potions (Page 2)\r\n\
#L2060#Bow Arrows\r\n\
#L2061#CrossBow Arrows\r\n\
#L2070#Stars\r\n\
#L2330#Bullets\r\n\
#L4006#Rocks");
        } else if (status == 1){
            if (selection == 0){
                cm.sendSimple("#e\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L1302#One Handed Sword\r\n\
#L1312#One Handed Axe\r\n\
#L1322#One Handed Blunt Weapon\r\n\
#L1402#Two Handed Sword\r\n\
#L1412#Two Handed Axe\r\n\
#L1422#Two handed Blunt Weapon\r\n\
#L1432#Spear\r\n\
#L1442#Polearm\r\n\
#L1332#Dagger\r\n\
#L1372#Wand\r\n\
#L1382#Staff\r\n\
#L1452#Bow\r\n\
#L1462#CrossBow\r\n\
#L1472#Claw\r\n\
#L1482#Knuckle\r\n\
#L1492#Gun")
            }else if (selection == 1){
                cm.sendSimple("#e\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L2040#Scrolls for Clothing (Page 1)\r\n\
#L2041#Scrolls for Clothing (Page 2)\r\n\
#L2043#Scrolls for Weapons (Page 1)\r\n\
#L2044#Scrolls for Weapons (Page 2)\r\n\
#L2048#Scrolls for Pet items\r\n\
#L2340#Other Scrolls[1]\r\n\
#L2049#Other Scrolls[2]");
            }else{
                if(selection == 9201011) {
                    cm.openNpcShop(9201011);
                    cm.dispose();
                }
                cm.openShopById(selection + cm.getGender()*(selection == 1050 || selection == 1040 ||
                    selection == 1060));
                cm.dispose();
            }
        } else if (status == 2){
            if(selection == 9201011) {
                cm.openNpcShop(9201011);
                cm.dispose();
            }
            if (wui == 0){
                cm.openShopById(selection);
                cm.dispose();
            }
        }
    }
}