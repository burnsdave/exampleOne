package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Project4 {
    public static void main(String[] args) throws FileNotFoundException {
        
        Cave gameArena = new Cave();
        HashMap<Integer, Party> parties = new HashMap<Integer, Party>();
        HashMap<Integer, Creature> creatures = new HashMap<Integer, Creature>();
        HashMap<Integer, Treasure> treasures = new HashMap<Integer, Treasure>();
        HashMap<Integer, Artifact> artifacts = new HashMap<Integer, Artifact>();
        HashMap<Integer, Jobs> jobs = new HashMap<Integer, Jobs>();
        
        //read project data from dataZ.txt
        
        JFileChooser chooser = new JFileChooser(".");
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            java.io.File fileSelected = chooser.getSelectedFile();
            Scanner input = new Scanner(fileSelected);
            while (input.hasNext()) {
                String line = input.nextLine().trim();
                if ((line.length() != 0) && (line.charAt(0) != '/')) {
                    Scanner parse = new Scanner(line).useDelimiter("\\s*:\\s*");
                    if (parse.hasNext()) {
                        switch(parse.next().charAt(0)) {
                            case 'p' :
                                int indexPassP = parse.nextInt();
                                String namePassP = parse.next();
                                Party party = new Party(indexPassP, namePassP);
                                parties.put(indexPassP, party);
                                break;
                            case 'c' :
                                int indexPassC = parse.nextInt();
                                String typePassC = parse.next();
                                String namePassC = parse.next();
                                int partyRefPassC = parse.nextInt();
                                int empathyPassC = parse.nextInt();
                                int fearPassC = parse.nextInt();
                                int carryPassC = parse.nextInt();
                                if (parse.hasNext()) {
                                    double agePassC = parse.nextDouble();
                                    double heightPassC = parse.nextDouble();
                                    double weightPassC = parse.nextDouble();
                                    Creature creature = new Creature(indexPassC, typePassC, namePassC, partyRefPassC, empathyPassC, 
                                        fearPassC, carryPassC, agePassC, heightPassC, weightPassC);
                                    creatures.put(indexPassC, creature);
                                } else {
                                    Creature creature = new Creature(indexPassC, typePassC, namePassC, partyRefPassC, empathyPassC, 
                                        fearPassC, carryPassC);
                                    creatures.put(indexPassC, creature);
                                }
                                break;
                            case 't' :
                                int indexPassT = parse.nextInt();
                                String typePassT = parse.next();
                                int creatureRefPassT = parse.nextInt();
                                double weightPassT = parse.nextDouble();
                                int valuePassT = parse.nextInt();
                                Treasure treasure = new Treasure(indexPassT, typePassT, creatureRefPassT, weightPassT, valuePassT);
                                treasures.put(indexPassT, treasure);
                                break;
                            case 'a' :
                                int indexPassA = parse.nextInt();
                                String typePassA = parse.next();
                                int creatureRefPassA = parse.nextInt();
                                if (parse.hasNext()) {
                                    String namePassA = parse.next();
                                    Artifact artifact = new Artifact(indexPassA, typePassA, creatureRefPassA, namePassA);
                                    artifacts.put(indexPassA, artifact);
                                } else {
                                    String namePassA2 = "*unnamed*";
                                    Artifact artifact = new Artifact(indexPassA, typePassA, creatureRefPassA, namePassA2);
                                    artifacts.put(indexPassA, artifact);
                                }
                                break;
                            case 'j' :
                                int indexPassJ = parse.nextInt();
                                String namePassJ = parse.next();
                                int creatureRefPassJ = parse.nextInt();
                                double durationPrepJ = parse.nextDouble();
                                long durationPassJ = Math.round(durationPrepJ);
                                parse.next();
                                int stonePassJ = parse.nextInt();
                                parse.next();
                                int potionPassJ = parse.nextInt();
                                parse.next();
                                int wandPassJ = parse.nextInt();
                                parse.next();
                                int weaponPassJ = parse.nextInt();
                                Jobs job = new Jobs(indexPassJ, namePassJ, creatureRefPassJ, durationPassJ, stonePassJ,
                                        potionPassJ, wandPassJ, weaponPassJ);
                                jobs.put(indexPassJ, job);
                                break;
                            default: break;
                        }
                    }             
                }
            }//end while
            
            input.close();
        }
        else {
            System.out.println("No file selected.");
        }//end if else
        
        for (Integer key : parties.keySet()) {
            gameArena.setParties(parties.get(key));
        }
        
        for (Integer key : creatures.keySet()) {
            Integer partyKey = creatures.get(key).getPartyRef();
            if (partyKey != 0){
                parties.get(partyKey).setCreatures(creatures.get(key));
            }
        }
        
        for (Integer key : treasures.keySet()) {
            Integer creatureKey = treasures.get(key).getCreatureRef();
            if (creatureKey != 0){
                creatures.get(creatureKey).setTreasures(treasures.get(key));
            }
        }
        
        for (Integer key : artifacts.keySet()) {
            Integer creatureKey = artifacts.get(key).getCreatureRef();
            creatures.get(creatureKey).setArtifacts(artifacts.get(key));
        }
        
        for (Integer key : jobs.keySet()) {
            Integer creatureKey = jobs.get(key).getCreatureRef();
            creatures.get(creatureKey).setJobs(jobs.get(key));
            
        }
        
        ArrayList<Party> partyList = new ArrayList<Party>(parties.values());
        ArrayList<Creature> creatureList = new ArrayList<Creature>(creatures.values());
        ArrayList<Treasure> treasureList = new ArrayList<Treasure>(treasures.values());
        ArrayList<Artifact> artifactList = new ArrayList<Artifact>(artifacts.values());
        ArrayList<Jobs> jobsList = new ArrayList<Jobs>(jobs.values());
          
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(gameArena);
    
    for (Party p : gameArena.getPartyList()) {
        DefaultMutableTreeNode party = new DefaultMutableTreeNode(p);
        root.add(party);
        
        for (Creature c : p.getCreatureList()) {
            DefaultMutableTreeNode creature = new DefaultMutableTreeNode(c);
            if (c.getPartyRef() == p.getIndex()) {
                party.add(creature);
            }
            
            for (Treasure t : treasureList) {
                DefaultMutableTreeNode treasure = new DefaultMutableTreeNode(t);
                if (t.getCreatureRef() == c.getIndex()) {
                    creature.add(treasure);
                }
            }
            
            for (Artifact a : artifactList) {
                DefaultMutableTreeNode artifact = new DefaultMutableTreeNode(a);
                if (a.getCreatureRef() == c.getIndex()) {   
                    creature.add(artifact);
                }
            }
            
            for (Jobs j : jobsList) {
                DefaultMutableTreeNode job = new DefaultMutableTreeNode(j);
                if (j.getCreatureRef() == c.getIndex()) {
                    creature.add(job);
                }
            }
         }
    }//end for loop for node load
    
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    final JTree caveJTree = new JTree(treeModel);
    
    //end tree info to pass
    
    for (Party p : gameArena.getPartyList()) {
        
        for (Creature c : p.getCreatureList()) {
            DefaultMutableTreeNode creature = new DefaultMutableTreeNode(c);
            if (c.getPartyRef() == p.getIndex()) {
                for (Artifact a : artifactList) {
                    if (a.getCreatureRef() == c.getIndex()) {   
                        p.setArtifacts(a);
                    }
                }
                for (Jobs j : jobsList) {
                    if (j.getCreatureRef() == c.getIndex()) {
                        j.setParentCreature(c);
                        j.setParentParty(p);
                    }
                }
            }
         }
    }//end creation of artifact list per party
    
    Gui nameFrame = new Gui(caveJTree, treeModel, root, parties, creatures, treasures, artifacts);
    for (Jobs j : jobsList) {
        j.setGui(nameFrame);
    }//assign gui to jobs
    nameFrame.setTitle("Project 4 by Dave Burns");
    nameFrame.setSize(1000, 650);
    nameFrame.setLocationRelativeTo(null);
    nameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    nameFrame.setVisible(true);
    nameFrame.runThreads();
    
    }//end main  
}//end Project4