package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.util.ArrayList;

public class Party {
    private int index;
    private String name;
    private int stones = 0;
    private int potions = 0;
    private int wands = 0;
    private int weapons = 0;
    private int totalArtifacts = 0;
    private int stonesAvail = 0;
    private int potionsAvail = 0;
    private int weaponsAvail = 0;
    private int wandsAvail = 0;
    private Boolean needsArt = false;
    private Jobs job;
    private String logAppend = "";
    private ArrayList<Artifact> partyArtifacts = new ArrayList<Artifact>();
    private ArrayList<Creature> creatures = new ArrayList<Creature>();
    private ArrayList<Artifact> usedArts = new ArrayList<Artifact>();
    private ArrayList<Artifact> unUsedArts = new ArrayList<Artifact>();
    public Party() {
        
    }
    
    public Party(int indexPass, String namePass) {
        this.index = indexPass;
        this.name = namePass;
    }
    
    
    public Party(int indexPass, String namePass, ArrayList<Creature> creaturePass) {
        this.creatures = creaturePass;
        this.index = indexPass;
        this.name = namePass;  
    }
    
    public void setCreatures(Creature creaturePass) {
        this.creatures.add(creaturePass);
    }
    
    public void setArtifacts(Artifact artPass) {
        this.partyArtifacts.add(artPass);
        if (artPass.getType().equalsIgnoreCase("stone")) {
            stones++;
            totalArtifacts++;
        }else if (artPass.getType().equalsIgnoreCase("potion")) {
            potions++;
            totalArtifacts++;
        }else if (artPass.getType().equalsIgnoreCase("wand")) {
            wands++;
            totalArtifacts++;
        }else if (artPass.getType().equalsIgnoreCase("weapon")) {
            weapons++;
            totalArtifacts++;
        }
    }
    
    public synchronized int hasRequiredArtifacts(int stonesReq, int potionsReq, int wandsReq, int weaponReq, Jobs j, Gui g, 
            Thread t) {
        int sReq = stonesReq;
        int pReq = potionsReq;
        int wandReq = wandsReq;
        int weapReq = weaponReq;
        int sUsed = 0;
        int pUsed = 0;
        int wandUsed = 0;
        int weapUsed = 0;
        Gui gui = g;
        final Jobs jobf = j;
        Thread threadRef = t;
        if (sReq > stones | pReq > potions | wandReq > wands | weapReq > weapons) {
            job.cancelJob(true);
            System.out.println("Not enough artifacts for " + j.getParentCreature().getName());
            j.setNeedsArtifacts(true);
            needsArt = true;
        }else {
            for (int i = 0; i < partyArtifacts.size(); i++) {
                String type = partyArtifacts.get(i).getType();
                if (type.equalsIgnoreCase("stone") && !partyArtifacts.get(i).getBusy() && sUsed < sReq) {
                    usedArts.add(partyArtifacts.get(i));
                    partyArtifacts.remove(i);
                    sUsed++;
                }else if (type.equalsIgnoreCase("potion") && !partyArtifacts.get(i).getBusy() && pUsed < pReq) {
                    usedArts.add(partyArtifacts.get(i));
                    partyArtifacts.remove(i);
                    pUsed++;
                }else if (type.equalsIgnoreCase("wand") && !partyArtifacts.get(i).getBusy() && wandUsed < wandReq) {
                    usedArts.add(partyArtifacts.get(i));
                    partyArtifacts.remove(i);
                    wandUsed++;
                }else if (type.equalsIgnoreCase("weapon") && !partyArtifacts.get(i).getBusy() && weapUsed < weapReq) {
                    usedArts.add(partyArtifacts.get(i));
                    partyArtifacts.remove(i);
                    weapUsed++;
                }
            }
            if (sReq == sUsed && pReq == pUsed && wandReq == wandUsed && weapReq == weapUsed) {
                j.setNeedsArtifacts(false);
                needsArt = false;
                String artString = "";
                for (int i = 0; i < usedArts.size(); i++) {
                    artString += usedArts.get(i).getName() + " ";
                }
                logAppend += j.getParentCreature().getName() + " is doing Job: " + j.getName() +
                        "...\n " + artString + "\n";
                gui.setLog(logAppend);
                return 0;
            }else {
                logAppend += j.getParentCreature().getName() + " COULD NOT acquire all artifacts for " + j.getName() + "\n";
                gui.setLog(logAppend);
                j.setNeedsArtifacts(true);
                needsArt = true;
                System.out.println(Thread.currentThread().toString() + "from party");
                return 1;
            }
        }
        return 0;
    }
    
    
    public synchronized void clearArtifactUse() {
        for (int i = 0; i < usedArts.size(); i++) {
            partyArtifacts.add(usedArts.get(i));
            usedArts.remove(i);
        }
    }
    
    public Boolean getArtNeeded() {
        return needsArt;
    }
    
    
    public String getArtifactString() {
        String output = "Party: " + name + " has " + stones + " stones " + potions + " potions " + wands
                + " wands " + weapons + " weapons.\n" + partyArtifacts.toString();
        return output;
    } 
    
    public ArrayList<Creature> getCreatureList() {
        final ArrayList<Creature> creaturePasser = creatures;
        return creaturePasser;
    }
    
    public String getCreatures() {
        String creatureReturn = "";
        for (int i=0; i < creatures.size(); i++) {
            String creatureAdd = creatures.get(i).toString() + " ";
            creatureReturn = creatureReturn.concat(creatureAdd);
        }
        return creatureReturn;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getIndex() {
        return index;
    }
    
    @Override
    public String toString() {
        String indexString = Integer.toString(this.index);
        String partyname = name;
        String creatureString = this.getCreatures();
        String result = (indexString + " " + partyname);
        return result;
    }

}
