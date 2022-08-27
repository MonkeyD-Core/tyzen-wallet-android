package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DATA = "data";
    private static final String KEY_SECRET = "key";
    private static final String KEY_EMPTY = "";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_KEY = "secret";
    private static final String KEY_ADDRESSB = "addressb";
    private static final String KEY_KEYB = "secretb";
    private EditText email;
    private EditText password;
    private EditText cpassword;
    private EditText name;
    private String emails;
    private String cpasswords;
    private String passwords;
    private String names;
    private String address;
    private String secret;
    private String BTC_address;
    private String BTC_secret;
    private ProgressDialog pDialog;
    private String api_url3 = "http://"+ conf.url() +":8080/v1/check_email";
    private SessionHandler session;
    //TextView go_register;
    private DataHandler enc;
    private String IP;

    TextView terms;
    TextView policy;

    Button signUP;
    LinearLayout signIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);

        signUP = (Button) findViewById(R.id.singup);
        signIN = (LinearLayout) findViewById(R.id.member);

        //terms = (TextView) findViewById(R.id.terms);
        //policy = (TextView) findViewById(R.id.policy);

        signUP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               try {
                   //Retrieve the data entered in the edit text

                   names = name.getText().toString().toLowerCase().trim();
                   emails = email.getText().toString().toLowerCase().trim();
                   passwords = password.getText().toString().trim();
                   cpasswords = cpassword.getText().toString().trim();

               } catch (Exception e) {
                   e.printStackTrace();
               }
               if (validateInputs()) {
                   passwords = password.getText().toString().trim();
                   cpasswords = cpassword.getText().toString().trim();
                   if (isEmailValid(emails)==false){
                       Message alert = new Message();
                       alert.showDialog(Signup.this, "Email is not valid");
                   }else{
                       if (names.contains(" ")){
                           Message alert = new Message();
                           alert.showDialog(Signup.this, "Username no valid");
                       }

                       if (passwords.length()<=7){
                           Message alert = new Message();
                           alert.showDialog(Signup.this, "Password must 8 length minimum");
                       }else if (passwords.equals(cpasswords)){
                           check_email(names);
                       }else{
                           Message alert = new Message();
                           alert.showDialog(Signup.this, "Password not match");
                       }
                   }

               }
            }
       });
        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, Signin.class);
                startActivity(i);
            }
        });
        //terms.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((conf.privacy())));
                //startActivity(browserIntent);
            //}
       // });
       // policy.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(conf.terms()));
                //startActivity(browserIntent);
           //}
        //});
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(names)) {
            name.setError("Full Name cannot be empty");
            name.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(emails)) {
            email.setError("Email cannot be empty");
            email.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(passwords)) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(cpasswords)) {
            cpassword.setError("Confirm Password cannot be empty");
            cpassword.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(Signup.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void check_email(String uname)
    {
        JSONObject request = new JSONObject();
        try {
            request.put("email", uname);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, api_url3, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res =response.getString("data");
                            //Toast.makeText(Signup.this, res, Toast.LENGTH_LONG).show();
                            if (res.contains("username")){
                                //Display error message if username is already existsing
                                name.setError("Username already taken!");
                                name.requestFocus();
                            }else{
                                Intent i = new Intent(getApplicationContext(), Word_Seed.class);
                                i.putExtra("email", emails);
                                i.putExtra("username",names);
                                i.putExtra("password",passwords);
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
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }
}