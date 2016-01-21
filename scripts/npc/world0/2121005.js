var status = 0;
var mobs = [8500001, 9400549, 9400014, 9400121, 9600001, 9400575, 9500124];

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
            var text1 = "#eWhich monster would you like to summon?\r\n\r\n\
#b#fUI/UIWindow.img/QuestIcon/3/0##b\r\n";
            for (var i = 0;i < mobs.length; i++)
                text1 += "#L" + mobs[i] + "# "+cm.getMobName(mobs[i])+"\r\n"; 
            cm.sendSimple(text1 + "\r\n\
#r#L0#Clear Drops\r\n\
#L1#Kill All Monsters");
        } else if (status==1) {
            if (selection == 0) {
                cm.clearDrops();
            } else if (selection == 1) {
                cm.killAllMonsters(cm.getPlayer().getMapId());
            } else {
                cm.sendOk(cm.spawnSuperMob(selection, 3, 4));
            }
            cm.dispose();
        }
    }
}