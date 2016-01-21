var status = 0;

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
    cm.sendSimple ("#eWelcome To #rSharpStory#k! I'm the #bEvent Trophy#k NPC exchanger, I exchange Event Trophy won from events for prizes!\r\n\\r\n#rWhat would you like?#b\r\n\
\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L80#GM Scrolls\r\n\
#L81#Chairs#b\r\n\
#L82#Trade Ten Trophies for Two Vote Points\r\n");
         } else if (selection == 80) {
              cm.sendSimple("#eYou can exchange #r10#k #i4000038# for 1 #bGM Scroll#k here! \r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n #L0# Gloves for ATT #l \r\n #L1# Claw for ATT #l \r\n #L2# Dagger for ATT \r\n #L3# Wand For Magic Att \r\n #L4# Staff For Magic Att \r\n #L5# Bow for ATT \r\n #L6# Crossbow for ATT \r\n #L7# One-Handed Axe for ATT \r\n #L8# One-Handed Sword for ATT \r\n #L9# One-Handed BW for ATT \r\n #L10# Pole Arm for ATT \r\n #L11# Spear for ATT \r\n #L12# Two-handed Axe for ATT \r\n #L13# Two-handed Sword for ATT \r\n #L14# Two-handed BW for ATT");
         } else if (selection == 81) {
             cm.sendSimple("#eYou can exchange #r50#k #i4000038# for #bRare Chairs#k here! \r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n #L15# #i3010064# \r\n #L16# #i3010043# \r\n #L17# #i3010071# \r\n #L18# #i3010085# \r\n #L19# #i3010098# \r\n #L20# #i3010073# \r\n #L21# #i3010099# \r\n #L22# #i3010069# \r\n #L23# #i3010106# \r\n #L24# #i3010111# \r\n #L25# #i3012010# \r\n #L26# #i3012011# \r\n #L27# #i3010080# \r\n #L28# #i3010116#");
         } else if (selection == 82) {
             if (cm.itemQuantity(4000038) > 9) {
			cm.gainItem(4000038, -10);
			cm.getPlayer().addVotePT(2);
			cm.sendOk("#eThank you for using my service!");
			cm.dispose();
		}else{
                 cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                 cm.dispose();
             }
         } else if (selection == 0) {
             if (cm.itemQuantity(4000038) > 9){
                 cm.gainItem(4000038, -10);
                 cm.gainItem(2040807, 1);
                 cm.sendOk("Thank you, please come again!");
                 cm.dispose();
             }else{
                 cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                 cm.dispose();
             }
             } else if (selection == 1) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044703, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
             }
             } else if (selection == 2) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043303, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
             }
             } else if (selection == 3) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043703, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
             }
             } else if (selection == 4) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043803, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 5) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044503, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                                      }
             } else if (selection == 6) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044603, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                                                           }
             } else if (selection == 7) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043103, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                                                           }
             } else if (selection == 8) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043003, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                                                           }
             } else if (selection == 9) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2043203, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                                                           }
             } else if (selection == 10) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044403, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 11) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044303, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 12) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044103, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 13) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044003, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 14) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -10);
                     cm.gainItem(2044203, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 15) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010064, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 16) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010043, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 17) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010071, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 18) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010085, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 19) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010098, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 20) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010073, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 21) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010099, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 22) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010069, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 23) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010106, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
               }
             } else if (selection == 24) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010111, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
               }
             } else if (selection == 25) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3012010, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 26) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3012011, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 27) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010080, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             } else if (selection == 28) {
                 if (cm.itemQuantity(4000038) > 49){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010116, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             }else if (selection == 29) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010116, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             }else if (selection == 30) {
                 if (cm.itemQuantity(4000038) > 9){
                     cm.gainItem(4000038, -50);
                     cm.gainItem(3010116, 1);
                     cm.sendOk("Thank you, please come again!");
                     cm.dispose();
                 }else{
                     cm.sendOk("#eSorry, you do not have enough Event Trophies!");
                     cm.dispose();
                 }
             }
    }
}