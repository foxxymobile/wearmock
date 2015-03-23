package com.foxxymobile.wearmock;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * An Activity for WearMock
 * Should be used as the super class instead of the Activity
 *
 * Created by Ran on 13/01/2015.
 */
public class WearMockActivity extends Activity {

    protected WearMockLayout wearMockLayout;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Create the WearMockLayout
        wearMockLayout = new WearMockLayout(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wearMockLayout.setLayoutParams(lp);
        
        // Remove the current content view and wrap it with the wearMockLayout
        ViewGroup parent = (ViewGroup) findViewById(android.R.id.content);
        View contentView = parent.getChildAt(0);
        parent.removeView(contentView);
        
        wearMockLayout.addView(contentView);
        
        parent.addView(wearMockLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.skins_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(wearMockLayout!=null) {
            return wearMockLayout.setSkinByMenuItem(item);
        } else {
            return false;
        }
    }


}
