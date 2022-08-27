package net.tyzen.io;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Word_Seed extends AppCompatActivity {

    private Config conf;
    private String api_url = "http://"+conf.url()+":8081/v1/btc";
    private String api_url2 = "http://"+ conf.url() +":8080/v1/generate/eth";
    private String api_url3 = "http://"+ conf.url()+":8085/v1/bch";

    TextView seed;
    TextView copy;
    Button next;
    private String BTC_address;
    private String BTC_secret;
    private String ETH_address;
    private String ETH_secret;
    private String BCH_address;
    private String BCH_secret;
    private String word_seed;
    private String email;
    private String pass;
    private  String user;
    private DataHandler enc;
    private String key = conf.key();
    private String initVector = conf.pass();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed);

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        seed = (TextView) findViewById(R.id.seed);
        copy = (TextView) findViewById(R.id.copy);
        next = (Button) findViewById(R.id.button3);

        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("password");
        user = getIntent().getStringExtra("username");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    BTC_createKey();
                    ETH_createKey();
                    //BCH_createKey();
                } catch (Exception e){
                    Toast.makeText(Word_Seed.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Word_Seed.this, Verify_Seed.class);
                i.putExtra("btc_address",BTC_address);
                i.putExtra("btc_secret",BTC_secret);
                i.putExtra("eth_address", ETH_address);
                i.putExtra("eth_secret",ETH_secret);
                i.putExtra("bch_address",BCH_address);
                i.putExtra("bch_secret",BCH_secret);
                i.putExtra("word_seed",word_seed);
                i.putExtra("email",email);
                i.putExtra("pass",pass);
                i.putExtra("username",user);
                startActivity(i);
                //finish();
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = seed.getText().toString();
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(Word_Seed.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //BTC word seed
    private void BTC_createKey()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("address", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String btcaddress = response.get("address").toString();
                            String btckey = response.get("keys").toString();
                            String words = response.get("seed").toString();

                            BTC_address = btcaddress;
                            BTC_secret = btckey;
                            word_seed =words;
                            seed.setText(words);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void ETH_createKey()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("address", "");

        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ethaddress =response.get("address").toString();
                            String str = response.get("keys").toString();
                            String add = str.replaceFirst("0x", "");

                            ETH_address = ethaddress;
                            ETH_secret = add;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void BCH_createKey()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("address", "");

        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url3, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String bchaddress =response.get("address").toString();
                            String str = response.get("key").toString();
                            String[] parts = bchaddress.split(":");
                            String part1 = parts[1]; // "price_usd": "0.7042710636"
                            //String add = str.replaceFirst("0x", "");

                            BCH_address = enc.encrypt(key,initVector, part1);
                            BCH_secret = enc.encrypt(key,initVector,str);

                            //Toast.makeText(Word_Seed.this, part1 +" -- "+ str, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
