/*
 * Description: This Class creates Influencer Objects that are read from two different csv files.
 * It includes various functions to do so.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package controller.setup;

import model.InfluencerType;
import model.TargetAudience;
import model.people.Creator;
import model.people.Established;
import model.people.Influencer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class InfluencerFactory {
    //protected file paths
    protected static final String creatorPATH = "controller/setup/DB/creator_Data.csv";
    protected static final String establishedPATH = "controller/setup/DB/Established.csv";


    //calls readfile to return a list of influencer objects
    public static List<Influencer> setUpTypeList(String path) {
        List<Influencer> influencerList = new ArrayList<>();
        readFile(path, influencerList);
        return influencerList;
    }

    //returns a list of all influencers both Est and non Est calling readFile() to do so
    public static List<Influencer> setUpInfluencerList() {
        List<Influencer> influencerList = new ArrayList<>();
        readFile(creatorPATH, influencerList);
        readFile(establishedPATH, influencerList);
        return influencerList;
    }

    //returns a list of Creator objects
    public static List<Influencer> setUpCreatorList() {
        List<Influencer> influencerList = new ArrayList<>();
        readFile(creatorPATH, influencerList);
        return influencerList;
    }

    //readFile method takes in a filename to read from and a list
    //iterates through specified file and parses each line to an influencer. Adds it to a list
    //and finally returns the list
    private static void readFile(String file, List<Influencer> list) {
        try(FileInputStream fis = new FileInputStream(file);
            Scanner scan = new Scanner(fis))
        {  // NOTE: resources will be closed automatically with this try block
            if (scan.hasNext()) {
                String header = scan.nextLine();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    Influencer i = parseInfluencer(line);
                    if(i != null) {
                        list.add(i);
                    }
                }
            }
            else {
                System.err.println("File was empty: " + file);
            }
        }

        catch (FileNotFoundException e) {
            System.err.println("File not found: " + file);
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }

    // parseInfluencer. Parses each line using a comma delimiter. If the influencer is not established
    //then we obtain the specific instance varaibles specific to Creator and create a new object; otherwise
    //we read create a new Established object.
    private static Influencer parseInfluencer(String line) {
        Influencer thing = null;
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter(",");
            String name = sc.next();
            int age = sc.nextInt();
            char gender = sc.next().charAt(0);
            int desiredRating = sc.nextInt();
            int following = sc.nextInt();
            int minPay = sc.nextInt();
            String targetAudience2 = sc.next();
            String influencerType2 = sc.next();
            TargetAudience targetAudience = TargetAudience.valueOf(targetAudience2.toUpperCase());
            InfluencerType influencerType = InfluencerType.valueOf(influencerType2.toUpperCase());
            boolean isEst = sc.nextBoolean();
            //String handle = sc.next();
            //boolean isVerified = sc.nextBoolean();
            if(!isEst) {
                String handle = sc.next();
                boolean isVerified = sc.nextBoolean();
                Double avgEngagement = sc.nextDouble();
                Double avgViews = sc.nextDouble();
                thing = new Creator(name, age, gender, desiredRating, following, minPay, targetAudience,
                        influencerType, handle, isVerified,avgEngagement,avgViews);
            }
            else{
                boolean openToSponsDeal = sc.nextBoolean();
                thing = new Established(name, age, gender, desiredRating, following, minPay, targetAudience,
                        influencerType, openToSponsDeal);
            }
        }
        catch(Exception e) {
            System.err.println("Error reading line of file: " + line + "\nerror; " + e);
        }
        return thing;
    }

    //this method takes in an influencer Type and path it iterates through influencers
    //with the given type and adds them to the batches arrayList and returns them
    public static List<Influencer> setUpMapFn(InfluencerType type, String path){
        List<Influencer> influencers = setUpTypeList(path);
        List<Influencer> batches = new ArrayList<>();
        for(Influencer i: influencers) {
            if(i.getInfluencerType() == type){
                batches.add(i);
            }
        }
        return batches;
    }

    // this map sets up a hashmap of just creators in which the key is the Influencer type and the value
    //is a List of type Influencer with influencers of that given Influencer Type. It utilizes the setUpMapFn()
    //to do so.
    public static Map<InfluencerType, List<Influencer>> getCreatorMap(){
        Map<InfluencerType, List<Influencer>> bigMap = new HashMap<>();
        for(InfluencerType i : InfluencerType.values()){
            bigMap.put(i, setUpMapFn(i,creatorPATH));
        }
        return bigMap;
    }

    //does the same thing as creator map except with only established influencers
    public static Map<InfluencerType, List<Influencer>> getEstablishedMap(){
        Map<InfluencerType, List<Influencer>> bigMap = new HashMap<>();
        for(InfluencerType i : InfluencerType.values()){
            bigMap.put(i, setUpMapFn(i,establishedPATH));
        }
        return bigMap;
    }


    public static List<Influencer> setUpInfluencerMapFn(InfluencerType type){
        List<Influencer> influencers = setUpInfluencerList();
        List<Influencer> batches = new ArrayList<>();
        for(Influencer i: influencers) {
            if(i.getInfluencerType() == type){
                batches.add(i);
            }
        }
        return batches;
    }
    //does the same thing as the prev two functions except it iterates through all influencers
    public static Map<InfluencerType, List<Influencer>> setUpInfluencerMap(){
        Map<InfluencerType, List<Influencer>> bigMap = new HashMap<>();
        for(InfluencerType i : InfluencerType.values()){
            bigMap.put(i, setUpInfluencerMapFn(i));
        }
        return bigMap;
    }
}
