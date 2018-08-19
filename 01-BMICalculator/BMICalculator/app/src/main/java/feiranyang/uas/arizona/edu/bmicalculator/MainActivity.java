package feiranyang.uas.arizona.edu.bmicalculator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

/*
    Feiran Yang
    CSCV 381
    Assignment 2 - BMICalculator
    This activity calculator user's BMI
 */

public class MainActivity extends AppCompatActivity {

    EditText weight;
    EditText height;
    TextView text;
    TextView bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set weight
        weight = findViewById(R.id.getWeight);
        height = findViewById(R.id.getHeight);
        text = findViewById(R.id.text);
        bmi = findViewById(R.id.bmi);

        // set button to open other activity
        Button heartRate = (Button) findViewById(R.id.heart);
        heartRate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = HeartRateActivity.newIntent(MainActivity.this);
                startActivityForResult(intent,0);
            }
        });


        // set ok button
        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(weight.getText().toString());
                try {
                    // try to compute BMI and final result for user input
                    double weightText = Double.parseDouble(weight.getText().toString());
                    double heightText = Double.parseDouble(height.getText().toString());
                    text.setTextColor(Color.GRAY);
                    text.setText(calculateBMI(weightText,heightText));
                    text.setTextSize(16);
                }catch (Exception e){
                    // if there is issue with user's data, display a err msg
                    clear();
                    text.setTextColor(Color.RED);
                    text.setTextSize(18);
                    text.setText("Input your weight and height first.");
                }
            }
        });

        // set a clear button event, reset every thing in this Activity
        Button reset = findViewById(R.id.clear);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                weight.setText("");
                height.setText("");
            }
        });

    }

    // reset activity except user input
    public void clear(){
        text.setTextColor(Color.GRAY);
        text.setTextSize(14);
        text.setText("Input your weight and height, then tap OK");
        bmi.setTextSize(14);
        bmi.setText("");
    }


    // calculate BMI and show/return result
    public String calculateBMI(double weight, double height){
        double bmiDate = weight * 703 / height /height;
        bmi.setTextSize(20);
        DecimalFormat df = new DecimalFormat("#.0");
        bmi.setText("BMI: "+ df.format(bmiDate));

        if(bmiDate < 18.5){
            return "Underweight";
        }else if(bmiDate <= 24.9){
            return "Normal";
        }
        else if(bmiDate < 30){
            return "Overweight";
        }else{
            return "Obese";
        }
    }


}
