var status; 



var scrolls = [2040834, 2044713, 2044613, 2044513, 2044420, 2044320, 2044120, 2044028, 2043313, 2043120, 2043022, 2043220, 2044220];
var scrollprice = [5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 , 3];

var pets = [1812000, 1812001, 1812004, 1812005, 1812006, 1812002, 1812003];
var petprice = [7, 7, 7, 6, 5, 3, 3];

var scroll = 0;
var pet = 0;

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == 1) { 
        status++; 
    }else{ 
        status--; 
    }
    if (status == -1) {
        cm.dispose();
    }
    else if (status == 0) { 
        if (cm.getPlayer().getVotePT() > 0) {
            cm.sendSimple("#eWelcome to the #rSharpStory#k Vote Point Trader!\r\n\
#eYou have - #b" + cm.getPlayer().getVotePT() + "#k Vote Point(s)#b\r\n\
#dThese are your options -#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#Scrolls & Tickets\r\n\
#L1#Ordinary Slaves\r\n\
#L2#NX Slaves\r\n\
#L3#Nexon Cash & Rare NX\r\n\
#L16#Pink Scrolls\r\n\
#L17#PET Equips\r\n");
        } else {
            cm.sendOk("#eI'm sorry, you don't have any vote points right now. To get vote points, visit the server website.");
            cm.dispose();
        }
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendSimple("#ePlease select what you want - \r\n\
#dThese are your options -#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L4# Exchange 1 vote points for 2 #v2340000#\r\n\
#L5# Exchange 1 vote points for 2 #v2049100#\r\n\
#L6# Exchange 5 vote points for 1 #v4031544#");
        } else if (selection == 1) {
            cm.sendSimple("#ePlease select what you want - \r\n\
#dThese are your options -#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L7# Exchange 1 vote points for #v1902000#\r\n\
#L8# Exchange 3 vote points for #v1902001#\r\n\
#L9# Exchange 5 vote points for #v1902002# \r\n\
#L10# Exchange 1 vote points for #v1902015#\r\n\
#L11# Exchange 3 vote points for #v1902016#\r\n\
#L12# Exchange 5 vote points for #v1902017#\r\n\
#L13# Exchange 9 vote points for #v1902018#\r\n\
#L20# Exchange 3 vote points for #v1902005#\r\n\
#L21# Exchange 5 vote points for #v1902006#\r\n\
#L22# Exchange 9 vote points for #v1902007#");
        } else if (selection == 2) {
            cm.openNpc(1052014);
        } else if (selection == 3) {
            cm.sendSimple("#ePlease select what you want - \r\n\
#dThese are your options -#b\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L14# Exchange 1 vote points for 15,000 NX Cash\r\n\
#L15# Exchange 5 vote points for a Rare NX Ticket\r\n\
#k#rThe Rare NX Ticket is a Taru Totem that will get you a Rare NX of your choice when given to a GM.");
        } else if (selection == 16) {
            scroll = 1;
            var text1 = "#ePlease select what you want - \r\n#dThese are your options -#b\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
            for (var i = 0;i < scrolls.length; i++)
                text1 += "#b\r\n#L" + i + "#Two #i" + scrolls[i] + "# for " + scrollprice[i] + " Vote Points."; 
            cm.sendSimple(text1);
        } else if (selection == 17) {
            pet = 1;
            var text1 = "#ePlease select what you want - \r\n#dThese are your options -#b\r\n\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n";
            for (var i = 0;i < pets.length; i++)
                text1 += "#b\r\n#L" + i + "##i" + pets[i] + "# for " + petprice[i] + " Vote Points."; 
            cm.sendSimple(text1);
        }
    }
    else if (status == 2) {
        if (selection >= 0 && scroll == 1) {
            if (cm.getPlayer().getVotePT() >= scrollprice[selection]) {
                cm.gainItem(scrolls[selection], 2);
                cm.getPlayer().takeVotePT(scrollprice[selection]);
                if (cm.getGuild() != null) cm.getGuild().gainGP(scrollprice[selection]-2);
                cm.sendOk("#eThank you for voting! Good luck scrolling!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection >= 0 && pet == 1) {
            if (cm.getPlayer().getVotePT() >= petprice[selection]) {
                cm.gainItem(pets[selection], 1);
                cm.getPlayer().takeVotePT(petprice[selection]);
                if (cm.getGuild() != null) cm.getGuild().gainGP(petprice[selection]-2);
                cm.sendOk("#eThank you for voting! Good luck scrolling!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 4) {
            if (cm.getPlayer().getVotePT() > 0) {
                cm.gainItem(2340000, 2);
                cm.getPlayer().takeVotePT(1);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 5) {
            if (cm.getPlayer().getVotePT() > 0) {
                cm.gainItem(2049100, 2);
                cm.getPlayer().takeVotePT(1);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 6) {
            if (cm.getPlayer().getVotePT() > 4) {
                cm.gainItem(4031544, 1);
                ;
                cm.getPlayer().takeVotePT(5);
                if (cm.getGuild() != null) cm.getGuild().gainGP(3);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 7) {
            if (cm.getPlayer().getVotePT() > 0) {
                cm.gainItem(1902000, 1);
                cm.getPlayer().takeVotePT(1);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 8) {
            if (cm.getPlayer().getVotePT() > 2) {
                cm.gainItem(1902001, 1);
                cm.getPlayer().takeVotePT(3);
                if (cm.getGuild() != null) cm.getGuild().gainGP(2);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 9) {
            if (cm.getPlayer().getVotePT() > 4) {
                cm.gainItem(1902002, 1);
                cm.getPlayer().takeVotePT(5);
                if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 10) {
            if (cm.getPlayer().getVotePT() > 0) {
                cm.gainItem(1902015, 1);
                cm.getPlayer().takeVotePT(1);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 11) {
            if (cm.getPlayer().getVotePT() > 2) {
                cm.gainItem(1902016, 1);
                cm.getPlayer().takeVotePT(1);
                if (cm.getGuild() != null) cm.getGuild().gainGP(2);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 12) {
            if (cm.getPlayer().getVotePT() > 4) {
                cm.gainItem(1902017, 1);
                cm.getPlayer().takeVotePT(5);
                if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 13) {
            if (cm.getPlayer().getVotePT() > 8) {
                cm.gainItem(1902018, 1);
                cm.getPlayer().takeVotePT(9);
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        }else if (selection == 20) {
            if (cm.getPlayer().getVotePT() > 2) {
                cm.gainItem(1902005, 1);
                cm.getPlayer().takeVotePT(3);
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        }else if (selection == 21) {
            if (cm.getPlayer().getVotePT() > 4) {
                cm.gainItem(1902006, 1);
                cm.getPlayer().takeVotePT(5);
                if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        }else if (selection == 22) {
            if (cm.getPlayer().getVotePT() > 8) {
                cm.gainItem(1902007, 1);
                cm.getPlayer().takeVotePT(9);
                if (cm.getGuild() != null) cm.getGuild().gainGP(8);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 14) {
            if (cm.getPlayer().getVotePT() > 0) {
                cm.getPlayer().getCashShop().gainCash(4,15000);
                cm.getPlayer().dropMessage("[Sharp]:: You gained 15,000 NX Cash.");
                cm.getPlayer().takeVotePT(1);
                if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                cm.sendOk("#eThank you for voting!");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        } else if (selection == 15) {
            if (cm.getPlayer().getVotePT() > 4) {
                cm.gainItem(4031755, 1);
                cm.getPlayer().takeVotePT(5);
                if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                cm.sendOk("#eThank you for voting! You can turn in this ticket to any GMs for a rare NX of your choice.");
                cm.dispose();
            } else {
                cm.sendOk("You don't have enough vote points, these can be obtained every 12 hours by voting online!");
                cm.dispose();
            }
        }
    }
}
