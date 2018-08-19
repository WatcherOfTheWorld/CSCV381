package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    Fragment fragment;
    protected abstract Fragment createFragment();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_abs);
        setTitle("History");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        FragmentManager manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            manager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }



}
