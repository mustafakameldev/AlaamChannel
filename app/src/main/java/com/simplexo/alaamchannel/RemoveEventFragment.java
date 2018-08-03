package com.simplexo.alaamchannel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RemoveEventFragment extends android.support.v4.app.Fragment {
    private ListView lv_events ;
    private DatabaseReference mainRef ;
    private ArrayList<Event> events ;
    private EventsListAdapter adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_removeevent, container, false);
        lv_events =(ListView)view.findViewById(R.id.lv_RemoveEvents_EventsTap);
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("events");
        mainRef.keepSynced(true);
        events =  new ArrayList<>();
        retrieve();
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
                event1.setDatasnapShot(dataSnapshot.getKey());
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
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());
                    alertDialogBuilder.setTitle("حذف المناسبه .");
                    alertDialogBuilder
                            .setMessage("هل انت متأكد من حذف المناسبه ؟  ")
                            .setCancelable(false)
                            .setPositiveButton("نعم",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DatabaseReference remove = mainRef.child(getItem(position).getDatasnapShot());
                                    remove.removeValue();
                                    Toast.makeText(getContext(), "تم حذف المناسبه  ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("لا",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });

            return convertView;
        }
    }
    private class Event
    {
        String date , family , event , image  , datasnapShot;

        public String getDate() {
            return date;
        }

        public String getDatasnapShot() {
            return datasnapShot;
        }

        public void setDatasnapShot(String datasnapShot) {
            this.datasnapShot = datasnapShot;
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
