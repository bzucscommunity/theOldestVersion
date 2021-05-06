package project.bzu.csc.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Adapters.GetCommentsAdapter;
import project.bzu.csc.Models.Comment;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;

public class ViewSearchPost extends AppCompatActivity {
    List<Post> posts;
    List<User> users;
    List<Comment> comments;
    TextView userName,postTime,postType,postTitle,postContent,tag1,tag2,tag3,tag4,tag5,postViews,postComments,postShares,PostClickView;
    ImageView postMoreMenu,image1,image2,image3,image4,image5;
    CircleImageView image;
    EditText commentsText;
    RecyclerView recyclerView;
    GetCommentsAdapter adapter;
    public static Context context;


    VideoView video1,video2,video3,video4,video5;
    ConstraintLayout tags,imagesPreviews,videosPreviews;

    int postID;

    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post_layout);

        Intent intent = getIntent();
        postID= (int) intent.getExtras().get("postIDFromSearch");

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
        PostClickView=findViewById(R.id.Postclick);
        recyclerView=findViewById(R.id.recyclerView);
        context=this;
        BottomNavigationView BttomnavigationView =findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.search);
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
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
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
        comments=new ArrayList<>();
        extractPosts();
        extractComments();
        PostClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addComment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ViewSearchPost.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });


//
    }
    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int postID= (int) intent.getExtras().get("postIDFromSearch");
        String JSON_URL="http://192.168.1.106:8080/api/getPost/"+postID;
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
    private void addComment()throws JSONException {
        Date date =new Date();
        SimpleDateFormat simple= new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String strdate =simple.format(date);
        String post_url = "http://192.168.1.106:8080/api/postcomment";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // postSubject = findViewById(R.id.post_subject);
        commentsText = findViewById(R.id.editcomments);

        JSONObject postData = new JSONObject();

        try {

            //postData.put("userID","1170288");
            postData.put("postID",postID);
            postData.put("body", commentsText.getText().toString().trim());
            postData.put("commentTime",strdate);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, post_url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("tag", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("tag", "onErrorResponse:ERROR");
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
    private void extractComments() {
        RequestQueue queue= Volley.newRequestQueue(this);

        String JSON_URL2="http://192.168.1.106:8080/api/getComments/"+postID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject commentObject = response.getJSONObject(i);
                        Comment comment = new  Comment();
                        String user1=  commentObject.getString("user");
                        Gson g = new Gson();
                        User user = g.fromJson(user1, User.class);
                        comment.setCommentID(commentObject.getInt("commentID"));
                        comment.setBody(commentObject.getString("body"));
                        comment.setCommentTime(commentObject.getString("commentTime"));
                        comment.setUser(user);
                        comment.setPostID(commentObject.getInt("postID"));

                        comments.add(comment);
                        Log.d("TAG", "onResponse: "+ comments.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter = new GetCommentsAdapter(getApplicationContext(),comments);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: testExtractPosts" + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }


}
