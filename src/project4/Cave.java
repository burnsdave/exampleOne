package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.util.ArrayList;

public class Cave {
    private ArrayList<Party> parties = new ArrayList<Party>();
    private String name = "Main Cave";
    
    public Cave() {
        
    }
    
    public Cave(ArrayList<Party> partyPass) {
        this.parties = partyPass;
    }
    
    public void setParties(Party partyPass) {
        this.parties.add(partyPass);
    }
    
    public ArrayList<Party> getPartyList() {
        final ArrayList<Party> partyPasser = parties;
        return partyPasser;
    }
    
    public String getParties() {
        String partyReturn = "";
        for (int i=0; i < parties.size(); i++) {
            String partyAdd = parties.get(i).toString() + " ";
            partyReturn = partyReturn.concat(partyAdd);
        }
        return partyReturn;
    }
    
    @Override
    public String toString() {
        String caveString = this.getParties();
        String result = (caveString);
        return name;
    }
}
