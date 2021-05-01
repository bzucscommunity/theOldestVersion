package project.bzu.csc.Activities;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import project.bzu.csc.Adapters.GetCategoriesAdapter;
import project.bzu.csc.Adapters.GridSubjectsListQuestionAdapter;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.Subject;
import project.bzu.csc.R;


public class Search extends AppCompatActivity {
    public static Context context;
    SearchView searchView;
    List<Post> posts;
    List<Subject> subjects;
    ImageView icon;
    RecyclerView browseCategories;
    GetCategoriesAdapter adapter;
    private String JSON_URL="http://192.168.1.111:8080/api/subject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        browseCategories=findViewById(R.id.browse_categories);
        icon=findViewById(R.id.Icon);
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

                        return true;
                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), MoreMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        subjects=new ArrayList<>();
        extractCategories();
        searchView=findViewById(R.id.search_view);
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //    if(TextUtils.isEmpty(newText)){
                //      adapter.filter("");
                //recyclerView.clea

                //   }
                //    else{
                //       adapter.filter(newText);
                //    }

                //    return true;
                return false;

            }
        });
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Post> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Post item : posts) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getPostTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
          //  adapter.filterList(filteredlist);
        }
    }
    private void extractCategories() {
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject subjectObject = response.getJSONObject(i);
                        Subject subject = new Subject();

                        subject.setName(subjectObject.getString("name").toString());
                        subject.setImageURL(subjectObject.getString("image"));

                        subjects.add(subject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                browseCategories.setLayoutManager(new GridLayoutManager(getApplicationContext(),1,GridLayoutManager.VERTICAL,false));

                adapter = new GetCategoriesAdapter(getApplicationContext(),subjects);
                browseCategories.setAdapter(adapter);
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