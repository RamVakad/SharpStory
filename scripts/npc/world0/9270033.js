var status;

var bowties = [1122001, 1122002, 1122003, 1122004, 1122006, 1122005];
var bowstats = [1000, 2100, 3200, 4300, 5400, 6500];
var bowprice = [64, 120, 180, 240, 300, 360];

var icecream = [1012070, 1012071, 1012072, 1012073];
var creamstats = [7600, 7600, 7600, 7600];
var creamprice = [400, 400, 400, 400];

var mask = [1012076, 1012077, 1012078, 1012079];
var maskstats = [11500, 11500, 11500, 11500];
var maskprice = [700, 700, 700, 700];

var mouth = [1012084, 1012106, 1012132];
var mouthstats = [9000, 9000, 9000, 9000];
var mouthprice = [500, 500, 500, 500];

var item = 4031050;

var shop;
var text2 = "#eThank you for using my service! Enjoy!";
var text3 = "#r#eYou don't have enough Cracked Black Crystals!";

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
        cm.sendSimple("#r#eWant some of my super stat equipment? They give amazing stat boosts!\r\n\
#dHere are your options - \r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0##b\r\n\
#L51#Bowtie Pendants\r\n\
#L52#Icecream Face Accessory\r\n\
#L53#Mask Face Accessory\r\n\
#L54#Mouth Face Accessory\r\n");
    } else if (status == 1) {
        if (selection == 51) {

            shop = 1;
            var text1 = "#r#eTake a look at these bowties - They're selling extremely fast -";
            for (var i = 0;i < bowties.length; i++)
                text1 += "#b\r\n#L" + i + "##i" + bowties[i] + "# - " + bowstats[i] + " STR,DEX,INT,LUK\r\n#k\t\t\t\t\t\t-" + cm.getWithCommas(bowprice[i]) + " Cracked Black Crystals."; 
            cm.sendSimple(text1);

        } else if (selection == 52) {

            shop = 2;
            var text1 = "#r#eTake a look at these never-melting Icecream Bars - They're selling extremely fast -";
            for (var i = 0;i < icecream.length; i++)
                text1 += "#b\r\n#L" + i + "##i" + icecream[i] + "# - " + creamstats[i] + " STR,DEX,INT,LUK\r\n#k\t\t\t\t\t\t-" + cm.getWithCommas(creamprice[i]) + " Cracked Black Crystals."; 
            cm.sendSimple(text1);

        } else if (selection == 53) {

            shop = 3;
            var text1 = "#r#eTake a look at these masks - They're selling extremely fast -";
            for (var i = 0;i < mask.length; i++)
                text1 += "#b\r\n#L" + i + "##i" + mask[i] + "# - " + maskstats[i] + " STR,DEX,INT,LUK\r\n#k\t\t\t\t\t\t-" + cm.getWithCommas(maskprice[i]) + " Cracked Black Crystals."; 
            cm.sendSimple(text1);

        } else if (selection == 54) {

            shop = 4;
            var text1 = "#r#eTake a look at these mouth pieces - They're selling extremely fast -";
            for (var i = 0;i < mouth.length; i++)
                text1 += "#b\r\n#L" + i + "##i" + mouth[i] + "# - " + mouthstats[i] + " STR,DEX,INT,LUK\r\n#k\t\t\t\t\t\t-" + cm.getWithCommas(mouthprice[i]) + " Cracked Black Crystals."; 
            cm.sendSimple(text1);

        }
    } else if (status == 2) {
        if (shop == 1) {
            if (cm.itemQuantity(item) >= bowprice[selection]) {
                cm.gainItem(item, -bowprice[selection]);
                cm.gainEqWithStat(bowties[selection], 0, 0, bowstats[selection],bowstats[selection],bowstats[selection],bowstats[selection], 0, 0, 0);
                cm.sendOk(text2);
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
            } else {
                cm.sendOk(text3);
            }
        } else if (shop == 2) {
            if (cm.itemQuantity(item) >= creamprice[selection]) {
                cm.gainItem(item, -creamprice[selection]);
                cm.gainEqWithStat(icecream[selection], 0, 0, creamstats[selection],creamstats[selection],creamstats[selection],creamstats[selection], 0 ,0 ,0);
                cm.sendOk(text2);
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
            } else {
                cm.sendOk(text3);
            }
        } else if (shop == 3) {
            if (cm.itemQuantity(item) >= maskprice[selection]) {
                cm.gainItem(item, -maskprice[selection]);
                cm.gainEqWithStat(mask[selection], 0, 0, maskstats[selection],maskstats[selection],maskstats[selection],maskstats[selection], 0 ,0 ,0);
                cm.sendOk(text2);
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
            } else {
                cm.sendOk(text3);
            }
        } else if (shop == 4) {
            if (cm.itemQuantity(item) >= mouthprice[selection]) {
                cm.gainItem(item, -mouthprice[selection]);
                cm.gainEqWithStat(mouth[selection], 0, 0, mouthstats[selection],mouthstats[selection],mouthstats[selection],mouthstats[selection], 0 ,0 ,0);
                cm.sendOk(text2);
                if (cm.getGuild() != null) cm.getGuild().gainGP(6);
            } else {
                cm.sendOk(text3);
            }
        } 
        cm.dispose();
    }
}
