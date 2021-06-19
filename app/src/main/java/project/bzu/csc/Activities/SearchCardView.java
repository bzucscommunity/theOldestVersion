package project.bzu.csc.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.bzu.csc.Adapters.GetCategoriesPostsAdapter;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;

public class SearchCardView extends AppCompatActivity {

    List<Post> posts;
    List<User> users;
    RecyclerView recyclerView;
    GetCategoriesPostsAdapter adapter;
    TextView categoriesText;
    String name;
    Dialog dialog;
    String filterBy;
    ImageView accountImage;
    SharedPreferences sp;
    User user;
    int userID;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("subjectNameFromSearch");
        Log.d("TAG", "onCreate: "+name);

        Log.d("TAG", "onCreate: YESS??" + name);
        setContentView(R.layout.recycler_view_search_layout);
        categoriesText=findViewById(R.id.categoryText);
        categoriesText.setText(name);
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
        recyclerView = findViewById(R.id.categoriesPostsList);
        posts = new ArrayList<>();
        users = new ArrayList<>();
        accountImage = findViewById(R.id.account);
        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sp.getInt("userID" , 0);
        extractUser();
        Log.d("test", "onCreate: hell0");

        FloatingActionButton fab_addNewPost = findViewById(R.id.fab_add);
        fab_addNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
               showAlertDialog();
            }


        });
        extractPosts();
    }

    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        String JSON_URL="http://192.168.1.111:8080/api/getPostBySubject/"+name;
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
                        post.setPostAttachment(postObject.getString("postAttachment").toString());
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

                adapter = new GetCategoriesPostsAdapter(getApplicationContext(),posts);
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

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchCardView.this);
        alertDialog.setTitle("Filter By");
        String[] items = {"Topic" , "Question"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        filterBy="Topic";
                        filter();
                      //  startActivity(new Intent(getApplicationContext(), CreatePostFromHome.class));
                       // Toast.makeText(SearchCardView.this, "Clicked on topic", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                       filterBy="Question";
                       filter();
                        break;

                }


            }
        });


        String negativeText = getString(android.R.string.cancel);
        alertDialog.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialog, start counter again
                        dialog.dismiss();

                    }
                });





        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void filter(){
        posts.clear();
        RequestQueue queue= Volley.newRequestQueue(this);
        String JSON_URL="http://192.168.1.111:8080/api/typeSubject/"+filterBy+"/"+name;
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

                adapter = new GetCategoriesPostsAdapter(getApplicationContext(),posts);
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






