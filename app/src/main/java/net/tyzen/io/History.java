package net.tyzen.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {

    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private RecyclerView mRVFishPrice;
    private Adaptor_history mAdapter;
    private SessionHandler session;
    String coiname;
    String coiname2;
    private String addressb;
    private String keysb;
    private String address;
    private String payin;
    private String keys;
    private String status;
    private String GetAddress;
    ImageView back;
    private Integer limit=10;
    private Integer offset=10;
    private String api_url = "http://"+ conf.url()+":8083/v1/getTransaction";
    private String api_url2 = "http://"+ conf.url() +":8080/v1/select_address";

    private DataHandler enc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getBKeys());
        keysb = enc.decrypt(key,initVector,user.getBSecrets());
        back = (ImageView) findViewById(R.id.back);

        coiname =getIntent().getStringExtra("coin");

        if (coiname.equals("BTC")){
            get_rate(addressb);

        }else
        {
            get_rate(address);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(History.this, Exchange.class);
                startActivity(i);
            }
        });

    }

    //Get listed status
    private void get_rate(String pay){
        JSONObject request = new JSONObject();
        try {
            request.put("address", pay);
            //Toast.makeText(History.this, pay, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.toString();
                            //Toast.makeText(History.this, result, Toast.LENGTH_LONG).show();
                            if (result.contains("true")){
                            }else {
                                List<cash_2> data=new ArrayList<>();
                                try {

                                    JSONObject jsonObject = new JSONObject(result);
                                    JSONArray dataArray = jsonObject.getJSONArray("data");

                                    for(int i=0;i<dataArray.length();i++) {
                                        JSONObject json_data = dataArray.getJSONObject(i);
                                        cash_2 fishData = new cash_2();

                                        fishData.timeStamp = json_data.getString("TransID");

                                        fishData.from = json_data.getString("exchange");
                                        String amts = json_data.getString("tos");

                                        //if (coiname.equals("BTC")) {
                                        //fishData.amount = amts + " BTC";
                                        //} else {
                                        fishData.amount = amts;
                                        //}

                                        fishData.status = "";

                                        data.add(fishData);
                                    }
                                    mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                                    mAdapter = new Adaptor_history(History.this, data);
                                    mRVFishPrice.setAdapter(mAdapter);
                                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(History.this));

                                } catch (Exception ex){
                                    Toast.makeText(History.this, ex.toString(), Toast.LENGTH_LONG).show();
                                }
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

    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YY");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    //Save Payin
    private void select_payin(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", addressb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.get("data").toString();
                            String[] parts = result.split("\"");
                            String part1 = parts[3]; // "price_usd": "0.7042710636"
                            get_rate(part1);
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
    //Save Payin
    private void select_payin_eth(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.get("data").toString();
                            String[] parts = result.split("\"");
                            String part1 = parts[3];

                            get_rate(part1);
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


}