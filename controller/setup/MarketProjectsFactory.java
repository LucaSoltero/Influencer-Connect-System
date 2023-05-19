/*
 * Description: This Class creates MarketingProject objects that represent users.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package controller.setup;

import model.InfluencerType;
import model.TargetAudience;
import model.firms.MarketingProject;
import model.firms.NonPremiumProject;
import model.firms.PremiumProject;
import model.people.Influencer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MarketProjectsFactory {

    //instance variable path for database of MarketingProjects
    protected static final String projectsPATH = "controller/setup/DB/MPDB.csv";

    //iterates through list of MarketingProjects and creates a hashmap of username:object
    public static HashMap<String,MarketingProject> getSampleDB(){
        HashMap<String,MarketingProject> exampleDB = new HashMap<>();
        for(MarketingProject mp: setUpMarketingProjects(projectsPATH)){
            exampleDB.put(mp.getName(),mp);
        }
        return exampleDB;
    }

    //calls readfile to make a list of mp objects
    public static List<MarketingProject> setUpMarketingProjects(String path) {
        List<MarketingProject> projects = new ArrayList<>();
        readFile(path, projects);
        return projects;
    }


    //iterates through csv of MP's and parses each line to MP objects
    private static void readFile(String file, List<MarketingProject> list) {
        try (FileInputStream fis = new FileInputStream(file);
             Scanner scan = new Scanner(fis)) {  // NOTE: resources will be closed automatically with this try block
            if (scan.hasNext()) {
                String header = scan.nextLine();
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    MarketingProject i = parseProject(line);
                    if (i != null) {
                        list.add(i);
                    }
                }
            } else {
                System.err.println("File was empty: " + file);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }
    //Iterates through files in negotiations folders, if the filename parameter is present
    //it returns the filepath of that file so that it can be written into a hashmap to be entered as a
    //parameter of the MarketProject obj param. Using File class to do so. Also calling .isDirectory method
    //to check to see if the path given is a Directory or not.

    private static String getNEGSFilePath(String fileName) {
        String folderPath = "controller/setup/DB/Negotiations";
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    return file.getAbsolutePath();
                }
            }
        }
        return "notFound";
    }

    //iterates through list of influencers and returns the Inf obj if found
    private static Influencer getInfluencer(String name){
        for(Influencer i : InfluencerFactory.setUpInfluencerList()){
            if(i.getName().equals(name)){
                return i;
            }
        }
        return null;
    }


    //parses lines in negotiating deals csv files  and return Influencer objects based off of influencer name
    private static Influencer parseINFKey(String line){
        Influencer i = null;
        try{
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            String name = sc.next();
            i = getInfluencer(name);
        }
        catch(Exception e) {
            System.err.println("Error reading line of file: " + line + "\nerror; " + e);
        }
        return i;
    }

    //parses integer bids from negotiating deals csv files
    private static Integer parseBid(String line){
        try{
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            sc.next();
            return sc.nextInt();
        }
        catch(Exception e) {
            System.err.println("Error reading line of file: " + line + "\nerror; " + e);
        }
        return 0;
    }

    //reads negotiating deals csv file to a negotiating deals hashmap
    private static HashMap<Influencer, Integer> readBidsToMAP(String PATH) {
        HashMap<Influencer, Integer> bids = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(PATH);
             Scanner scan = new Scanner(fis)) {  // NOTE: resources will be closed automatically with this try block
            if (scan.hasNext()) {
                String header = scan.nextLine();
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    Influencer i = parseINFKey(line);
                    Integer bid = parseBid(line);
                    if (i != null) {
                        bids.put(i, bid);
                    }
                }
            } else {
                System.err.println("File was empty: " + PATH);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + PATH);
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bids;
    }

    //parses line in MPDB csv to MarketingProjects
    private static MarketingProject parseProject(String line) {
        MarketingProject thing = null;
        HashMap<Influencer, Integer> negotiatingDeals;
        HashMap<Influencer, Integer> confirmedDeals = new HashMap<>();
        try {
            ArrayList<TargetAudience> targetAudienceList = new ArrayList<>();
            ArrayList<InfluencerType> targetInfluencerTypeList = new ArrayList<>();
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            String name = sc.next();
            String bidPathFileName = getNEGSFilePath(name + ".csv");

            //if the account has no potential deals associated with it a new hashMap is instantiated (empty). otherwise
            //we will read its deals from the csv associated with the account name
            if(!bidPathFileName.equals("notFound")){
                negotiatingDeals = readBidsToMAP(bidPathFileName);
            }
            else{
                negotiatingDeals = new HashMap<>();
            }
            int age = sc.nextInt();
            int rating = sc.nextInt();
            String password = sc.next();
            int dFollwoing = sc.nextInt();
            int minPay = sc.nextInt();
            String targetAudience2 = sc.next();
            String influencerType2 = sc.next();
            TargetAudience targetAudience = TargetAudience.valueOf(targetAudience2.toUpperCase());
            InfluencerType influencerType = InfluencerType.valueOf(influencerType2.toUpperCase());
            targetAudienceList.add(targetAudience);
            targetInfluencerTypeList.add(influencerType);
            boolean isPremium = sc.nextBoolean();
            if (isPremium) {
                double avgEngagement = sc.nextDouble();
                double avgViews = sc.nextDouble();
                thing = new PremiumProject(name, age, rating, password, dFollwoing, minPay,
                            targetAudienceList, targetInfluencerTypeList, negotiatingDeals,confirmedDeals,
                            avgEngagement, avgViews);

                }
            else{
                thing = new NonPremiumProject(name, age, rating, password, dFollwoing, minPay,
                        targetAudienceList, targetInfluencerTypeList, negotiatingDeals,confirmedDeals);
            }
        }

        catch(Exception e) {
            System.err.println("Error reading line of file: " + line + "\nerror; " + e);
        }
        return thing;
    }
}