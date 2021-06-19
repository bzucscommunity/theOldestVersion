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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.Subject;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class CreatePostFromHome extends AppCompatActivity {
    EditText postTitle, postTags, postBody;
    Button submit;
    Spinner subjectNameSpinner;
    Spinner postTypeSpinner;
    TextView textFile;
    ArrayList<String> subjectsListSpinner = new ArrayList<>();
    private ArrayAdapter<String> subjectSpinnerAdapter;
    private ArrayAdapter<CharSequence> subjectSpinnerAdapter2;
    SharedPreferences sp;
    User user;
    int userID;
    CircleImageView accountImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_post_from_home_layout);
        BottomNavigationView BttomnavigationView = findViewById(R.id.bottomNavigationView);
        //  BttomnavigationView.setSelectedItemId(R.id.menu);
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
        subjectNameSpinner = findViewById(R.id.subjectNameSpinner);
        postTypeSpinner = findViewById(R.id.postTypeSpinner);
        populateSpinner();
        subjectSpinnerAdapter2= ArrayAdapter.createFromResource(this, R.array.PostType,android.R.layout.simple_spinner_item);
        subjectSpinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postTypeSpinner.setAdapter(subjectSpinnerAdapter2);
        textFile = (TextView)findViewById(R.id.filePath);
        submit = (Button) findViewById(R.id.post_submit);
        sp = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
         userID = sp.getInt("userID" , 0);
        Log.d("userID", String.valueOf(userID));
         extractID();
        accountImage = findViewById(R.id.account);
        extractUser();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    submitPost();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Post Posted!", Toast.LENGTH_SHORT).show();
                postTypeSpinner.setSelection(0);
                subjectNameSpinner.setSelection(0);
                postTitle.setText("");
                postTags.setText("");
                postBody.setText("");
                textFile.setText("");
            }
        });

    }

    private void submitPost() throws JSONException {

        String post_url = "http://192.168.1.111:8080/api/post";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // postSubject = findViewById(R.id.post_subject);
        postTitle = findViewById(R.id.post_title);
        postTags = findViewById(R.id.post_tags);
        postBody = findViewById(R.id.post_body);
        subjectNameSpinner= findViewById(R.id.subjectNameSpinner);
        JSONObject postData = new JSONObject();
        Date date =new Date();
        SimpleDateFormat simple= new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String strdate =simple.format(date);



        try {
            postData.put("postType", postTypeSpinner.getSelectedItem().toString().trim());
            postData.put("postSubject",  subjectNameSpinner.getSelectedItem().toString().trim());
            //  postData.put("postSubject", ob.getName().toString());
            postData.put("postTitle", postTitle.getText().toString().trim());
            postData.put("postTags", postTags.getText().toString().trim());
            postData.put("postBody", postBody.getText().toString().trim());
          //  postData.put("postAttachment", textFile.getText().toString().trim());
            postData.put("postTime", strdate);
          //  postData.put("user",user);
          //  Log.d("user33" , user.toString());


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
                        subjectSpinnerAdapter= new ArrayAdapter<>(CreatePostFromHome.this, android.R.layout.simple_spinner_item,subjectsListSpinner);
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

    private void extractID() {
        RequestQueue queue= Volley.newRequestQueue(this);

        String JSON_URL="http://192.168.1.111:8080/api/" + userID;
     //   JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                JSON_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                    try {
//                        JSONObject postObject = response.getJSONObject(i);
//                        String user1=  postObject.getString("user");
//
//                        Gson g = new Gson();
//                         user = g.fromJson(user1, User.class);

                        user=new User();

                    user.setUserID(response.getInt("userID"));
                    user.setEmail(response.getString("email").toString());
                    user.setUserType(response.getString("userType").toString());
                    user.setFirstName(response.getString("firstName").toString());
                    user.setLastName(response.getString("lastName").toString());
                    user.setUserPassword(response.getString("userPassword").toString());
                    user.setUserImage((response.getString("userImage").toString()));
                        Log.d("Ussser",user.toString());

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
        queue.add(jsonObjReq);
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
