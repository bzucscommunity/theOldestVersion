package project.bzu.csc.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Adapters.GetCommentsAdapter;
import project.bzu.csc.Models.Comment;
import project.bzu.csc.Models.Favorites;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class ViewPostInHome extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    List<Post> posts;
    List<Favorites> favoritesList;
    public ArrayList<User> users;
    List<Comment> comments;
    List<Integer> IDs;
    TextView userName,postTime,postType,postTitle,postContent,tag1,tag2,tag3,tag4,tag5,PostClickView;
    ImageView image1,image2,image3,image4,image5;
    ImageButton favorite,postMoreMenu;
    Post post;
    EditText commentsText;
    RecyclerView recyclerView;
    GetCommentsAdapter adapter;
    public static Context context;
    VideoView video1,video2;
    ConstraintLayout tags,imagesPreviews,videosPreviews;
    int postID,userID;
    SharedPreferences sp;
    CircleImageView image,accountImage;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post_layout);

        Intent intent = getIntent();
        postID= (int) intent.getExtras().get("postID");
        Log.d("TAG", "onCreate: why"+postID);

        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sp.getInt("userID" , 0);
        Log.d("userIdFromSP", String.valueOf(userID));


        userName=findViewById(R.id.userName);
        postTime=findViewById(R.id.post_time);
        postType=findViewById(R.id.postType);
        postTitle=findViewById(R.id.post_Title);
        favorite=findViewById(R.id.fav);
        postContent=findViewById(R.id.post_content);
        tag1=findViewById(R.id.tag1);
        tag2=findViewById(R.id.tag2);
        tag3=findViewById(R.id.tag3);
        tag4=findViewById(R.id.tag4);
        tag5=findViewById(R.id.tag5);


        image = (CircleImageView) findViewById(R.id.userImage);
        postMoreMenu=findViewById(R.id.post_more_menu);
        postMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ViewPostInHome.this, v);
                popup.setOnMenuItemClickListener(ViewPostInHome.this);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.post_menu, popup.getMenu());
                popup.show();
            }
        });
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
        BttomnavigationView.setSelectedItemId(R.id.homeIcon);
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
                        startActivity(new Intent(getApplicationContext(), Favorits.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        favoritesList=new ArrayList<>();
        posts=new ArrayList<>();
        users = new ArrayList<>();
        comments=new ArrayList<>();
        IDs=new ArrayList<>();
        accountImage = findViewById(R.id.account);
        accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Favorits.class));
            }
        });

        extractUser();
        extractPosts();
        if(isFavorite()){
            Log.d("TAG", "onCreate: yessssssssssss");
            favorite.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_24));

        }
        else if(!isFavorite()){
            Log.d("TAG", "onCreate: nooooooooo");
            favorite.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_border_24));
        }
        extractComments();
        PostClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addComment();
                    Intent intent=new Intent(getApplicationContext(),ViewPostInHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("postID",postID);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ViewPostInHome.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isFavorite()) {
                        Log.d("TAG", "onClick: it is fav");
                        removeFromFavorites();
                        Toast.makeText(ViewPostInHome.this, "Removed from Favorites!", Toast.LENGTH_LONG).show();
                    }else if(!isFavorite()){
                        Log.d("TAG", "onClick: it is not fav");
                        addToFavorites();
                        Toast.makeText(ViewPostInHome.this, "Added to Favorites!", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("TAG", "onClick: not working");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


//
    }
    private boolean isFavorite(){
        Log.d("TAG", "isFavorite: im here");
        RequestQueue queue= Volley.newRequestQueue(this);

        String JSON_URL2="http://192.168.1.111:8080/api/isFavorite/"+userID+"/"+postID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    Log.d("TAG", "onResponseeeee: "+response.length());
                    Log.d("TAG", "onResponse2: "+ response.toString());
                    try {
                        JSONObject favoritesObject = response.getJSONObject(i);
                        Favorites favorites = new Favorites();
                        favorites.setPostID(favoritesObject.getInt("postID"));
                        favorites.setUserID(favoritesObject.getInt("userID"));
                        if(favorites.getPostID()==postID && favorites.getUserID()==userID){
                            flag=true;
                            Log.d("TAG", "onResponseasdasdasd: "+flag);
                        }else{
                            flag=false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: no response");
            }
        });
        Log.d("TAG", "isFavorite: im in");
        queue.add(jsonArrayRequest);
        return flag;

    }
    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int postID= (int) intent.getExtras().get("postID");
        String JSON_URL="http://192.168.1.111:8080/api/getPostByID/"+postID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject postObject = response.getJSONObject(i);
                        post = new Post();


                        post.setPostAttachment(postObject.getString("postAttachment"));
                        post.setPostBody(postObject.getString("postBody"));
                        post.setPostID(postObject.getInt("postID"));
                        post.setPostSubject(postObject.getString("postSubject"));
                        post.setPostTags(postObject.getString("postTags"));
                        post.setPostTitle(postObject.getString("postTitle"));
                        post.setPostType(postObject.getString("postType"));
                        post.setPostTime(postObject.getString("postTime"));
                        post.setUserID(postObject.getInt("userID"));
                        post.setFirstName(postObject.getString("firstName"));
                        post.setLastName(postObject.getString("lastName"));
                        post.setUserImage(postObject.getString("userImage"));


                        Picasso.get().load(post.getUserImage()).into(image);
                        userName.setText(post.getUserName());
                        if(post.getPostType().equals("Question")){
                            postType.setText("Q");}
                        else if(post.getPostType().equals("Topic")){
                            postType.setText("T");}
                        postTitle.setText(post.getPostTitle());
                        postContent.setText(post.getPostBody());
                        postTime.setText((post.getPostTime()));
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
                        String imagesString=post.getPostAttachment();

                        if(imagesString.length()==0){
                            imagesPreviews.setVisibility(View.GONE);
                            image1.setVisibility(View.GONE);
                            image2.setVisibility(View.GONE);
                            image3.setVisibility(View.GONE);
                            image4.setVisibility(View.GONE);
                            image5.setVisibility(View.GONE);
                        }
                        else if(!(imagesString=="")){
                            String[] imagesArray=imagesString.split(",");

                            if(imagesArray.length==1){
                                Picasso.get().load(imagesArray[0]).into(image1);
                                imagesPreviews.setVisibility(View.VISIBLE);
                                image1.setVisibility(View.VISIBLE);
                            }else if(imagesArray.length==2){
                                Picasso.get().load(imagesArray[0]).into(image1);
                                Picasso.get().load(imagesArray[1]).into(image2);
                                imagesPreviews.setVisibility(View.VISIBLE);
                                image1.setVisibility(View.VISIBLE);
                                image2.setVisibility(View.VISIBLE);
                            }else if(imagesArray.length==3){
                                Picasso.get().load(imagesArray[0]).into(image1);
                                Picasso.get().load(imagesArray[1]).into(image2);
                                Picasso.get().load(imagesArray[2]).into(image3);
                                imagesPreviews.setVisibility(View.VISIBLE);
                                image1.setVisibility(View.VISIBLE);
                                image2.setVisibility(View.VISIBLE);
                                image3.setVisibility(View.VISIBLE);
                            }else if(imagesArray.length==4) {
                                Picasso.get().load(imagesArray[0]).into(image1);
                                Picasso.get().load(imagesArray[1]).into(image2);
                                Picasso.get().load(imagesArray[2]).into(image3);
                                Picasso.get().load(imagesArray[3]).into(image4);
                                imagesPreviews.setVisibility(View.VISIBLE);
                                image1.setVisibility(View.VISIBLE);
                                image2.setVisibility(View.VISIBLE);
                                image3.setVisibility(View.VISIBLE);
                                image4.setVisibility(View.VISIBLE);

                            }else if(imagesArray.length==5) {
                                Picasso.get().load(imagesArray[0]).into(image1);
                                Picasso.get().load(imagesArray[1]).into(image2);
                                Picasso.get().load(imagesArray[2]).into(image3);
                                Picasso.get().load(imagesArray[3]).into(image4);
                                Picasso.get().load(imagesArray[4]).into(image5);
                                imagesPreviews.setVisibility(View.VISIBLE);
                                image1.setVisibility(View.VISIBLE);
                                image2.setVisibility(View.VISIBLE);
                                image3.setVisibility(View.VISIBLE);
                                image4.setVisibility(View.VISIBLE);
                                image5.setVisibility(View.VISIBLE);
                            }
                        }
                        if(post.getPostTitle().equalsIgnoreCase("Dijkstra???s Shortest Path Algorithm")) {
                            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video2;
                            Uri uri = Uri.parse(videoPath);
                            video1.setVideoURI(uri);


                            MediaController mc = new MediaController(context);
                            video1.setMediaController(mc);
                            mc.setAnchorView(video1);
                            videosPreviews.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.VISIBLE);

                        }
                        else if(post.getPostTitle().equalsIgnoreCase("Data structures")){
                            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
                            Uri uri = Uri.parse(videoPath);
                            video1.setVideoURI(uri);


                            MediaController mc = new MediaController(context);
                            video1.setMediaController(mc);
                            mc.setAnchorView(video1);
                            videosPreviews.setVisibility(View.VISIBLE);
                            video1.setVisibility(View.VISIBLE);
                        }
                        if(post.getUserID()==userID){
                            postMoreMenu.setVisibility(View.VISIBLE);
                        }else{
                            postMoreMenu.setVisibility(View.GONE);
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

//    private String calculateTimeAgo(String times) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//
//        try {
//            long time = sdf.parse(times).getTime();
//
//            long now = System.currentTimeMillis();
//            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
//
//            return ago+ "";
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
    private void addComment()throws JSONException {
        Date date =new Date();
        SimpleDateFormat simple= new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String strdate =simple.format(date);
        String post_url = "http://192.168.1.111:8080/api/postcomment";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // postSubject = findViewById(R.id.post_subject);
        commentsText = findViewById(R.id.editcomments);

        JSONObject postData = new JSONObject();

        try {

            postData.put("userID",userID);
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

        String JSON_URL2="http://192.168.1.111:8080/api/getPostComments/"+postID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject commentObject = response.getJSONObject(i);
                        Comment comment = new  Comment();
                        comment.setBody(commentObject.getString("body"));
                        comment.setCommentTime(commentObject.getString("commentTime"));
                        comment.setUserID(commentObject.getInt("userID"));
                        comment.setPostID(commentObject.getInt("postID"));
                        comment.setFirstName(commentObject.getString("firstName"));
                        comment.setLastName(commentObject.getString("lastName"));
                        comment.setUserImage(commentObject.getString("userImage"));
                        comments.add(comment);
                        Log.d("456",comments.toString());

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
    private void addToFavorites() throws JSONException {

        String post_url = "http://192.168.1.111:8080/api/addTofavorites";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        Favorites favorites=new Favorites();

        try {
            postData.put("userID", userID);
            postData.put("postID",postID);


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
        favorite.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_24));


    }
    private void removeFromFavorites(){
        favorite.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_border_24));
        RequestQueue queue3= Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "removeFromFavorites: "+postID);
        String JSON_URL3="http://192.168.1.111:8080/api/deletePost/" +userID+"/"+ postID;
        Log.d("TAG", "removeFromFavorites: "+JSON_URL3);
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.DELETE, JSON_URL3, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("deleteAPi","worked" + postID);
            }


        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag22", "onErrorResponse: " + error.getMessage());
            }
        });
        queue3.add(jsonObjReq2);

    }
    private void extractUser() {

        RequestQueue queue2= Volley.newRequestQueue(getApplicationContext());
        String JSON_URL2="http://192.168.1.111:8080/api/" + userID;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, JSON_URL2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {


                    User user=new User();

                    user.setUserID(response.getInt("userID"));
                    user.setEmail(response.getString("email").toString());
                    user.setUserType(response.getString("userType").toString());
                    user.setFirstName(response.getString("firstName").toString());
                    user.setLastName(response.getString("lastName").toString());
                    user.setUserPassword(response.getString("userPassword").toString());
                    user.setUserImage((response.getString("userImage").toString()));
                    Picasso.get().load(user.getUserImage()).into(accountImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue2.add(jsonObjReq);



    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_post:
                Intent intent=new Intent(this,EditPost.class);
                intent.putExtra("postID",postID);
                startActivity(intent);
                return true;
            case R.id.delete_post:
                deletePost();
                Toast.makeText(ViewPostInHome.this, "Post Deleted!", Toast.LENGTH_SHORT).show();
                intent=new Intent(this,Home.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private void deletePost() {
        RequestQueue queue3= Volley.newRequestQueue(getApplicationContext());
        String JSON_URL3="http://192.168.1.111:8080/api/deletePost/" + postID;
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.DELETE, JSON_URL3, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("deleteAPi","worked" + postID);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag22", "onErrorResponse: " + error.getMessage());
            }
        });
        queue3.add(jsonObjReq2);
    }



}
