var status = -1;

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
    if (status == 0) { 
        cm.sendNext("Go ahead and press Next so I can proceed to the next status."); 
    } else if (status == 1) { 
        cm.sendSimple("Would you like to see how I use status again? \r\n #L0# Yes. #l \r\n #L1# No. #l"); 
    } else if (status == 2) { 
        if (selection == 0) { 
            cm.sendOk("Here I am, in another status. As you can see from the script, this window is in status 2."); 
            cm.dispose(); 
        } else if (selection == 1) { 
            cm.sendOk("Well, sucks to be you, don't it? This window is also in status 2 :) "); 
            cm.dispose(); 
        } 
    } 
}  