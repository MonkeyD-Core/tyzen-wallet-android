package net.tyzen.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Reward extends AppCompatActivity {
    private SessionHandler session;
    LinearLayout logout;
    ImageView back;
    TextView name;
    TextView email;
    TextView code;
    String fname;
    String femail;
    RelativeLayout referral;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;
    private String url="http://"+ conf.url() +":8080/v1/email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        session = new SessionHandler(getApplicationContext());

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Reward.this, MainActivity.class);
                startActivity(i);
            }
        });



    }

}