package project4;
//
//Author: David Burns
//Date: 10-10-2013
//Purpose: Project 4 for CMSC 335
//
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Gui extends JFrame {
    private static final long serialVersionUID = 1L;
    private Cave gameArena = new Cave();
    private HashMap<Integer, Party> parties = new HashMap<Integer, Party>();
    private HashMap<Integer, Creature> creatures = new HashMap<Integer, Creature>();
    private HashMap<Integer, Treasure> treasures = new HashMap<Integer, Treasure>();
    private HashMap<Integer, Artifact> artifacts = new HashMap<Integer, Artifact>();
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode(gameArena);
    private DefaultTreeModel treeModel = new DefaultTreeModel(root); 
    private JTree treeShow;
//    private Cave caveSearch;
    
    private String[] searchCategories = {"Index", "Name", "Type"};
    private String[] creatureSortCats = {"Name", "Age", "Height", "Weight"};
    private String[] treasureSortCats = {"Weight", "Value"};
    private String log = "";
    private JButton jbtSearch = new JButton("Search");
    private JButton jbtSort = new JButton("Sort");
    private JButton jbtLog = new JButton("Show Thread Log");
    private JTextField searchText = new JTextField();
    private JComboBox jcSearchBy = new JComboBox(searchCategories);
    private JComboBox jcSortCreatures = new JComboBox(creatureSortCats);
    private JComboBox jcSortTreasures = new JComboBox(treasureSortCats);
    private JTextArea jtfSearchOutput = new JTextArea("Awaiting Search or Sort");
    private JTextArea jtfProgressBars = new JTextArea("Progress of Current Jobs: ");
    private JProgressBar testBar = new JProgressBar(0,10);
    private JScrollPane progBarScrollPane = new JScrollPane();
    private JPanel barCollection = new JPanel();
    private ArrayList<Thread> threadArray = new ArrayList<Thread>();
    private Thread thisThread;
    private Jobs thisJob;
    
    public Gui(JTree treeFromMain, DefaultTreeModel treePass, DefaultMutableTreeNode nodePass,
            HashMap<Integer, Party> partyFromMain, HashMap<Integer, Creature> creatureFromMain, 
            HashMap<Integer, Treasure> treasureFromMain, HashMap<Integer, Artifact> artifactFromMain) {
        treeShow = treeFromMain;
        this.treeModel = treePass;
        this.root = nodePass;
        this.parties = partyFromMain;
        this.creatures = creatureFromMain;
        this.treasures = treasureFromMain;
        this.artifacts = artifactFromMain;
        
        JPanel menuText = new JPanel();
        menuText.setLayout(new GridLayout(2,6,5,5));
        menuText.add(new JLabel("Enter Search Term:"));
        menuText.add(searchText);
        menuText.add(new JLabel("Search Type:"));
        menuText.add(jcSearchBy);
        menuText.add(jbtSearch);
        menuText.add(jbtLog);
        menuText.add(new JLabel("Sort Creatures by:"));
        menuText.add(jcSortCreatures);
        menuText.add(new JLabel("Sort Treasures by:"));
        menuText.add(jcSortTreasures);
        menuText.add(jbtSort);
        
        
        JPanel outputs = new JPanel();
        JScrollPane showTree = new JScrollPane(treeShow);
        showTree.setPreferredSize(new Dimension(475,325));
        JScrollPane showSearch = new JScrollPane(jtfSearchOutput);
        showSearch.setPreferredSize(new Dimension(475,325));
        outputs.add(showTree);
        outputs.add(showSearch);
        
        barCollection.setLayout(new GridLayout(0,5,5,5));
        progBarScrollPane.setPreferredSize(new Dimension(900,200));
        progBarScrollPane.setViewportView(barCollection);
        //panel for progress bars
        
        setLayout(new BorderLayout(5,5));
        add(menuText, BorderLayout.NORTH);
        add(outputs, BorderLayout.CENTER);
        add(progBarScrollPane, BorderLayout.SOUTH);
        
        //add panels to frame
        
        jbtSearch.addActionListener(new Search());
        jbtSort.addActionListener(new Sort());
        jbtLog.addActionListener(new Log());
        //listeners
        
    }//end Gui
    
    public void setProgBars(JProgressBar bar, int creatureRef, String nameRef, Thread threadRef, int jobRef) {
        String creatureName = this.creatures.get(creatureRef).getName();
        String jobName = nameRef;
        thisJob = this.creatures.get(creatureRef).getJobs(jobRef);
        thisThread = threadRef;
        final Jobs actionJob = thisJob;
        final Thread actionThread = thisThread;
        JLabel cName = new JLabel(creatureName);
        JTextField jName = new JTextField(jobName);
        jName.setEditable(false);
        JButton pause = new JButton("Pause");
        JButton cancel = new JButton("Cancel");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                if (source.getText().equals("Pause")) {
                    source.setText("Resume");
                    System.out.println(Thread.currentThread() + " pause button");
                    actionJob.setPause(true);
                    }else {
                        source.setText("Pause");
                        System.out.println(Thread.currentThread() + " pause button 2");
                        actionJob.setPause(false);
                        synchronized (actionJob) {
                            actionJob.notify();
                        }
                    }
            }//anonymous listener for pause/resume
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                if (actionJob.checkJobDone()) {
                    System.out.println(Thread.currentThread() + " cancel button");
                    source.setText("JOB FINISHED");
                    source.setEnabled(false);
                }else {
                    System.out.println(Thread.currentThread() + " cancel button 2");
                    source.setText("JOB CANCELLED");
                    source.setEnabled(false);
                    actionJob.cancelJob(true);
                }
            }//anonymous listener for cancel button
        });
        this.barCollection.add(bar);
        this.barCollection.add(cName);
        this.barCollection.add(jName);
        this.barCollection.add(pause);
        this.barCollection.add(cancel);
        barCollection.updateUI();
    }//end progressbar building
    
    
    public void runThreads() {
        for (Integer key : creatures.keySet()) {
            int size = creatures.get(key).getJobsSize();
            Thread singleJob = new Thread(creatures.get(key).getJobs(0));
            creatures.get(key).getJobs(0).setJobIntRef(0);
            creatures.get(key).getJobs(0).setThisThread(singleJob);
            creatures.get(key).getJobs(0).setSingleJob(true);
            singleJob.start();
            try {
                Thread.currentThread().sleep(100);
            } catch(InterruptedException e) {
            }
            if (size > 1) {
                for (int i = 1; i < size; i++) {
                    creatures.get(key).getJobs(i).setSingleJob(false);
                    Thread anotherJob = new Thread(creatures.get(key).getJobs(i));
                    creatures.get(key).getJobs(i).setJobIntRef(i);
                    creatures.get(key).getJobs(i).setThisThread(anotherJob);
                    creatures.get(key).getJobs(i).setWaitThread(singleJob);
                    anotherJob.start();
                    singleJob = anotherJob;
                    try {
                    Thread.currentThread().sleep(100);
                    } catch(InterruptedException e) {
                    }
                }
            }
        }
    }//end threadrunner
    
    public void setLog(String logPass) {
        this.log += logPass;
    }
    
    private class Log implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jtfSearchOutput.setText(log);
            
        }//end method for log display
    }//end ActionListener class
    
    private class Search implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userSearch = searchText.getText().toLowerCase();
            if (jcSearchBy.getSelectedItem() == "Index") {
                if (traverseIndex(userSearch, root).equals("Not Found") != true) {
                    jtfSearchOutput.setText(traverseIndex(userSearch, root));
                } else {
                jtfSearchOutput.setText("No matching results");
                }
            }
            else if (jcSearchBy.getSelectedItem() == "Name") {
                if (traverseName(userSearch, root).equals("Not Found") != true) {
                    jtfSearchOutput.setText(traverseName(userSearch, root));
                } else {
                jtfSearchOutput.setText("No matching results");
                }
               
            }
            else { //jcSearchBy is Type
                if (traverseType(userSearch, root).equals("Not Found") != true) {
                    jtfSearchOutput.setText(traverseType(userSearch, root));
                } else {
                String textOut = ("Search term found = False");
                jtfSearchOutput.setText("No matching results");
                }
               
            }
        }//end method
    }//end ActionListener class
    
    private class Sort implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (jcSortCreatures.getSelectedItem().equals("Name") && jcSortTreasures.getSelectedItem().equals("Weight")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureNameComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureWeightComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Name") && jcSortTreasures.getSelectedItem().equals("Value")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureNameComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureValComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Age") && jcSortTreasures.getSelectedItem().equals("Weight")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureAgeComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureWeightComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Age") && jcSortTreasures.getSelectedItem().equals("Value")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureAgeComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureValComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Height") && jcSortTreasures.getSelectedItem().equals("Weight")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureHeightComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureWeightComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Height") && jcSortTreasures.getSelectedItem().equals("Value")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureHeightComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureValComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Weight") && jcSortTreasures.getSelectedItem().equals("Weight")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureWeightComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureWeightComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }else if (jcSortCreatures.getSelectedItem().equals("Weight") && jcSortTreasures.getSelectedItem().equals("Value")) {
                String output = "";
                ArrayList<Party> partyLister = new ArrayList<Party>(parties.values());
                for (Party i: partyLister) {
                    output += ("\nParty: " + i.getName() + "\nContains the following Creatures (age, height, weight) and Treasures\n");
                    ArrayList<Creature> creatureLister = new ArrayList<Creature>(i.getCreatureList());
                    Collections.sort(creatureLister, CreatureWeightComp);
                    for (Creature j: creatureLister) {
                        output += ("  " + j.getName() + " (" + j.getAge() + ", "+ j.getHeight() + ", " + j.getWeight() + ")" +"\n");
                        ArrayList<Treasure> treasureLister = new ArrayList<Treasure>(j.getTreasureList());
                        Collections.sort(treasureLister, TreasureValComp);
                        for (Treasure m: treasureLister) {
                            output += ("       " + m.getType() + ", Weight = " + m.getWeight() + ", Value = " + m.getValue() + "\n");
                        }
                    }
                }
                jtfSearchOutput.setText(output);
            }
        }//end method
    }//end ActionListener class
    
    public String traverseIndex(String lookFor, DefaultMutableTreeNode node) {
        String returner = "";
        String negReturn = "Not Found";
        Enumeration<DefaultMutableTreeNode> traverser = node.breadthFirstEnumeration(); 
        while (traverser.hasMoreElements()) {
            String toPrepare = traverser.nextElement().toString().toLowerCase();
            String[] toCheck = toPrepare.split(" ");
            if (toCheck[0].equals(lookFor)) {
                returner = returner + toPrepare + "\n";
            }
            System.out.println(toCheck[0]);
        }
        if (returner.equals("")) {
            return negReturn;
        }
        return returner;
    }
    
     public String traverseName(String lookFor, DefaultMutableTreeNode node) {
        String returner = "";
        String negReturn = "Not Found";
        Enumeration<DefaultMutableTreeNode> traverser = node.breadthFirstEnumeration(); 
        while (traverser.hasMoreElements()) {
            String toPrepare = traverser.nextElement().toString().toLowerCase();
            String[] toCheck = toPrepare.split(" ");
            if (toCheck.length == 2) {
                System.out.println(toCheck[1]);
                if (toCheck[1].equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
            else if (toCheck.length == 10) {
                System.out.println(toCheck[2]);
                if (toCheck[2].equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
            else if (toCheck[0].matches("4.*")) {
                System.out.println(toCheck[3]);
                String includeSpace = "";
                if (toCheck.length == 4) {
                    includeSpace = toCheck[3];
                }
                else if (toCheck.length == 5) {
                    includeSpace = (toCheck[3] + " " + toCheck[4]);
                }
                else if (toCheck.length == 6) {
                    includeSpace = (toCheck[3] + " " + toCheck[4] + " " + toCheck[5]);
                }
                System.out.println(includeSpace);
                if (includeSpace.equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
        }
        if (returner.equals("")) {
            return negReturn;
        }
        return returner;
    }
     
     public String traverseType(String lookFor, DefaultMutableTreeNode node) {
        String returner = "";
        String negReturn = "Not Found";
        Enumeration<DefaultMutableTreeNode> traverser = node.breadthFirstEnumeration(); 
        while (traverser.hasMoreElements()) {
            String toPrepare = traverser.nextElement().toString().toLowerCase();
            String[] toCheck = toPrepare.split(" ");
            if (toCheck[0].matches("2.*")) {
                System.out.println(toCheck[1]);
                if (toCheck[1].equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
            else if (toCheck[0].matches("3.*")) {
                System.out.println(toCheck[1]);
                String includeSpace = "";
                if (toCheck.length == 5) {
                    includeSpace = toCheck[1];
                }
                else if (toCheck.length == 6) {
                    includeSpace = (toCheck[1] + " " + toCheck[2]);
                }
                System.out.println(includeSpace);
                if (includeSpace.equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
            else if (toCheck[0].matches("4.*")) {
                System.out.println(toCheck[1]);
                if (toCheck[1].equals(lookFor)) {
                    returner = returner + toPrepare + "\n";
                }
            }
        }
        if (returner.equals("")) {
            return negReturn;
        }
        return returner;
    }
     
    private static Comparator<Creature> CreatureNameComp = new Comparator<Creature>() {
        @Override
        public int compare(Creature one, Creature two) {
            String name1 = one.getName().toLowerCase();
            String name2 = two.getName().toLowerCase();
            return name1.compareTo(name2);
         }
    };
    
    private static Comparator<Creature> CreatureAgeComp = new Comparator<Creature>() {
        @Override
        public int compare(Creature one, Creature two) {
            double age1 = one.getAge();
            double age2 = two.getAge();
            
            if (age1 < age2) {
                return -1;
            } else if (age1 == age2) {
                return 0;
            } else {
                return 1;
            }
         }
    };
    
    private static Comparator<Creature> CreatureHeightComp = new Comparator<Creature>() {
        @Override
        public int compare(Creature one, Creature two) {
            double height1 = one.getHeight();
            double height2 = two.getHeight();
            if (height1 < height2) {
                return -1;
            } else if (height1 == height2) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    
    private static Comparator<Creature> CreatureWeightComp = new Comparator<Creature>() {
        @Override
        public int compare(Creature one, Creature two) {
            double weight1 = one.getWeight();
            double weight2 = two.getWeight();
            if (weight1 < weight2) {
                return -1;
            } else if (weight1 == weight2) {
                return 0;
            } else {
                return 1;
            }
         }
    };
    
    private static Comparator<Treasure> TreasureWeightComp = new Comparator<Treasure>() {
        @Override
        public int compare(Treasure one, Treasure two) {
            double height1 = one.getWeight();
            double height2 = two.getWeight();
            if (height1 < height2) {
                return -1;
            } else if (height1 == height2) {
                return 0;
            } else {
                return 1;
            }
        }
    };
    
    private static Comparator<Treasure> TreasureValComp = new Comparator<Treasure>() {
        @Override
        public int compare(Treasure one, Treasure two) {
            double weight1 = one.getValue();
            double weight2 = two.getValue();
            if (weight1 < weight2) {
                return -1;
            } else if (weight1 == weight2) {
                return 0;
            } else {
                return 1;
            }
         }
    };
    
}