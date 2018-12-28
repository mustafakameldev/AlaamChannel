package com.simplexo.alaamchannel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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

import static android.app.Activity.RESULT_OK;

public class AddEmployeeActivity extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private EditText name , phone , job , et_place ;
    private Button btn_add ;
    private ImageButton imgBtn ;
    private DatabaseReference mainRef, numRef  ;
    private ProgressDialog progressDialog ;
    private final int GALLARY_REQUIST =2 ;
    private Integer num ;
    private Uri imageUri = null ;
    private AdView mAdView;
    private StorageReference mainStorageRef  ;
    private InterstitialAd interstitialAd ;
    public AddEmployeeActivity() {
        // Required empty public constructor
    }
    public static AddEmployeeActivity newInstance() {
        AddEmployeeActivity fragment = new AddEmployeeActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.fragment_add_employee, container, false);
        name =(EditText)view.findViewById(R.id.et_name_AddEmployeeFragment) ;
        phone =(EditText)view.findViewById(R.id.et_phone_AddEmployeeFragment);
        job =(EditText)view.findViewById(R.id.et_job_AddEmployeeFragment);
        btn_add =(Button)view.findViewById(R.id.btn_addEmployee_Fragment);
        imgBtn = (ImageButton)view.findViewById(R.id.imageButton_addEmployeeFragment);
        et_place=(EditText) view.findViewById(R.id.place_addEmployeeFragment);
        btn_add.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        MobileAds.initialize(getContext(),"ca-app-pub-9502802921397120~4573557064");
        mAdView = view.findViewById(R.id.adView_addEmployee);



        AdRequest adRequest = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitialAd = new InterstitialAd(getContext());
// Insert the Ad Unit ID
        interstitialAd.setAdUnitId("ca-app-pub-9502802921397120/8838754062");
        interstitialAd.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
// Call displayInterstitial() function
                displayInterstitial();
            }
        });


        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);
        //adapter = new NewsListAdapter(getContext() ,an
        progressDialog = new ProgressDialog(getContext()) ;
        numRef =FirebaseDatabase.getInstance().getReference().child("channel").child("employees").child("empNo");
        numRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num =dataSnapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("employees");
        mainStorageRef= FirebaseStorage.getInstance().getReference();

        return view ;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onClick(View v) {
        if(v==btn_add)
        {
          final   String nameStr = name.getText().toString();
          final   String phoneStr =phone.getText().toString().trim();
            final String jobStr = job.getText().toString().trim();
        final     String placeStr = et_place.getText().toString();
            progressDialog.setMessage("جاري التحميل ...");
            progressDialog.show();
            if (!TextUtils.isEmpty(nameStr)|| !TextUtils.isEmpty(phoneStr) || ! TextUtils.isEmpty(jobStr) || imageUri !=null || !TextUtils.isEmpty(placeStr) )
            {
                StorageReference pushImage = mainStorageRef.child("Image").child(imageUri.getLastPathSegment());
                pushImage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        DatabaseReference pushEmployee = mainRef.push();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        pushEmployee.child("name").setValue(nameStr);
                        pushEmployee.child("phone").setValue(phoneStr);
                        pushEmployee.child("job").setValue(jobStr);
                        pushEmployee.child("place").setValue(placeStr);
                        pushEmployee.child("image").setValue(downloadUri.toString()) ;

                        numRef.setValue(num+1);
                        name.setText(" ");
                        job.setText(" ");
                        et_place.setText(" ");
                        phone.setText(" ");
                        Toast.makeText(getContext(), "تم اضافه الموظف .", Toast.LENGTH_SHORT).show();
                        getActivity();
                        progressDialog.dismiss();
                    }
                });
            }else
            {
                Toast.makeText(getContext(),"اضف البيانات كامله", Toast.LENGTH_SHORT).show();
            }
        }if(v==imgBtn)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT) ;
            intent.setType("image/*") ;
            startActivityForResult(intent ,GALLARY_REQUIST );
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLARY_REQUIST && resultCode == RESULT_OK)
        {
            imageUri=data.getData() ;
            imgBtn.setImageURI(imageUri);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}
