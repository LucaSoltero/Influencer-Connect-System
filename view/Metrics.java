/*
 * Description: Metrics Interface connects common functionality between both Influencer and UserFirm
 * inheritance hierarchies. If a user is premium they will implement this interface in order to compare these metrics
 * to that of the Creators they are interested in.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */
package view;

public interface Metrics {
    Double getPricerPerFollower();
    Double getAvgEngagement();
    Double getAvgViews();
    String metricsToString();
}
