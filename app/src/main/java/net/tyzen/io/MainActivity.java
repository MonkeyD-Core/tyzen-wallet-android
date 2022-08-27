package net.tyzen.io;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient client;
    private SessionHandler session;
    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String bch_address;
    private String bch_key;
    private TextView result;
    private TextView exchange_site;
    private TextView USD;
    private String version="1.0"; //Update your version
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;
    Button setting;
    Button accounts, deposit, withdraw;
    private Spinner spin;
    private TextView lyte_balance;
    private TextView lyte_usd, name;

    private String api_url = "http://"+ conf.url() +":8080/v1/ethbalance"; //Change your IP
    private String api_url2 = "http://"+ conf.url()+":8080/v1/tokenbalance";//Change your IP
    private String api_url3 = "http://"+ conf.url()+":8082/v1/coin";//Change your IP
    private String api_price="https://www.exbitron.com/api/v2/peatio/public/markets/tznusdt/tickers";

    RelativeLayout btn_lyte;
    private Handler handler = new Handler();
    Handler h = new Handler();
    int delay = 5000;
    Runnable runnable;

    private String slyte_rates;
    private String slyte_rate;

    private String update_content;

    LinearLayout account;
    ImageView notify;
    private String coins1, femail;

    private RecyclerView rv;
    private List<List_Data> list_data;
    private MyAdapter adapter;

    private SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        femail = user.getFullName();
        //Set language to usd
        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        final Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        spin = (Spinner) findViewById(R.id.spinner1);

        lyte_balance = (TextView) findViewById(R.id.lyte_balance);
        btn_lyte = (RelativeLayout) findViewById(R.id.ethlyte_button);
        lyte_usd = (TextView) findViewById(R.id.etlytet_usd);

        USD = (TextView) findViewById(R.id.total_balance);
        name = (TextView) findViewById(R.id.username);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        setting = (Button) findViewById(R.id.settings);
        accounts = (Button) findViewById(R.id.accounts);
        deposit = (Button) findViewById(R.id.deposit);
        withdraw = (Button) findViewById(R.id.withdraw);

        rv=(RecyclerView)findViewById(R.id.fishPriceList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list_data=new ArrayList<>();
        adapter=new MyAdapter(list_data,this);

        main_task get_info = new main_task();
        get_info.execute();


        coins1="No";
        name.setText(femail);

        btn_lyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Account.class);
                startActivity(i);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, qrcode.class);
                startActivity(i);
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, Withdrawal.class);
//                startActivity(i);
                Intent i = new Intent(MainActivity.this, erc_send.class);
                startActivity(i);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                get_Lyte_balance();
                //getUSD_BXC();
                get_Lyte_price();
                bxc_transaction();

                //Calculate to usd
                onResume();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void get_Lyte_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("username", femail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, conf.api()+"get_balance", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            //String format = String.format("%.8f",res);
                            lyte_balance.setText(res.toString());
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

    ///////////////////////////////////////////////////////////////////////////////////////////////USD RATE

    protected void onResume() {

        h.postDelayed( runnable = new Runnable() {
            public void run() {

                    try {

                        Double lyt = Double.parseDouble(slyte_rates);
                        Double eth = Double.parseDouble(lyte_balance.getText().toString());
                        Double totals = lyt * eth;
                        String lytes = String.format("%.2f", totals);

                        slyte_rate = lytes;
                        //Total
                        double ly = Double.parseDouble(slyte_rate);

                        double total = ly  ;
                        String tot = String.format("%.2f", total);
                        USD.setText(" "+tot);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    //https://www.exbitron.com/api/v2/peatio/public/markets/tznusdt/tickers
    private void get_Lyte_price(){
        JSONObject request = new JSONObject();
        try {
            request.put("username", femail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.GET, api_price, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Double res = Double.parseDouble(response.getString("ticker").toString());
                            String res = response.get("ticker").toString();
                            String resu = res.split("\"")[15];
                            //Toast.makeText(MainActivity.this, resu, Toast.LENGTH_LONG).show();
                            lyte_usd.setText("$"+resu);
                            slyte_rates = resu;
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

    private void bxc_transaction(){
        JSONObject request = new JSONObject();
        try {
            request.put("username", femail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, conf.api()+"get_transaction_list", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            list_data.clear();

                            JSONObject jsonObject=new JSONObject(response.toString());
                            JSONArray array=jsonObject.getJSONArray("transactions");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                List_Data ld = new List_Data();

                                String type= ob.getString("category");
                                if (type.equals("receive")){
                                    ld.image=(R.drawable.deposit);
                                    ld.name = ob.getString("txid");
                                    ld.amount= ob.getString("amount")+ " TZN";
                                }else{
                                    ld.image=(R.drawable.withdraw);
                                    ld.name = ob.getString("txid");
                                    ld.amount= ob.getString("amount")+ " TZN";
                                }
                                list_data.add(ld);
                            }
                            rv.setAdapter(adapter);

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

    private class main_task extends AsyncTask<Integer,Integer,String>{

        @Override
        protected String doInBackground(Integer... integers) {

            try{
                //get_version_content();
                get_Lyte_balance();
                //getUSD_BXC();
                get_Lyte_price();
                bxc_transaction();
                onResume();

            }catch (Exception e)
            {
                //Log.wtf("Main Activity", e.getMessage());
            }
            return null;
        }
    }

}
