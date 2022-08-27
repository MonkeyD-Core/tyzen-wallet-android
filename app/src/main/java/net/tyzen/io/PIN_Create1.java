package net.tyzen.io;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class PIN_Create1 extends AppCompatActivity {

    Button create;
    EditText code;
    private SessionHandler session;
    Handler h = new Handler();
    int delay = 1000;
    Runnable runnable;

    private String BTC_address;
    private String BTC_secret;
    private String ETH_address;
    private String ETH_secret;
    private String word_seed;
    private String BCH_address;
    private String BCH_secret;
    private String email;
    private String pass;
    private  String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin1);
        session = new SessionHandler(getApplicationContext());

        //create = (Button) findViewById(R.id.button);
        code = (EditText) findViewById(R.id.code);

        code.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//        BTC_address=getIntent().getStringExtra("btc_address");
//        BTC_secret=getIntent().getStringExtra("btc_secret");
//        ETH_address=getIntent().getStringExtra("eth_address");
//        ETH_secret=getIntent().getStringExtra("eth_secret");
        word_seed=getIntent().getStringExtra("word_seed");

//        BCH_address=getIntent().getStringExtra("bch_address");
//        BCH_secret=getIntent().getStringExtra("bch_secret");
        email=getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        username= getIntent().getStringExtra("username");

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String codes = code.getText().toString();
                if (codes.length() == 6) {
                    Intent i = new Intent(getApplicationContext(), PIN_Create2.class);
                    i.putExtra("code", code.getText().toString());
//                    i.putExtra("btc_address",BTC_address);
//                    i.putExtra("btc_secret",BTC_secret);
//                    i.putExtra("eth_address", ETH_address);
//                    i.putExtra("eth_secret",ETH_secret);
                    i.putExtra("word_seed",word_seed);
//                    i.putExtra("bch_address",BCH_address);
//                    i.putExtra("bch_secret",BCH_secret);
                    i.putExtra("email",email);
                    i.putExtra("pass",pass);
                    i.putExtra("username",username);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}

