package net.tyzen.io;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Message extends AppCompatActivity {

  public void showDialog(Activity activity, String msg){
    final Dialog dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.activity_message);

    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
    text.setText(msg);

    Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
    dialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();

  }
}