package project.bzu.csc.Activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;

public class Login extends AppCompatActivity {

    EditText userID, password;
    Button login;
    TextView register;
    boolean flag=false;
    TextInputLayout emailError, passError;
    SharedPreferences sp;
    int ID;
    String pass;
    CircleImageView profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.login_layout);
        profile=findViewById(R.id.profile);
        profile.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.logo2));
        userID = (EditText) findViewById(R.id.ID);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        sp=getSharedPreferences("User" , Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sp.edit();
                if(!userID.getText().toString().equals("") && !password.getText().toString().equals("")){
                    ID = Integer.parseInt(userID.getText().toString());
                    pass= password.getText().toString();
                    boolean validated=SetValidation(ID,pass);
                    if(validated){
                        editor.putInt("userID",ID);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);}
                    else{
                        Toast.makeText(Login.this, "Invalid User or Password!", Toast.LENGTH_SHORT).show();
                        userID.setText("");
                        password.setText("");
                    }
                }else {
                    Toast.makeText(Login.this, "Please Fill Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
//                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(intent);
            }
        });
    }
    public boolean SetValidation(int id,String password){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        String JSON_URL="http://192.168.1.111:8080/api/" + id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {

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
                    if(user.getUserPassword().equals(pass) && user!=null){
                        flag=true;
                    }

                    Log.d("TAG", "onResponse: "+user.toString());
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
        queue.add(jsonObjReq);


        return flag;
    }

}
