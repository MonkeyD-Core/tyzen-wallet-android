package net.tyzen.io;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class blockchain extends AppCompatActivity {

    private String Thash;
    private String coiname;
    private TextView trans;
    private Button send;
    ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockchain);

        trans=(TextView) findViewById(R.id.hash);
        send = (Button) findViewById(R.id.btn_send);
        back = (ImageView) findViewById(R.id.back);

        Thash = getIntent().getStringExtra("hash");
        coiname =getIntent().getStringExtra("coin");

        trans.setText(Thash);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coiname.contains("BTC"))
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.blockchain.com/btc/tx/"+Thash));
                    startActivity(browserIntent);
                }else if (coiname.contains("TZN")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://explorer.tyzen.io/tx/"+Thash));
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
                Intent i = new Intent(blockchain.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}

