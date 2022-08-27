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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class Transaction_BTC extends AppCompatActivity {
    private SessionHandler session;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
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
    private String Typess;
    private Button received;
    private Button btn_send;
    private String sbtc_rate;
    ImageView sendIMG;
    ImageView receivedIMG;
    ImageView back;
    private DataHandler enc;
    private SwipeRefreshLayout pullToRefresh;

    private RecyclerView rv;
    private List<List_Data>list_data;
    private MyAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btc_transaction);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        client = new OkHttpClient();
        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getBKeys());
        keysb = enc.decrypt(key,initVector,user.getBSecrets());

        result = (TextView) findViewById(R.id.BTC_Balance);
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

        rv=(RecyclerView)findViewById(R.id.fishPriceList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list_data=new ArrayList<>();
        adapter=new MyAdapter(list_data,this);

        getBTC();
        gettranc();
        getUSD_BTC();


        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BTC.this, qrcode.class);
                i.putExtra("coin","BTC");
                startActivity(i);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BTC.this, btc_send.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_BTC.this, MainActivity.class);
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
    private void getBTC()
    {
        final Request request = new Request.Builder().url("https://blockchain.info/balance?active="+ addressb).build();
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
                        try {
                            String res = response.body().string();
                            String[] parts = res.split(",");
                            String part1 = parts[0]; // "price_usd": "0.7042710636"
                            String[] part2 = part1.split(":");
                            String part3 = part2[2];
                            String remove=part3.replace(" ", "");
                            Double results = Double.parseDouble(remove);
                            Double total= results/100000000;
                            String btcs = String.format("%.8f", total);
                            result.setText(btcs);
                            //} catch (JSONException e) {
                            //Toast.makeText(MainActivity.this, part1, Toast.LENGTH_LONG).show();
                        } catch (IOException e){
                            //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(Transaction_BTC.this, dataArray.toString(), Toast.LENGTH_LONG).show();
                            for(int i=0;i<dataArray.length();i++){
                                JSONObject json_data = dataArray.getJSONObject(i);
                                List_Data ld = new List_Data();

                                String typ = json_data.getString("tx_output_n");

                                if (typ.contains("-"))
                                {
                                    //fishData.from="Payment Sent";
                                    String amt =  json_data.getString("value");
                                    Double vals = Double.parseDouble(amt);
                                    Double total= vals/100000000;
                                    String btcs = String.format("%.8f", total);
                                    ld.amount=btcs+" BTC";
                                    ld.image=(R.drawable.arrow_left);
                                    String hashs= json_data.getString("tx_hash");
                                    ld.name=hashs;
                                }
                                else
                                {
                                    //fishData.from="Payment Received";
                                    String amt =  json_data.getString("value");
                                    Double vals = Double.parseDouble(amt);
                                    Double total= vals/100000000;
                                    String btcs = String.format("%.8f", total);
                                    ld.amount=btcs+" BTC";
                                    ld.image=(R.drawable.arrow_r);
                                    String hashs= json_data.getString("tx_hash");
                                    ld.name=hashs;
                                }
                                list_data.add(ld);
                            }
                            rv.setAdapter(adapter);
                        } catch (IOException e){
                            Toast.makeText(Transaction_BTC.this, e.toString(), Toast.LENGTH_LONG).show();
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
            request.put("coin", "bitcoin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
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

    private void getData() {
        StringRequest stringRequest=new StringRequest(com.android.volley.Request.Method.GET, "https://api.blockcypher.com/v1/btc/main/addrs/"+addressb, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray dataArray = new JSONArray(response);
                    for(int i=0;i<dataArray.length();i++){
                        JSONObject json_data = dataArray.getJSONObject(i);
                        List_Data ld = new List_Data();

                        String types= json_data.getString("from");
                        String add1= address.substring(0,5);
                        String add2 =types.substring(0,5);

                        String up1 = add1.toUpperCase();
                        String up2 = add2.toUpperCase();

                        if (up1.equals(up2))
                        {
                            String EthAm= json_data.getString("value");
                            ld.amount = EthAm+ " BTC";
                            ld.image=(R.drawable.arrow_left);
                            String hashs= json_data.getString("hash");
                            ld.name=hashs;
                        }
                        else
                        {
                            String EthAm= json_data.getString("value");
                            ld.amount = EthAm+ " BTC";
                            ld.image=(R.drawable.arrow_r);
                            String hashs= json_data.getString("hash");
                            ld.name=hashs;
                        }
                        list_data.add(ld);
                    }
                    rv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}



