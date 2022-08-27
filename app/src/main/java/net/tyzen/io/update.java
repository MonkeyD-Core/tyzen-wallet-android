package net.tyzen.io;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class update extends AppCompatActivity {

    ImageView back;
    TextView email;
    TextView code;
    private OkHttpClient client;
    TextView version;
    Button update;
    String number="2.5";
    private Config conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        client = new OkHttpClient();
        back = (ImageView) findViewById(R.id.back);
        version = (TextView) findViewById(R.id.cont);
        update = (Button) findViewById(R.id.update);
        get_version();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(update.this, MainActivity.class);
                startActivity(i);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("Your App store link"));
                startActivity(browserIntent);
            }
        });

    }
    //Get version
    private void get_version()
    {
        final okhttp3.Request request = new okhttp3.Request.Builder().url("http://"+ conf.url() +":8080/v1/version").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String res = response.body().string().trim();
                            ///version =res;
                            if (res.equals(number)){
                                update.setVisibility(View.GONE);
                            }else{
                                update.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e){

                        }
                    }
                });
            }
        });
    }

}

