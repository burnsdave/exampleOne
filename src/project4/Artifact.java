package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
public class Artifact {
    private int index;
    private String type;
    private int creatureRef;
    private String name;
    private Boolean inUse = false;
    
    public Artifact() {
    }
    
    public Artifact(int indexPass, String typePass, int creatureRefPass, String namePass) {
        this.index = indexPass;
        this.type = typePass;
        this.creatureRef = creatureRefPass;
        this.name = namePass;
    }
    
    public int getCreatureRef() {
        return this.creatureRef;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setBusy(Boolean tf) {
        this.inUse = tf;
    }
    
    public Boolean getBusy() {
        return inUse;
    }
    
    @Override
    public String toString() {
        String indexString = Integer.toString(this.index);
        String creatureString = Integer.toString(this.creatureRef);
        String result = (indexString + " " + type + " " + creatureString + " " + name);
        return result;
    }
}