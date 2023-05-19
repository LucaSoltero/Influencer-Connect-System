
/*
 * Description: This class Creator is the child class of Influencer. It contains instance variables that represent a
 * creator and their assets (like their performance metrics). It implements the Metrics Interface which includes various
 * methods to return values that represents a specific creators' performance and corresponding market value.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */


package model.people;

import controller.StoreMenu;
import model.InfluencerType;
import model.TargetAudience;
import view.Metrics;


public class Creator extends Influencer implements Metrics{
    //instance variables
    private String handle;
    private boolean isVerified;
    private Double avgEngagement;
    private Double avgViews;

    //constructor
    public Creator(String name, int age, char gender, int desiredRating, int following, int minPay,
                   TargetAudience targetAud, InfluencerType influencerType,
                   String handle, boolean isVerified, Double avgEngagement, Double avgViews) {
        super(name, age, gender, desiredRating, following, minPay, targetAud, influencerType, false);
        this.handle = handle;
        this.isVerified = isVerified;
        this.avgEngagement = avgEngagement;
        this.avgViews = avgViews;
    }

    //get and setters
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }



    //getPricePer follower implementation from Metrics interface return dollar value per follower based on their
    //minimum desired salary
    @Override
    public Double getPricerPerFollower() {
        Double pay = (Double)(double) getMinPay();
        Double following = (Double)(double)getFollowing();
        return Double.parseDouble(StoreMenu.formatString(pay/following));
    }

    //Metrics implementation returns avgEngagement of a creator
    @Override
    public Double getAvgEngagement() {
        return avgEngagement;
    }

    public void setAvgEngagement(Double avgEngagement) {
        this.avgEngagement = avgEngagement;
    }
    //Metrics implementation returns average Views of a creator
    @Override
    public Double getAvgViews() {
        return avgViews;
    }

    public void setAvgViews(Double avgViews) {
        this.avgViews = avgViews;
    }


    //equals hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Creator creator)) return false;
        if (!super.equals(o)) return false;

        if (isVerified != creator.isVerified) return false;
        if (!handle.equals(creator.handle)) return false;
        if (!avgEngagement.equals(creator.avgEngagement)) return false;
        return avgViews.equals(creator.avgViews);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + handle.hashCode();
        result = 31 * result + (isVerified ? 1 : 0);
        result = 31 * result + avgEngagement.hashCode();
        result = 31 * result + avgViews.hashCode();
        return result;
    }


    //toString
    @Override
    public String toString() {
        return super.toString() + "Creator{" +
                "handle='" + handle + '\'' +
                ", isVerified=" + isVerified +"}" +
                ", avgEngagement=" + avgEngagement +
                ", avgViews=" + avgViews +
                '}';
    }

    //nonPremiumTo String overrides parent implementation
    public String nonPremiumToString(){
        return super.toString() + "Creator{" +
                "handle='" + handle + '\'' +
                ", isVerified=" + isVerified +"}";
    }

    //Metrics implementation metrics toString summarizes a creators performance
    @Override
    public String metricsToString(){
        return "CREATOR: " + handle + "\n" +
                "Influencer_Type: " + getInfluencerType() + "\n" +
                "Target_Audience: " + getTargetAud() +  "\n" +
                "following: " + getFollowing() + "\n" +
                "avgEngagement: " +  StoreMenu.formatString(getAvgEngagement()) + "%\n" +
                "avgViews: " + StoreMenu.formatString(getAvgViews()) + "\n" +
                "Price Per Follower: " + StoreMenu.formatString(getPricerPerFollower()) + "$";
    }

}