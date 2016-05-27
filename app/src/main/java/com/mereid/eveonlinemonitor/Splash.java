package com.mereid.eveonlinemonitor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by mereid on 5/27/2016.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,CharacterListActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }
}
