package net.tyzen.io;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Config conf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * setting up the content view of this screen
         */
        setContentView(R.layout.activity_splash);
        /**
         * handler for showing splash screen for 4 seconds and after then finishing this activity and staring the login activity
         */


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * finishing this activity after 4 seconds
                 */
                finish();
                /**
                 * starting login activity after 4 seconds
                 */
                startActivity(new Intent(SplashActivity.this, Welcome.class));
            }
        }, 3000);//this will finish after 4 seconds
    }
}
