package com.simplexo.alaamchannel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RemoveNewsFragment extends android.support.v4.app.Fragment {
    private ListView lv_news ;
    private DatabaseReference mainRef ;
    private ArrayList<NewsItem> newsItems ;
    private NewsListAdapter adapter ;

    Integer n  ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove_news, container, false);
        lv_news =(ListView)view.findViewById(R.id.newsLV_RemoveNewsTap);
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("news");
        newsItems =  new ArrayList<>();
        retrieve();
        //adapter = new NewsListAdapter(getContext() ,android.R.layout.simple_list_item_1 , newsItems);
        adapter = new NewsListAdapter(newsItems ,getContext());
        lv_news.setAdapter(adapter);
        return view;
    }
    private void retrieve() {

        mainRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String newsItem = dataSnapshot.child("item").getValue(String.class);
                String itemImage = dataSnapshot.child("image").getValue(String.class);
                NewsItem item =new NewsItem() ;
                item.setImage(itemImage);
                item.setNewsItem(newsItem);
                item.setDatasnapShot(dataSnapshot.getKey());
                newsItems.add(item);
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

    class NewsListAdapter extends BaseAdapter

    {

        private ArrayList<NewsItem> newsArray = new ArrayList<>();
        private LayoutInflater inflater ;
        private  Context context ;

        public NewsListAdapter(ArrayList<NewsItem> newsArray,  Context context) {
            this.newsArray = newsArray;
            inflater = LayoutInflater.from(context);

            this.context = context;
        }

        @Override
        public int getCount() {
            return newsArray.size();
        }

        @Override
        public NewsItem getItem(int position) {
            return newsArray.get(getCount() - position -1);
        }

        @Override
        public long getItemId(int position) {
            return position-1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            if(convertView == null)
            {
                convertView= inflater.inflate(R.layout.layout_lv_news, null);
            }
            TextView item =(TextView)convertView.findViewById(R.id.tv_newsItem_newsLayout);
            ImageView img =(ImageView)convertView.findViewById(R.id.imageView_newsLayout);
            item.setText(getItem(position).getNewsItem());
            Picasso.with(getContext()).load(getItem(position).getImage()).into(img);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setTitle("حذف الخبر .");
                    alertDialogBuilder
                            .setMessage("هل انت متأكد من حذف الخبر ؟ ")
                            .setCancelable(false)
                            .setPositiveButton("نعم",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                        DatabaseReference remove = mainRef.child(getItem(position).getDatasnapShot());
                                        remove.removeValue();
                                        Toast.makeText(context, "تم حذف الخبر  ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("لا",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
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

    public class NewsItem
    {
        String newsItem , image  , datasnapShot ,date;

        public String getNewsItem() {
            return newsItem;
        }

        public void setNewsItem(String newsItem) {
            this.newsItem = newsItem;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDatasnapShot() {
            return datasnapShot;
        }

        public void setDatasnapShot(String datasnapShot) {
            this.datasnapShot = datasnapShot;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "NewsItem{" +
                    "newsItem='" + newsItem + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}


