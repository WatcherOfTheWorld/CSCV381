package feiranyang.uas.arizona.edu.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
    Feiran Yang
    CSCV 381
    Assignment 2 - BMICalculator
    This activity calculator user's target and max heart rate
 */

public class HeartRateActivity extends AppCompatActivity {
    int MAX = 220;
    TextView max;
    TextView target;
    EditText getAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        setTitle("Heart Rate Calculator");

        // set elements
        max = findViewById(R.id.max);
        target = findViewById(R.id.target);
        getAge = findViewById(R.id.age);

        // display heart rate result
        Button compute = findViewById(R.id.next);
        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    resetTextView();
                    int maxInt = getMax(Integer.parseInt(getAge.getText().toString()));
                    max.setTextSize(16);
                    max.setText("Your Max Heart Rate is "+ maxInt+" bpm");
                    int[] range = getTarget(maxInt);
                    target.setText("Your Target Heart Rate is "+ range[0]+" bpm to "+range[1]+" bpm");
                // if can't, show a err msg on screen
                }catch (Exception e){
                    max.setText("");
                    target.setTextSize(18);
                    target.setTextColor(Color.RED);
                    target.setText("Please enter your age first");
                }
            }
        });

        // set event handler for reset activity
        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetTextView();
                getAge.setText("");
            }
        });

    }

    // reset event except user input
    public void resetTextView(){
        target.setTextSize(14);
        max.setTextSize(14);
        target.setTextColor(Color.GRAY);
        target.setText("maximum heart and target heart rate");
        max.setText("Input your age to Calculate your");
    }

    // return max heart rate
    public int getMax(int age){
        return MAX-age;
    }

    // return the range of target heart rate in array
    public int[] getTarget(int max){
        int[] range = new int[2];
        Long l = Math.round(max*.5);
        range[0] = Integer.valueOf(l.intValue());
        l = Math.round(max*.85);
        range[1] = Integer.valueOf(l.intValue());
        return range;
    }



    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HeartRateActivity.class);
        return intent;
    }


}
