package project.bzu.csc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.bzu.csc.Adapters.GetAllPostsAdapter;
import project.bzu.csc.Adapters.GetQuestionPostsAdapter;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class QuestionCardView extends AppCompatActivity {

    List<Post> posts;
    List<User> users;
    RecyclerView recyclerView;
    GetQuestionPostsAdapter adapter;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name= intent.getStringExtra("subjectNameFromQuestion");
        Log.d("TAG", "onCreate: YESS??"+name);
        setContentView(R.layout.recycler_view_with_add_layout);
        BottomNavigationView BttomnavigationView =findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.question);
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
        recyclerView = findViewById(R.id.subjectsList);
        posts=new ArrayList<>();
        users = new ArrayList<>();
        Log.d("test", "onCreate: hell0");



        extractPosts();




        FloatingActionButton fab_addNewPost = findViewById(R.id.fab_add);
        fab_addNewPost.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View view){
                                                  Intent intent1=new Intent(getApplicationContext(),CreateQuestionPost.class);
                                                  Log.d("osama", "onClick: "+name);
                                                  intent1.putExtra("subjectName",name);
                                                  startActivity(intent1);

                                              }
                                          }
        );

    }
    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        String JSON_URL="http://192.168.1.111:8080/api/typeSubject/Question/"+name;
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
                        post.setPostTime(postObject.getString("postTime").toString()); ;
                        Gson g = new Gson();
                        User user = g.fromJson(user1, User.class);
                        post.setUser(user);


                        posts.add(post);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter = new GetQuestionPostsAdapter(getApplicationContext(),posts);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }
}
