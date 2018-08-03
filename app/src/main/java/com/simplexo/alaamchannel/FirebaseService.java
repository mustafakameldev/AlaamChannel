package com.simplexo.alaamchannel;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mostafa kamel on 15/07/2018.
 */

public class FirebaseService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken() ;
        Log.d("TOKEN", token);

    }
}
