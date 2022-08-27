package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Withdrawal extends AppCompatActivity {
    private SessionHandler session;
    private ProgressDialog pDialog;
    ImageView back;
    Button to_bxc, to_btc, to_bank;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_option);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        back = (ImageView) findViewById(R.id.back);
        to_bxc = (Button) findViewById(R.id.bxc);
        to_btc = (Button) findViewById(R.id.btc);
        to_bank = (Button) findViewById(R.id.bank);

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdrawal.this, MainActivity.class);
                startActivity(i);
            }
        });

        to_bxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdrawal.this, erc_send.class);
                startActivity(i);
            }
        });
        to_btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdrawal.this, Withdraw_btc.class);
                startActivity(i);
            }
        });
        to_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdrawal.this, Withdraw_bank.class);
                startActivity(i);
            }
        });
    }

}

