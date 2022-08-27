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

public class PIN_Verify extends AppCompatActivity {

    Button create;
    EditText code;
    private SessionHandler session;
    Handler h = new Handler();
    int delay = 1000;
    Runnable runnable;

    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String word_seed;
    private String pin;
    TextView word;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verify);
        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        //address = enc.decrypt(key,initVector,user.getKeys());
        //keys = enc.decrypt(key,initVector,user.getSecrets());
        //addressb = enc.decrypt(key,initVector,user.getBKeys());
        //keysb = enc.decrypt(key,initVector,user.getBSecrets());
        word_seed=user.word();
        pin = user.getOTP();


        code = (EditText) findViewById(R.id.code);
        word = (TextView) findViewById(R.id.word);

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
                String codes = code.getText().toString();
                if (codes.length()==6) {
                    if (codes.equals(pin)) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        //word.setText("Code not match.");
                        Spannable WordtoSpan = new SpannableString("You entered wrong PIN");
                        WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        word.setText(WordtoSpan);
                    }
                }else {
                    word.setText("Please enter your 6 digit PIN code");
                }
            }
        });
    }

}

