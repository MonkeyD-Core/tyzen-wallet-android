package net.tyzen.io;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class qrcode extends AppCompatActivity {
    private SessionHandler session;
    private ProgressDialog pDialog;
    private TextView qr_address;
    private String address;
    private String keys;
    private String addressb;
    private String keysb;
    private String bch_address;
    private String bch_keys;

    private String logs, femail;
    private ImageView img, back;
    private Button copyButton;
    private TextView coin_name;
    private Config conf;
    private String key = conf.key();
    private String initVector = conf.pass();
    private DataHandler enc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        //address = enc.decrypt(key,initVector,user.getKeys());
        //keys = enc.decrypt(key,initVector,user.getSecrets());
        //addressb = enc.decrypt(key,initVector,user.getBKeys());
        //keysb = enc.decrypt(key,initVector,user.getBSecrets());
        //bch_address = enc.decrypt(key,initVector,user.getCKeys());
        //bch_keys = enc.decrypt(key,initVector,user.getCSecrets());
        qr_address = (TextView) findViewById(R.id.tvQrcode);
        back = (ImageView) findViewById(R.id.back);
        copyButton = (Button) findViewById(R.id.btn_copy);

        //Get the string
        femail = user.getFullName();
        String message=getIntent().getStringExtra("coin");
        coin_name = (TextView) findViewById(R.id.tvcoin);
        coin_name.setText("YOUR TZN ADDRESS");

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        get_bxc_address();

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = qr_address.getText().toString();
                // Create a new ClipData.
                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                // Set it as primary clip data to copy text to system clipboard.
                clipboardManager.setPrimaryClip(clipData);
                // Popup a snackbar.
                Toast.makeText(qrcode.this, "Copied successfully.", Toast.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qrcode.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, generating your address...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private void get_bxc_address(){
        JSONObject request = new JSONObject();
        try {
            request.put("username", femail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, conf.api()+"get_address", request, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.get("address").toString();
                            qr_address.setText(res.toString());
                            new DownloadImageFromInternet((ImageView) findViewById(R.id.imageQr))
                                    .execute("https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=" + res.toString() + "&choe=UTF-8");
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
