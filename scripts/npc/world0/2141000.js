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
            cm.sendSimple("#eWelcome, what job would you like to change to?\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#BEGINNER#l\r\n\r\n\
#L100#WARRIOR#l\r\n\
#L110#FIGHTER#l #L111#CRUSADER#l #L112#HERO#l\r\n\
#L120#PAGE#l #L121#WHITEKNIGHT#l #L122#PALADIN#l\r\n\
#L130#SPEARMAN#l #L131#DRAGONKNIGHT#l #L132#DARKKNIGHT#l\r\n\r\n\
#L200#MAGICIAN#l\r\n\
#L210#FP_WIZARD#l #L211#FP_MAGE#l #L212#FP_ARCHMAGE#l\r\n\
#L220#IL_WIZARD#l #L221#IL_MAGE#l #L222#IL_ARCHMAGE#l\r\n\
#L230#CLERIC#l #L231#PRIEST#l #L232#BISHOP#l\r\n\r\n\
#L300#BOWMAN#l\r\n\
#L310#HUNTER#l #L311#RANGER#l #L312#BOWMASTER#l\r\n\
#L320#CROSSBOWMAN#l #L321#SNIPER#l #L322#MARKSMAN#l\r\n\r\n\
#L400#THIEF#l\r\n\
#L410#ASSASSIN#l #L411#HERMIT#l #L412#NIGHTLORD#l\r\n\
#L420#BANDIT#l #L421#CHIEFBANDIT#l #L422#SHADOWER#l\r\n\\r\n\
#L500#PIRATE#l\r\n\
#L510#BRAWLER#l #L511#MARAUDER#l #L512#BUCCANEER#l\r\n\
#L520#GUNSLINGER#l #L521#OUTLAW#l #L522#CORSAIR#l\r\n\\r\n\
#L2000#LEGEND#l\r\n\
#L2100#ARAN1#l #L2110#ARAN2#l #L2111#ARAN3#l #L2112#ARAN4#l#b\r\n\r\n\r\n\r\n\r\n\r\n\
#r\t\t\t\t\t\t\t\t\tCYGNUS KNIGHTS#k\r\n\r\n\
#b\t\tDAWNWARRIOR#k\r\n\
#L1100#1#l #L1110#2#l #L1111#3#l #L1112#4#l\r\n\r\n\
#b\t\tBLAZEWIZARD#k\r\n\
#L1200#1#l #L1210#2#l #L1211#3#l #L1212#4#l\r\n\r\n\
#b\t\tWINDARCHER#k\r\n\
#L1300#1#l #L1310#2#l #L1311#3#l #L1312#4#l\r\n\r\n\
#b\t\tNIGHTWALKER#k\r\n\
#L1400#1#l #L1410#2#l #L1411#3#l #L1412#4#l\r\n\r\n\
#b\t\tTHUNDERBREAKER#k\r\n\
#L1500#1#l #L1510#2#l #L1511#3#l #L1512#4#l");
        } else if (status==1) {
            if (cm.getPlayer().getReborns() > 9) {
            cm.changeJobById(selection);
            cm.dispose();
            } else {
                cm.sendOk("#eYou need atleast 10 rebirths to use this feature!");
                cm.dispose();
            }
        }
    }
}