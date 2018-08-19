package hellodate.feiranyang.uas.arizona.edu.hellodate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

/*
    Feiran Yang
    CSCV 381
    Assignment 1 - Hello Date
    This app display current date when first run, update current date if button has been clicked
    This app has target ADK version of 25
 */

public class DateActivity extends AppCompatActivity {

    private Button clickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        // get button style from xml layout and set text to the button
        clickButton = (Button) findViewById(R.id.button);
        clickButton.setText(new Date().toString());

        // set OnClickListener to update current date
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton.setText(new Date().toString());
            }
        });
    }
}
