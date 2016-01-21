var status; 
var slot;
var newpot;
var newact;

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
            cm.sendSimple("#eWelcome, I'm the #rGuild Headquarters NPC#k, would you like to rent a guild headquarter?\r\n\
\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
" + cm.getAvaliableHQS() + "\r\n\
#L0##bVisit MY Guild Headquarters\r\n\
#L1##bLearn about Guild Hideouts"); 
        } else if (status == 1) {
            if (selection == 0) {
                var guildid = cm.getPlayer().getGuildId();
                if (guildid < 1) {
                    cm.sendOk("#e#rI'm sorry, you are not in a guild!");
                    cm.dispose();
                } else {
                    var hideout = cm.getPlayer().getGuildHQ();
                    if (hideout == 0) {
                        cm.sendOk("#e#rI'm sorry, your guild does not seem to have a hideout!");
                        cm.dispose();
                    } else {
                        cm.getPlayer().changeMap(hideout);
                    }
                }
            } else if(selection == 1) {
                cm.sendOk("#eGuild Headquarters serve as a hideout for your guild, and #ronly#k your guild members can enter it. Inside your headquarters, you can change your job #rregardless#k of your level and current job. Also, you can spawn bosses that are stronger and provide much #rmore exp#k than usual.");
                cm.dispose();
            } else {
                var guildid = cm.getPlayer().getGuildId();
                if (guildid < 1) {
                    cm.sendOk("#e#rI'm sorry, you are not in a guild!");
                    cm.dispose();
                } else {
                    if (cm.getPlayer().getGuildRank() == 1) {
                        if (cm.getGuild().getGP() >= cm.getGuild().getHideoutCost(selection)) {
                            if (cm.getGuild() != null) cm.getGuild().gainGP(-1 * (cm.getGuild().getHideoutCost(selection)));
                            cm.sendOk(cm.getGuild().leaseHideout(selection));
                            cm.dispose();
                        } else {
                            cm.sendOk("#e#rI'm sorry, your guild does not have enough guild points!");
                            cm.dispose();
                        }
                    } else {
                        cm.sendOk("#e#rI'm sorry, you do not have permission to rent a headquarter for your guild!");
                        cm.dispose();
                    }
                }
            }
        }
    }  
}
