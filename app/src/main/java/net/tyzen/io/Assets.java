package net.tyzen.io;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Assets extends AppCompatActivity {

    private String keysETH;
    private String keysBTC;
    private SessionHandler session;
    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String bch_address;
    private String bch_key;

    private TextView ethlyte;
    private TextView btc;
    private TextView eth;
    private TextView pax;
    private TextView bch;
    private TextView bnb;
    private TextView bat;
    private TextView omg;
    private TextView usdc;
    private TextView mkr;
    private TextView tusd;

    private RelativeLayout ethlyte_button;
    private RelativeLayout btc_button;
    private RelativeLayout eth_button;
    private RelativeLayout pax_button;
    private RelativeLayout bch_button;
    private RelativeLayout bnb_button;
    private RelativeLayout bat_button;
    private RelativeLayout omg_button;
    private RelativeLayout usdc_button;
    private RelativeLayout mkr_button;
    private RelativeLayout tusd_button;
    ImageView back;

    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getBKeys());
        keysb = enc.decrypt(key,initVector,user.getBSecrets());
        bch_address = enc.decrypt(key,initVector,user.getCKeys());
        bch_key = enc.decrypt(key,initVector,user.getCSecrets());

        ethlyte = (TextView) findViewById(R.id.ethlyte);
        btc = (TextView) findViewById(R.id.btc);
        eth = (TextView) findViewById(R.id.eth);
        pax = (TextView) findViewById(R.id.pax);
        bch = (TextView) findViewById(R.id.bch);
        bnb = (TextView) findViewById(R.id.bnb);
        bat = (TextView) findViewById(R.id.bat);
        omg = (TextView) findViewById(R.id.omg);
        usdc = (TextView) findViewById(R.id.usdc);
        mkr = (TextView) findViewById(R.id.mkr);
        tusd = (TextView) findViewById(R.id.tusd);

        back = (ImageView) findViewById(R.id.back);

        ethlyte.setText(keys);
        btc.setText(keysb);
        eth.setText(keys);
        pax.setText(keys);
        bch.setText(bch_key);
        bnb.setText(keys);
        bat.setText(keys);
        omg.setText(keys);
        usdc.setText(keys);
        mkr.setText(keys);
        tusd.setText(keys);

        ethlyte_button =findViewById(R.id.ethlyte_button);
        btc_button =findViewById(R.id.btc_button);
        eth_button =findViewById(R.id.eth_button);
        pax_button =findViewById(R.id.pax_button);
        bch_button =findViewById(R.id.bch_button);
        bnb_button =findViewById(R.id.bnb_button);
        bat_button =findViewById(R.id.bat_button);
        omg_button =findViewById(R.id.omg_button);
        usdc_button =findViewById(R.id.usdc_button);
        mkr_button =findViewById(R.id.mkr_button);
        tusd_button =findViewById(R.id.tusd_button);

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Assets.this, Settings.class);
                startActivity(i);
            }
        });

        ethlyte_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = ethlyte.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        btc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = btc.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        eth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = eth.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        pax_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = pax.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        bch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = bch.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        bnb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = bnb.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        bat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = bat.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

       omg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = omg.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        usdc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = usdc.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });
        mkr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = mkr.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });
        tusd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = tusd.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Assets.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
