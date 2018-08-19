package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public class ListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new ListFragment();
    }


    // set a backButton action, just for fun = =
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
