package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Signin extends AppCompatActivity {
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    LinearLayout signUP;
    LinearLayout forgot;
    Button signIn;
    EditText email;
    EditText password;
    String emails;
    String passwords;
    private SessionHandler session;
    private static final String KEY_EMPTY = "";
    private ProgressDialog pDialog;
    private String login_url = "http://"+ conf.url() +":8080/v1/auth";
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        //pin = user.getOTP();

        if(session.isLoggedIn()){
          loadDashboard();
          finish();
        }

        setContentView(R.layout.activity_signin);

        signUP = (LinearLayout) findViewById(R.id.signup);
        forgot = (LinearLayout) findViewById(R.id.forgot);
        signIn = (Button) findViewById(R.id.signin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        forgot.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent i = new Intent(Signin.this, Forgot.class);
                startActivity(i);

            }
        });

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signin.this, Signup.class);
                startActivity(i);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emails = email.getText().toString().toLowerCase().trim();
                passwords = password.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(emails)){
            email.setError("Username cannot be empty");
            email.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(passwords)){
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(Signin.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("email", emails);
            request.put("password", passwords);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            Integer res = response.getInt("data");
                            if (res > 0) {
                                JSONArray dataArray = response.getJSONArray("user");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    //String btc_address = object.getString("btc_address");
                                    //String btc_secret = object.getString("btc_key");
                                    //String eth_address = object.getString("eth_address");
                                    //String eth_secret = object.getString("eth_key");
                                    //String bch_address = object.getString("bch_address");
                                    //String bch_secret = object.getString("bch_secret");
                                    String word = object.getString("word");
                                    String email = object.getString("email");
                                    String user = object.getString("username");
                                    String pass = object.getString("password");

                                    //Toast.makeText(Signin.this, response.toString(), Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(getApplicationContext(), PIN_Create1.class);
                                    //in.putExtra("btc_address",btc_address);
                                    //in.putExtra("btc_secret",btc_secret);
                                    //in.putExtra("eth_address", eth_address);
                                    //in.putExtra("eth_secret",eth_secret);
                                    //in.putExtra("bch_address","");
                                    //in.putExtra("bch_secret","");
                                    in.putExtra("word_seed",word);
                                    in.putExtra("email",email);
                                    in.putExtra("pass",pass);
                                    in.putExtra("username",user);
                                    //session.loginUser(word, email,eth_address,eth_secret,btc_address,btc_secret,"1234", bch_address, bch_secret);
                                    startActivity(in);
                                    finish();
                                }
                            }
                            else{
                                Message alert = new Message();
                                alert.showDialog(Signin.this, "Username and Password is wrong!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Message alert = new Message();
                        alert.showDialog(Signin.this, error.getMessage());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), PIN_Verify.class);
        startActivity(i);
        finish();

    }


}