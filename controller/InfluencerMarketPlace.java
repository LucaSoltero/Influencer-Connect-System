/*
 * Description: This class runs the Influencer Market Place. It uses the modules found in model and view in order
 * to manipulate data represented by various objects being UserFirm and Person implementations.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package controller;
import controller.setup.InfluencerFactory;
import controller.setup.MarketProjectsFactory;
import model.InfluencerType;
import model.TargetAudience;
import model.firms.MarketingProject;
import model.firms.NonPremiumProject;
import model.firms.PremiumProject;
import model.people.Creator;
import model.people.Established;
import model.people.Influencer;
import view.BFF;
import view.Metrics;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/*
 compile :
 *javac model/firms/*.java model/people/*.java view/*.java controller/*.java controller/setup/*.java
 run : java controller/InfluencerMarketPlace
 */



public class InfluencerMarketPlace {
    //instance variables
    private final BFF helper;
    private final HashMap<String, MarketingProject> userDB;
    private MarketingProject currentUser;
    private final Influencer[] creatorArray;
    private final Map<InfluencerType, List<Influencer>> allInfluencers;
    private final Map<InfluencerType, List<Influencer>> creatorMap;
    private final Map<InfluencerType, List<Influencer>> establishedMap;

    private final HashMap<Influencer,Integer> confirmedDeals;
    private final HashMap<Influencer,Integer> negotiatingDeals;

    //constructor instantiating values for each data structure.
    //calling methods from influencer factory and MarketFactory to get data structures that contain data from
    //the database
    public InfluencerMarketPlace() {
        allInfluencers = InfluencerFactory.setUpInfluencerMap();
        creatorMap = InfluencerFactory.getCreatorMap();
        establishedMap = InfluencerFactory.getEstablishedMap();
        helper = new BFF();
        userDB = MarketProjectsFactory.getSampleDB();
        creatorArray = InfluencerFactory.setUpCreatorList().toArray(new Influencer[0]);
        confirmedDeals = new HashMap<>();
        negotiatingDeals = new HashMap<>();
        currentUser = null;
    }

    //run method calling other functions in this module and stringing them together
    //using a while loop to present a CLI interface where a user can interact with the
    //various functions
    public void run() {
        String welcomeMsg = """
                Welcome to Influencer Connect.
                This CLI application allows users (hypothetical marketing firms) to connect with influencers\s
                they are interested in working with. As a premium member you will have access to view metrics\s
                for Creators which are essentially social media personalities who do not have an established marketing\s
                presence. The ability to access these metrics can in theory allow Creators who are not established have\s
                a greater potential value to firms who can measure their out reach via these metrics. Premium firms\s
                will also have access to a reccomended page output that takes into consideration their personal preferences\s
                and feeds them creators who match these preferences along with the ability to input multiple target/influencer\s
                types for their preferences. Premium members can also view Established Influencers and make deals with them\s
                directly. To make a deal bid to match the Influencers desired salary; if this salary is not met you can re-\s
                negotiate by going to section 10 of the main menu. Good Luck !
                """;
        helper.print(welcomeMsg);
        boolean quit = false;
        while (!quit) {
            System.out.println(StoreMenu.getMenuOptions());
            // helper to choose option
            int userChoice = helper.inputInt("", 0, StoreMenu.values().length - 1);
            StoreMenu option = StoreMenu.getOption(userChoice);
            switch (option) {
                case CREATE_ACCOUNT:
                    if (currentUser != null) {
                        System.out.println("You are already logged in! You must logout to create an account!");
                    } else {
                        createAccount();
                    }
                    break;
                case LOGIN:
                    if (currentUser != null) {
                        System.out.println("You are already logged in! You must logout to login!");
                    } else {
                        login();
                    }
                    break;
                case LOG_OUT:
                    if (verifyUser()) {
                        if (!currentUser.getConfirmedDeals().isEmpty()) {
                            writeConfirmedDealsToFile();
                        }
                        if(!currentUser.getNegotiatingDeals().isEmpty()){
                            saveNegotiations();
                        }
                        currentUser = null;
                        System.out.println("You have successfully logged out !");
                    }
                    break;

                case CHANGE_PASSWORD:
                    if (verifyUser()) {
                        changePassword();
                    }
                    break;
                case VIEW_ALL:
                    viewInfluencers(allInfluencers);
                    break;
                case VIEW_METRICS:
                    if (currentUser != null && currentUser instanceof Metrics) {
                        viewMetrics();
                    } else if (currentUser == null) {
                        verifyUser();
                    } else {
                        System.out.println("You must be PREMIUM to access this section");
                    }
                    break;
                case VIEW_RECCOMENDED:
                    if (currentUser != null && currentUser instanceof Metrics) {
                        viewReccomendedDeals();
                    } else if (currentUser == null) {
                        verifyUser();
                    } else {
                        System.out.println("You must be PREMIUM to access this section");
                    }
                    break;
                case DEAL_BY_CATEGORY:
                    if (verifyUser()) {
                        makeDealsByCategory();
                    }
                    break;
                case DEAL_ESTABLISHED:
                    if (currentUser != null && currentUser instanceof Metrics) {
                        makeEstablishedDeals();
                    } else if (currentUser == null) {
                        verifyUser();
                    } else {
                        System.out.println("You must be PREMIUM to access this section");
                    }
                    break;
                case VIEW_DEALS:
                    if (verifyUser()) {
                        viewYourDeals();
                    }
                    break;
                case FINALIZE_DEAL:
                    if (verifyUser()) {
                        finalizeDeals();
                    }
                    break;
                case QUIT:
                    quit = true;
                    break;
            }
            if (!quit) { //pause
                helper.input("enter something to continue");
            }
        }
        //after a user exits if they are still logged in their data will automatically be saved
        //(assuming they had data to be stored)
        //confirmed deals will NOT be saved. It is assumed that once a deal is confirmed it will be written
        //into a recept with the exact time it occurred. Users can make multiple deals with the same
        //influencer across runs
        //deals still in negotiation however will be saved across runs as they have not been finalized
        if(currentUser != null){
                if (!currentUser.getConfirmedDeals().isEmpty()) {
                    writeConfirmedDealsToFile();
                }
                if (!currentUser.getNegotiatingDeals().isEmpty()) {
                    saveNegotiations();
            }
        }
        //db will continually be saved across runs
        updateProjectsDB();
        System.out.println("Thank you for using Influencer Connect");
    }

    // 3 filter methods below will filter by Target Audience, Influencer type, Average engagement, and
    //average views in order to give the user a reccomended list of influencer to reach out too
    private List<Influencer> filterITMatches(){
        List<Influencer> filter = new ArrayList<>();
        for(InfluencerType i : allInfluencers.keySet()){
            if(currentUser.getInfluencerType().contains(i)){
                filter.addAll(allInfluencers.get(i));
            }
            }
        return filter;
    }

    private List<Influencer> filterTargetAuds(List<Influencer> influencerList){
        List<Influencer> filter = new ArrayList<>();
        for(Influencer i : influencerList){
            TargetAudience ta = i.getTargetAud();
            if(currentUser.getTargetAudience().contains(ta) || ta.equals(TargetAudience.ALL)){
                filter.add(i);
            }
        }
        if(currentUser.getTargetAudience().contains(TargetAudience.ALL)){
            return influencerList;
        }
        else {
            return filter;
        }
    }

    private List<Influencer> filterAvgEngagement(List<Influencer> influencerList){
        List<Influencer> filter = new ArrayList<>();
        for(Influencer i : influencerList){
            if(i instanceof Metrics m) {
                if (((PremiumProject) currentUser).getAvgEngagement() - 1 < m.getAvgEngagement()) {
                    filter.add(i);
                }
            }
            else{
                if(((Established)i).isOpenToSponsorDeal()){
                filter.add(i);
                }
            }
        }
        return filter;

    }
    private List<Influencer> filterAvgViews(List<Influencer> influencerList){
        List<Influencer> filter = new ArrayList<>();
        for(Influencer i : influencerList){
            if(i instanceof Metrics m) {
                if (((PremiumProject) currentUser).getAvgViews() - 1000 < m.getAvgViews()) {
                    filter.add(i);
                }
            }
            else{
                filter.add(i);
            }
        }
        return filter;

    }
    //iterate through filtered list and print out influencers that match. If influencer is not an instance of
    //Metrics we assume they must be established, and we print their strings accordingly
    private void viewReccomendedDeals(){
        List<Influencer> it  = filterITMatches();
        List<Influencer> targetAuds = filterTargetAuds(it);
        List<Influencer> avgEng = filterAvgEngagement(targetAuds);
        List<Influencer> reccomended = filterAvgViews((avgEng));
        if(reccomended.isEmpty()){
            String srryMsg = "Sorry there are no influencers in our system that currentley match your specifications :(";
            System.out.println(srryMsg);
        }
        else {
            helper.print("RECCOMENDED CREATORS");
            for (Influencer i : reccomended) {
                if(i instanceof Metrics) {
                    helper.printPretty(((Creator) i).metricsToString());
                }
                else{
                    helper.printPretty(((Established)i).prettyToString());
                }
            }
        }
    }

    //iterate through negotiations hashmap and write it to a csv file so that negotiations can be saved
    //and continued between runs. We will identify each negotiation by the name of its file which is the same as the
    //username.
    private void saveNegotiations() {
        System.out.println("SAVING Negotiations....");
        String filename = "controller/setup/DB/Negotiations/" + currentUser.getName() + ".csv";
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Handle,Bid\n");
            for(Influencer i : currentUser.getNegotiatingDeals().keySet()){
                bufferedWriter.write(i.getName() + "," + currentUser.getNegotiatingDeals().get(i) + "\n");
            }
            bufferedWriter.close();
            System.out.println("Successfully wrote to file: " + filename);
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
    }

    //iterate through userDB hashmap and write users to a csv file so that they can be saved across runs.
        private void updateProjectsDB(){
        System.out.println("SAVING PROJECTS....");
        String filename = "controller/setup/DB/MPDB.csv";
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("name,age,rating,password,dFollowing,minPay,TargetAudience,InfluencerType," +
                    "isPremium,fillDeals,dAvgEngagement,dAvgViews\n");
            for (String name : userDB.keySet()) {
                MarketingProject mp = userDB.get(name);
                Double dAvgEng;
                Double dAvgViews;
                if(mp instanceof PremiumProject){
                    dAvgEng = ((PremiumProject)mp).getAvgEngagement();
                    dAvgViews = ((PremiumProject)mp).getAvgViews();
                }
                else{
                    dAvgEng = 0.0;
                    dAvgViews = 0.0;
                }
                String targetAud = mp.getTargetAudience().get(0).toString();
                String editTA = targetAud.replace("[", "").replace("]", "").trim();
                String infType = mp.getInfluencerType().get(0).toString();
                String editIT = infType.replace("[", "").replace("]", "").trim();
                String bufferString = name + "," + mp.getAge() + "," + mp.getRating()+ "," + mp.getPassword() +
                        "," + mp.getdFollowing()+ "," + mp.getMinPay()+ "," +
                        editTA+ ","
                        + editIT+ "," + mp.isPremium()+ "," +
                        dAvgEng + "," + dAvgViews + "\n";
                bufferedWriter.write(bufferString);
            }
            bufferedWriter.close();
            System.out.println("Successfully wrote to file: " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    //iterate through confirmed deals hashmap and write a receipt for each user. The exact date and time
    //that the deal is also included in the filename in order to differentiate between different deals made by the same
    //user. A user can make a deal with the same influencer across various runs.
    private void writeConfirmedDealsToFile(){
        System.out.println("PRINTING CONFIRMED DEALS RECEIPT ....");
        String filename = "controller/setup/DB/Receipts/" +
                currentUser.getName() + LocalDateTime.now() + ".csv";
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Finalized Deals for " + currentUser.getName() + " : " +  LocalDateTime.now() + "\n");

            for (Influencer i : currentUser.getConfirmedDeals().keySet()) {
                bufferedWriter.write(i.getName() + " : Contracted Pay " + currentUser.getConfirmedDeals().get(i) + "$\n");
            }

            bufferedWriter.close();
            System.out.println("Successfully wrote to file: " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
    }
    }

    //finalize deals method will essentially do the same thing as dealInfluencerFromList method except it iterates through
    //negotiating deals Keyset to get names of each influencer they are in negotiations with.
    // we use the toArray() method and add in a new Instantiation of an Influencer array of size keyset to get
    // an array to iterate through and print different influencers available to renegotiate with.
    private void finalizeDeals(){
        if(currentUser.getNegotiatingDeals().isEmpty()){
            System.out.println("You currently have no deals in negotiation!");
        }
        else{
            do{
                StringBuilder options = new StringBuilder("Select influencer to finalize a deal with:\n");
                int size = currentUser.getNegotiatingDeals().keySet().size();
                Influencer[] deals = currentUser.getNegotiatingDeals().keySet().toArray(new Influencer[size]);
                for (int z = 0; z < size; z ++){
                    Influencer i = deals[z];
                    String influencer = z + "." + i.getName() + " : Minimum Expected Salary " +
                            i.getMinPay() + "$ \n" +
                            "Your current offer: " + currentUser.getNegotiatingDeals().get(i) + "$\n";
                    options.append(influencer);
                }
                int infNum = helper.inputInt(options + "\nChoose Influencer # (or -1 to not deal) >>> ",
                        0, currentUser.getNegotiatingDeals().keySet().size() - 1, -1);
                if (infNum != -1) {
                    makeDeal(deals[infNum]);
                }
            }
            while(!currentUser.getNegotiatingDeals().isEmpty() && helper.inputYesNo("Would you like to continue to " +
                    "negotiate deals?"));
        }
    }

    //viewMetrics method will call the Arrays.sort() method to utilize the Comparable interface in Influencer
    //this will sort the list of Creators first by pricePerFollower, then by engagement, then by views.
    private void viewMetrics() {
        Arrays.sort(creatorArray);
        System.out.println(((Metrics)currentUser).metricsToString());
        for(Influencer i : creatorArray){
            if(i instanceof Metrics){
                helper.printPretty(((Metrics) i).metricsToString());
            }
        }
    }
    //viewInfluencers method will iterate through a map of Influencer type keys and list<Influencer> values.
    //It will print out influencers by their InfluencerType categories. If a user is not premium they will not
    //get access to metrics data.
    private void viewInfluencers(Map<InfluencerType, List<Influencer>> infMap){
        for(InfluencerType i : infMap.keySet()){
            if(infMap.get(i).isEmpty()){
                System.out.println();
                System.out.println(i);
                System.out.println("NO INFLUENCERS found here :(");
            }
            else{
                System.out.println();
                System.out.println(i);
                if(currentUser instanceof PremiumProject) {
                    for (Influencer f : infMap.get(i)) {
                        System.out.println(f);
                    }
                }
                else{
                    for (Influencer f : infMap.get(i)) {
                        if(f instanceof Creator) {
                            System.out.println(f.nonPremiumToString());
                        }
                    }
                }
            }
        }
    }

    // this method allows a user to see what deals they have both in negotiation and confirmed
    //basically a viewCart method but for this specific project
    private void viewYourDeals(){
        if(currentUser.getNegotiatingDeals().isEmpty()){
            System.out.println("You currently have no deals in negotiation");
        }
        else {
            System.out.println("Deals still in Negotiation for " + currentUser.getName());
            for (Influencer i : currentUser.getNegotiatingDeals().keySet()) {
                System.out.println(i.getName() + " : Minimum Expected Salary " + i.getMinPay() + "$ \n" +
                        "Your current offer: " + currentUser.getNegotiatingDeals().get(i) + "$");
            }
        }
        if (currentUser.getConfirmedDeals().isEmpty()) {
            System.out.println("You currently have no finalized deals");
        }
        else {
            System.out.println("Finalized Deals for " + currentUser.getName());
            for (Influencer i : currentUser.getConfirmedDeals().keySet()) {
                System.out.println(i.getName() + " : Contracted Pay " + currentUser.getConfirmedDeals().get(i) + "$");
            }
        }
    }
    //this method allows users to deal influencers from a specific list obtained from choosing a specific
    //influencer type from a HashMap. They will be placed in a loop allowing them to make deals with the influencers
    //available to them based off of their query selection.
    private void dealInfluencerFromList(List<Influencer> influencerList, InfluencerType type){
        if(influencerList.isEmpty()){
            helper.print("No Influencers found in this given category :(");
        }
        else{
            do{
                StringBuilder options = new StringBuilder("Select influencer to make a deal with:\n");
                for (int z = 0; z < influencerList.size(); z++){
                    Influencer i = influencerList.get(z);
                    String influencer;
                    if(currentUser instanceof PremiumProject) {
                        influencer = z + "." + i.toString() + "\n";
                    }
                    else{
                        influencer = z + "." + i.nonPremiumToString() + "\n";
                    }
                    options.append(influencer);

                }
                int infNum = helper.inputInt(options + "\nChoose Influencer # (or -1 to not purchase) >>> ",
                        0, influencerList.size() - 1, -1);
                if (infNum != -1) {
                    Influencer inf = influencerList.get(infNum);
                    if(currentUser.getNegotiatingDeals().containsKey(inf)){
                        System.out.println("You are already in negotiation with " + inf.getName() + ". " +
                                "Go to section 10 of the Main Menu to continue negotiations.");
                    }
                    else if(currentUser.getConfirmedDeals().containsKey(inf)){
                        System.out.println("You have already have a signed deal with " + inf.getName());
                    }
                    else {
                        makeDeal(inf);
                    }
                }
            }
            while(helper.inputYesNo("Would you like to make a deal with another influencer in the " +
                    type + " category?"));
        }
    }
    //makeDeal allows a user to make a deal with a specific influencer. If they are sure that they want to bid for
    //their specified influencer then they are taken to the bidForInfluencer method which will allow them to add
    //influencers to either their deal map or confirmed deals map. If an established influencer is not open to sponsorship
    //they will not be able to deal with them.
    private void makeDeal(Influencer influencer){
        boolean verify = helper.inputYesNo("You have selected: " + influencer.getName() + "\nType \"y\"" +
                " to negotiate a deal with this influencer," +
                " \"n\" if you don't wish to make a deal with " + influencer.getName() + ": ");
        if(verify) {
            if(influencer instanceof Established est) {
                helper.print(influencer.getName() + " Is a very established professional; if you cannot" +
                        " meet their salary expectations you can try and re negotiate later but no deal will go through" +
                        "\n until their minimum expected pay is met.");
                if(est.isOpenToSponsorDeal()){
                    bidForInfluencer(influencer);

            }
                else{
                    System.out.println("Im sorry but " + influencer.getName() + " is not open to sponsorship deals" +
                            "at the moment,\n try reaching out to a different influencer.");
                }
        }
            else{
                bidForInfluencer(influencer);
            }
        }
    }
    //bidForInfluencer allows a user to make bids on an influencer to sign them. If their bid is greater than or equal
    //to the influencers minimum desired pay then the influencer will be added to their confirmed deal hashmap. Otherwise
    //the influencer will be added to their negotiations hashmap
    public void bidForInfluencer(Influencer influencer){
        String sorryMsg = "Sorry your offer was not significant enough. This influencer" +
                " will be placed in your un processed deals list \n so if you are interested in re" +
                " negotiating later on you can still reach out to " + influencer.getName() + " !";

        helper.print(influencer.getExpectedSalaryString());
        Integer bid = helper.inputInt("Enter the salary you would be willing to pay " + influencer.getName() + ": ",
                0,10000000);
        if(bid >= influencer.getMinPay()) {
            currentUser.getConfirmedDeals().put(influencer, bid);
            currentUser.getNegotiatingDeals().remove(influencer);
            helper.print("Congratulations we have finalized your deal with " + influencer.getName() + "!" + "" +
                    " For a promised pay of " + bid + "$");
        }
        else{
            helper.print(sorryMsg);
            currentUser.getNegotiatingDeals().put(influencer,bid);

        }
    }

    //makeEstDeals and makeDealsByCategory essentially do the same thing. Both call dealInfluencerFrom list
    //and feed in the List<Influencer> parameter based on the selected category type of the user
    //the only difference is that for makeEst deals method we only include a map filled with Established type
    //influencers
    private void makeEstablishedDeals(){
        InfluencerType influencerType = selectINFCategory();
        dealInfluencerFromList(establishedMap.get(influencerType), influencerType);
    }
    private void makeDealsByCategory() {
        InfluencerType influencerType = selectINFCategory();
        if(currentUser.isPremium()) {
            dealInfluencerFromList(allInfluencers.get(influencerType), influencerType);
        }

        else{
            dealInfluencerFromList(creatorMap.get(influencerType), influencerType);
        }
    }

    //asks user for a Enum type and then uses the values() method to discern the enum type
    //returns the type. Same is done for select AUD category. I basically did the same thing as
    //ITP Store here
    private InfluencerType selectINFCategory() {
        InfluencerType type = InfluencerType.OTHER;
        System.out.println("Select the Category of Influencer you would like to choose from:");
        helper.print(InfluencerType.getInfluencerTypeToString());
        int categoryOption = helper.inputInt("Category #", 1, InfluencerType.values().length);
        type = InfluencerType.values()[categoryOption-1];
        return type;
    }

    private TargetAudience selectAUDCategory() {
        TargetAudience type = TargetAudience.OTHER;
        System.out.println("Select your desired Target Audience:");
        helper.print(TargetAudience.getTargetTypeToString());
        int categoryOption = helper.inputInt("Category #", 1, TargetAudience.values().length);
        type = TargetAudience.values()[categoryOption-1];
        return type;
    }

    //if current user = null  return false, otherwise return true
    private boolean verifyUser() {
        boolean hasUser = true;
        if(currentUser == null){
            helper.print("Must be logged in to complete this action");
            hasUser = false;
        }
        return hasUser;
    }

    //if password == to entered password then it allows user to set a new password
    private void changePassword() {
        String password = helper.input("Enter your current Password:");
        if(password.equals(currentUser.getPassword())) {
            String newPassword = helper.input("Enter your new Password:");
            currentUser.setPassword(newPassword);
            helper.print("update successful");
        }
        else{
            helper.print("update failed");
        }
    }
    //login method will call the finduser method to check to see if the user exists
    //if they do we ask them to enter a password and if the password equals the password
    //from the specified object then we set currentUser to the current MarketingProject object
    private void login() {
        String name = helper.input("Username:");
        MarketingProject mp = findUser(name);
        // you can use the other method but map call can be used instead or just create
        // the findUser method below
        if(mp != null) {
            String password = helper.input("Password:");
            if(mp.getPassword().equals(password)){
                currentUser = mp;
                System.out.println("You have logged in ! ");
            }
            else{
                helper.print("password doesn't match");
            }
        }
        else{
            helper.print("No user found with name: " + name);
        }
    }
    //calls HashMap.getOrDefault method which returns null if the string key is not found and returns the
    //corresponding value if it is found
    public MarketingProject findUser(String username){
        return userDB.getOrDefault(username, null);
    }

    //createAccount method allows a user to create an account (a MarketingProject object)
    //asks for input for each instance variable of Marketing project. If the user decided to be premium
    //then we ask for the Premium Project instance variables otherwise will implement Marketing Project in the form
    //of NonPremiumProject
    public void createAccount() {
        String name = helper.input("Enter your firm's username:");
        while (userDB.containsKey(name)) {
            System.out.println("That username already exists! Please enter an original username");
            name = helper.input("Enter your firm's username:");
        }
        int age = helper.inputInt("Enter the age of your firm (years)");
        int rating = helper.inputInt("Enter the rating of your firm (1-5)", 1, 5);
        int dFollowing = helper.inputInt("What is the minimum following you desire for an influencer?");
        int minPay = helper.inputInt("How much would you be willing to spend on this project?");
        boolean isPremium = helper.inputYesNo("Would you like to sign up for premium?");
        ArrayList<TargetAudience> targetAud;
        ArrayList<InfluencerType> influencerType;
        targetAud = getDesiredAUDTypes(isPremium);
        influencerType = getDesiredINFTypes(isPremium);
        String password = helper.input("Create a password for your account: ");
        String password2 = helper.input("Confirm Password");
        if(password.equals(password2)) {
            System.out.println("Password confirmed!");
            if(!isPremium){
                currentUser = new NonPremiumProject(name,age,rating,password,dFollowing,minPay,targetAud,
                        influencerType, negotiatingDeals,confirmedDeals);
                userDB.put(currentUser.getName(), currentUser);

            }
            else{
                helper.printPretty("""
                        Since you are a premium member you can access the Metrics Interface\s
                        of our System which allows you to view influencer metrics and allow those metrics\s
                        to be matched with your own. Please enter your desired metrics for Average Engagement\s
                        and Average Views.""");
                double avgEngagement = helper.inputDouble("What is the minimum average %engagement" +
                        " you are looking for in a client?");
                double avgViews = helper.inputDouble("What is the minimum average views" +
                        " you are looking for in a client?");
                currentUser = new PremiumProject(name,age,rating,password,dFollowing,minPay,targetAud,
                        influencerType, negotiatingDeals, confirmedDeals,avgEngagement, avgViews);
                userDB.put(currentUser.getName(), currentUser);
            }
            helper.print("created new account and logged in user as " + currentUser.getName());
        }
        else{
            helper.print("Account not created. Passwords didn't match");
        }
    }
    //get desired INFTypes and getAUDTypes will return an arrayList of InfluencerTypes/ Audiences if the user isPremium
    //then they can add multiple Influencer Types/ Audiences to their list. If they are not they can only add 1
    //and will be exited from the loop
    public ArrayList<InfluencerType> getDesiredINFTypes(boolean isPremium){
        boolean again = true;
        ArrayList<InfluencerType> desiredInfluencerTypes = new ArrayList<>();
        InfluencerType type;
        boolean exit = false;
        while(again && !exit){
            type = selectINFCategory();
            if(desiredInfluencerTypes.contains(type)){
                System.out.println("You have already selected this category!");
            }
            else{
                desiredInfluencerTypes.add(type);
            }
            if(!isPremium){
                exit = true;
            }
            else{
                again = helper.inputYesNo("Would you like to add another Influencer Type? ");
            }
        }
        return desiredInfluencerTypes ;
    }

    public ArrayList<TargetAudience> getDesiredAUDTypes(boolean isPremium){
        boolean again = true;
        ArrayList<TargetAudience> targetAud = new ArrayList<>();
        TargetAudience audience;
        boolean exit = false;
        while(again && !exit){
            audience = selectAUDCategory();
            if(targetAud.contains(audience)){
                System.out.println("You have already selected this category!");
            }
            else{
                targetAud.add(audience);
            }
            if(!isPremium){
                exit = true;
            }
            else{
                again = helper.inputYesNo("Would you like to add another Target Audience? ");
            }
        }
        return targetAud;
    }
    //main method here. We call run to start the method. Thanks !
    public static void main(String[] args) {
        InfluencerMarketPlace i = new InfluencerMarketPlace();
        i.run();
    }
}