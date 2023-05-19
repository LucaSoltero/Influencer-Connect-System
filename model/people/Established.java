/*
 * Description: This class Established represents Established influencers. It inherits from Influencer.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package model.people;

import model.InfluencerType;
import model.TargetAudience;

public class Established extends Influencer{
    //instance variable
    private boolean openToSponsorDeal;
    //constructor
    public Established(String name, int age, char gender, int desiredRating, int following, int minPay,
                       TargetAudience targetAud, InfluencerType influencerType,boolean openToSponsorDeal) {

        super(name, age, gender, desiredRating, following, minPay, targetAud, influencerType,true);
        this.openToSponsorDeal = openToSponsorDeal;
    }

    public boolean isOpenToSponsorDeal() {
        return openToSponsorDeal;
    }

    public void setOpenToSponsorDeal(boolean openToSponsorDeal) {
        this.openToSponsorDeal = openToSponsorDeal;
    }

    //equals hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Established that)) return false;
        if (!super.equals(o)) return false;

        return openToSponsorDeal == that.openToSponsorDeal;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (openToSponsorDeal ? 1 : 0);
        return result;
    }

    //toString
    @Override
    public String toString() {
        return super.toString() + "Established{" +
                "openToSponsorDeal=" + openToSponsorDeal +
                '}';
    }
    //to String for metrics
    public String prettyToString(){
        return "ESTABLISHED: " + getName() + "\n" +
                "Influencer_Type: " + getInfluencerType() + "\n" +
                "Target_Audience: " + getTargetAud();
    }
    //non Premium ToString
    @Override
    public String nonPremiumToString(){
        return toString();
    }

}
