package net.tyzen.io;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Status extends AppCompatActivity {

    Button viewblock;
    ImageView back;
    private SessionHandler session;

    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String word_seed;
    private String pin;
    private String coiname;
    private String Thash;

    TextView status;
    TextView ids;
    TextView ex;
    TextView amounts;
    TextView amountr;
    TextView hash;

    private String key = ""; //Enter your key
    private String initVector = "";//Enter your pass
    private DataHandler enc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getBKeys());
        keysb = enc.decrypt(key,initVector,user.getBSecrets());
        word_seed=user.word();
        pin = user.getOTP();

        back = (ImageView) findViewById(R.id.back);
        status = (TextView) findViewById(R.id.status);
        ids = (TextView) findViewById(R.id.id);
        ex = (TextView) findViewById(R.id.ex);
        amounts = (TextView) findViewById(R.id.amountS);
        amountr = (TextView) findViewById(R.id.amountR);
        hash = (TextView) findViewById(R.id.hash);

        viewblock = (Button) findViewById(R.id.btn_view);
        //copy = (Button) findViewById(R.id.btn_copy);

        //Catch the data from
        coiname = getIntent().getStringExtra("coin1");
        Thash =  getIntent().getStringExtra("hash");

        ids.setText(getIntent().getStringExtra("id"));
        ex.setText(getIntent().getStringExtra("coin1") + "-"+ getIntent().getStringExtra("coin2"));
        amounts.setText(getIntent().getStringExtra("from")+ " "+ getIntent().getStringExtra("coin1"));
        amountr.setText(getIntent().getStringExtra("to")+ " "+ getIntent().getStringExtra("coin2"));
        hash.setText(getIntent().getStringExtra("hash"));

        viewblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coiname.equals("BTC"))
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.blockchain.com/btc/tx/"+Thash));
                    startActivity(browserIntent);
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://etherscan.io/tx/"+Thash));
                    startActivity(browserIntent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Status.this, Exchange.class);
                startActivity(i);
            }
        });
    }

}