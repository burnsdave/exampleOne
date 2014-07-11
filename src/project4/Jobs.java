package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JProgressBar;

public class Jobs implements Runnable {
    private int index;
    private int creatureRef;
    private int jobRefInt;
    private String name;
    private long duration;
    private int stoneReq = 0;
    private int potionReq = 0;
    private int wandReq = 0;
    private int weaponReq = 0;
    private Gui frameRef;
    private Thread thisThread;
    private Thread priorThread = null;
    private Thread partyThread;
    private Thread threadFromMain;
    private ArrayList<Integer> creatureJobList = new ArrayList<Integer>();
    private Boolean singleJob = false;
    private Boolean paused = false;
    private Boolean wasPaused = false;
    private Boolean cancelThread = false;
    private Boolean jobDone = false;
    private Boolean needsArtifacts;
    private Party parentParty;
    private Creature parentCreature;
    private long delay = 200L;
    private final Object lock = new Object();
    private ReentrantLock lockR = new ReentrantLock();
    private Condition hold = lockR.newCondition();
    
    public Jobs() {
    }
    
    public Jobs(int indexPass, String namePass, int creatureRefPass, long durationPass, int stonePass, int potionPass,
            int wandPass, int weaponPass) {
        
        this.index = indexPass;
        this.name = namePass;
        this.creatureRef = creatureRefPass;
        this.duration = durationPass;
        this.stoneReq = stonePass;
        this.potionReq = potionPass;
        this.wandReq = wandPass;
        this.weaponReq = weaponPass;
    }
    
    public void setGui(Gui passed) {
        this.frameRef = passed;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCreatureRef() {
        return this.creatureRef;
    }
    
    public void setParentCreature(Creature on) {
        this.parentCreature = on;
    }
    
    public Creature getParentCreature() {
        return this.parentCreature;
    }
    
    public void setParentParty(Party on) {
        this.parentParty = on;
    }
    
    public Party getParentParty() {
        return this.parentParty;
    }
    
    public void setCreatJobList(int i) {
        this.creatureJobList.add(i);
    }
    
    public void setSingleJob(Boolean tf) {
        this.singleJob = tf;
    }
    
    public void setJobIntRef(int i) {
        this.jobRefInt = i;
    }
    
    public void setMainThread(Thread t) {
        this.threadFromMain = t;
    }
    
    public void cancelJob(Boolean tf) {
        this.cancelThread = tf;
    }
    
    public void setNeedsArtifacts(Boolean tf) {
        this.needsArtifacts = tf;
    }
    
    public boolean checkJobDone() {
        return jobDone;
    }
    
    public void setPause(Boolean tf) {
        this.paused = tf;
    }
    
    public void setWaitThread(Thread t) {
        this.priorThread = t;
    }
    
    public void setThisThread(Thread t) {
        this.thisThread = t;
    }
    
    @Override
    public String toString() {
        String indexString = Integer.toString(this.index);
        String creatureString = Integer.toString(this.creatureRef);
        String result = (indexString + " " + name + " " + creatureString + " " + duration);
        return result;
    }
    
    @Override
    public synchronized void run() {
        Thread threadToJoin = this.priorThread;
        int cRef = this.creatureRef;
        String jobName = this.name;
        final Party partyLock = parentParty;
        JProgressBar progBar = new JProgressBar(0,10);
        progBar.setStringPainted(true);
        frameRef.setProgBars(progBar, cRef, jobName, thisThread, jobRefInt);
        if (singleJob == false) {
            try {
                threadToJoin.join();
            } catch(InterruptedException e) {
            }
        }
        System.out.println(needsArtifacts);
        synchronized (lock) {
            while (parentParty.hasRequiredArtifacts(stoneReq, potionReq, wandReq, weaponReq, this, frameRef, thisThread) == 0) {
                try {
                    System.out.println(Thread.currentThread().toString() + needsArtifacts + " from lock");
                    lock.notify();
                    System.out.println(needsArtifacts);
                    thisThread.sleep(1000);
                }
                catch(InterruptedException ex) {
                }
            }
        }
        if (singleJob == false) {
            try {
                threadToJoin.join();
            } catch(InterruptedException e) {
            }
        }
        duration *= 10;
            for (int i = 1; i < duration; i++) {
                if (!cancelThread) {
                    if (paused == true) {
                        try {
//                            Thread.sleep(delay);
                            synchronized(this) {
                                while (paused == true) {
                                    System.out.println(Thread.currentThread() + " pause job ");
                                    wait();
                                }
                            }
                        } catch(InterruptedException ex) {
                        } 
                    }
                    progBar.setValue((int) ( (i * 10 ) / duration  ));
                }
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {
                }
                
            }
        if (cancelThread) {
            progBar.setValue(0);
        }else {
            progBar.setValue(100);
            this.jobDone = true;
            parentParty.clearArtifactUse();
        }
        
    }
}