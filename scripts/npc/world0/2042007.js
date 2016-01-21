var status;

var bowties = [1082145, 1082146, 1082147, 1082148, 1082149, 1003023, 1003025, 1142100, 1032061, 1072344, 1022060];
var bowstats = [1000, 1000, 1000, 1000, 1000, 4000, 4000, 10000, 5000, 5000, 5000];
var bowprice = [25000, 25000, 25000, 25000, 25000, 100000, 100000, 200000, 150000, 150000, 150000];

var item = 4031917;

var sets = 0;
var remain = 0;

var text2 = "#eThank you for using my service! Enjoy!";
var text3 = "#r#eYou don't have enough Crystals!";

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
} else if (status == 0) {
cm.sendNext ("#eHey, I'm the Godly Crystal system of #rSharpStory!#k");
} else if (status == 1) {
var text1 = "#r#eTake a look at these - They're selling extremely fast -";
for (var i = 0;i < bowties.length; i++)
text1 += "#b\r\n#L" + i + "##i" + bowties[i] + "# - " + bowstats[i] + " STR,DEX,INT,LUK#k " + cm.getWithCommas(bowprice[i]) + " #v4031917#"; 
cm.sendSimple(text1);
}  else if (status == 2) {
			if (cm.itemQuantity(item) >= bowprice[selection]) {
			sets = bowprice[selection]/32767;
			remain = bowprice[selection]%32767;
			for (var i = 1; i <= sets; i++) {
			cm.gainItem(item, -32767);
			}
			cm.gainItem(item, (-1 * remain));
			cm.gainEqWithStat(bowties[selection], 0, 0, bowstats[selection],bowstats[selection],bowstats[selection],bowstats[selection], 0, 0, 0);
			cm.sendOk(text2);
			if (cm.getGuild() != null) cm.getGuild().gainGP(6);
                        cm.dispose();
			} else {
			cm.sendOk(text3);
			cm.dispose();
			}
	} 
}







