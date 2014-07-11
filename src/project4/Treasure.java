package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
public class Treasure {
    private int index;
    private String type;
    private int creatureRef;
    private double weight;
    private int value;
    
    public Treasure() {
        
    }
    
    public Treasure(int indexPass, String typePass, int creatureRefPass, double weightPass, int valuePass) {
        this.index = indexPass;
        this.type = typePass;
        this.creatureRef = creatureRefPass;
        this.weight = weightPass;
        this.value = valuePass;
    }
    
    public int getCreatureRef() {
        return this.creatureRef;
    }
    
    public String getType(){
        return this.type;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    @Override
    public String toString() {
        String indexString = Integer.toString(this.index);
        String creatureString = Integer.toString(this.creatureRef);
        String weightString = Double.toString(this.weight);
        String valueString = Integer.toString(this.value);
        String result = (indexString + " " + type + " " + creatureString + " " + weightString + " " + valueString);
        return result;
    }

}