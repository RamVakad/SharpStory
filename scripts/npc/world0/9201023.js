var status = 0;
var auctionid = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#eHello, I'm the #rSharpStory#k #bAuction Income Dropbox#k NPC! You can collect your income here!\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0##b\r\n\
" + cm.getDropbox());
        } else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("#eYou will be notified when someone buys any of your auctions. You do not need to keep checking with me.");
                cm.dispose();
            } else {
                auctionid = selection;
                cm.sendYesNo(cm.getDropboxInfo(auctionid));
            }
        } else if (status == 2) {
           cm.sendOk(cm.retriveDropbox(auctionid)); 
           cm.dispose();
        }
    }
}