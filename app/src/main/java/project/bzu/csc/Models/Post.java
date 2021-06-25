package project.bzu.csc.Models;



public class Post {

    private int postID;
    private String postType;
    private String postSubject;
    private String postTitle;
    private String postTags;
    private String postBody;
    private String postAttachment;
    private String postTime;
    private int userID;
    private String firstName;
    private String lastName;
    private String userImage;

    public Post() {
        super();
    }

    public Post(int postID, String postType, String postSubject, String postTitle, String postTags, String postBody, String postAttachment, String postTime, int userID, String firstName, String lastName, String userImage) {
        this.postID = postID;
        this.postType = postType;
        this.postSubject = postSubject;
        this.postTitle = postTitle;
        this.postTags = postTags;
        this.postBody = postBody;
        this.postAttachment = postAttachment;
        this.postTime = postTime;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostSubject() {
        return postSubject;
    }

    public void setPostSubject(String postSubject) {
        this.postSubject = postSubject;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostTags() {
        return postTags;
    }

    public void setPostTags(String postTags) {
        this.postTags = postTags;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getPostAttachment() {
        return postAttachment;
    }

    public void setPostAttachment(String postAttachment) {
        this.postAttachment = postAttachment;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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
        return "Post{" +
                "postID=" + postID +
                ", postType='" + postType + '\'' +
                ", postSubject='" + postSubject + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postTags='" + postTags + '\'' +
                ", postBody='" + postBody + '\'' +
                ", postAttachment='" + postAttachment + '\'' +
                ", postTime='" + postTime + '\'' +
                ", userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }
}








