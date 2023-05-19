package controller;

/*
 * Description: This Enum creates the Menu for the CLI. It also contains some usefull methods like format String to
 * format doubles and getOption and getMenuOptions ( which were written by kendra) I basically took this from
 * ITPStoreMenu and added some methods and changed most of the enum values
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */
public enum StoreMenu {
    CREATE_ACCOUNT("Create Account"),
    LOGIN("Login into Marketing Portal"),
    LOG_OUT("Log Out"),
    CHANGE_PASSWORD("Change Password"),
    VIEW_ALL("View all Influencers"),
    VIEW_METRICS("View Influencer Metrics (PREMIUM ONLY)"),
    VIEW_RECCOMENDED("View reccomended Influencers based on your preferences (PREMIUM ONLY)"),
    DEAL_BY_CATEGORY("Make Deals by Category (ALL Influencers)"),
    DEAL_ESTABLISHED("Make Categorical Deals with Established Influencers (PREMIUM ONLY)"),
    VIEW_DEALS("View Current Deal Summary"),
    FINALIZE_DEAL("Re-Negotiate an Offer and Solidify your Deal"),
    //SHOW_STORE_STATS("Show back-end store details"),
    QUIT("Quit");

    private String description;
    private StoreMenu(String description){
        this.description = description;
    }

    public String getDisplayString(){
        return this.description;
    }
    public static int getNumOptions() {
        return StoreMenu.values().length;
    }

    public static StoreMenu getOption(int num) {
        return StoreMenu.values()[num];
    }
    public static String formatString(Double num){
        return String.format("%.2f", num);
    }
    public static String getMenuOptions() {
        String prompt = "*****\tInfluencer Connect Menu\t*****";

        for(StoreMenu m : StoreMenu.values()){ //array from the enum
            prompt += "\n" + (m.ordinal()) + ": " + m.getDisplayString();
        }
        prompt+="\n**********************************************\n";
        return prompt;
    }
}