package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.util.ArrayList;

public class Creature {
    private int index;
    private String type;
    private String name;
    private int partyRef;
    private int empathy;
    private int fear;
    private int carryCapacity;
    private double age;
    private double height;
    private double weight;
    private boolean jobRunning = false;
    private ArrayList<Treasure> treasures = new ArrayList<Treasure>();
    private ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
    private ArrayList<Jobs> jobs = new ArrayList<Jobs>();
    
    public Creature() {
        
    }
    
    public Creature(int indexPass, String typePass, String namePass, int partyRefPass, int empathyPass, 
            int fearPass, int carryCapacityPass, double agePass, double heightPass, double weightPass, 
            ArrayList<Treasure> treasurePass, ArrayList<Artifact> artifactPass) {
        this.index = indexPass;
        this.type = typePass;
        this.name = namePass;
        this.partyRef = partyRefPass;
        this.empathy = empathyPass;
        this.fear = fearPass;
        this.carryCapacity = carryCapacityPass;
        this.age = agePass;
        this.height = heightPass;
        this.weight = weightPass;
        this.treasures = treasurePass;
        this.artifacts = artifactPass;
    }
    
    public Creature(int indexPass, String typePass, String namePass, int partyRefPass, int empathyPass, 
            int fearPass, int carryCapacityPass, double agePass, double heightPass, double weightPass) {
        this.index = indexPass;
        this.type = typePass;
        this.name = namePass;
        this.partyRef = partyRefPass;
        this.empathy = empathyPass;
        this.fear = fearPass;
        this.carryCapacity = carryCapacityPass;
        this.age = agePass;
        this.height = heightPass;
        this.weight = weightPass;
    }
    
    public Creature(int indexPass, String typePass, String namePass, int partyRefPass, int empathyPass, 
            int fearPass, int carryCapacityPass) {
        this.index = indexPass;
        this.type = typePass;
        this.name = namePass;
        this.partyRef = partyRefPass;
        this.empathy = empathyPass;
        this.fear = fearPass;
        this.carryCapacity = carryCapacityPass;
    }
    
    public int getPartyRef() {
        return this.partyRef;
    }
    
    public void setTreasures(Treasure treasurePass) {
            this.treasures.add(treasurePass);
    }
    
    public String getTreasures() {
        String treasureReturn = "";
        for (int i=0; i < treasures.size(); i++) {
            String treasureAdd = treasures.get(i).toString() + " ";
            treasureReturn = treasureReturn.concat(treasureAdd);
        }
        return treasureReturn;
    }
    
    public ArrayList<Treasure> getTreasureList() {
        final ArrayList<Treasure> treasurePasser = treasures;
        return treasurePasser;
    }
    
    public void setArtifacts(Artifact artifactsPass) {
        this.artifacts.add(artifactsPass);
    }
    
    public String getArtifacts() {
        String artifactReturn = "";
        for (int i=0; i < artifacts.size(); i++) {
            String artifactAdd = artifacts.get(i).toString() + " ";
            artifactReturn = artifactReturn.concat(artifactAdd);
        }
        return artifactReturn;
    }
    
    public void setJobs(Jobs jobPass) {
        this.jobs.add(jobPass);
    }
    
    public String getJobsString() {
        String jobsReturn = "";
        for (int i=0; i < jobs.size(); i++) {
            String jobAdd = jobs.get(i).toString() + " - ";
            jobsReturn = jobsReturn.concat(jobAdd);
        }
        return jobsReturn;
    }
    
    public int getJobsSize() {
        return this.jobs.size();
    }
    
    public Jobs getJobs(int i) {
        return this.jobs.get(i);
    }
    
    public void setJobRunTrue(){
        this.jobRunning = true;
    }
    
    public void setJobRunFalse(){
        this.jobRunning = false;
    }
    
    public int getIndex() {
        return index;
    }
    
    public double getAge() {
        return this.age;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    @Override
    public String toString() {
        String indexString = Integer.toString(this.index);
        String partyRefString = Integer.toString(this.partyRef);
        String empathyString = Integer.toString(this.empathy);
        String fearString = Integer.toString(this.fear);
        String carryString = Integer.toString(this.carryCapacity);
        String ageString = Double.toString(this.age);
        String heightString = Double.toString(this.height);
        String weightString = Double.toString(this.weight);
        String treasureString = this.getTreasures();
        String artifactString = this.getArtifacts();
        String result = (indexString + " " + type + " " + name + " " + partyRefString + " " + empathyString + " " + fearString + 
                " " + carryString + " " + ageString + " " + heightString + " " + weightString);
        return result;
    }

}
