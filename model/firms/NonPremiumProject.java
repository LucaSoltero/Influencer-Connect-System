/*
 * Description: NonPremiumProject is essentially the Implementation of Marketing Project.
 * It inherits from Marketing project. Does not contain POJO methods as it does not contain any unique instance vars
 * as it is a normal project.
 *
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

public class NonPremiumProject extends MarketingProject {


    public NonPremiumProject(String name, int age, int rating, String password, int dFollowing, int minPay,
                             ArrayList<TargetAudience> targetAudience, ArrayList<InfluencerType> influencerType
    ,HashMap<Influencer,Integer> negotiatingDeals, HashMap<Influencer,Integer> confirmedDeals) {
        super(name, age, rating, password,dFollowing, minPay, targetAudience,
                influencerType, false,negotiatingDeals,confirmedDeals);
    }

}
