package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class Forgot extends AppCompatActivity {
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    Button signUP;
    LinearLayout forgot;
    private String api_url = "http://"+ conf.url() +":8080/v1/select";
    EditText seed;
    private SessionHandler session;
    Button submit;
    private String BTC_address;
    private String BTC_secret;
    private String ETH_address;
    private String ETH_secret;
    private String word_seed;

    private DataHandler enc;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        session = new SessionHandler(getApplicationContext());
        seed = (EditText) findViewById(R.id.code);
        submit =(Button) findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               String text = seed.getText().toString();
               if (text.equals("")){
                   seed.setError("required fields!");
                   seed.requestFocus();
               }else{
                   String datas = text; //enc.encrypt(key,initVector,text);
                   get_data(datas);
               }

            }
        });
    }

    private void get_data(String word_phrase){
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put("word", word_phrase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            if (response.toString().contains("id")){
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    String btc_address = object.getString("btc_address");
                                    String btc_secret = object.getString("btc_secret");
                                    String eth_address = object.getString("eth_address");
                                    String eth_secret = object.getString("eth_secret");
                                    String bch_address = object.getString("bch_address");
                                    String bch_secret = object.getString("bch_secret");
                                    String word = object.getString("word");
                                    String email = object.getString("email");
                                    String user = object.getString("username");
                                    String pass = object.getString("password");

                                    //session.loginUser(word, email,eth_address,eth_secret,btc_address,btc_secret,"1234", bch_address, bch_secret);
                                    Intent in = new Intent(getApplicationContext(), PIN_Create1.class);
                                    in.putExtra("btc_address",btc_address);
                                    in.putExtra("btc_secret",btc_secret);
                                    in.putExtra("eth_address", eth_address);
                                    in.putExtra("eth_secret",eth_secret);
                                    in.putExtra("bch_address",bch_address);
                                    in.putExtra("bch_secret",bch_secret);
                                    in.putExtra("word_seed",word);
                                    in.putExtra("email",email);
                                    in.putExtra("pass",pass);
                                    in.putExtra("username",user);
                                    startActivity(in);
                                }
                            }else {
                                Message dialog = new Message();
                                dialog.showDialog(Forgot.this, "12-Word Passphrase is not valid.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(Forgot.this);
        pDialog.setMessage("Verifying.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}