var status = 0;
var chair1 = Array(3010000, 3010001, 3010004, 3010005, 3010011, 3010015);
var chair2 = Array(3010019, 3010006, 3010002, 3010003, 3010012, 3010062, 3010060);
var chair3 = Array(3010007, 3010026, 3010028, 3010057, 3010058, 3010010, 3010013, 3010014, 3010016, 3010072, 3010017, 3010018, 3010022, 3010023, 3010024, 3010025);
var chair4 = Array(3010040, 3010041, 3010045, 3010046, 3010047, 3012005);
var chair5 = Array(3010064, 3010043, 3010071, 3010085, 3010098, 3010073, 3010099, 3010069, 3010106, 3010111, 3012010, 3012011, 3010080, 3010116);
var chosen;

function start() {
    status = -1;
    action(1, 0, 0);
}
   
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) 
            cm.dispose();  
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0){
            var txt1 = "#eHello #b" + cm.getPlayer().getName() + "#kI am the chair seller of #rSharpStory#k!\r\nWhich chair would you like to buy?\r\n\r\n";
            var txt2 ="-----------------------------------#e#rFREE#n#k-----------------------------------\r\n\r\n";
            for (var i = 0; i < chair1.length; i++)
                txt2 += "#L" + i + "##v" + chair1[i] + "##l    ";
            var txt3 ="\r\n\r\n\r\n----------------------------#e#b2000 #v4031917# #k,#b 2 #v4000415##k----------------------------\r\n\r\n";
            for (var i = 0; i < chair2.length; i++)
                txt3 += "#L" + (20+i) + "##v" + chair2[i] + "##l    ";
            var txt4 ="\r\n\r\n--------------------------#e#b4000 #v4031917# #k,#b 4 #v4000415##k--------------------------\r\n";
            for (var i = 0; i < chair3.length; i++)
                txt4 += "#L" + (40+i) + "##v" + chair3[i] + "##l    ";
            var txt5 ="\r\n\r\n--------------------------#e#b4000 #v4031917# #k,#b 10 #v4031050##k--------------------------\r\n\r\n";
            for (var i = 0; i < chair4.length; i++)
                txt5 += "#L" + (60+i) + "##v" + chair4[i] + "##l    ";
            var txt6 ="\r\n\r\n--------------------------#e#b6000 #v4031917# #k,#b 12 #v4000150##k--------------------------\r\n\r\n";
            for (var i = 0; i < chair5.length; i++)
                txt6 += "#L" + (80+i) + "##v" + chair5[i] + "##l    ";
            cm.sendSimple(txt1 + txt2 + txt3 + txt4 + txt5);
        }else if (status == 1) {
            if (selection >= 0 && selection < 12) {
                if (!(cm.haveItem(chair1[selection]))) {
                    cm.gainItem(chair1[selection]);
                    cm.sendOk("#eHere, you've received a free #v"+chair1[selection]+"#!!");
                } else
                    cm.sendOk("Sorry, you already have a #v"+chair1[selection]+"#!!");
                cm.dispose();
            } else {
                cm.sendYesNo("You sure want to buy this chair?");
                chosen = selection;
            }
        }else if (status == 2) {
            if (chosen >= 20 && chosen < 28) {
                if (cm.itemQuantity(4031917) > 1999 && cm.itemQuantity(4000415) > 1) {
                    cm.gainItem(4031917, -2000);
                    cm.gainItem(4000415, -2);
                    cm.gainItem(chair2[chosen-20]);
                    if (cm.getGuild() != null) cm.getGuild().gainGP(2);
                } else
                    cm.sendOk("Sorry, you can't afford it..\r\n");
            } else if (chosen >= 40 && chosen < 57) {
                if (cm.itemQuantity(4031917) > 3999 && cm.itemQuantity(4000415) > 3) {
                    cm.gainItem(4031917, -4000);
                    cm.gainItem(4000415, -4);
                    cm.gainItem(chair3[chosen-40]);
                    if (cm.getGuild() != null) cm.getGuild().gainGP(3);
                } else
                    cm.sendOk("Sorry, you can't afford it..\r\n");
            } else if (chosen >= 60 && chosen < 75) {
                if (cm.itemQuantity(4031917) > 3999 && cm.itemQuantity(4031050) > 9) {
                    cm.gainItem(4031917, -4000);
                    cm.gainItem(4031050, -10);
                    cm.gainItem(chair4[chosen-60]);
                    if (cm.getGuild() != null) cm.getGuild().gainGP(4);
                } else
                    cm.sendOk("Sorry, you can't afford it..\r\n");
            }else if (chosen >= 80 && chosen < 95) {
                if (cm.itemQuantity(4031917) > 5999 && cm.itemQuantity(4000150) > 11) {
                    cm.gainItem(4031917, -6000);
                    cm.gainItem(4000150, -12);
                    cm.gainItem(chair5[chosen-80]);
                    if (cm.getGuild() != null) cm.getGuild().gainGP(5);
                } else
                    cm.sendOk("Sorry, you can't afford it..\r\n");
            }
            cm.dispose();
        }
    }
}
