var setupTask;

importPackage(Packages.tools);

function init() {
    scheduleNew();
}

function scheduleNew() {
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis()) {
        nextTime += 5000000;
    }
    setupTask = em.scheduleAtTimestamp("start", nextTime);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    scheduleNew();
    var rand1=Randomizer.nextInt(10);
    var rand2=Math.floor(Math.random()*10+1);
    if (rand1<5){
        em.incRate(rand2*2000000);
        if (em.getRate() > 1100000000) em.setRate(1100000000);
    } else {
        em.decRate(rand2*2000000);
        if (em.getRate() < 900000000) em.setRate(900000000);
    }
}