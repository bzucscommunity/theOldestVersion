package project.bzu.csc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import project.bzu.csc.Adapters.GetAllPostsAdapter;
import project.bzu.csc.Models.Favorites;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;

public class Favorits extends AppCompatActivity {

    List<Post> posts;
    List<User> users;
    List<Favorites> favoritesList;
    RecyclerView recyclerView;
    GetAllPostsAdapter adapter;
    TextView userName;
    ImageView userImage;
    SharedPreferences sp;
    User user;
    int userID;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_layout);
        BottomNavigationView BttomnavigationView =findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.menu);
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

                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.favoritesList);
        userImage = findViewById(R.id.imageView);
        userName = findViewById(R.id.textView);

        posts=new ArrayList<>();
        users=new ArrayList<>();
        favoritesList=new ArrayList<>();
        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sp.getInt("userID" , 0);
        extractUser();
        extractPostFromFavorites();




}


    private void extractPostFromFavorites() {
        String JSON_URL = "http://192.168.1.109:8080/api/getFavorites";
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++) {
                    try {
                        JSONObject postObject = response.getJSONObject(i);
                        Favorites favoritePost = new Favorites();

                        favoritePost.setPostID(postObject.getInt("postID"));
                        favoritePost.setUserID(postObject.getInt("userID"));


                        favoritesList.add(favoritePost);


//                        for (int i2 = 0; i2 < favoritesList.size(); i2++) {
                            int postID = favoritesList.get(i).getPostID();
                            Log.d("postIDInFav", String.valueOf(favoritesList.size()));
                            String JSON_URL2 = "http://192.168.1.109:8080/api/getPost/" + postID;
                            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
                            JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, JSON_URL2, null, new Response.Listener<JSONArray>() {

                                @Override
                                public void onResponse(JSONArray response) {
                                    for (int i = 0; i < response.length(); i++) {
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
                                            String user1 = postObject.getString("user");
                                            post.setPostTime(postObject.getString("postTime").toString());
                                            ;
                                            Gson g = new Gson();
                                            User user = g.fromJson(user1, User.class);

                                            post.setUser(user);
                                            Log.d("postInFav", post.toString());


                                            posts.add(post);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                    adapter = new GetAllPostsAdapter(getApplicationContext(), posts);
                                    recyclerView.setAdapter(adapter);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("tag", "onErrorResponse: testExtractPosts" + error.getMessage());
                                }
                            });
                            queue1.add(jsonArrayRequest2);

                       // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error2) {
                Log.d("tag", "onErrorResponse: testExtractPosts" + error2.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void extractUser() {


                        RequestQueue queue2= Volley.newRequestQueue(getApplicationContext());
                        String JSON_URL2="http://192.168.1.109:8080/api/" + userID;
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

                                    Picasso.get().load(user.getUserImage()).into(userImage);
                                    userName.setText(user.getFirstName()+" "+user.getLastName());
                                  // Log.d("userName",user.getFirstName());

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


                  ;
    }


}
