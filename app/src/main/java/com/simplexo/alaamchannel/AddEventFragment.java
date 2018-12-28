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

import static android.app.Activity.RESULT_OK;
public class AddEventFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private Button btn_addEvent ;
    private EditText et_family , et_event  , et_date;
    private DatabaseReference mainRef ;
    private DatabaseReference numRef ;
    private int index ;
    private ImageButton imgBtn ;
    private ProgressDialog progressDialog ;
    private final int GALLARY_REQUIST =2 ;
    private Uri imageUri = null ;
    private AdView mAdView;
    private StorageReference mainStorageRef  ;
    public AddEventFragment() {
        // Required empty public constructor
    }
    public static AddEventFragment newInstance() {
        AddEventFragment fragment = new AddEventFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_event, container, false);
        btn_addEvent=(Button)view.findViewById(R.id.btn_addEvent_Fragment) ;
        et_event= (EditText)view.findViewById(R.id.et_writeEvent_fragment);
        et_family =(EditText)view.findViewById(R.id.et_writeFamily_Fragment) ;
        imgBtn =(ImageButton)view.findViewById(R.id.imageButton_AddEventFragment);
        imgBtn.setOnClickListener(this);
        et_date = (EditText)view.findViewById(R.id.et_date_AddEvent);
        et_family.requestFocus() ;
        btn_addEvent.setOnClickListener(this);
        MobileAds.initialize(getContext(),"ca-app-pub-9502802921397120~4573557064");
        mAdView = view.findViewById(R.id.adView_addevent);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("events");
        numRef =mainRef.child("eventNo");
        progressDialog = new ProgressDialog(getContext());
        numRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Integer num = dataSnapshot.getValue(Integer.class);
                index=num ;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        if(v== btn_addEvent)
        {
           final String familyStr =et_family.getText().toString() ;
           final String eventStr = et_event.getText().toString() ;
            final String dateStr = et_date.getText().toString() ;
            progressDialog.setMessage("جاري التحميل ...");
            progressDialog.show();
          if(!TextUtils.isEmpty(familyStr)|| !TextUtils.isEmpty(eventStr) || imageUri ==null || TextUtils.isEmpty(dateStr)) {
              StorageReference pushImage = mainStorageRef.child("Image").child(imageUri.getLastPathSegment());
              pushImage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      DatabaseReference pushEvent = mainRef.push() ;
                      Uri downloadUri = taskSnapshot.getDownloadUrl();
                      pushEvent.child("image").setValue(downloadUri.toString());
                      pushEvent.child("event").setValue(eventStr) ;
                      pushEvent.child("family").setValue(familyStr);
                      pushEvent.child("uploaddate").setValue(new SimpleDateFormat("yyyyMMdd_HHmmss")
                               .format(Calendar.getInstance().getTime()));
                      pushEvent.child("date").setValue(dateStr);
                      Toast.makeText(getContext(), "تم اضافه المناسبه..", Toast.LENGTH_SHORT).show();
                      int newIndex = index +1 ;
                      numRef.setValue(newIndex);
                      et_family.setText(" ");
                      et_event.setText(" ");
                      et_date.setText(" ");
                      progressDialog.dismiss();
                      getActivity();
                  }
              });
          }else{
              Toast.makeText(getContext(),"تأكد من كتابة المناسبة اولا ..", Toast.LENGTH_SHORT).show();
          }
        }if(v== imgBtn)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT) ;
            intent.setType("image/*") ;
            startActivityForResult(intent ,GALLARY_REQUIST );
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLARY_REQUIST && resultCode == RESULT_OK)
        {
            imageUri=data.getData();
            imgBtn.setImageURI(imageUri);
        }
    }
}
