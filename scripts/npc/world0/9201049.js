var status = 0;

function start() {
    cm.sendOk("#eBeing recoded.");
    cm.dispose();
    //status = -1;
    //action(1, 0, 0);
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
            cm.sendSimple ("#eWelcome To SharpStory! I used to be a #rHero#k, but I'm not anymore so I'm selling my #rHero Items#k.\r\n\\r\n#rWhat would you like?#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L80#Hero's Hat#b\r\n\
#L81#Hero's Shoe#b\r\n\
#L82#Hero's Glove#b\r\n\
#L83#Hero's Cape#b\r\n");
        } else if (selection == 80) {
            cm.sendSimple ("#e#rThis are the Hero Hats -#e#d" +
                "#k\r\n\r\n#L0##r#v1002082#Yellow Bandana - 69 Stats Each & 10 WA & MA\r\n\#k1 Cloud Pieces and 400 Crystal Shards" +
                "#k\r\n\r\n#L1##r#v1002081#Blue Bandana - 137 Stats Each & 30 WA & MA\r\n\#k2 Cloud Pieces, 800 Crystal Shards and 1 Yellow Bandana" +
                "#k\r\n\r\n#L2##r#v1002080#Red Bandana - 310 Stats Each & 65 WA & MA\r\n\#k200 Snail Shell, 5 Cloud Pieces, 1500 Crystal Shards and 1 Blue Bandana" +
                "#k\r\n\r\n#L3##r#v1002391#Green Bandana - 650 Stats Each & 105 WA & MA\r\n\#k320 Tree Branch, 9 Cloud Pieces, 3000 Crystal Shards and 1 Red Bandana" +
                "#k\r\n\r\n#L4##r#v1002395#Purple Bandana - 1400 Stats Each & 155 WA & MA\r\n\#k500 Octopus Leg, 20 Cloud Pieces, 6500 Crystal Shards and 1 Green Bandana" +
                "#k\r\n\r\n#L5##r#v1002394#Grey Bandana - 3000 Stats Each & 235 WA & MA\r\n\#k290 Leather, 650 Stone Golem Rubble, 40 Cloud Pieces, 10000 Crystal Shards and 1 Purple Bandana" +
                "#k\r\n\r\n#L6##r#v1002083#Black Bandana - 6500 Stats Each & 350 WA & MA\r\n\#k800 Yeti Horn, 500 Fly-Eye Wing,  1200 Lunar Pixie's Moonpiece, 80 Cloud Pieces, 20000 Crystal Shards and 1 Grey Bandana");
        } else if (selection == 81) {
            cm.sendSimple ("#e#rThis are the Hero Shoes -#e#d" +
                "#k\r\n\r\n#L7##b#v1072012#Red Whitebottom Boots - 80 Stats Each & 15 WA & MA\r\n#k1 Cloud Pieces and 400 Crystal Shards" +
                "#k\r\n\r\n#L8##b#v1072054#Orange Whitebottom Boots - 175 Stats Each & 40 WA & MA\r\n#k4 Cloud Pieces, 250 Blue Snail Shell, 900 Crystal Shards and 1 Red Whitebottom Boots" +
                "#k\r\n\r\n#L9##b#v1072055#Pink Whitebottom Boots - 350 Stats Each & 70 WA & MA\r\n#k8 Cloud Pieces, 420 Squishy Liquid, 1800 Crystal Shards and 1 Orange Whitebottom Boots" +
                "#k\r\n\r\n#L10##b#v1072056#Blue Whitebottom Boots - 800 Stats Each & 120 WA & MA\r\n#k20 Cloud Pieces , 700 Orange Mushroom Cap, 3500 Crystal Shards and 1 Pink Whitebottom Boots");
        } else if (selection == 82) {
            cm.sendSimple ("#e#rThis are the Hero Gloves - #e#d#k\r\n\r\n#L11##d#v1082223#Stormcaster Glove - 250 Stats Each, 100 WA & MA\r\b#k12 Cloud Pieces, 1000 Crystal Shards and 700 Shark Denture.#k\r\n\r\n#L12##d#v1082230#Glitter Glove - 500 Stats Each, 200 WA & MA\r\n#k 25 Cloud Pieces, 1000 Crystal Shards and 75 Lady Boss Comb.");
        } else if (selection == 83) {
            cm.sendSimple ("#e#rThis is Hero Cape - #e#d" +
                "#k\r\n\r\n#L13##g#v1102064#Goblin Cape - 200 Stats Each & 70 WA & MA\r\n#k20 Cloud Pieces, 1000 Crystal Shards and 300 Tiger Skin");
        } else if (selection == 0) {
            if(cm.itemQuantity(4001063) >=1 && cm.itemQuantity(4031917) >= 400) {
                cm.gainItem(4001063, -1); 
                cm.gainItem(4031917, -400);                  
                cm.gainEqWithStat(1002082, 10, 10, 69, 69, 69, 69, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rYellow Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Yellow Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(5);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 1) {
            if(cm.itemQuantity(4001063) >=2 && cm.itemQuantity(1002082) >= 1 && cm.itemQuantity(4031917) >= 800) {
                cm.gainItemBy10(4001063, -2);
                cm.gainItem(4031917, -800);  
                cm.gainItem(1002082, -1);                   
                cm.gainEqWithStat(1002081, 30, 30, 137, 137, 137, 137, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rBlue Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Blue Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 2) {
            if(cm.itemQuantity(4001063) >=5 && cm.itemQuantity(1002081) >= 1 && cm.itemQuantity(4000019) >= 200 && cm.itemQuantity(4031917) >= 1500) {
                cm.gainItem(4031917, -1500); 
                cm.gainItem(4001063, -5);                  
                cm.gainItem(1002081, -1);
                cm.gainItem(4000019, -200);
                cm.gainEqWithStat(1002080, 65, 65, 310, 310, 310, 310, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rRed Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Red Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(7);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 3) {
            if(cm.itemQuantity(4001063) >= 9&& cm.itemQuantity(1002080) >= 1 && cm.itemQuantity(4000003) >= 320 && cm.itemQuantity(4031917) >= 3000) {
                cm.gainItem(4031917, -3000); 
                cm.gainItem(4001063, -9);                   
                cm.gainItem(1002080, -1);
                cm.gainItem(4000003, -320);
                cm.gainEqWithStat(1002391, 105, 105, 650, 650, 650, 650, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rGreen Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Green Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 4) {
            if(cm.itemQuantity(4001063) >= 20&& cm.itemQuantity(1002391) >= 1&& cm.itemQuantity(4000006) >= 500 && cm.itemQuantity(4031917) >= 6500) {
                cm.gainItem(4031917, -6500); 
                cm.gainItem(4001063, -20);                   
                cm.gainItem(1002391, -1);
                cm.gainItem(4000006, -500);
                cm.gainEqWithStat(1002395, 155, 155, 1400, 1400, 1400, 1400, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rPurple Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Purple Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(9);
                cm.dispose();
                
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 5) {
            if(cm.itemQuantity(4001063) >= 40&& cm.itemQuantity(1002395) >= 1&& cm.itemQuantity(4000022) >= 650&& cm.itemQuantity(4000021) >= 290&& cm.itemQuantity(4000060) >= 1200 && cm.itemQuantity(4031917) >= 10000) {
                cm.gainItem(4031917, -10000);
                cm.gainItem(4001063, -40);                   
                cm.gainItem(1002395, -1);
                cm.gainItem(4000022, -650);
                cm.gainItem(4000021, -290);
                cm.gainEqWithStat(1002394, 235, 235, 3000, 3000, 3000, 3000, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rGrey Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Grey Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(10);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 6) {
            if(cm.itemQuantity(4001063) >= 80&& cm.itemQuantity(1002394) >= 1&& cm.itemQuantity(4000076) >= 500&& cm.itemQuantity(4000049) >= 800&& cm.itemQuantity(4000060) >= 1200 && cm.itemQuantity(4031917) >= 20000) {
                cm.gainItem(4031917, -20000); 
                cm.gainItem(4001063, -80);                   
                cm.gainItem(1002394, -1);
                cm.gainItem(4000049, -800);
                cm.gainItem(4000076, -500);
                cm.gainItem(4000060, -1200);
                cm.gainEqWithStat(1002083, 350, 350, 6500, 6500, 6500, 6500, 0 ,0 ,0);
                cm.sendOk("Kay here is your #e#rBlack Bandana#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Black Hero Bandana!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(15);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 7) {
            if(cm.itemQuantity(4001063) >= 1 && cm.itemQuantity(4031917) >= 400) {
                cm.gainItem(4031917, -400);
                cm.gainItem(4001063, -1);                   
                cm.gainEqWithStat(1072012, 15, 15, 80, 80, 80, 80, 0, 0 ,0);
                cm.sendOk("Kay here is your #e#rRed Whitebottom boots#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Red Whitebottom boots!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(5);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
        } else if (selection == 8) {
            if(cm.itemQuantity(4001063) >= 4&& cm.itemQuantity(1072012) >= 1&& cm.itemQuantity(4000000) >= 250 && cm.itemQuantity(4031917) >= 900) {
                cm.gainItem(4031917, -900);
                cm.gainItem(4001063, -4);                   
                cm.gainItem(1072012, -1);
                cm.gainItem(4000000, -250);
                cm.gainEqWithStat(1072054, 40, 40, 175, 175, 175, 175, 0, 0, 0);
                cm.sendOk("Kay here is your #e#rOrange Whitebottom boots#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Orange Hero Whitebottom Boots!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 9) {
            if(cm.itemQuantity(4001063) >= 8&& cm.itemQuantity(1072054) >= 1&& cm.itemQuantity(4000004) >= 420 && cm.itemQuantity(4031917) >= 1800) {
                cm.gainItem(4031917, -1800);
                cm.gainItem(4001063, -8);                   
                cm.gainItem(1072054, -1);
                cm.gainItem(4000004, -420);
                cm.gainEqWithStat(1072055, 70, 70, 350, 350, 350, 350, 0, 0, 0);
                cm.sendOk("Kay here is your #e#rPink Whitebottom boots#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Pink whitebottom boots!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(7);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        } else if (selection == 10) {
            if(cm.itemQuantity(4001063) >= 20&& cm.itemQuantity(1072055) >= 1&& cm.itemQuantity(4000001) >= 700) {
                cm.gainItem(4001063, -20);                   
                cm.gainItem(1072055, -1);
                cm.gainItem(4000001, -700);
                cm.gainEqWithStat(1072056, 120, 120, 800, 800, 800, 800, 0, 0, 0);
                cm.sendOk("Kay here is your #e#rBlue Whitebottom boots#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Blue Hero Whitebottom boots!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(10);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        }else if (selection == 11) {
            if(cm.itemQuantity(4001063) >= 12 && cm.itemQuantity(4000180) >= 700 && cm.itemQuantity(4031917) >= 1000) {
                cm.gainItem(4031917, -1000);
                cm.gainItem(4001063, -12);
                cm.gainItem(4000180, -700);                   
                cm.gainEqWithStat(1082223, 100, 100, 250, 250, 250, 250, 0, 0, 0);
                cm.sendOk("Ok, here is your #e#rStormcaster Glove#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Hero Stormcaster Glove!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.dispose();
            } else {
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
                cm.dispose();
            }
        } else if (selection == 12) {
            if(cm.itemQuantity(4001063) >=25 && cm.itemQuantity(4000138) >= 75 && cm.itemQuantity(4031917) >= 1000 && cm.itemQuantity(1082223) >= 1) {
                cm.gainItem(4031917, -1000);
                cm.gainItem(4001063, -25);
                cm.gainItem(4000138, -75);  
                cm.gainItem(1082223, -1);
                cm.gainEqWithStat(1082230, 200, 200, 500, 500, 500, 500, 0, 0, 0);
                cm.sendOk("Ok, here is your #e#rGlitter Glove#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Hero Glitter Glove!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.dispose();
            } else {
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
                cm.dispose();
            }
        } else if (selection == 13) {
            if(cm.itemQuantity(4001063) >=20&& cm.itemQuantity(4000171) >= 300 && cm.itemQuantity(4031917) >= 1000) {
                cm.gainItem(4031917, -1000);
                cm.gainItem(4001063, -20);
                cm.gainItem(4000171, -300);                   
                cm.gainEqWithStat(1102064, 70, 70, 200, 200, 200, 200, 0, 0, 0);
                cm.sendOk("Kay here is your #e#rGolblin Cape#k.");
                cm.worldMessage("[Hero] Congratulations to " + cm.getChar().getName() + " on getting his/her Hero Goblin Cape!");
                if (cm.getGuild() != null) cm.getGuild().gainGP(7);
                cm.dispose();
            } else
                cm.sendOk("#eYou are not going to be the future hero if you can't collect the right items.");
            cm.dispose();
        }
    }
}

