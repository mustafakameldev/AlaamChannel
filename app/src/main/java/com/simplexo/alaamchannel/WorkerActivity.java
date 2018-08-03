package com.simplexo.alaamchannel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class WorkerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_place, tv_name, tv_job, tv_phone;
    private ImageView img;
    private DatabaseReference mainRef;
    private String phoneStr;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        String emp = getIntent().getExtras().getString("emp");
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("employees").child(emp);
        declare();
    }

    void declare() {
        tv_name = (TextView) findViewById(R.id.tv_name_WorkerActivity);
        tv_job = (TextView) findViewById(R.id.tv_job_WorkerActivity);
        tv_phone = (TextView) findViewById(R.id.tv_phone_WorkerActivity);
        tv_place = (TextView) findViewById(R.id.tv_place_WorkerActivity);
        img = (ImageView) findViewById(R.id.imgV_WorkerActivity);
        btn = (Button) findViewById(R.id.btn_call_WorkerActivity);
        btn.setOnClickListener(this);
        retrieve();

    }

    void retrieve() {
        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_name.setText(dataSnapshot.child("name").getValue(String.class));
                tv_place.setText(dataSnapshot.child("place").getValue(String.class));
                tv_phone.setText(dataSnapshot.child("phone").getValue(String.class));
                tv_job.setText(dataSnapshot.child("job").getValue(String.class));
                phoneStr = dataSnapshot.child("phone").getValue(String.class);
                Picasso.with(WorkerActivity.this).load(dataSnapshot.child("image").getValue(String.class)).into(img);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneStr)) ;

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
    }
}
