package net.tyzen.io;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Trade extends AppCompatActivity {
    private OkHttpClient client;

    ImageView back;
    Button mex;
    Button flyer;


    private TextView vol;
    private TextView high;
    private TextView low;

    private TextView evol;
    private TextView ehigh;
    private TextView elow;

    private TextView btc_price;
    private TextView eth_price;
    private TextView usd_btc;
    private TextView usd_eth;
    ImageView stat;
    ImageView stat2;
    private String sbtc_rates;
    private String seth_rates;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        client = new OkHttpClient();

        //Set language to usd
        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        vol = (TextView) findViewById(R.id.vol);
        high = (TextView) findViewById(R.id.high_p);
        low = (TextView) findViewById(R.id.low_p);
        btc_price = (TextView) findViewById(R.id.btc_price);
        eth_price = (TextView) findViewById(R.id.eth_price);

        evol = (TextView) findViewById(R.id.evol);
        ehigh = (TextView) findViewById(R.id.ehigh_p);
        elow = (TextView) findViewById(R.id.elow_p);

        mex = (Button) findViewById(R.id.mex);
        flyer = (Button) findViewById(R.id.flyer);
        back = (ImageView) findViewById(R.id.back);
        stat= (ImageView) findViewById(R.id.stat);
        stat2= (ImageView) findViewById(R.id.stat2);

        usd_btc = (TextView) findViewById(R.id.usd_btc);
        usd_eth = (TextView) findViewById(R.id.usd_eth);

        main_task get_info = new main_task();
        get_info.execute();
        // Run the above code block on the main thread after 2 seconds
        handler.post(runnableCode);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Trade.this, MainActivity.class);
                startActivity(i);
            }
        });

        mex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(browserIntent);
            }
        });

        flyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(browserIntent);
            }
        });


    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            get_coingecko_btc_price();
            getUSD_BTC();
            getUSD_ETH();
            handler.postDelayed(this, 5000);
        }
    };

    private void get_lyte() {
        final okhttp3.Request request = new okhttp3.Request.Builder().url("").build();
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
                            String res = response.body().string();

                            String[] vos = res.split(",");
                            String vos1 = vos[4]; // "price_usd": "0.7042710636"
                            String[] vos4 = vos1.split("\"");
                            String vos5 = vos4[1];
                            Double lyt1 = Double.parseDouble(vos5);
                            String volss = String.format("%.2f", lyt1);
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString = formatter.format(lyt1);
                            vol.setText(yourFormattedString + " - EtLyteT");

                            String[] hs = res.split(",");
                            String hs1 = hs[1]; // "price_usd": "0.7042710636"
                            String[] hs4 = hs1.split("\"");
                            String hs5 = hs4[1];
                            high.setText(hs5);

                            String[] ls = res.split(",");
                            String ls1 = ls[2]; // "price_usd": "0.7042710636"
                            String[] ls4 = ls1.split("\"");
                            String ls5 = ls4[1];
                            low.setText(ls5);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void get_lyte_ether() {
        final okhttp3.Request request = new okhttp3.Request.Builder().url("").build();
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
                            String res = response.body().string();
                            //String[] parts = res.split(",");
                            //String part1 = parts[5]; // "price_usd": "0.7042710636"
                            //String[] part4 = part1.split("\"");
                            //String part5 = part4[3];

                            try{
                                JSONObject obj = new JSONObject(res);
                                JSONObject currently = obj.getJSONObject("result");
                                String p = currently.getString("last");
                                eth_price.setText(p);
                                Double eth1 = Double.parseDouble(eth_price.getText().toString());
                                Double eth2 = Double.parseDouble(p);
                                if (eth2 > eth1){
                                    stat2.setImageResource(R.drawable.up);
                                }else{
                                    stat2.setImageResource(R.drawable.down);
                                }

                                Double v = currently.getDouble("volume");
                                DecimalFormat formatter = new DecimalFormat("#,###.00");
                                String yourFormattedString = formatter.format(v);

                                evol.setText(yourFormattedString+" - EtLyteT");

                                String h = currently.getString("high");
                                ehigh.setText(h);
                                String l = currently.getString("low");
                                elow.setText(l);


                            }catch (Exception e){

                            }



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void get_coingecko_btc_price() {
        final okhttp3.Request request = new okhttp3.Request.Builder().url("").build();
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
                            String res = response.body().string();

                            try {
                                JSONObject obj = new JSONObject(res);
                                JSONObject currently = obj.getJSONObject("0x1d8ca7baf0895da8afcf153657be064b5092a274");
                                Double p = currently.getDouble("btc");
                                Double olyt1 = Double.parseDouble(btc_price.getText().toString());
                                if (olyt1> p){
                                    stat.setImageResource(R.drawable.down);
                                }else{
                                    stat.setImageResource(R.drawable.up);
                                }
                                String volss = String.format("%.8f", p);
                                btc_price.setText(volss);
                                //Toast.makeText(Trade.this, p.toString(), Toast.LENGTH_LONG).show();

                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
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
                (Request.Method.GET, "", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            sbtc_rates = eths;

                            Double lyt = Double.parseDouble(btc_price.getText().toString());
                            Double eth = Double.parseDouble(sbtc_rates);
                            Double totals = lyt * eth;
                            String lytes = String.format("%.2f", totals);
                            usd_btc.setText("EtLyteT/BTC - $"+lytes);

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

    private void getUSD_ETH() {

        JSONObject request = new JSONObject();
        try {
            request.put("coin", "ethereum");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            //Float bal = BigDecimal.valueOf(jsonObject.getDouble("price")).floatValue();
                            Double rate =jsonObject.getDouble("USD");
                            //String btcs = String.format("%.2f", rate);

                            Double lyt = Double.parseDouble(eth_price.getText().toString());
                            //Double eth = Double.parseDouble(btcs);
                            Double totals = lyt * rate;
                            String lytes = String.format("%.2f", totals);
                            usd_eth.setText("EtLyteT/ETH - $"+lytes);

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

    private class main_task extends AsyncTask<Integer,Integer,String> {

        @Override
        protected String doInBackground(Integer... integers) {

            try{

                getUSD_BTC();
                getUSD_ETH();

            }catch (Exception e)
            {
                //Log.wtf("Trade", e.getMessage());
            }
            return null;
        }
    }
}
