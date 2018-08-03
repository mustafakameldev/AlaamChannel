package com.simplexo.alaamchannel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventsService extends Service {
    DatabaseReference reference ;
    public EventsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent , int flags, int startId) {
        reference  = FirebaseDatabase.getInstance().getReference().child("channel").child("events").child("eventNo") ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext() );
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("قناه بيت علام") ;
                builder.setContentText("هناك مناسبه جديدة ...")  ;
                builder.setLights(0xff00ff00, 300, 100) ;
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext()) ;
                manager.notify(NotificationManagerCompat.IMPORTANCE_DEFAULT ,builder.build());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        })       ;



        return super.onStartCommand(intent, flags, startId);
    }
}
