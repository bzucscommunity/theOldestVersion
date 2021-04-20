package project.bzu.csc.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class ViewTopicPost extends AppCompatActivity{
    List<Post> posts;
    List<User> users;
    TextView userName,postTime,postType,postTitle,postContent,tag1,tag2,tag3,tag4,tag5,postViews,postComments,postShares;
    ImageView postMoreMenu,image1,image2,image3,image4,image5;
    CircleImageView image;


    VideoView video1,video2,video3,video4,video5;
    ConstraintLayout tags,imagesPreviews,videosPreviews;

    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post_layout);
        userName=findViewById(R.id.userName);
        postTime=findViewById(R.id.post_time);
        postType=findViewById(R.id.postType);
        postTitle=findViewById(R.id.post_Title);
        postContent=findViewById(R.id.post_content);
        tag1=findViewById(R.id.tag1);
        tag2=findViewById(R.id.tag2);
        tag3=findViewById(R.id.tag3);
        tag4=findViewById(R.id.tag4);
        tag5=findViewById(R.id.tag5);

        postViews=findViewById(R.id.post_views);
        postComments=findViewById(R.id.post_comments);
        postShares=findViewById(R.id.post_shares);
        image = (CircleImageView) findViewById(R.id.userImage);
        postMoreMenu=findViewById(R.id.post_more_menu);
        image1=findViewById(R.id.image_preview1);
        image2=findViewById(R.id.image_preview2);
        image3=findViewById(R.id.image_preview3);
        image4=findViewById(R.id.image_preview4);
        image5=findViewById(R.id.image_preview5);
        video1=findViewById(R.id.video_preview1);
        video2=findViewById(R.id.video_preview2);
        tags=findViewById(R.id.tags);
        imagesPreviews=findViewById(R.id.images_previews);
        videosPreviews=findViewById(R.id.videos_previews);
        BottomNavigationView BttomnavigationView =findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.topic);
        BttomnavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeIcon:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.question:
                        startActivity(new Intent(getApplicationContext(), Question.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.topic:
                        startActivity(new Intent(getApplicationContext(), Topic.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MoreMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        posts=new ArrayList<>();
        users = new ArrayList<>();

        extractPosts();


//
    }
    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int postID= (int) intent.getExtras().get("postIDFromTopic");
        String JSON_URL="http://192.168.1.111:8080/api/getPost/"+postID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject postObject = response.getJSONObject(i);
                        Post post = new Post();

                        post.setPostAttachment(postObject.getString("postAttachment").toString());
                        post.setPostBody(postObject.getString("postBody").toString());
                        post.setPostID(postObject.getInt("postID"));
                        post.setPostSubject(postObject.getString("postSubject").toString());
                        post.setPostTags(postObject.getString("postTags").toString());
                        post.setPostTitle(postObject.getString("postTitle").toString());
                        post.setPostType(postObject.getString("postType").toString());
                        String user1=  postObject.getString("user");
                        post.setPostTime(postObject.getString("postTime").toString());
                        Gson g = new Gson();
                        User user = g.fromJson(user1, User.class);
                        post.setUser(user);


                        Picasso.get().load(post.getUser().getUserImage()).into(image);
                        userName.setText(post.getUser().getUserName());
                        if(post.getPostType().equals("Question")){
                            postType.setText("Q");}
                        else if(post.getPostType().equals("Topic")){
                            postType.setText("T");}
                        postTitle.setText(post.getPostTitle());
                        postContent.setText(post.getPostBody());
                        postTime.setText(calculateTimeAgo(post.getPostTime()));
                        String tagsString=post.getPostTags();
                        String[] tagsArray=tagsString.split(",");
                        if(tagsArray.length==1){
                            tag1.setText(tagsArray[0]);
                            tag1.setVisibility(View.VISIBLE);
                            tags.setVisibility(View.VISIBLE);
                        }else if(tagsArray.length==2){
                            tag1.setText(tagsArray[0]);
                            tag2.setText(tagsArray[1]);
                            tag1.setVisibility(View.VISIBLE);
                            tag2.setVisibility(View.VISIBLE);
                            tags.setVisibility(View.VISIBLE);
                        }else if(tagsArray.length==3){
                            tag1.setText(tagsArray[0]);
                            tag2.setText(tagsArray[1]);
                            tag3.setText(tagsArray[2]);
                            tag1.setVisibility(View.VISIBLE);
                            tag2.setVisibility(View.VISIBLE);
                            tag3.setVisibility(View.VISIBLE);
                            tags.setVisibility(View.VISIBLE);
                        }else if(tagsArray.length==4){
                            tag1.setText(tagsArray[0]);
                            tag2.setText(tagsArray[1]);
                            tag3.setText(tagsArray[2]);
                            tag4.setText(tagsArray[3]);
                            tag1.setVisibility(View.VISIBLE);
                            tag2.setVisibility(View.VISIBLE);
                            tag3.setVisibility(View.VISIBLE);
                            tag4.setVisibility(View.VISIBLE);
                            tags.setVisibility(View.VISIBLE);

                        }else if(tagsArray.length==5) {
                            tag1.setText(tagsArray[0]);
                            tag2.setText(tagsArray[1]);
                            tag3.setText(tagsArray[2]);
                            tag4.setText(tagsArray[3]);
                            tag5.setText(tagsArray[4]);
                            tag1.setVisibility(View.VISIBLE);
                            tag2.setVisibility(View.VISIBLE);
                            tag3.setVisibility(View.VISIBLE);
                            tag4.setVisibility(View.VISIBLE);
                            tag5.setVisibility(View.VISIBLE);
                            tags.setVisibility(View.VISIBLE);

                        }
                        posts.add(post);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }
    private String calculateTimeAgo(String times) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            long time = sdf.parse(times).getTime();

            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            return ago+ "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


}
