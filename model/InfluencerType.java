/*
 * Description: This Enum represents the various types of Influencers found in my data. It contains a toString
 * method which prints out the enum in a string fashion
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */
package model;

public enum InfluencerType {
    ATHLETIC,
    POLITICAL,
    COMEDY,
    MUSICIAN,
    ACTOR,
    OTHER;

    public static String getInfluencerTypeToString(){
        StringBuilder sb = new StringBuilder();
        int count = 1;
        sb.append("Influencer Categories\n");
        for(InfluencerType i : InfluencerType.values()){
            sb.append(count).append(".").append(i).append("\n");
            count ++;
        }
        return sb.toString();
    }
}
