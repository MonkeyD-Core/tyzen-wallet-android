package net.tyzen.io;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.utils.Convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class Transaction_ERC extends AppCompatActivity{
    private SessionHandler session;
    private OkHttpClient client;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();

    private String address;
    private String keys;
    private TextView result;
    private String Jresult;
    private String coin;
    private String contract;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private Adaptor mAdapter;
    private RecyclerView mrecyclerView;
    private String Typess;
    private Button receive;
    private Button btn_send;
    private TextView rates;
    private ImageView logo;
    private String name;
    ImageView back;
    TextView usd;
    TextView cname;

    Handler h = new Handler();
    int delay = 3000;
    Runnable runnable;

    private String seth_rate;
    private String sbnb_rate;
    private String sbnt_rate;
    private String sven_rate;
    private String somg_rate;
    private String szrx_rate;
    private String smkr_rate;
    private String szil_rate;
    private String saion_rate;
    private String sae_rate;
    private String sbat_rate;
    private String sppt_rate;
    private String srep_rate;
    private String susdc_rate;
    private String scs_rate;
    private String sbtc_rate;
    private String spax_rate;
    private String stusd_rate, femail;
    private String api_url2 = "http://"+ conf.url()+":8080/v1/tokenbalance";
    private String api_url3 = "http://"+ conf.url()+":8082/v1/coin";

    private DataHandler enc;
    private SwipeRefreshLayout pullToRefresh;

    private static final String HI = "https://uniqueandrocode.000webhostapp.com/hiren/androidweb.php";
    private RecyclerView rv;
    private List<List_Data>list_data;
    private MyAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erc_transaction);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        client = new OkHttpClient();
        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());

        femail = user.getFullName();

        client = new OkHttpClient();
        result = (TextView) findViewById(R.id.BNB_Balance);
        receive = (Button) findViewById(R.id.btn_receive);
        btn_send=(Button) findViewById(R.id.btn_send);
        cname= (TextView) findViewById(R.id.name);
        back = (ImageView) findViewById(R.id.back);

        coin= getIntent().getStringExtra("coin");
        name= getIntent().getStringExtra("coin");
        contract = getIntent().getStringExtra("contract");
        pullToRefresh = findViewById(R.id.pullToRefresh);



        rv=(RecyclerView)findViewById(R.id.fishPriceList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        list_data=new ArrayList<>();
        adapter=new MyAdapter(list_data,this);

        //new AsyncFetch().execute();
        //getData();
        //Set language to usd
        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        main_task get_info = new main_task();
        get_info.execute();

        if (name.equals("TZN")){
           cname.setText("Tyzen");
        }else if (name.equals("PAX")){
            cname.setText(coin);
        }else if (name.equals("OMG")){
            cname.setText(coin);
        }else if (name.equals("BAT")){
            cname.setText(coin);
        }else if (name.equals("BNB")){
            cname.setText(coin);
        }else if (name.equals("USDC")){
            cname.setText(coin);
        }else if (name.equals("MKR")){
            cname.setText(coin);
        }else if (name.equals("TUSD")){
           cname.setText(coin);
      }

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_ERC.this, qrcode.class);
                i.putExtra("coin",coin);
                startActivity(i);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_ERC.this, erc_send.class);
                i.putExtra("contract",contract);
                i.putExtra("coin",coin);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Transaction_ERC.this, MainActivity.class);
                startActivity(i);
            }
        });


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get_ERC_balance();
                //new AsyncFetch().execute();

                if (name.equals("TZN")){
                    get_bxc_balance();
                    bxc_transaction();
                }else{
                    get_ERC_balance();
                    getData();
                }

                getUSD_BNB();
                getUSD_PAX();
                getUSD_OMG();
                getUSD_MKR();
                getUSD_USDC();
                getUSD_BAT();
                getUSD_TUSD();

                pullToRefresh.setRefreshing(false);

            }
        });

    }

    private void get_ERC_balance(){
        JSONObject request = new JSONObject();
        try {
            if (name.equals("USDC")){
                request.put("address", address);
                request.put("contract", contract);
                request.put("coin", "USDC");
            }else{
                request.put("address", address);
                request.put("contract", contract);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (name.equals("USDC")){
                                Double res = Double.parseDouble(response.get("balance").toString());
                                Double rates = res / 1000000;
                                String format = String.format("%.6f",rates);
                                result.setText(format);
                            }else {
                                Double res = Double.parseDouble(response.get("balance").toString());
                                String format = String.format("%.8f",res);
                                result.setText(format);
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

    private void get_bxc_balance(){
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
                            result.setText(res.toString());
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
    //This the transaction area
    private class AsyncFetch extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(Transaction_ERC.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://api.ethplorer.io/getAddressHistory/"+address+"?apiKey=kpms9035lUDFiS15&token="+contract+"&type=transfer");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray dataArray = jsonObject.getJSONArray("operations");
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<dataArray.length();i++){
                    JSONObject json_data = dataArray.getJSONObject(i);
                    //cash_ fishData = new cash_();


                    String types= json_data.getString("from");
                    String add1= address.substring(0,5);
                    String add2 =types.substring(0,5);

                    String up1 = add1.toUpperCase();
                    String up2 = add2.toUpperCase();

                        String EthAm= json_data.getString("value");

                        BigDecimal wei = Convert.fromWei(EthAm, Convert.Unit.ETHER);
                        String hashs= json_data.getString("transactionHash");

                }
                rv.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(Transaction_ERC.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

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

    //////////////////////////////////////////////////////////////////////////////////USD RATE
    private void getUSD_BNB() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "binance-coin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=BNB&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            sbnb_rate = rate.toString();
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

    private void getUSD_PAX() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "paxos-standard-token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=PAX&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            spax_rate = rate.toString();
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

    private void getUSD_ZRX() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "0x");
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
                            szrx_rate = rate.toString();
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

    private void getUSD_OMG() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "omisego");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=OMG&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            somg_rate = rate.toString();
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

    private void getUSD_MKR() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "maker");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=MKR&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            smkr_rate = rate.toString();
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

    private void getUSD_USDC() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "usd-coin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=USDC&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            susdc_rate = rate.toString();
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

    private void getUSD_AE() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "aeternity");
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
                            sae_rate = rate.toString();
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

    private void getUSD_BAT() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "basic-attention-token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=BAT&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            sbat_rate = rate.toString();
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

    private void getUSD_REP() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "augur");
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
                            srep_rate = rate.toString();
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
    private void getUSD_TUSD() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "trueusd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, "https://min-api.cryptocompare.com/data/price?fsym=TUSD&tsyms=USD", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.toString();
                            JSONObject jsonObject = new JSONObject(res);
                            Double rate =jsonObject.getDouble("USD");
                            String eths = String.format("%.2f", rate);
                            stusd_rate = rate.toString();
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
                            JSONObject jsonObject=new JSONObject(response.toString());
                            JSONArray  array=jsonObject.getJSONArray("transactions");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                List_Data ld = new List_Data();

                                String type= ob.getString("category");
                                if (type.equals("receive")){
                                    ld.image=(R.drawable.arrow_r);
                                    ld.name = ob.getString("txid");
                                    ld.amount= ob.getString("amount")+ " TZN";
                                }else{
                                    ld.image=(R.drawable.arrow_left);
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

    private void getData() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://api.ethplorer.io/getAddressHistory/"+address+"?apiKey="+conf.ethplorerAPI()+"&token="+contract+"&type=transfer", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray  array=jsonObject.getJSONArray("operations");
                    for (int i=0; i<array.length(); i++){
                        JSONObject ob=array.getJSONObject(i);
                        List_Data ld = new List_Data();

                        if (name.equals("USDC")){

                            String types= ob.getString("from");
                            String add1= address.substring(0,5);
                            String add2 =types.substring(0,5);

                            String up1 = add1.toUpperCase();
                            String up2 = add2.toUpperCase();

                            if (up1.equals(up2))
                            {
                                Double res = Double.parseDouble(ob.getString("value"));
                                Double rates = res / 1000000;
                                String format = String.format("%.6f",rates);
                                ld.image=(R.drawable.arrow_left);
                                ld.name = ob.getString("transactionHash");
                                ld.amount=format + " "+name;

                            }else{
                                Double res = Double.parseDouble(ob.getString("value"));
                                Double rates = res / 1000000;
                                String format = String.format("%.6f",rates);
                                ld.image=(R.drawable.arrow_r);
                                ld.name = ob.getString("transactionHash");
                                ld.amount=format + " "+name;
                            }

                        }else {
                            String types= ob.getString("from");
                            String add1= address.substring(0,5);
                            String add2 =types.substring(0,5);

                            String up1 = add1.toUpperCase();
                            String up2 = add2.toUpperCase();

                            if (up1.equals(up2))
                            {
                                String res = ob.getString("value");
                                BigDecimal wei = Convert.fromWei(res, Convert.Unit.ETHER);
                                ld.image=(R.drawable.arrow_left);
                                ld.name = ob.getString("transactionHash");
                                ld.amount=wei.toString() + " "+name;

                            }else {
                                String res = ob.getString("value");
                                BigDecimal wei = Convert.fromWei(res, Convert.Unit.ETHER);
                                ld.image=(R.drawable.arrow_r);
                                ld.name = ob.getString("transactionHash");
                                ld.amount=wei.toString() + " "+name;
                            }
                        }
                        list_data.add(ld);
                    }
                    rv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private class main_task extends AsyncTask<Integer,Integer,String>{

        @Override
        protected String doInBackground(Integer... integers) {

            try{

                if (name.equals("TZN")){
                    get_bxc_balance();
                    bxc_transaction();
                }else{
                    get_ERC_balance();
                    getData();
                }

                getUSD_BNB();
                getUSD_PAX();
                getUSD_OMG();
                getUSD_MKR();
                getUSD_USDC();
                getUSD_BAT();
                getUSD_TUSD();

            }catch (Exception e)
            {
                Log.wtf("T_ERC Activity", e.getMessage());
            }
            return null;
        }
    }
}


