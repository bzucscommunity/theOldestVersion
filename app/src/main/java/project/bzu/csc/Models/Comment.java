package project.bzu.csc.Models;

public class Comment {

    private int commentID;
    private String body;
    private String commentTime;
    private  int postID;
    private int userID;


    public Comment() {
        super();
    }

    public Comment(String body, String commentTime, int postID, int userID) {
        this.body = body;
        this.commentTime = commentTime;
        this.postID = postID;
        this.userID = userID;
    }

    public Comment(int commentID, String body, String commentTime, int postID, int userID) {
        this.commentID = commentID;
        this.body = body;
        this.commentTime = commentTime;
        this.postID = postID;
        this.userID = userID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", body='" + body + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", postID=" + postID +
                ", userID=" + userID +
                '}';
    }
}
