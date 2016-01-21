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
            var event = "";
            if (cm.getPlayer().getClient().getChannelServer().isEventAvaliable()) {
                event = "#r#L1092097#Event\r\n\r\n";
            }
            cm.sendSimple("#e#rMoo #dMoo #bMoo #gMoo #k... ?\r\n\#bTranslation #k- #r Welcome, I'm the NPC-Network-Center manager, What would you like?#k\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
"+event+"\
#b#L9040011#Bulletin Board\r\n\
#L9030000#All-In-One-Shop\r\n\
#L9000021#Job Advancer\r\n\
#L9001100#Skill Maxer\r\n\
#L9000020#Transportation Manager\r\n\
#L9000019#Reward NPC\r\n\
#L9900001#Stylist\r\n\
#L2013001#Mesos Converter\r\n\
#L9201000#Currency Exchange\r\n\
#L1052015#NX Seller\r\n\
#L9101000#Chair Seller\r\n\
#L1204020#Godly Timeless Trader\r\n\
#L1052014#Slave Trader\r\n\
#L1052013#Item Upgrader\r\n\
#L9000036#Vote Point Trader\r\n\
#L9270033#Super Stat Item Maker\r\n\
#L9201049#Hero Items\r\n\
#L2042007#Godly Crystal System\r\n\
#L9040008#Ranking\r\n\
#L9201024#Item Auction NPC\r\n\
#L9201026#Equipment Auction NPC\r\n\
#L9201025#Item Potential NPC\r\n\
#L9201023#Auction Income Dropbox NPC\r\n\
#L9201027#Rebirth Belts\r\n\
#L9900000#Hotkey NPC\r\n\
#L9201082#Event Trophy NPC\r\n\
#L9201142#Guild Headquarters\r\n\
#L1043001#Abandon Pet\r\n\
#L1022101#Max Stat Item Maker\r\n\
#L1002003#Max Stat Item v2");

        } else if (status==1) {
            cm.openNpc(selection);
        }
    }
}