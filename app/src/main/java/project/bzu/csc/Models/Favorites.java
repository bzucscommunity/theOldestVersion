package project.bzu.csc.Models;

public class Favorites {

    private int postID;
    private int userID;

    public Favorites() {
    }

    public Favorites(int postID, int userID) {
        this.postID = postID;
        this.userID = userID;
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
        return "Favorites{" +
                "postID=" + postID +
                ", userID=" + userID +
                '}';
    }
}
