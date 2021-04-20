package project.bzu.csc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.bzu.csc.Adapters.spinnerAdapter;
import project.bzu.csc.R;

public class CreateTopicPost extends AppCompatActivity {
    EditText postSubject, postTitle, postTags, postBody;
    Button submit;
    TextView subjectNameValue;
    ArrayList<String> subjectsListSpinner = new ArrayList<>();
    private spinnerAdapter adapter;
    private ArrayAdapter<String> subjectSpinnerAdapter;
    TextView textFile;
    Intent intent;
    String name;
    private static final int PICKFILE_RESULT_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        name= intent.getStringExtra("subjectName");
        Log.d("TAG", "onCreate: HASLSLOSDOSKL"+name);

        setContentView(R.layout.create_topic_post_layout);
        BottomNavigationView BttomnavigationView = findViewById(R.id.bottomNavigationView);
        BttomnavigationView.setSelectedItemId(R.id.topic);
        postTitle = findViewById(R.id.post_title);
        postTags = findViewById(R.id.post_tags);
        postBody = findViewById(R.id.post_body);
        subjectNameValue= findViewById(R.id.subjectNameValue);
        subjectNameValue.setText(name);
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
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MoreMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        subjectNameValue = findViewById(R.id.subjectNameValue);
        Button buttonPick = (Button)findViewById(R.id.post_attachment);
        textFile = (TextView)findViewById(R.id.filePath);

        submit = (Button) findViewById(R.id.post_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitPost();
                Toast.makeText(CreateTopicPost.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPick.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);

            }});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    textFile.setText(FilePath);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void submitPost() {
        String post_url = "http://192.168.1.111:8080/api/post";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // postSubject = findViewById(R.id.post_subject);

        //Log.d("TAG", "submitPost: "+name);
        JSONObject postData = new JSONObject();
        Date date =new Date();
        SimpleDateFormat simple= new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String strdate =simple.format(date);



        try {
            postData.put("postType", "Topic");
            // Log.d("TAG", "submitPost: " + subjectNameValue.getSelectedItem().toString());
            // postData.put("postSubject",  subjectNameValue.getSelectedItem().toString().trim());
            //postData.put("post")
            postData.put("postSubject", name);
            postData.put("postTitle", postTitle.getText().toString().trim());
            postData.put("postTags", postTags.getText().toString().trim());
            postData.put("postBody", postBody.getText().toString().trim());
            Log.d("TAG", "submitPost: "+textFile.getText().toString().trim());
            postData.put("postAttachment", textFile.getText().toString().trim());
            postData.put("postTime", strdate);

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

}
