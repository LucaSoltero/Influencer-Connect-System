/*
 * Description: This abstract class Influencer is a Parent class of Established and Creator. It inherits from Person.
 * It represents Influencers and their characteristics. It also implements the Comparable Interface in order to
 * compare Creator objects and order them on specific parameters.
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package model.people;

import model.InfluencerType;
import model.TargetAudience;


public abstract class Influencer extends Person implements Comparable<Influencer>{
    //instance vars
    private int desiredRating;
    private int following;
    private int minPay;
    private TargetAudience targetAudience;
    private InfluencerType influencerType;
    private boolean isEst;
    //constructor
    public Influencer(String name, int age, char gender, int desiredRating, int following, int minPay,
                      TargetAudience targetAudience, InfluencerType influencerType, boolean isEst) {
        super(name, age, gender);
        this.desiredRating = desiredRating;
        this.following = following;
        this.minPay = minPay;
        this.targetAudience = targetAudience;
        this.influencerType = influencerType;
        this.isEst = isEst;
    }

    //getters and setters
    public int getDesiredRating() {
        return desiredRating;
    }

    public void setDesiredRating(int desiredRating) {
        this.desiredRating = desiredRating;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getMinPay() {
        return minPay;
    }

    public void setMinPay(int minPay) {
        this.minPay = minPay;
    }

    public TargetAudience getTargetAud() {
        return targetAudience;
    }

    public void setTargetAud(TargetAudience targetAud) {
        this.targetAudience = targetAud;
    }

    public InfluencerType getInfluencerType() {
        return influencerType;
    }

    public void setInfluencerType(InfluencerType influencerType) {
        this.influencerType = influencerType;
    }

    public boolean isEst() {
        return isEst;
    }

    public void setEst(boolean est) {
        isEst = est;
    }

    //equals toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Influencer that)) return false;
        if (!super.equals(o)) return false;

        if (desiredRating != that.desiredRating) return false;
        if (following != that.following) return false;
        if (minPay != that.minPay) return false;
        if (isEst != that.isEst) return false;
        if (targetAudience != that.targetAudience) return false;
        return influencerType == that.influencerType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + desiredRating;
        result = 31 * result + following;
        result = 31 * result + minPay;
        result = 31 * result + targetAudience.hashCode();
        result = 31 * result + influencerType.hashCode();
        result = 31 * result + (isEst ? 1 : 0);
        return result;
    }

    //toString
    @Override
    public String toString() {
        return super.toString() +
                "desiredRating=" + desiredRating +
                ", following=" + following +
                ", minPay=" + minPay +
                ", targetAudience=" + targetAudience +
                ", influencerType=" + influencerType +
                ", isEst=" + isEst +
                '}';
    }
    //nonPrem toString will be overridden by both child classes. If non Prem they will not get access
    //to metrics
    public String nonPremiumToString(){
        return "";
    };


    //gets salary string for the receipt
    public String getExpectedSalaryString(){
        return getName() + " Salary Expectations: \n" + "A minimum pay of " +
                getMinPay() + "$ per sponsorship.";
    }

    //compareTo method for implemented for the Comparable Interface
    //compare price per follower, then avgEngagement,then avgviews, and last resort is whether they are verified
    @Override
    public int compareTo(Influencer o) {
        if (this instanceof Creator) {
            if (Math.abs(((Creator) this).getPricerPerFollower() - ((Creator) o).getPricerPerFollower()) < 0.0001) {
                if(Math.abs(((Creator) this).getAvgEngagement() - ((Creator) o).getAvgEngagement()) < 0.0001){
                    if((Math.abs(((Creator) this).getAvgViews() - ((Creator) o).getAvgViews()) < 0.0001)) {
                        return Boolean.compare(((Creator) this).isVerified(), ((Creator) o).isVerified());
                    }
                    else{
                        return Double.compare(((Creator) this).getAvgViews(), ((Creator) o).getAvgViews());
                    }
                }
                else{
                    return Double.compare(((Creator) this).getAvgEngagement(), ((Creator) o).getAvgEngagement());
                }
            }
            else{
                return Double.compare(((Creator) this).getPricerPerFollower(),((Creator)o).getPricerPerFollower());
            }
        }
        else {
            return Integer.compare(this.getFollowing(), o.getFollowing());
        }
    }
}