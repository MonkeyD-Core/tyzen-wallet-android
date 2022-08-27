package net.tyzen.io;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bch_send extends AppCompatActivity {
    private SessionHandler session;
    private OkHttpClient client;
    private String address;
    private String addressb;
    private EditText toaddress;
    private EditText amount;
    private String keys;
    private String keysb;
    private String balance;
    private TextView result;
    private ProgressDialog pDialog;
    private String contract;
    private String to;
    private String amt;
    private String famt;
    public Integer fee;
    private String rawTransaction;
    private TextView gas;
    private TextView usdA;
    private boolean mStopHandler = false;
    Button send;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();

    private String api_url = "http://"+conf.url()+":8085/v1/transaction/bch";
    private String api_url3 = "http://"+ conf.url()+":8082/v1/coin";
    private String api_fees = "http://"+ conf.url() +":8081/v1/fees";

    private String otp_url = "http://"+ conf.url() +":8081/v1/otp";
    private String emails = "http://"+ conf.url() +"/email.php";
    private String url="http://"+ conf.url() +":8080/v1/otpcode";

    private static final String KEY_STATUS = "error";
    private static final String KEY_MESSAGE = "data";
    private TextView usd;
    private String sbtc_rate;
    private TextView max;
    ImageButton scan;
    private IntentIntegrator qrScan;
    ImageView back;
    private DataHandler enc;

    private String otp;
    private String femail;
    private String otp_status;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bch_send);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        femail = user.getFullName();
        client = new OkHttpClient();
        address = enc.decrypt(key,initVector,user.getKeys());
        keys = enc.decrypt(key,initVector,user.getSecrets());
        addressb = enc.decrypt(key,initVector,user.getCKeys());
        keysb = enc.decrypt(key,initVector,user.getCSecrets());

        send = (Button) findViewById(R.id.btn_send);
        result = (TextView) findViewById(R.id.BTC_Balance);
        toaddress = (EditText) findViewById(R.id.etAddress);
        amount = (EditText) findViewById(R.id.etAmount);
        gas = (TextView) findViewById(R.id.fee);
        usd = (TextView)findViewById(R.id.AFCASH_usd);
        usdA = (TextView)findViewById(R.id.usd);
        scan=(ImageButton) findViewById(R.id.btn_scan);
        max = (TextView)findViewById(R.id.max);
        back = (ImageView) findViewById(R.id.back);

        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //intializing scan object
        //toaddress.setText(keysb);
        qrScan = new IntentIntegrator(this);

        contract =getIntent().getStringExtra("contract");

        getOTP();
        get_status_otp();

        getBTC();
        getBTC_fees();
        getUSD_BTC();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    double rat = Double.parseDouble(sbtc_rate);
                    double bals = Double.parseDouble(result.getText().toString());
                    bals= rat * bals;
                    String eths = String.format("%.2f", bals);
                    usd.setText("$"+String.valueOf(eths));
                } catch (Exception e){

                }
            }
        }, 3000);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Double get = Double.parseDouble(gas.getText().toString());
                    Double total= get / 100000000;

                    double mybals = Double.parseDouble(result.getText().toString());

                    Double d = mybals - total;
                    if (d.toString().contains("-")){
                        amount.setText("0.00");
                    }else{
                        String eths = String.format("%.8f", d);
                        amount.setText(eths);
                    }

                }catch (Exception e){

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(bch_send.this, Transaction_BCH.class);
                startActivity(i);
            }
        });

        //Amount keypress watcher
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (!mStopHandler) {
                    new Handler().postDelayed(this, 1000);

                    try {
                        double toamount = Double.parseDouble(amount.getText().toString());
                        double mybals = Double.parseDouble(result.getText().toString());

                        double rat = Double.parseDouble(sbtc_rate);
                        double bals = Double.parseDouble(amount.getText().toString());
                        bals= rat * bals;
                        String eths = String.format("%.2f", bals);
                        usdA.setText("$"+String.valueOf(eths));

                        //limit the balance here
                        if (toamount > mybals){
                            amount.setError("Insufficient funds.");
                        }
                    } catch (Exception e){
                        usdA.setText("0.00");
                    }

                }
            }
        };
        new Handler().post(runnable);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                to = toaddress.getText().toString();
                amt = amount.getText().toString();


                if (to.isEmpty() || to.length() == 0 || to.equals("") || to == null || amt.isEmpty() || amt.length() == 0 || amt.equals("") || amt == null) {
                    amount.setError("This field is required.");
                    toaddress.setError("This field is required.");
                }
                else {

                    double toamount = Double.parseDouble(amount.getText().toString());
                    double mybals = Double.parseDouble(result.getText().toString());

                    //limit the balance here
                    if (toamount > mybals){
                        amount.setError("Insufficient funds.");
                    }else{
                        Double results = Double.parseDouble(amt);
                        famt=amt;
                        if (otp_status.equals("1")){
                            //Start Here
                            //Add the OTP here
                            email("ltbesitulo@gmail.com");
                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(bch_send.this);
                            View promptsView = li.inflate(R.layout.activity_otp, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bch_send.this);
                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);
                            final EditText userInput = (EditText) promptsView.findViewById(R.id.email);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    String OTPs = userInput.getText().toString();
                                                    if (otp.equals(OTPs)){
                                                        send_transaction();
                                                    }else{
                                                        dialog.cancel();
                                                        Message alert = new Message();
                                                        alert.showDialog(bch_send.this, "OTP is wrong.");
                                                    }
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            //End here
                        }else{
                            send_transaction();
                        }
                    }

                }
                //Toast.makeText(getApplicationContext(),
                //contract+" address to"+ to +" amount "+ amt.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(bch_send.this);
        pDialog.setMessage("Sending.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void displayLoader2() {
        pDialog = new ProgressDialog(bch_send.this);
        pDialog.setMessage("Broadcasting.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
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
                            balance = total.toString();
                            result.setText(btcs);
                        } catch (IOException ioe) {
                            result.setText("0.00");
                        }
                    }
                });
            }
        });
    }

    private void send_transaction()
    {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("address", addressb);
            request.put("algo", keysb);
            request.put("toaddress", to);
            request.put("amount", famt);
            request.put("fees", fee.toString());
            request.put("balance", balance);
            //Toast.makeText(btc_send.this, "Address: " + addressb + " KEY: "+ keysb+ " amount: "+ famt+ " to: " + to+ " fee: "+fee + " Balance: " + balance, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getString(KEY_STATUS) == "false") {
                                if (response.toString().contains("hash")){

                                    String[] parts = response.toString().split("\"");
                                    String part1 = parts[3]; // "price_usd": "0.7042710636"
                                    //String[] part2 = part1.split("\"");
                                    //String part3 = part1[3];

                                    rawTransaction = part1;
                                    Intent i = new Intent(getApplicationContext(), blockchain.class);
                                    i.putExtra("hash", rawTransaction);
                                    i.putExtra("coin", "BCH");
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Transaction failed.", Toast.LENGTH_SHORT).show();
                                }
                            }else if(response.getString(KEY_STATUS) == "true"){

                                Toast.makeText(getApplicationContext(),"Transaction failed.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Transaction failed.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),"Transaction failed.", Toast.LENGTH_SHORT).show();

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

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
                            fee = results;
                            gas.setText(fee.toString());
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
    //This is the qr scanner
    //QR code scanner class
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    String datas = result.getContents();
                    if (datas.contains(":")){
                        String[] parts = datas.split(":");
                        String part1 = parts[1];
                        toaddress.setText(part1);
                    }else{
                        toaddress.setText(result.getContents());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getOTP()
    {
        JSONObject request = new JSONObject();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, otp_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            otp = res;
                            //Toast.makeText(btc_send.this, otp, Toast.LENGTH_LONG).show();
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

    private void email(String emailss){
        JSONObject request = new JSONObject();
        try {
            request.put("totp", otp);
            request.put("email", emailss);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, emails, request, new com.android.volley.Response.Listener<JSONObject>() {
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

    private void get_status_otp(){
        JSONObject request = new JSONObject();
        try {
            request.put("email", femail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(Settings.this,response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            // Extract data from json and store into ArrayList as class objects
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject json_data = dataArray.getJSONObject(i);
                                String otps = json_data.getString("otpcode");

                                otp_status=otps;
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
}


