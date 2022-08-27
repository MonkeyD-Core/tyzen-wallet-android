package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

import okhttp3.OkHttpClient;

public class Withdraw_bank extends AppCompatActivity {
    private OkHttpClient client;
    private SessionHandler session;
    private ProgressDialog pDialog;
    ImageView back;
    EditText bank_name, bank_account, bank_iban, bank_bic, bank_address, bxc_amount;
    TextView balance, btc_receive;
    Button btn_send;

    private static final String KEY_EMPTY = "";
    private String email, user_id, fees, bxc_price, btc_price, bxc_balance, final_fee, final_btc_amt;
    private Config conf;
    private String api_id = "http://"+conf.url()+":8080/v1/select_id";
    private String api_btcp = "http://"+conf.url()+":8080/v1/price_btc";
    private String api = "http://"+conf.url()+":8080/v1/bxc_price_address_fees";
    private String api_send = "http://"+conf.url()+":8080/v1/insert_withdraw_bank";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_withdraw_bank);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        email = user.getFullName();

        back = (ImageView) findViewById(R.id.back);
//        bank_name =(EditText) findViewById(R.id.bank_name);
//        bank_account =(EditText) findViewById(R.id.bank_account);
//        bank_iban =(EditText) findViewById(R.id.bank_iban);
//        bank_bic =(EditText) findViewById(R.id.bank_bic);
//        bank_address =(EditText) findViewById(R.id.bank_address);
//        bxc_amount =(EditText) findViewById(R.id.bxc_amount);
//        balance = (TextView) findViewById(R.id.balance);
//        btc_receive = (TextView) findViewById(R.id.btc_receive);
//        btn_send = (Button) findViewById(R.id.btn_send);

        Locale locale = new Locale("eng");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //intializing scan object

        get_bxc_balance();
        select_id(email);
        getUSD_BTC();
        getBXC_price_fees();

        bxc_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    double amt = Double.parseDouble(bxc_amount.getText().toString());
                    double btc_rate = 1.0;
                    double fee = Double.parseDouble(fees);
                    double bxcp = Double.parseDouble(bxc_price);

                    //calculate here
                    if (amt>=1){
                        double fe = amt * fee;
                        double famt = amt - fe;
                        double fp = famt * bxcp;

                        final_fee = String.valueOf(famt);
                        final_btc_amt = String.valueOf(fp);
                        btc_receive.setText(String.valueOf("Receive: "+fp + " USD"));
                    }else{
                        btc_receive.setText("0.0");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdraw_bank.this, Withdrawal.class);
                startActivity(i);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bname = bank_name.getText().toString();
                String baccount = bank_account.getText().toString();
                String biban = bank_iban.getText().toString();
                String bbic = bank_bic.getText().toString();
                String baddress = bank_address.getText().toString();
                String bxc_a = bxc_amount.getText().toString();

                if (KEY_EMPTY.equals(bname)){
                    bank_name.setError("Bank name cannot be empty!");
                }else if (KEY_EMPTY.equals(baccount)){
                    bank_account.setError("Bank account cannot be empty!");
                }else if (KEY_EMPTY.equals(biban)){
                    bank_iban.setError("Bank IBAN cannot be empty!");
                }else if (KEY_EMPTY.equals(bbic)){
                    bank_bic.setError("Bank BIC cannot be empty!");
                }else if (KEY_EMPTY.equals(baddress)){
                    bank_address.setError("Bank address cannot be empty!");
                }else if (KEY_EMPTY.equals(bxc_a)){
                    bxc_amount.setError("TZN amount cannot be empty!");
                }
                else{
                    double bal = Double.parseDouble(bxc_balance);
                    double amt = Double.parseDouble(bxc_amount.getText().toString());

                    if (amt > bal){
                        bxc_amount.setError("Insufficient fund!");
                    }//else if (amt<1){
                        //bxc_amount.setError("Minimum amount is 100 TZN");
                    //}
                    else{
                        send();
                    }
                }

            }
        });

    }

    private void get_bxc_balance(){
        JSONObject request = new JSONObject();
        try {
            request.put("username", email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, conf.api()+"get_balance", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double res = Double.parseDouble(response.get("balance").toString());
                            balance.setText("TZN Balance : "+ res.toString());
                            bxc_balance = res.toString();
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
            request.put("coin", "bitcoin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_btcp, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resutlt = response.getString("message").toString();
                            if (resutlt.equals("success")){
                                JSONObject jsonObject=new JSONObject(response.toString());
                                JSONArray array=jsonObject.getJSONArray("data");
                                for (int i=0; i<array.length(); i++){
                                    JSONObject ob=array.getJSONObject(i);
                                    String sp2 = ob.getString("last_price");
                                    btc_price = sp2;
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

    private void getBXC_price_fees() {
        JSONObject request = new JSONObject();
        try {
            request.put("coin", "bitcoin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("data").toString();
                            fees = res.split(",")[2];
                            bxc_price = res.split(",")[0];
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

    private void select_id(String username){
        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_id, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resutlt = response.getString("message").toString();
                            if (resutlt.equals("success")){
                                JSONObject jsonObject=new JSONObject(response.toString());
                                JSONArray array=jsonObject.getJSONArray("data");
                                for (int i=0; i<array.length(); i++){
                                    JSONObject ob=array.getJSONObject(i);
                                    String sp2 = ob.getString("user_id");
                                    user_id = sp2;

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

    private void send(){
        JSONObject request = new JSONObject();
        try {
            String trans = getSaltString();

            request.put("userid", user_id);
            request.put("trans_id", trans);
            request.put("bname", bank_name.getText().toString());
            request.put("baccount", bank_account.getText().toString());
            request.put("biban", bank_iban.getText().toString());
            request.put("bbic", bank_bic.getText().toString());
            request.put("baddress", bank_address.getText().toString());
            request.put("bxc_amount", bxc_amount.getText().toString());
            request.put("fees", final_fee);
            request.put("bank_amount", final_btc_amt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_send, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resutlt = response.getString("message").toString();
                            if (resutlt.equals("success")){
                                Message alert = new Message();
                                alert.showDialog(Withdraw_bank.this, "Transaction sent successfully");
                                //Intent i = new Intent(Withdraw_btc.this, Withdrawal.class);
                                //startActivity(i);
                            }else{
                                Message alert = new Message();
                                alert.showDialog(Withdraw_bank.this, "Transaction failed, please check your transaction.");
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
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}



