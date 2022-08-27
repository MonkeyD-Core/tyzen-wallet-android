package net.tyzen.io;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class Account_Edit extends AppCompatActivity {
    private net.tyzen.io.SessionHandler session;
    LinearLayout logout;
    ImageView back;
    EditText name;
    EditText old_pass;
    EditText new_pass;
    EditText c_pass;
    Button save;
    Button save2;
    String password;
    TextView email;
    TextView code;
    String fname;
    String femail;
    RelativeLayout referral;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;
    private String url="http://"+ conf.url() +":8080/v1/email";
    private String urls="http://"+ conf.url() +":8080/update";
    private String url2="http://"+ conf.url() +":8080/update2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        femail = user.getFullName();
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        password = getIntent().getStringExtra("password");
        save = (Button) findViewById(R.id.submit);
        save2 = (Button) findViewById(R.id.save);
        back = (ImageView) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.fullname);
        old_pass = (EditText) findViewById(R.id.old_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        c_pass = (EditText) findViewById(R.id.confirm_pass);

        get_data();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account_Edit.this, Account.class);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_passs =  new_pass.getText().toString();
                String c_passs =  c_pass.getText().toString();
                String old_passs =  old_pass.getText().toString();
                if (password.equals(old_passs)){
                    if (new_passs.equals(c_passs)){
                        update_data();
                    }else{
                        Message alert = new Message();
                        alert.showDialog(Account_Edit.this, "Your password is wrong");
                    }
                }else{
                    Message alert = new Message();
                    alert.showDialog(Account_Edit.this, "Your old password is wrong");
                }

                //Intent i = new Intent(Account_Edit.this, Signin.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(i);
                //finish();
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String names =  name.getText().toString();
                if (names.equals("")){
                        Message alert = new Message();
                        alert.showDialog(Account_Edit.this, "Please enter your name.");
                }else{
update_data2();
                }

            }
        });

    }

    private void get_data(){
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
                            //Toast.makeText(Account.this,response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            // Extract data from json and store into ArrayList as class objects
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject json_data = dataArray.getJSONObject(i);

                                name.setText(json_data.getString("username"));
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

    private void update_data(){
        JSONObject request = new JSONObject();
        try {
            request.put("code", conf.code());
            request.put("name", name.getText().toString());
            request.put("password", c_pass.getText().toString());
            request.put("email", femail);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.PUT, urls, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(Account_Edit.this, "Password change successfully.",Toast.LENGTH_LONG).show();
                            session.logoutUser();
                            Intent i = new Intent(Account_Edit.this, Signin.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                        catch (Exception e) {
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void update_data2(){
        JSONObject request = new JSONObject();
        try {
            request.put("code", "");
            request.put("name", name.getText().toString());
            request.put("email", femail);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.PUT, url2, request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(Account_Edit.this, "Profile is now updated.",Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
