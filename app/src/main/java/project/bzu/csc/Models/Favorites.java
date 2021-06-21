package project.bzu.csc.Models;

public class Favorites {
    private int ID;
    private int postID;
    private int userID;

    public Favorites() {
    }

    public Favorites(int ID,int postID, int userID) {
        this.ID=ID;
        this.postID = postID;
        this.userID = userID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
                "ID=" + ID +
                ", postID=" + postID +
                ", userID=" + userID +
                '}';
    }
}
