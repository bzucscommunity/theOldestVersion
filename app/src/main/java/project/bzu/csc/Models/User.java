package project.bzu.csc.Models;

public class User {


    private int userID;


    private String userType;


    private String email;


    private String userPassword;


    private String firstName;


    private String lastName;


    private String userImage;


    public User() {
        super();
    }


    public User(int userID, String userType, String email, String userPassword, String firstName, String lastName,
                String userImage) {
        super();
        this.userID = userID;
        this.userType = userType;
        this.email = email;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;
    }


    public int getUserID() {
        return userID;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getUserType() {
        return userType;
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserPassword() {
        return userPassword;
    }


    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getUserImage() {
        return userImage;
    }


    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName(){
        return getFirstName() + " " + getLastName();
    }


    @Override
    public String toString() {
        return "userModel [userID=" + userID + ", userType=" + userType + ", email=" + email + ", userPassword="
                + userPassword + ", firstName=" + firstName + ", lastName=" + lastName + ", userImage=" + userImage
                + "]";
    }



}
