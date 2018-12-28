package com.simplexo.alaamchannel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class NewsService extends Service {
    private int id = 1;
    private NotificationCompat.Builder notification_builder;
    private NotificationManagerCompat notification_manager;
    public NewsService() {
        Log.d(TAG, "NewsService:  start Service  mmmmmmmmmmmmmmmmmmmmmmmmmm");
    }
    DatabaseReference reference ;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " +" mmmmmmmmmmmmmmmmmmmmmmmmmm" );
        reference  = FirebaseDatabase.getInstance().getReference().child("channel").child("news").child("newsNo") ;
        ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                    SetNotification.notify(getApplicationContext() , "dddd" ,1);
                Log.d(TAG, "onDataChange: " + "notified mmmmmmmmmmmmmmmmmmm");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}/*
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext() );
                builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle("قناه بيت علام") ;
                        builder.setContentText("هناك خبر جديد ...")  ;
                        builder.setLights(0xff00ff00, 300, 100) ;
                        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext()) ;
                        manager.notify(NotificationManagerCompat.IMPORTANCE_DEFAULT ,builder.build());

                          ///////////////////////////

                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
// build notification
// the addAction re-use the same intent to keep the example short

                Log.d(TAG, "onDataChange: " + "start notification mmmmmmmmmmmmmmmmmmmm");
                        Notification n  = new Notification.Builder(getBaseContext())
                        .setContentTitle("New mail from " + "test@gmail.com")
                        .setContentText("Subject")
                        .setSmallIcon(R.drawable.news_icon)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .build();
                        Log.d(TAG, "onDataChange: " + "after  notification built");

                        NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        notificationManager.notify(0, n);
                        */