package net.tyzen.io;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Transaction_BCH extends AppCompatActivity {
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private SessionHandler session;
    private OkHttpClient client;
    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private TextView result;
    private String Jresult;
    private String Hash;
    private String Val;
    private String Addr;
    private TextView rates;
    private String api_url3 = "http://"+ conf.url()+":8082/v1/coin";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private Adaptor mAdapter;
    private String Typess;
    private Button received;
    private Button btn_send;
    private String sbtc_rate;
    ImageView sendIMG;
    ImageView receivedIMG;
    ImageView back;

    private DataHandler enc;
    private SwipeRefreshLayout pullToRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bch_transaction);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        client = new OkHttpClient();
        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getCKeys());
        keysb = enc.decrypt(key,initVector,user.getCSecrets());

        result = (TextView) findViewById(R.id.BCH_Balance);
        received = (Button) findViewById(R.id.btn_receive);
        btn_send=(Button) findViewById(R.id.btn_send);
        rates= (TextView) findViewById(R.id.AFCASH_rate);
        back = (ImageView) findViewById(R.id.back);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        //Set language to usd
        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        getBTC();
        gettranc();
        getUSD_BTC();

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BCH.this, qrcode.class);
                i.putExtra("coin","BCH");
                startActivity(i);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BCH.this, bch_send.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BCH.this, MainActivity.class);
                startActivity(i);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBTC();
                gettranc();
                getUSD_BTC();
                pullToRefresh.setRefreshing(false);
            }
        });
        //Change the logo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    double rat = Double.parseDouble(sbtc_rate);
                    double bals = Double.parseDouble(result.getText().toString());
                    bals= rat * bals;
                    String eths = String.format("%.2f", bals);
                    rates.setText("$"+String.valueOf(eths));
                } catch (Exception e){

                }
            }
        }, 3000);
    }

    private void getBTC() {
        final Request request = new Request.Builder().url("https://bcc.zupago.pe/api/addr/"+addressb+"/Balance").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("0.00");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String bla = response.body().string();
                            Double results = Double.parseDouble(bla);
                            Double total= results /100000000;
                            String btcs = String.format("%.8f", total);
                            result.setText(btcs);
                        } catch (IOException ioe) {
                            result.setText("0.00");
                        }
                    }
                });
            }
        });
    }




    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    private void gettranc()
    {
        final Request request = new Request.Builder().url("https://api.blockcypher.com/v1/btc/main/addrs/"+addressb).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //result.setText("0");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<cash_> data=new ArrayList<>();
                        try {
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray dataArray = jsonObject.getJSONArray("txrefs");

                            for(int i=0;i<dataArray.length();i++){
                                JSONObject json_data = dataArray.getJSONObject(i);
                                cash_ fishData = new cash_();

                                String typ = json_data.getString("tx_output_n");

                                if (typ.contains("-"))
                                {
                                    fishData.from="Payment Sent";
                                    String amt =  json_data.getString("value");
                                    Double vals = Double.parseDouble(amt);
                                    Double total= vals/100000000;
                                    String btcs = String.format("%.8f", total);
                                    fishData.amount=btcs+" BTC";
                                    fishData.timeStamp=(R.drawable.arrow_left);
                                    String hashs= json_data.getString("tx_hash");
                                    fishData.hash="Hash: "+hashs;
                                }
                                else
                                {

                                    fishData.from="Payment Received";
                                    String amt =  json_data.getString("value");
                                    Double vals = Double.parseDouble(amt);
                                    Double total= vals/100000000;
                                    String btcs = String.format("%.8f", total);
                                    fishData.amount=btcs+" BTC";
                                    fishData.timeStamp=(R.drawable.arrow_r);
                                    String hashs= json_data.getString("tx_hash");
                                    fishData.hash="Hash: "+hashs;
                                }


                                data.add(fishData);
                            }

                            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                            mAdapter = new Adaptor(Transaction_BCH.this, data);
                            mRVFishPrice.setAdapter(mAdapter);
                            mRVFishPrice.setLayoutManager(new LinearLayoutManager(Transaction_BCH.this));

                        } catch (IOException e){
                            Toast.makeText(Transaction_BCH.this, e.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e){
                            Log.d(e.getMessage(), "run: ");
                        }

                    }
                });
            }
        });
    }

    private void getUSD_BTC() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "bitcoin-cash");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url3, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("price");
                            String eths = String.format("%.2f", rate);
                            //btc_usd.setText("$"+eths);
                            sbtc_rate = eths;
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

