package com.simplexo.alaamchannel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView develop, connectToProvider, connectToProvider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        develop = (TextView) findViewById(R.id.tv_connectTodevelop);
        develop.getText();
        develop.setOnClickListener(this);
        connectToProvider = (TextView) findViewById(R.id.connectToProvider);
        connectToProvider.setOnClickListener(this);
        connectToProvider2 = (TextView) findViewById(R.id.connectToProvider2);
        connectToProvider2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == develop) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{develop.getText().toString()});
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        if (v == connectToProvider) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+"+96598817177"));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        }
         if (v == connectToProvider2)
        {
            Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100008904822514");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
