
var status; 


function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    var people = cm.getFullConnected();
    if (mode == 1) { 
        status++; 
    }else{ 
        status--; 
    }
    if (status == -1) { 
        cm.dispose();
    }
    else if (status == 0) { 

        cm.sendSimple("#e\t\t\t\t\t\t\t\t\t#r  <Notice Board>\r\n\
#bGlobal Announcement -#r Don't forget to vote~!\r\n\
#d\t\t\t\t\t\t\t\tTotal Players Online - "+people+"\r\n\
#b\r\n\
#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#General Announcements\r\n\
#L3##rUn-mute Me!#b\r\n\
#L2#Staff\r\n\
#L1#FM Guide");
    } else if (status == 1) { 
        if (selection == 0) {
            cm.sendOk("#eNo Announcements");
            cm.dispose();
        } else if (selection == 1) {
            cm.sendOk("#e\t\t\t\t\t\t\t\t\t#rFM GUIDE\r\n\
#rFM1 - #bMushmom\r\n\
#rFM2 - #bMushmom\r\n\
#rFM3 - #bHeadless Horseman\r\n\
#rFM4 - #bHeadless Horseman\r\n\
#rFM5 - #bBigfoot\r\n\
#rFM6 - #bBigfoot\r\n\
#rFM7 - #bBlack Crow\r\n\
#rFM8 - #bBlack Crow\r\n\
#rFM9 - #bPapulatus Clock\r\n\
#rFM10 - #bPianus\r\n\
#rFM11 - #bFemale Boss\r\n\
#rFM12 - #bInferno Kyrin\r\n\
#rFM13+ - #bGuild Headquarters");
            cm.dispose();
        } else if (selection == 2) {
            cm.sendOk("#e\t\t\t\t\t\t\t\t#r  SharpStory Staff\r\n\
#rOwner - #bCoffee - India\r\n\
#rModerator - #bIsabelle - Canada\r\n\
#rModerator - #bFadedMemory - USA\r\n\
#rModerator - #bEnder - USA\r\n\
#rModerator - #bInsane - Malaysia\r\n\
#rModerator - #bRascaL - USA (Puerto Rico)");
            cm.dispose();
        } else if (selection == 3) {
            cm.getPlayer().allowChat();
            cm.dispose();
        }
    }
}  