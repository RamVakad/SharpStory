importPackage(Packages.tools);
importPackage(Packages.client);
var status; 

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
        cm.sendNext("#eWelcome to #bSharpStory#k, #r#h ##k. I'm Sharp, and I will guide you until you're ready to set off on your own! I'll give you a present in the end also!");
    } else if (status == 1) { 
        cm.sendNext("#eUhm, Ok?", 2);
    }else if (status == 2) { 
        cm.sendNextPrev("#e\
#kLet us start with the #rRULES#k :\r\n\
#dIt's cumpulsory to obey them, or else you'll be behind bars in no time!\r\n\
\r\n\
#rRule #1 #k- #bAbsolutely NO HACKING in any possible way, NO 3RD PARTY PROGRAMS!\r\n\
#rRule #2 #k- #bDon't irritate the GM's unless it is important in the true sense of the word 'important'!\r\n\
#rRule #3 #k- #bProfane language is allowed, but with acute moderation.\r\n\
#rRule #4 #k- #bNO ADVERTISING!\r\n\
#rRule #5 #k- #bThe staff has permission to ban any disrespectful players, let's hope you're not one of them.\r\n\
#rRule #6 #k- #bDon't SPAM the avatar smegas. We don't need to see your face.\r\n\
#rRule #7 #k- #bAbusing exploits will not be tolerated.\r\n\
#rRule #8 #k- #bLoss of items due to insufficient inventory space won't be refunded.\r\n\
#rRule #9 #k- #dWell, this isn't really a rule, but it would help if you vote for us online!\r\n\
\r\n\
#kRemember, the community is growing, so never give up hope if you don't find friends right away."); 
    } else if (status == 3) { 
        cm.sendNext("#eOk! I'm ready! Let's go!", 2);
    } else if (status == 4) { 
        cm.sendNextPrev("#e#dDon't get ahead of yourself!\r\n#rSince you know the rules now, I'll explain a bit about this server.\r\n\r\n\
#kThe rates are the following:\r\n\
#rEXP RATE#k : #b400#kx\r\n\
#rMESO RATE#k : #b200#kx\r\n\
#rDROP RATE#k : #b5#kx\r\n\
\r\n\
#kWe offer features like Item Potential, Occupations, Hotkeys, Auctioning, Offline Mail and Guild Headquarters just to name a few.\r\n\
\r\n\
#kMost of these features should be self explanatory if you pay attention to what the NPCs tell you, but you can always find extra help at the forums.\r\n\
\r\n\
#kA quick note about your mail inbox - It cannot exceed more than 50 messages, so you should clear it every now and then. Or else, you won't receive new messages!\r\n\
\r\n\
#kFinally! You won't feel like this server is 400x EXP RATE at first, but you will get used to it and you won't feel like you're getting very low exp anymore.\r\n\
\r\n\
#dJust like Google, #r@help#d command is your best friend in SharpStory. Try it after you've completed the tutorial. I cannot stress that enough.");
    } else if (status == 5) {
        cm.sendSimple("#eIn #rSharpStory#k, we all have occupations which we can choose from. These occupations advance as you level up. Currently, there are four occupations you can choose from - click on each one of them to find out the more.#b\r\n\
\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#L0#Citizen\r\n\
#L1#Casher\r\n\
#L2#Tryhard\r\n\
#L3#Winner");
    }
    else if (status == 6) {
        if (selection == 0) {
            cm.sendNextPrev("#e#r\t\t\t\t\t\t\t\t\t\t\t  Citizen\r\n\
#bBenefit#k - As a citizen, you will have a chance to gain additional #rABILITY POINTS#k when you level up.\r\nEach occupation level will increase the chance of gaining benefit.");
        } else if (selection == 1) {
            cm.sendNextPrev("#e#r\t\t\t\t\t\t\t\t\t\t\t  Casher\r\n\
#bBenefit#k - As a Casher, you will have a chance to gain #rMAPLE POINTS#k when you kill a monster.\r\nEach occupation level will increase the quantity of benefit gained.");
        }
        else if (selection == 2) {
            cm.sendNextPrev("#e#r\t\t\t\t\t\t\t\t\t\t\t  Tryhard\r\n\
#bBenefit#k - As a Tryhard, you will gain more #rEXPERIENCE#k than usual when you kill a monster.\r\nEach occupation level will increase the quantity of benifit gained.");
        }
        else if (selection == 3) {
            cm.sendNextPrev("#e#r\t\t\t\t\t\t\t\t\t\t\t  Winner\r\n\
#bBenefit#k - As a Winner, you will gain more #rMESOS#k than usual when you pick up cash.\r\nEach occupation level will increase the quantity of benifit gained.");
        }
    }else if (status == 7) {
        cm.sendNext("#eThat's awesome and all, but can I start playing now?", 2);
    }
    else if (status == 8) {
        cm.sendSimple("#eJust bear with me for a few more seconds! \r\nChoose your occupation:\r\n\
\r\n#fUI/UIWindow.img/QuestIcon/3/0#\r\n\
#b#L0#Citizen\r\n\
#L1#Casher\r\n\
#L2#Tryhard\r\n\
#L3#Winner");
    } else if (status == 9) {
        cm.sendOk("#eRemember, you can always change your occupation once your occupation level reaches 100!");
        cm.getPlayer().setOccupation(selection); 
    } else if (status == 10) {
        cm.sendOk("#eI'm going to send you to Bobby, he will brief you before you start your journey, alright? Bye.");
    } else if (status == 11) {
        cm.sendOk("#e#rThat's great, but wait!#k Who's Bobby?", 2);
    }
    else if (status == 12) {
        cm.gainItem(5076000, 10);
        cm.gainItem(5072000, 20);
        cm.gainItem(5390000, 5);
        cm.gainItem(5390001, 5);
        cm.gainItem(5390002, 5);
        cm.gainItem(2040807, 7);
        cm.gainItem(2022179, 10);
        cm.gainItem(2000005, 400);
        cm.gainItem(1082149, 1);
        cm.gainItem(2002025, 1);
        cm.gainItem(2002024, 1);
        cm.gainItem(2002023, 1);
        cm.gainItem(2002022, 1);
        cm.gainItem(2002021, 1);
        cm.getPlayer().giveSkill(4001003);
        cm.getPlayer().changeMap(970030020, 0);
        cm.getPlayer().dropMessage(6, "[Sharp]::Just talk to Bobby!");
        cm.getClient().getSession().write(MaplePacketCreator.sendHint("#eWhat? Wait. #rWTF!@! #bWhere am I??", 280, 5));
        cm.dispose();
    }
} 
