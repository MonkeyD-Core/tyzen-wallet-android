package net.tyzen.io;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class Account extends AppCompatActivity {
    private net.tyzen.io.SessionHandler session;
    private net.tyzen.io.Config conf;
    Button logout;
    ImageView back;
    ImageView edit;
    TextView name;
    TextView email;
    TextView password;
    TextView code;
    String fname;
    String femail;
    RelativeLayout referral;
    private String key = conf.key();
    private String initVector = conf.pass();
    private net.tyzen.io.DataHandler enc;

    private String url= "http://"+ conf.url() + ":8080/v1/email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        session = new net.tyzen.io.SessionHandler(getApplicationContext());
        net.tyzen.io.User user = session.getUserDetails();

        femail = user.getFullName();

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        logout = (Button) findViewById(R.id.logout_button);
        back = (ImageView) findViewById(R.id.back);
        edit = (ImageView) findViewById(R.id.edit);
        name = (TextView) findViewById(R.id.fullname);
        email = (TextView) findViewById(R.id.email);
        code = (TextView) findViewById(R.id.code);
        password = (TextView) findViewById(R.id.password);
        referral = (RelativeLayout) findViewById(R.id.referral_button);

        email.setText(femail);
        edit.setVisibility(View.GONE);
        get_data();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account.this, net.tyzen.io.MainActivity.class);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Account.this, net.tyzen.io.Account_Edit.class);
                i.putExtra("password", password.getText().toString());
                startActivity(i);
            }
        });

        referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = code.getText().toString();
                // Create a new ClipData.
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                // Set it as primary clip data to copy text to system clipboard.
                clipboardManager.setPrimaryClip(clipData);
                // Popup a snackbar.
                Toast.makeText(Account.this, "Copied successfully.", Toast.LENGTH_LONG).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(Account.this, net.tyzen.io.Signin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });


    }

    private void get_data(){
        JSONObject request = new JSONObject();
        try {
            request.put("email", email.getText().toString());
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
                                code.setText(json_data.getString("code"));
                                password.setText(json_data.getString("password"));
                                email.setText(json_data.getString("email"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                    }
                });
        net.tyzen.io.MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}
