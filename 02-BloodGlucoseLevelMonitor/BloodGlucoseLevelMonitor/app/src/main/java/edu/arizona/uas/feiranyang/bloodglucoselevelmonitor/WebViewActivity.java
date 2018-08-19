package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public class WebViewActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context, Uri uri){
        Intent i = new Intent(context, WebViewActivity.class);
        // set the web site URL
        i.setData(uri);
        return i;
    }

    @Override
    protected Fragment createFragment(){
        // return a new fragment for display WebView
        return WebViewFragment.newInstance(getIntent().getData());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Create a action for back button.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
