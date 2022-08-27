package net.tyzen.io;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {
    private TextView nametxt;
    private ImageView fullimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();

        String name=i.getStringExtra("name");
        //nametxt.setText(name);

        if (name.contains("0x")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://etherscan.io/tx/"+name));
            startActivity(browserIntent);
        }else{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://explorer.tyzen.io/tx/"+name));
            startActivity(browserIntent);
        }

    }
}
