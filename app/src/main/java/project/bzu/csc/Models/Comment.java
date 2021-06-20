package project.bzu.csc.Models;

public class Comment {


    private String body;
    private String commentTime;
    private int userID;
    private  int postID;
    private String firstName;
    private String lastName;
    private String userImage;

    public Comment() {
    }

    public Comment(String body, String commentTime, int userID, int postID, String firstName, String lastName, String userImage) {
        this.body = body;
        this.commentTime = commentTime;
        this.userID = userID;
        this.postID = postID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;
    }

    public Comment(String body, String commentTime, int userID, int postID) {
        this.body = body;
        this.commentTime = commentTime;
        this.userID = userID;
        this.postID = postID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
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
        return "Comment{" +
                "body='" + body + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", userID=" + userID +
                ", postID=" + postID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }
}
