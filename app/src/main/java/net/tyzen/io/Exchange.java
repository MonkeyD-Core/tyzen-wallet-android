package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Exchange extends AppCompatActivity {
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();

    private String api_url = "http://"+conf.url()+":8083/v1/getXamount";
    private String api_url1 = "http://"+ conf.url() +":8080/v1/ethbalance";
    private String api_url2 = "http://"+ conf.url()+":8080/v1/tokenbalance";
    private String api_url3 = "http://"+ conf.url() +":8083/v1/create";
    private String api_url4 = "http://"+ conf.url()+":8083/v1/minimum";
    private String api_url5 = "http://"+ conf.url()+":8080/v1/insert_address";
    private String api_urlbtc = "http://"+ conf.url()+":8081/v1/transaction/btc";
    private String api_urleth = "http://"+ conf.url()+":8080/v1/send/ethraw";
    private String api_urlerc = "http://"+ conf.url()+":8080/v1/send/eth";
    private String api_fees = "http://"+ conf.url()+":8081/v1/fees";
    private String api_url6 = "http://"+ conf.url() +":8084/api/v1/transfer";
    private static final String KEY_STATUS = "error";
    private static final String KEY_MESSAGE = "data";
    private SessionHandler session;
    private String address;
    private OkHttpClient client;
    private String keys;
    public String ethfee;
    Double ethbals;
    public String contract;
    private String addressb;
    private String keysb;
    private String TransID;
    private String coins1;
    private String coins2;
    private Spinner spin;
    private Spinner spin2;
    private String rawTransaction;
    private ProgressDialog pDialog;
    TextView rate;
    TextView btcfee;
    TextView bal;
    TextView maxs;
    private EditText c1;
    TextView c2;
    TextView title;
    Handler h = new Handler();
    int delay = 3000;
    Runnable runnable;

    ImageView back;
    ImageButton wallets;
    ImageButton exchanges;
    ImageButton cashouts;
    Button convert;
    Button history;
    Double total_balance;
    private String youraddress;
    private boolean mStopHandler = false;

    private DataHandler enc;
    private String bch_address;
    private String bch_key;

    String[] fruits={"BTC","ETH","PAX","BNB", "BAT", "OMG", "USDC", "MKR","TUSD"};
    int images[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};

    String[] fruits_btc ={"ETH","PAX","BNB", "BAT", "OMG", "USDC", "MKR","TUSD"};
    int images_btc[] = {R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};

    String[] fruits_eth={"BTC","PAX","BNB", "BAT", "OMG", "USDC", "MKR","TUSD"};
    int images_eth[] = {R.drawable.btc, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};

    String[] fruits_pax={"BTC","ETH","BNB", "BAT", "OMG", "USDC", "MKR","TUSD"};
    int images_pax[] = {R.drawable.btc,R.drawable.eth, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};
    String[] fruits_bnb={"BTC","ETH","PAX","BAT", "OMG", "USDC", "MKR","TUSD"};
    int images_bnb[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};
    String[] fruits_bat={"BTC","ETH","PAX","BNB", "OMG", "USDC", "MKR","TUSD"};
    int images_bat[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.omg,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};
    String[] fruits_omg={"BTC","ETH","PAX","BNB", "BAT", "USDC", "MKR","TUSD"};
    int images_omg[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.usdc,R.drawable.mkr,R.drawable.tusd};
    String[] fruits_usdc={"BTC","ETH","PAX","BNB", "BAT", "OMG", "MKR","TUSD"};
    int images_usdc[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.mkr,R.drawable.tusd};
    String[] fruits_mkr={"BTC","ETH","PAX","BNB", "BAT", "OMG", "USDC", "TUSD"};
    int images_mkr[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.tusd};
    String[] fruits_tusd={"BTC","ETH","PAX","BNB", "BAT", "OMG", "USDC", "MKR"};
    int images_tusd[] = {R.drawable.btc,R.drawable.eth, R.drawable.pax, R.drawable.bnb, R.drawable.bat,R.drawable.omg,R.drawable.usdc,R.drawable.mkr};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        client = new OkHttpClient();
        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getBKeys());
        keysb = enc.decrypt(key,initVector,user.getBSecrets());
        bch_address = enc.decrypt(key,initVector,user.getCKeys());
        bch_key = enc.decrypt(key,initVector,user.getCSecrets());

        //Set language to usd
        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        spin = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        rate = (TextView) findViewById(R.id.rate);
        btcfee = (TextView) findViewById(R.id.btcfee) ;
        bal = (TextView) findViewById(R.id.USD) ;
        c1=(EditText) findViewById(R.id.send);
        c2=(TextView) findViewById(R.id.receive);

        convert = (Button) findViewById(R.id.btn_convert);
        history = (Button) findViewById(R.id.btn_history);
        title = (TextView) findViewById(R.id.title4);
        maxs = (TextView) findViewById(R.id.max);
        back = (ImageView) findViewById(R.id.back);

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images,fruits);
        spin.setAdapter(customAdapter);

        c1.setText("1");
        youraddress = address;

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                coins1 = fruits[position1];

                    if (coins1.equals("BTC")){
                        getBTC();
                        getBTC_fees();
                        title.setText("MINERS FEE SATOSHI");

                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_btc,fruits_btc);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("ETH")){
                        get_ETH_balance();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_eth,fruits_eth);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("BNB")){
                        get_BNB_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0xB8c77482e45F1F44dE1745F52C74426C631bDD52";

                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_bnb,fruits_bnb);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("PAX")){
                        get_PAX_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0x8E870D67F660D95d5be530380D0eC0bd388289E1";

                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_pax,fruits_pax);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("OMG")){
                        get_OMG_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";

                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_omg,fruits_omg);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("BCH")){
                        get_BCH_balance();
                        getBTC_fees();
                        title.setText("MINERS FEE SATOSHI");

                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("BAT")){
                        get_BAT_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0x0d8775f648430679a709e98d2b0cb6250d2887ef";
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_bat,fruits_bat);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("USDC")){
                        get_USDC_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_usdc,fruits_usdc);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("MKR")){
                        get_MKR_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2";
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_mkr,fruits_mkr);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    }else if (coins1.equals("TUSD")){
                        get_TUSD_balance();
                        get_ETH_balance2();
                        getETH_fees();
                        title.setText("MINERS FEE GWEI");
                        contract="0x0000000000085d4780B73119b644AE5ecd22b376";
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),images_tusd,fruits_tusd);
                        spin2.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                        //Get rate
                        if(c1.equals("0") || (c2.equals("0")))
                        {
                            //nothing happen
                        }else{
                            get_rate();
                            get_rate_actual();
                        }
                    //}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                //coins2 = fruits[position2];
                if (coins1.equals("BTC")){
                    coins2 = fruits_btc[position2];
                }else if (coins1.equals("ETH")){
                    coins2 = fruits_eth[position2];
                }else if (coins1.equals("BNB")){
                    coins2 = fruits_bnb[position2];
                }else if (coins1.equals("PAX")){
                    coins2 = fruits_pax[position2];
                }else if (coins1.equals("OMG")){
                    coins2 = fruits_omg[position2];
                }else if (coins1.equals("BAT")){
                    coins2 = fruits_bat[position2];
                }else if (coins1.equals("MKR")){
                    coins2 = fruits_mkr[position2];
                }else if (coins1.equals("USDC")){
                    coins2 = fruits_usdc[position2];
                }else if (coins1.equals("TUSD")){
                    coins2 = fruits_tusd[position2];
                }

                if (coins1.equals(coins2)){
                    c2.setText("0");
                }else{
                    if (coins1.equals(coins2)) {
                        c2.setText("0");
                    }else{
                        if (coins2.equals("BTC")){
                            youraddress = addressb;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("ETH")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("BNB")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("PAX")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("OMG")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("BAT")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("BCH")){
                            youraddress = bch_address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("USDC")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("MKR")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }else if (coins2.equals("TUSD")){
                            youraddress = address;
                            //Get rate
                            if(c1.equals("0") || (c2.equals("0")))
                            {
                                //nothing happen
                            }else{
                                get_rate();
                                get_rate_actual();
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calculate the fee and balance then send amount
                try {
                    if (coins1.equals("BTC") || coins1.equals("BCH")){
                        double toamount = Double.parseDouble(c1.getText().toString());
                        //Calculate with fees
                        Double get = Double.parseDouble(btcfee.getText().toString());
                        Double total= get / 100000000;
                        Double d = total_balance - total;
                        if (toamount > d){
                            Message dialog = new Message();
                            dialog.showDialog(Exchange.this, "Your fund is not enough");
                        }else{
                            String r = rate.getText().toString();
                            if (r.equals("0")){
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Not available.");
                            }else{
                                get_minimum();
                            }
                        }
                    } else {
                        if (coins1.equals("ETH")) {
                            double toamount = Double.parseDouble(c1.getText().toString());
                            //Calculate with fees
                            Double get = Double.parseDouble(ethfee);
                            Double total = get / 1000000000;
                            Double totals = total * 210000;
                            Double d = total_balance - totals;
                            if (toamount > d) {
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Your fund is not enough");
                            } else {
                                String r = rate.getText().toString();
                                if (r.equals("0")){
                                    Message dialog = new Message();
                                    dialog.showDialog(Exchange.this, "Not available.");
                                }else{
                                    get_minimum();
                                }
                            }
                        }else{
                            Double get = Double.parseDouble(ethfee);
                            Double total = get / 1000000000;
                            Double totals = total * 210000;
                            if (ethbals > totals ){
                                String r = rate.getText().toString();
                                if (r.equals("0")){
                                    Message dialog = new Message();
                                    dialog.showDialog(Exchange.this, "Not available.");
                                }else{
                                    get_minimum();
                                }
                            }else{
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Deposit ethereum(ETH) to cover this ERC20 token transaction");
                            }
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Bottom menu
        maxs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins1.equals("BTC")){
                    Double get = Double.parseDouble(btcfee.getText().toString());
                    Double total= get / 100000000;

                    double mybals =total_balance;

                    Double d = mybals - total;
                    String eths = String.format("%.8f", d);
                    c1.setText(eths);
                }else{
                    if (coins1.equals("ETH")){
                        Double get = Double.parseDouble(ethfee);
                        Double total= get / 1000000000;
                        Double totals= total * 210000;
                        //String eths = String.format("%.8f", totals);

                        double mybals = ethbals;
                        Double d = mybals - totals;
                        String eths = String.format("%.8f", d);
                        c1.setText(String.valueOf(eths));
                    }else{
                        c1.setText(total_balance.toString());
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exchange.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Exchange.this, History.class);
                i.putExtra("coin", coins1);
                startActivity(i);
                finish();
            }
        });
        //text change listener
        c1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(c1.equals("0") || (c2.equals("0")))
                {
                    //nothing happen
                }else{
                    get_rate();
                    get_rate_actual();
                }
            }
        });
    }

    //Get the actual rate
    private void get_rate(){
        JSONObject request = new JSONObject();
        try {
            request.put("coin1", coins1);
            request.put("coin2", coins2);
            request.put("amount", c1.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            if (res.contains("error")){
                                c2.setText("0");
                            }else {
                                String[] parts = res.split(",");
                                String part1 = parts[2]; // "price_usd": "0.7042710636"
                                String[] splitterString = part1.split("\"");
                                String result = splitterString[3];
                                //c2.setText(result);
                                double bals = Double.parseDouble(result);
                                //double val = bals * 0.005;
                                //double fres = bals - val;
                                String feess = String.format("%.6f", bals);
                                c2.setText(String.valueOf(feess));
                                //String feesss = String.format("%.6f", val);
                                //fee.setText(String.valueOf(feesss) + " "+ coins2);
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

    //Get the rate
    private void get_rate_actual(){
        JSONObject request = new JSONObject();
        try {
            request.put("coin1", coins1);
            request.put("coin2", coins2);
            request.put("amount", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            if (res.contains("error")){
                                rate.setText("0");
                            }else {
                                String[] parts = res.split(",");
                                String part1 = parts[2]; // "price_usd": "0.7042710636"
                                String[] splitterString = part1.split("\"");
                                String result = splitterString[3];
                                double bals = Double.parseDouble(result);
                                String feess = String.format("%.6f", bals);
                                rate.setText("1 "+coins1+ " = " +String.valueOf(feess)+ " "+ coins2);

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

    private void getBTC()
    {
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, "https://blockchain.info/balance?active="+addressb, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //eth_balance.setText(response.get("balance").toString());
                            String res = response.toString();
                            String[] parts = res.split(",");
                            String part1 = parts[0]; // "price_usd": "0.7042710636"
                            String[] part2 = part1.split(":");
                            String part3 = part2[2];
                            String remove=part3.replace(" ", "");
                            Double results = Double.parseDouble(remove);
                            Double total= results/100000000;
                            String btcs = String.format("%.8f", total);
                            //bal = total.toString();
                            bal.setText(btcs + " BTC");
                            total_balance = Double.parseDouble(btcs);
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
    //This is the balance for coins
    private void get_ETH_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url1, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" ETH");

                            total_balance = Double.parseDouble(response.get("balance").toString());
                            ethbals = Double.parseDouble(response.get("balance").toString());
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
    //This is the balance for coins
    private void get_ETH_balance2(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url1, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ethbals = Double.parseDouble(response.get("balance").toString());
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

    private void get_BNB_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0xB8c77482e45F1F44dE1745F52C74426C631bDD52");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" BNB");

                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_PAX_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x8E870D67F660D95d5be530380D0eC0bd388289E1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" PAX");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_OMG_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" OMG");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_BCH_balance(){
        final okhttp3.Request request = new okhttp3.Request.Builder().url("https://bcc.zupago.pe/api/addr/"+bch_address+"/Balance").build();
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
                            Double result = Double.parseDouble(res);
                            Double total= result/100000000;
                            String btcs = String.format("%.8f", total);
                            bal.setText(btcs+" BCH");
                            total_balance = Double.parseDouble(btcs);

                        } catch (IOException e) {
                        }
                    }
                });
            }
        });
    }

    private void get_MKR_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" MKR");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_AION_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x4CEdA7906a5Ed2179785Cd3A40A69ee8bc99C466");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" AION");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_AE_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x5ca9a71b1d01849c0a95490cc00559717fcf0d1d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" AE");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_BAT_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x0d8775f648430679a709e98d2b0cb6250d2887ef");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" BAT");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_REP_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x1985365e9f78359a9B6AD760e32412f4a445E862");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" REP");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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

    private void get_USDC_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48");
            request.put("coin", "USDC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            Double rates = res / 1000000;
                            String format = String.format("%.6f",rates);
                            bal.setText(format+" USDC");
                            total_balance = rates;
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
    private void get_TUSD_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("contract", "0x0000000000085d4780B73119b644AE5ecd22b376");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            String format = String.format("%.8f",res);
                            bal.setText(format+" TUSD");
                            total_balance = Double.parseDouble(response.get("balance").toString());
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
    //Get the minimum
    private void get_minimum(){
        JSONObject request = new JSONObject();
        try {
            request.put("coin1", coins1);
            request.put("coin2", coins2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url4, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            if (res.contains("error")){
                                rate.setText("0");
                            }else {
                                String[] parts = res.split(",");
                                String part1 = parts[2]; // "price_usd": "0.7042710636"
                                String[] splitterString = part1.split("\"");
                                String result = splitterString[3];

                                Double amount = Double.parseDouble(result);
                                Double results = Double.parseDouble(c1.getText().toString());

                                if(results >= amount){
                                    if (results <= total_balance){
                                        //Create transaction
                                        create_transaction();
                                    }else{
                                        Message dialog = new Message();
                                        dialog.showDialog(Exchange.this, "Your fund is not enough");
                                    }
                                }else{
                                    Message dialog = new Message();
                                    dialog.showDialog(Exchange.this, "Minimum amount is "+ amount);
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

    //Create Transaction
    private void create_transaction(){
        JSONObject request = new JSONObject();
        try {
            request.put("coin1", coins1);
            request.put("coin2", coins2);
            request.put("amount", c1.getText().toString());
            request.put("address", youraddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url3, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            if (res.contains("error")){
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Invalid Transaction.");
                            }else {
                                String[] parts = res.split(",");
                                String part1 = parts[2]; // "result": {"id": "1fefuster5r0sbf2"
                                String[] part2 = part1.split("\"");
                                TransID = part2 [5];

                                String[] partss = res.split(",");
                                String parts1 = partss[12]; // "payinAddress": "0x7fd1a0c905fc54892a14a908b60d12570ba408e3"
                                String[] parts2 = parts1.split("\"");
                                String payinAddress = parts2 [3];
                                String amt = c1.getText().toString();
                                if (coins1.equals("BTC")){
                                    send_transaction_btc(payinAddress, amt, total_balance.toString());
                                }else if (coins1.equals("ETH")){
                                    send_transaction_eth(payinAddress, amt);
                                }else
                                {
                                    if (coins1.equals("USDC")){
                                        send_transaction_usdc(payinAddress, amt);
                                    }else{
                                        send_transaction_erc(payinAddress, amt);
                                    }
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

    private void displayLoader(String message) {
        pDialog = new ProgressDialog(Exchange.this);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    //Save Payin
    private void save_payin_btc( String addresss, String ID, String ex, String from, String to, String hash){
        JSONObject request = new JSONObject();
        try {
            request.put("address", addresss);
            request.put("id", ID);
            request.put("ex", ex);
            request.put("from", from);
            request.put("to", to);
            request.put("hash", hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url5, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //String res = response.get("data").toString();
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
    private void save_payin_eth(String payaddress){
        JSONObject request = new JSONObject();
        try {
            request.put("address", address);
            request.put("payin", payaddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url5, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //String res = response.get("data").toString();
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

    private void getBTC_fees()
    {
        JSONObject request = new JSONObject();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, api_fees, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            //Toast.makeText(getApplicationContext(),res, Toast.LENGTH_SHORT).show();
                            Integer results = Integer.parseInt(res);
                            //Integer total = results / 1000000000;
                            btcfee.setText(results.toString());
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

    private void getETH_fees()
    {
        final Request request = new Request.Builder().url("https://www.etherchain.org/api/gasPriceOracle").build();
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
                            String[] part2 = part1.split("\"");
                            String part3 = part2[3];
                            ethfee = part3;
                            btcfee.setText(part3);
                        } catch (IOException e){
                        }
                    }
                });
            }
        });
    }

    //Send Transaction BTC
    private void send_transaction_btc(final String payinaddress, String famt, String  balance){
        displayLoader("Processing.. Please wait....");
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("address", addressb);
            request.put("algo", keysb);
            request.put("toaddress", payinaddress);
            request.put("amount", famt);
            request.put("fees", btcfee.getText().toString());
            request.put("balance", balance);

        } catch (JSONException e) {
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_urlbtc, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //pDialog.dismiss();
                        try {
                            if (response.getString(KEY_STATUS) == "false") {
                                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                                if (response.toString().contains("hash")){
                                    //Save the transaction

                                    String[] parts = response.toString().split(",");
                                    String part1 = parts[3]; // "price_usd": "0.7042710636"
                                    String[] part2 = part1.split("\"");
                                    String part3 = part2[3];
                                    rawTransaction = part3;

                                    save_payin_btc(addressb,TransID,coins1+coins2,c1.getText().toString(),c2.getText().toString(),rawTransaction);

                                    Intent i = new Intent(getApplicationContext(), Status.class);
                                    i.putExtra("id", TransID);
                                    i.putExtra("coin1", coins1);
                                    i.putExtra("coin2", coins2);
                                    i.putExtra("from", c1.getText().toString());
                                    i.putExtra("to", c2.getText().toString());
                                    i.putExtra("hash", rawTransaction);
                                    //i.putExtra("payin", payinaddress);
                                    i.putExtra("status", "process");
                                    startActivity(i);
                                    finish();
                                }else{
                                    Message dialog = new Message();
                                    dialog.showDialog(Exchange.this, "Transaction Failed");
                                }
                            }else if(response.getString(KEY_STATUS) == "true"){

                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Transaction Failed");
                            }
                            else{
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Transaction Failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Message dialog = new Message();
                        dialog.showDialog(Exchange.this, "Transaction Failed");
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void send_transaction_eth(final String payinaddress, String famt) //ETH send
    {
        displayLoader("Processing.. Please wait....");
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("contract", "");
            request.put("address", address);
            request.put("algo", keys);
            request.put("toaddress", payinaddress);
            request.put("amount", famt);
            request.put("fees", btcfee.getText().toString());

        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_urleth, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            String res = response.toString();
                            if (res.contains("true")) {
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Transaction Failed");
                            }else {
                                String IDs = response.getString(KEY_MESSAGE);
                                save_payin_btc(address,TransID,coins1+coins2,c1.getText().toString(),c2.getText().toString(),IDs);

                                Intent i = new Intent(getApplicationContext(), Status.class);
                                i.putExtra("id", TransID);
                                i.putExtra("coin1", coins1);
                                i.putExtra("coin2", coins2);
                                i.putExtra("from", c1.getText().toString());
                                i.putExtra("to", c2.getText().toString());
                                i.putExtra("hash", IDs);
                                //i.putExtra("payin", payinaddress);
                                i.putExtra("status", "process");
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        Message dialog = new Message();
                        dialog.showDialog(Exchange.this, "Transaction Failed");

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void send_transaction_erc(final String payinaddress, String famt){//ERC Send
        displayLoader("Processing.. Please wait....");
        JSONObject request = new JSONObject();
        try {
            request.put("contract", contract);
            request.put("address", address);
            request.put("algo", keys);
            request.put("toaddress", payinaddress);
            request.put("amount", famt);
            request.put("fees", btcfee.getText().toString());
            request.put("coin", coins1);

        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_urlerc, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                            String res = response.toString();
                            if (res.contains("true")) {
                                Message dialog = new Message();
                                dialog.showDialog(Exchange.this, "Transaction Failed");
                            }else {
                                String IDs = response.getString(KEY_MESSAGE);
                                //Go back to dashboard
                                save_payin_btc(address,TransID,coins1+coins2,c1.getText().toString(),c2.getText().toString(),IDs);

                                Intent i = new Intent(getApplicationContext(), Status.class);
                                i.putExtra("id", TransID);
                                i.putExtra("coin1", coins1);
                                i.putExtra("coin2", coins2);
                                i.putExtra("from", c1.getText().toString());
                                i.putExtra("to", c2.getText().toString());
                                i.putExtra("hash", IDs);
                                //i.putExtra("payin", payinaddress);
                                i.putExtra("status", "process");
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        Toast.makeText(getApplicationContext(),
                                "Connection was interrupted: please try to resend", Toast.LENGTH_SHORT).show();

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void send_transaction_usdc(final String payinaddress, String famt){//ERC Send
        {
            displayLoader("Processing.. Please wait....");
            JSONObject request = new JSONObject();
            try {
                //Populate the request parameters
                request.put("contract", contract);
                request.put("address", address);
                request.put("algo", keys);
                request.put("toaddress", payinaddress);
                request.put("amount", famt);
                request.put("fees", btcfee.getText().toString());
                request.put("coin", coins1);

            } catch (JSONException e) {
                //e.printStackTrace();
            }
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                    (com.android.volley.Request.Method.POST, api_url6, request, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pDialog.dismiss();
                            try {
                                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                                String res = response.toString();
                                if (res.contains("true")) {
                                    Message dialog = new Message();
                                    dialog.showDialog(Exchange.this, "Transaction Failed");
                                }else {
                                    String IDs = response.getString("hash");
                                    //Go back to dashboard
                                    save_payin_btc(address,TransID,coins1+coins2,c1.getText().toString(),c2.getText().toString(),IDs);

                                    Intent i = new Intent(getApplicationContext(), Status.class);
                                    i.putExtra("id", TransID);
                                    i.putExtra("coin1", coins1);
                                    i.putExtra("coin2", coins2);
                                    i.putExtra("from", c1.getText().toString());
                                    i.putExtra("to", c2.getText().toString());
                                    i.putExtra("hash", IDs);
                                    //i.putExtra("payin", payinaddress);
                                    i.putExtra("status", "process");
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();

                            Toast.makeText(getApplicationContext(),
                                    "Connection was interrupted: please try to resend", Toast.LENGTH_SHORT).show();

                        }
                    });
            MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        }
    }
}
