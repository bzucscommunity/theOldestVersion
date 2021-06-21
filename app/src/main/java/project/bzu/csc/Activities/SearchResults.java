package project.bzu.csc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Adapters.GetCategoriesAdapter;
import project.bzu.csc.Adapters.SearchResultsAdapter;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.Subject;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;

public class SearchResults extends AppCompatActivity {
    public static Context context;
    List<Post> posts;
    String query;
    TextView searchText;
    CircleImageView accountImage;
    SharedPreferences sp;
    User user;
    int userID;
    RecyclerView searchResults;
    SearchResultsAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_layout);

        BottomNavigationView BttomnavigationView = findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.search);
        BttomnavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeIcon:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);

                        return true;
                    case R.id.question:
                        startActivity(new Intent(getApplicationContext(), Question.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.topic:
                        startActivity(new Intent(getApplicationContext(), Topic.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), Favorits.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        accountImage = findViewById(R.id.account);
        accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Favorits.class));
            }
        });
        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sp.getInt("userID" , 0);
        extractUser();
        posts = new ArrayList<>();
        Intent intent = getIntent();
        query= (String) intent.getExtras().get("query");
        searchText=findViewById(R.id.searchText);
        searchText.setText(query);
        searchResults = findViewById(R.id.recyclerView2);
        search(query);

    }

    private void search (String query){

            RequestQueue queue = Volley.newRequestQueue(this);
            Log.d("search2", query);
            String JSON_URL = "http://192.168.1.111:8080/api/searchPosts/" + query;

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

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
                            post.setPostAttachment(postObject.getString("postAttachment").toString());
                            String user1 = postObject.getString("user");
                            post.setPostTime(postObject.getString("postTime").toString());
                            ;
                            Gson g = new Gson();
                            User user = g.fromJson(user1, User.class);
                            post.setUser(user);

                            posts.add(post);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    searchResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    searchAdapter = new SearchResultsAdapter(getApplicationContext(), posts);
                    searchResults.setAdapter(searchAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("tag", "onErrorResponse: " + error.getMessage());
                }
            });
            queue.add(jsonArrayRequest);
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
                    //  userName.setText(user.getFirstName()+" "+user.getLastName());
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