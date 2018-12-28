package com.simplexo.alaamchannel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn ;
    private EditText editText ;
    private DatabaseReference mainRef , numRef ;
    private Integer num ;
    private ImageButton imgBtn ;
    private ProgressDialog progressDialog ;
    private final int GALLARY_REQUIST =2 ;
    private Uri imageUri = null ;
    private StorageReference mainStorageRef  ;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        editText =(EditText)findViewById(R.id.ed_addNews_Fragment1) ;
        btn =(Button )findViewById(R.id.btn_addNews_Fragment1);
        btn.setOnClickListener(this);
        imgBtn =(ImageButton)findViewById(R.id.imgBtn_addNews_Fragment1);
        imgBtn.setOnClickListener(this);

        MobileAds.initialize(this,"ca-app-pub-9502802921397120~4573557064");
        mAdView =(AdView)findViewById(R.id.adView_addNews);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        progressDialog = new ProgressDialog(this);
        mainRef= FirebaseDatabase.getInstance().getReference().child("channel").child("news");
        numRef = mainRef.child("newsNo");
        numRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num =dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mainStorageRef= FirebaseStorage.getInstance().getReference();
    }
    @Override
    public void onClick(View v) {
        if (v == btn) {
            final String news = editText.getText().toString();

            progressDialog.setMessage("جاري التحميل ..");
            StorageReference filePath = mainStorageRef.child("Image").child(imageUri.getLastPathSegment());
            progressDialog.show();
            if (!TextUtils.isEmpty(news)||  imageUri != null) {

                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        DatabaseReference addNews = mainRef.push();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        addNews.child("image").setValue(downloadUri.toString());
                        addNews.child("item").setValue(news);
                        addNews.child("date").setValue(new SimpleDateFormat("yyyyMMdd_HHmmss")
                                .format(Calendar.getInstance().getTime()));
                        editText.setText(" ");
                        numRef.setValue(num+1);
                        Toast.makeText(AddNewsActivity.this, "تم اضافه الخبر ..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddNewsActivity.this , ChooseActioActivity.class));
                        progressDialog.dismiss();


                    }
                }) ;
            } else
                {
                    Toast.makeText(getApplicationContext(),"تأكد من كتابة المناسبة اولا..", Toast.LENGTH_SHORT).show();
            }
        }else if (v==imgBtn)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT) ;
            intent.setType("image/*") ;
            startActivityForResult(intent ,GALLARY_REQUIST );
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLARY_REQUIST && resultCode == RESULT_OK)
        {
            imageUri=data.getData() ;
            imgBtn.setImageURI(imageUri);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
