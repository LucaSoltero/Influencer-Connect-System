/*
 * Description: This Enum represents the various types of TargetAudiences found in our data. It contains a toString
 * method which prints out the enum in a string fashion
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */
package model;

public enum TargetAudience {
    KIDS,
    YOUNG_ADULTS,
    ADULTS,
    OLDER_ADULTS,
    SENIORS,
    ALL,
    OTHER;
    public static String getTargetTypeToString(){
        StringBuilder sb = new StringBuilder();
        int count = 1;
        sb.append("Target Audience Categories\n");
        for(TargetAudience t : TargetAudience.values()){
            sb.append(count).append(".").append(t).append("\n");
            count ++;
        }
        return sb.toString();
    }



}
