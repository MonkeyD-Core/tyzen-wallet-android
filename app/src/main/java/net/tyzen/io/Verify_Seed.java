package net.tyzen.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class Verify_Seed extends AppCompatActivity {

    Button next;
    EditText word;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;
    private String BTC_address;
    private String BTC_secret;
    private String ETH_address;
    private String ETH_secret;
    private String BCH_address;
    private String BCH_secret;
    private String email;
    private String pass;
    private  String username;
    private String seeds;
    private String api_url = "http://"+conf.url()+":8080/v1/insert";
    private String api_id = "http://"+conf.url()+":8080/v1/select_id";
    private String api_insertid = "http://"+conf.url()+":8080/v1/insert_id";
    private String emails = "http://"+conf.url() +"/email_reg.php";
    private SessionHandler session;

    private String dataSeeds;
    private String code;
    private String user_id;
    private String pwd;

    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button ten;
    private Button eleven;
    private Button twelve;
    private Button clear;

    ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_seed);
        session = new SessionHandler(getApplicationContext());

        next = (Button) findViewById(R.id.submit);
        word = (EditText) findViewById(R.id.seed);

        BTC_address=getIntent().getStringExtra("btc_address");
        BTC_secret=getIntent().getStringExtra("btc_secret");
        ETH_address=getIntent().getStringExtra("eth_address");
        ETH_secret=getIntent().getStringExtra("eth_secret");
        BCH_address=getIntent().getStringExtra("bch_address");
        BCH_secret=getIntent().getStringExtra("bch_secret");
        seeds=getIntent().getStringExtra("word_seed");
        email=getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        username= getIntent().getStringExtra("username");
        dataSeeds = seeds;
        pwd = pass;
        //Toast.makeText(Verify_Seed.this, email, Toast.LENGTH_LONG).show();

        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        ten = (Button) findViewById(R.id.ten);
        eleven = (Button) findViewById(R.id.eleven);
        twelve = (Button) findViewById(R.id.twelve);
        clear = (Button) findViewById(R.id.clear);
        back = (ImageView) findViewById(R.id.back);

        code = "LYTE"+getSaltString();

        if (dataSeeds.length()>0){
            String[] parts = dataSeeds.split(" ");
            String part1 = parts[8];
            one.setText(part1);

            String part2 = parts[6];
            two.setText(part2);

            String part3 = parts[9];
            three.setText(part3);

            String part4 = parts[2];
            four.setText(part4);

            String part5 = parts[7];
            five.setText(part5);

            String part6 = parts[3];
            six.setText(part6);

            String part7 = parts[10];
            seven.setText(part7);

            String part8 = parts[0];
            eight.setText(part8);

            String part9 = parts[1];
            nine.setText(part9);

            String part10 = parts[4];
            ten.setText(part10);

            String part11 = parts[5];
            eleven.setText(part11);

            String part12 = parts[11];
            twelve.setText(part12);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String words = word.getText().toString();
                String Swords = words.replace(" ","");
                String match = dataSeeds.replace(" ","");

                if (words.equals("")){
                    Message dialog = new Message();
                    dialog.showDialog(Verify_Seed.this, "Please verify your 12 word seed");
                    word.setText("");
                }else {
                    if (Swords.equals(match)){
                        Insert_data();
                    }else{
                        Message dialog = new Message();
                        dialog.showDialog(Verify_Seed.this, "12 Word passphrase did not match.");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Verify_Seed.this, Word_Seed.class);
                startActivity(i);
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[8];
                    word.append(" "+part1);
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[6];
                    word.append(" "+part1);
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[9];
                    word.append(" "+part1);
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[2];
                    word.append(" "+part1);
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[7];
                    word.append(" "+part1);
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[3];
                    word.append(" "+part1);
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[10];
                    word.append(" "+part1);
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[0];
                    word.append(" "+part1);
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[1];
                    word.append(" "+part1);
                }
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[4];
                    word.append(" "+part1);
                }
            }
        });
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[5];
                    word.append(" "+part1);
                }
            }
        });
        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                    String[] parts = dataSeeds.split(" ");
                    String part1 = parts[11];
                    word.append(" "+part1);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSeeds.length()>0){
                   word.setText("");
              }

            }
        });
    }

    //Random
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    //Save the data
    private void Insert_data()
    {
        JSONObject request = new JSONObject();
        try {

            request.put("btcaddress", BTC_address);
            request.put("btcsecret", BTC_secret);
            request.put("ethaddress", ETH_address);
            request.put("ethsecret", ETH_secret);
            request.put("bchaddress", BCH_address);
            request.put("bchsecret", BCH_secret);
            request.put("word", seeds);
            request.put("email", email);
            request.put("password", pass);
            request.put("username", username);
            request.put("code", code);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resutlt = response.getString("message").toString();
                            if (resutlt.equals("success")){
                                select_id(username);
                                Intent i = new Intent(Verify_Seed.this, PIN_Create1.class);
                                i.putExtra("btc_address",BTC_address);
                                i.putExtra("btc_secret",BTC_secret);
                                i.putExtra("eth_address", ETH_address);
                                i.putExtra("eth_secret",ETH_secret);
                                i.putExtra("bch_address",BCH_address);
                                i.putExtra("bch_secret",BCH_secret);
                                i.putExtra("word_seed",seeds);
                                i.putExtra("email",email);
                                i.putExtra("pass",pass);
                                i.putExtra("username",username);

                                //email(email);
                                startActivity(i);
                                finish();
                            }else{
                                Message dialog = new Message();
                                dialog.showDialog(Verify_Seed.this, "Unexpected error");
                                Intent i = new Intent(Verify_Seed.this, Welcome.class);
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void email(String email){
        JSONObject request = new JSONObject();
        try {
            request.put("email", email);
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
                                    //Message dialog = new Message();
                                    //dialog.showDialog(Verify_Seed.this, sp2);
                                    user_id = sp2;
                                    insert_id(user_id);
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

    private void insert_id(String id){
        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
            request.put("userid", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_insertid, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resutlt = response.getString("message").toString();
                            if (resutlt.equals("success")){

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
