/*
 * Description: UserFirm is the Parent class of the Firm Hierarchy. It had 3 inheritors MarketingProject and
 * Premium/NonPremiumProject. It is abstract.
 *
 * @author Luca Soltero
 * ITP 265, Section BOBA
 * Email: lsoltero@usc.edu
 *
 */

package model.firms;

public abstract class UserFirm {
    //instance variables
    private String name;
    private int age;
    private int rating;
    private String password;

    //constructor
    public UserFirm(String name, int age, int rating, String password) {
        this.name = name;
        this.age = age;
        this.rating = rating;
        this.password = password;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //equals hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFirm userFirm)) return false;

        if (age != userFirm.age) return false;
        if (rating != userFirm.rating) return false;
        if (!name.equals(userFirm.name)) return false;
        return password.equals(userFirm.password);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + rating;
        result = 31 * result + password.hashCode();
        return result;
    }

    //toString
    @Override
    public String toString() {
        return "UserFirm{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", rating=" + rating +
                ", password='" + password + '\'' +
                '}';
    }
}
