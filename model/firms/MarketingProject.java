/*
 * Description: MarketingProject is the Parent class of Premium/NonPremium Project.
 * It is abstract and inherits from UserFirm. It represents a Marketing Project.
 * Premium/NonPremiumProject. It is abstract.
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */
package model.firms;

import model.InfluencerType;
import model.TargetAudience;
import model.people.Influencer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MarketingProject extends UserFirm{
    //instance variables
    private int dFollowing;
    private int minPay;
    private ArrayList<TargetAudience> targetAudience;
    private ArrayList<InfluencerType> influencerType;
    private boolean isPremium;
    private HashMap<Influencer, Integer> negotiatingDeals;
    private HashMap<Influencer, Integer> confirmedDeals;

    //constructor
    public MarketingProject(String name, int age, int rating, String password, int dFollowing, int minPay,
                            ArrayList<TargetAudience> targetAudience, ArrayList<InfluencerType> influencerType,
                            boolean isPremium, HashMap<Influencer, Integer> negotiatingDeals,
                            HashMap<Influencer,Integer> confirmedDeals) {
        super(name, age, rating, password);
        this.dFollowing = dFollowing;
        this.minPay = minPay;
        this.targetAudience = targetAudience;
        this.influencerType = influencerType;
        this.isPremium = isPremium;
        this.negotiatingDeals = negotiatingDeals;
        this.confirmedDeals = confirmedDeals;
    }

    //getters and setters
    public int getdFollowing() {
        return dFollowing;
    }

    public void setdFollowing(int dFollowing) {
        this.dFollowing = dFollowing;
    }

    public int getMinPay() {
        return minPay;
    }

    public void setMinPay(int minPay) {
        this.minPay = minPay;
    }

    public ArrayList<TargetAudience> getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(ArrayList<TargetAudience> targetAudience) {
        this.targetAudience = targetAudience;
    }

    public ArrayList<InfluencerType> getInfluencerType() {
        return influencerType;
    }

    public void setInfluencerType(ArrayList<InfluencerType> influencerType) {
        this.influencerType = influencerType;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public HashMap<Influencer, Integer> getNegotiatingDeals() {
        return negotiatingDeals;
    }

    public void setNegotiatingDeals(HashMap<Influencer, Integer> negotiatingDeals) {
        this.negotiatingDeals = negotiatingDeals;
    }

    public HashMap<Influencer, Integer> getConfirmedDeals() {
        return confirmedDeals;
    }

    public void setConfirmedDeals(HashMap<Influencer, Integer> confirmedDeals) {
        this.confirmedDeals = confirmedDeals;
    }

    //equals hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketingProject that)) return false;
        if (!super.equals(o)) return false;

        if (dFollowing != that.dFollowing) return false;
        if (minPay != that.minPay) return false;
        if (isPremium != that.isPremium) return false;
        if (!targetAudience.equals(that.targetAudience)) return false;
        if (!influencerType.equals(that.influencerType)) return false;
        if (!negotiatingDeals.equals(that.negotiatingDeals)) return false;
        return confirmedDeals.equals(that.confirmedDeals);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dFollowing;
        result = 31 * result + minPay;
        result = 31 * result + targetAudience.hashCode();
        result = 31 * result + influencerType.hashCode();
        result = 31 * result + (isPremium ? 1 : 0);
        result = 31 * result + negotiatingDeals.hashCode();
        result = 31 * result + confirmedDeals.hashCode();
        return result;
    }

    //toString
    @Override
    public String toString() {
        return super.toString() +"{MarketingProject" +
                "dFollowing=" + dFollowing +
                ", minPay=" + minPay +
                ", targetAudience=" + targetAudience +
                ", influencerType=" + influencerType +
                ", isPremium=" + isPremium +
                ", negotiatingDeals=" + negotiatingDeals +
                ", confirmedDeals=" + confirmedDeals +
                '}';
    }
}
