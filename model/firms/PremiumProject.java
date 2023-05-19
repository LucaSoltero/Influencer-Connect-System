/*
 * Description: PremiumProject inherits from Marketing Project and implements the Metrics interface.
 * It includes the information to describe a premium Marketing Project which grants access to Creator metrics
 * and Established Influencers along with multiple categorical query's.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package model.firms;

import controller.StoreMenu;
import model.InfluencerType;
import model.TargetAudience;
import model.people.Influencer;
import view.Metrics;

import java.util.ArrayList;
import java.util.HashMap;

public class PremiumProject extends MarketingProject implements Metrics {
    //instance variables
    private final double dAvgEngagement;
    private final double dAvgViews;
    //constructor
    public PremiumProject(String name, int age, int rating, String password, int dFollowing, int minPay,
                          ArrayList<TargetAudience> targetAudience, ArrayList<InfluencerType> influencerType,
                          HashMap<Influencer,Integer> negotiatingDeals ,HashMap<Influencer,Integer> confirmedDeals
            ,Double dAvgEngagement, Double dAvgViews) {
        super(name, age, rating, password, dFollowing, minPay, targetAudience, influencerType,
                true, negotiatingDeals, confirmedDeals);
        this.dAvgEngagement = dAvgEngagement;
        this.dAvgViews = dAvgViews;
    }

    //equals hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PremiumProject that)) return false;
        if (!super.equals(o)) return false;

        if (Double.compare(that.dAvgEngagement, dAvgEngagement) != 0) return false;
        return Double.compare(that.dAvgViews, dAvgViews) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(dAvgEngagement);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dAvgViews);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    //toString
    @Override
    public String toString() {
        return super.toString() +
                "PremiumProject{" +
                "dAvgEngagement=" + dAvgEngagement +
                ", dAvgViews=" + dAvgViews +
                '}';
    }
    //Metrics implementation
    @Override
    public Double getPricerPerFollower() {
        Double pay = (Double)(double) getMinPay();
        Double following = (Double)(double)getdFollowing();
        return Double.parseDouble(StoreMenu.formatString(pay/following));
    }
    @Override
    public Double getAvgEngagement() {
        return dAvgEngagement;
    }

    @Override
    public Double getAvgViews() {
        return dAvgViews;
    }

    @Override
    public String metricsToString() {
            return "PREMIUM Firm: " + getName() + "\n" +
                    "Influencer_Type: " + getInfluencerType() + "\n" +
                    "Target_Audience: " + getTargetAudience() +  "\n" +
                    "Desired Influencer Metrics:\n" +
                    "avgEngagement: " + StoreMenu.formatString(getAvgEngagement())+ "%\n" +
                    "avgViews: " + StoreMenu.formatString(getAvgViews()) + "\n" +
                    "Price Per Follower: " + StoreMenu.formatString(getPricerPerFollower()) + "$";
    }
}