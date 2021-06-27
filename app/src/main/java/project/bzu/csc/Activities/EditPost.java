package project.bzu.csc.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.Subject;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class EditPost extends AppCompatActivity {
    EditText postTitle, postTags, postBody;
    Button edit;
    Spinner subjectNameSpinner;
    Spinner postTypeSpinner;
    TextView textFile;
    ArrayList<String> subjectsListSpinner = new ArrayList<>();
    private ArrayAdapter<String> subjectSpinnerAdapter;
    private ArrayAdapter<CharSequence> subjectSpinnerAdapter2;
    SharedPreferences sp;
    int userID,postID;
    CircleImageView accountImage;
    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_layout);
        BottomNavigationView BttomnavigationView = findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.homeIcon);
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
        posts=new ArrayList<>();
        Intent intent = getIntent();
        postID= (int) intent.getExtras().get("postID");
        subjectNameSpinner = findViewById(R.id.subjectNameSpinner1);
        postTypeSpinner = findViewById(R.id.postTypeSpinner1);
        postTitle = findViewById(R.id.post_title1);
        postTags = findViewById(R.id.post_tags1);
        postBody = findViewById(R.id.post_body1);
        subjectNameSpinner= findViewById(R.id.subjectNameSpinner1);
        populateSpinner();
        subjectSpinnerAdapter2= ArrayAdapter.createFromResource(this, R.array.PostType,android.R.layout.simple_spinner_item);
        subjectSpinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postTypeSpinner.setAdapter(subjectSpinnerAdapter2);
        textFile = (TextView)findViewById(R.id.filePath1);
        edit = (Button) findViewById(R.id.post_edit1);
        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        userID = sp.getInt("userID" , 0);
        accountImage = findViewById(R.id.account);
        extractUser();
        extractPosts();
        accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Favorits.class));
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    editPost();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Changes Saved!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }
        });

    }
    private void extractPosts() {
        RequestQueue queue= Volley.newRequestQueue(this);
        String JSON_URL="http://192.168.1.111:8080/api/getPostByID/"+postID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject postObject = response.getJSONObject(i);
                        Post post = new Post();
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
                        postBody.setText(post.getPostBody());
                        Log.d("TAG", "onResponse:ssss "+post.getPostSubject());

                        if(post.getPostType().equals("Question")){
                            postTypeSpinner.setSelection(1);
                        }
                        if(post.getPostType().equals("Topic")){
                            postTypeSpinner.setSelection(0);
                        }
                        if(post.getPostSubject().equals("Algorithms")){
                            Log.d("TAG", "onResponse: hello");
                            subjectNameSpinner.setSelection(0);
                        }
                        if(post.getPostSubject().equals("Database")){
                            subjectNameSpinner.setSelection(1);
                        }
                        if(post.getPostSubject().equals("Java")){
                            subjectNameSpinner.setSelection(2);
                        }
                        if(post.getPostSubject().equals("Programming Languages")){
                            subjectNameSpinner.setSelection(3);
                        }
                        if(post.getPostSubject().equals("Software Engineering")){
                            Log.d("TAG", "onResponse: hello1");
                            subjectNameSpinner.setSelection(4);
                        }
                        if(post.getPostSubject().equals("Web")){
                            subjectNameSpinner.setSelection(5);
                        }
                        postTags.setText(post.getPostTags());
                        postTitle.setText(post.getPostTitle());
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

    private void editPost() throws JSONException {

        String post_url = "http://192.168.1.111:8080/api/edit/"+postID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();

        try {
            postData.put("postType", postTypeSpinner.getSelectedItem().toString().trim());
            postData.put("postSubject",subjectNameSpinner.getSelectedItem().toString().trim());
            postData.put("postTitle", postTitle.getText().toString().trim());
            postData.put("postTags", postTags.getText().toString().trim());
            postData.put("postBody", postBody.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, post_url, postData, new Response.Listener<JSONObject>() {
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

    private void populateSpinner(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.111:8080/api/subject";

        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject subjectObject = response.getJSONObject(i);
                        Subject subject = new Subject();
                        subject.setName(subjectObject.getString("name"));

                        subjectsListSpinner.add(subject.getName());
                        subjectSpinnerAdapter= new ArrayAdapter<>(EditPost.this, android.R.layout.simple_spinner_item,subjectsListSpinner);
                        subjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjectNameSpinner.setAdapter(subjectSpinnerAdapter);
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


        ;
    }

}
