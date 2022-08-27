package net.tyzen.io;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PIN_Create2 extends AppCompatActivity {

    Button create;
    EditText code;
    private String codes;
    private SessionHandler session;
    TextView word;

    Handler h = new Handler();
    int delay = 1000;
    Runnable runnable;

    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String word_seed;

    private String BTC_address;
    private String BTC_secret;
    private String ETH_address;
    private String ETH_secret;
    private String BCH_address;
    private String BCH_secret;
    private String email;
    private String pass;
    private  String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin2);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

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
        //create = (Button) findViewById(R.id.button);
        code = (EditText) findViewById(R.id.code);
        word = (TextView) findViewById(R.id.word);
        codes=getIntent().getStringExtra("code");

        code.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String get_code = code.getText().toString();
                if (get_code.length()==6) {
                    if (get_code.equals(codes)) {

                        //session.loginUser(word_seed,ETH_address,ETH_secret,BTC_address,BTC_secret,get_code);
                        session.loginUser(word_seed, username,"","","","",codes, "","");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        //word.setText("Code not match.");
                        Spannable WordtoSpan = new SpannableString("PIN code do not match.");
                        WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        word.setText(WordtoSpan);
                    }
                }
            }
        });
    }

}

