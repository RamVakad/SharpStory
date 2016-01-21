
var status = 0;
var shop = 0;
var timeless = new Array(1302081, 1402046, 1442063, 1482023, 1332073, 1332074, 1432047, 1452057, 1462050, 1472068, 1492023, 1442057, 1382057);


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
            cm.dispose();
    if (status ==0){
          cm.sendSimple("#eHello! I can trade godly timeless equipment for your crystals!\r\n\
Currenty, this is what you have -#b\r\n\
#c4000150# #v4000150#\r\n\
#c4001063# #v4001063#\r\n\
\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L99##rGoto #v4000150# Exchange Shop. \r\n\
#L98##rGoto #v4001063# Exchange Shop.");
    }else if (status == 1){
        if (selection == 99){
            shop = 4000150;
            cm.sendSimple("#eYou Currently have #r#c4000150##k  #v4000150##k\r\n\
You can get these by trading in your crystals that you got from monsters.#b\r\n\
\r\n#fUI/UIWindow.img/QuestIcon/3/0##k\r\n\
#L0# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1302081#\r\n\
#L1# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1402046#\r\n\
#L2# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1442063#\r\n\
#L3# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1482023#\r\n\
#L4# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1332073#\r\n\
#L5# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1332074#\r\n\
#L6# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1432047#\r\n\
#L7# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1452057#\r\n\
#L8# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1462050#\r\n\
#L9# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1472068#\r\n\
#L10# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1492023#\r\n\
#L11# Exchange 400 #v4000150# for a #r10,000 WATT#k #v1442057#\r\n\
#L12# Exchange 400 #v4000150# for a #r10,000 MATT#k #v1382057#");
        }else if (selection == 98){
            shop = 12345;
            cm.sendSimple("#eYou currently have #r#c4001063##k  #v4001063#\r\n\
You can get these by trading in your mesos.\r\n\
#rThe following sets come with 7 ARMORS that have bonus stats -#b7,500 STR, DEX, LUK, INT #rEACH PIECE#b\r\n\
\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L13# Exchange 200 #v4001063# for Warrior's Timeless Set\r\n\
#L14# Exchange 200 #v4001063# for Bowman's Timeless Set\r\n\
#L15# Exchange 200 #v4001063# for Magician's Timeless Set\r\n\
#L16# Exchange 200 #v4001063# for Thief's Timeless Set\r\n\
#L17# Exchange 200 #v4001063# for Pirate's Timeless Set\r\n\
");
        }
    } else if ((status == 2)&&(shop==4000150)) {
		if (selection == 12) {
		if (cm.itemQuantity(4000150) > 399) {
			cm.gainItem(4000150, -400);
			cm.gainEqWithStat(timeless[selection], 0, 10000, 0, 0, 0, 0, 0 ,0 ,0);
			cm.sendOk("#eThank you for your #v4000150#!");
			if (cm.getGuild() != null) cm.getGuild().gainGP(10);
		} else {
                    cm.sendOk("#eSorry you dont have enough #v4000150#!");
                }
             } else if (selection <= 11) { 
		if (cm.itemQuantity(4000150) > 399) {
                    cm.gainItem(4000150, -400);
                    cm.gainEqWithStat(timeless[selection], 10000, 0, 0, 0, 0, 0, 0 ,0 ,0);
                    cm.sendOk("#eThank you for your #v4000150#!");
		    if (cm.getGuild() != null) cm.getGuild().gainGP(10);
               } else {
                    cm.sendOk("#eSorry you dont have enough #v4000150#!");
                }
         } 
         cm.dispose();
     } else if ((status==2)&&(shop==12345)){
         if (selection == 13){
             if (cm.itemQuantity(4001063) > 199){
cm.gainItem(4001063, -200);
cm.gainEqWithStat(1122011, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1032031, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1102172, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1002776, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1082234, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1052155, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1072355, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
if (cm.getGuild() != null) cm.getGuild().gainGP(25);
                 cm.sendOk("#eThank you for the purchase!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eYou do not have enough #v4001063#");
                 cm.dispose();
             }
         } else if (selection == 14){
              if (cm.itemQuantity(4001063) > 199){
cm.gainItem(4001063, -200);
cm.gainEqWithStat(1122011, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1032031, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1102172, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1002778, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1082236, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1052157, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1072357, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
if (cm.getGuild() != null) cm.getGuild().gainGP(25);
                 cm.sendOk("#eThank you for the purchase!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eYou do not have enough #v4001063#");
                 cm.dispose();
             }
} else if (selection == 15){
              if (cm.itemQuantity(4001063) > 199){
cm.gainItem(4001063, -200);
cm.gainEqWithStat(1122011, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1032031, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1102172, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1002777, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1052156, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1082235, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1072356, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
if (cm.getGuild() != null) cm.getGuild().gainGP(25);
                 cm.sendOk("#eThank you for the purchase!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eYou do not have enough #v4001063#");
                 cm.dispose();
             }
}else if (selection == 16){
              if (cm.itemQuantity(4001063) > 199){
cm.gainItem(4001063, -200);
cm.gainEqWithStat(1122011, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1032031, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1102172, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1002779, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1082237, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1052158, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1072358, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
if (cm.getGuild() != null) cm.getGuild().gainGP(25);
                 cm.sendOk("#eThank you for the purchase!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eYou do not have enough #v4001063#");
                 cm.dispose();
             }
}else if (selection == 17){
              if (cm.itemQuantity(4001063) > 199){
cm.gainItem(4001063, -200);
cm.gainEqWithStat(1122011, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1032031, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1102172, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1082238, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1002780, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1052159, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
cm.gainEqWithStat(1072359, 0, 0, 7500, 7500, 7500, 7500, 0 ,0 ,0);
if (cm.getGuild() != null) cm.getGuild().gainGP(25);
                 cm.sendOk("#eThank you for the purchase!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eYou do not have enough #v4001063#");
                 cm.dispose();
             }
         }
     }
   }
}
