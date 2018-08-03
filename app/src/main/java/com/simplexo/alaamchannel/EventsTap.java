package com.simplexo.alaamchannel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsTap extends android.support.v4.app.Fragment {
    private ListView lv_events ;
    private DatabaseReference mainRef ;
    private ArrayList<Event> events ;
    private EventsListAdapter adapter ;
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_layout, container, false);



        lv_events =(ListView)view.findViewById(R.id.lv_events_EventsTap);
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("events");
        mainRef.keepSynced(true);
        events =  new ArrayList<>();
        retrieve();
        MobileAds.initialize(getContext(),"ca-app-pub-9502802921397120~4573557064");
        mAdView = view.findViewById(R.id.adView12);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        adapter = new EventsListAdapter(getContext() ,android.R.layout.simple_list_item_1 , events);
        lv_events.setAdapter(adapter);
        return view ;

    }


    void  retrieve()
    {  Query query = mainRef.orderByChild("eventNo") ;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String date = dataSnapshot.child("date").getValue(String.class);
                String itemImage = dataSnapshot.child("image").getValue(String.class);
                String event = dataSnapshot.child("event").getValue(String.class);
                String family = dataSnapshot.child("family").getValue(String.class);

                    Event event1 = new Event() ;
                    event1.setImage(itemImage);
                    event1.setDate(date);
                    event1.setEvent(event);
                    event1.setFamily(family);
                    events.add(event1);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

    }




    public class EventsListAdapter extends ArrayAdapter {
        private LayoutInflater layoutInflater;
        ArrayList<Event> events = new ArrayList<>();
        public EventsListAdapter(Context context, int resource , ArrayList<Event> eventsA) {
            super(context, resource , eventsA);
            this.events = eventsA ;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Event getItem(int position) {
            return events.get(getCount() - position -1);
        }

        @Override
        public long getItemId(int position) {
            return position-1;
        }

        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_item_event, null);

            final TextView family = (TextView) convertView.findViewById(R.id.tv_family_itemEvent);
            final ImageView imgV =(ImageView)convertView.findViewById(R.id.imageView_itemEvent);
            final TextView event =(TextView)convertView.findViewById(R.id.tv_itemEvent) ;
            final TextView date =(TextView)convertView.findViewById(R.id.tv_date_itemEvent);

            family.setText(events.get(position).getFamily());
            date.setText(events.get(position).getDate());
            event.setText(events.get(position).getEvent());
            Picasso.with(getContext()).load(events.get(position).getImage()).into(imgV);


            family.setText(getItem(position).getFamily());
            date.setText(getItem(position).getDate());
            event.setText(getItem(position).getEvent());
            Picasso.with(getContext()).load(getItem(position).getImage()).into(imgV);
            return convertView;
        }
    }
    private class Event
    {
        String date , family , event , image ;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
