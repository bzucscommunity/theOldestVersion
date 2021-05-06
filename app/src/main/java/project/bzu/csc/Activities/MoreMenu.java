package project.bzu.csc.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import project.bzu.csc.R;


public class MoreMenu extends AppCompatActivity {
    ChipGroup group;
    Chip topic, question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_menu_layout);
        //PopupWindow display method
        group = findViewById(R.id.group1);
        topic = findViewById(R.id.topicch1);
        question = findViewById(R.id.Questionch2);

        CompoundButton.OnCheckedChangeListener filterChipListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("mirannn", buttonView.getId() + "");
            }
        };
        topic.setOnCheckedChangeListener(filterChipListener);
        question.setOnCheckedChangeListener(filterChipListener);

//                topic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // check whether the chips is filtered by user or
//                // not and give the suitable Toast message
//                if (topic.isChecked()) {
//                    startActivity(new Intent(getApplicationContext(), CreatePostFromHome.class));
//                } else {
//                    Toast.makeText(MoreMenu.this, "JAVA deselected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // check whether the chips is filtered by user or
//                // not and give the suitable Toast message
//                if (question.isChecked()) {
//                    Toast.makeText(MoreMenu.this, "Python selected", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MoreMenu.this, "Python deselected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }
    public void showPopupWindow ( final View view){


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.more_menu_layout, null);

        //Specify the length and width through constants
        int width = 700;
        int height = 500;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler


        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

}