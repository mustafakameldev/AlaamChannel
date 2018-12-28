package com.simplexo.alaamchannel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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

import static android.os.Build.VERSION_CODES.M;

public class RemoveEmployeeFraagment extends android.support.v4.app.Fragment implements TextWatcher {
    private ListView lv_employees1 ;
    private ArrayList<Worker> workers ;
    private DatabaseReference mainRef ;
    private EmployeeListAdapter adapter ;
    private EditText searchEditText ;
    int Permission_All = 1;
    String[]  Permissions = { android.Manifest.permission.CALL_PHONE} ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove_employee , container , false);
        lv_employees1 =(ListView)view.findViewById(R.id.lv_RemoveRmployees_fragment) ;
        mainRef = FirebaseDatabase.getInstance().getReference().child("channel").child("employees");
        searchEditText =(EditText)view.findViewById(R.id.searchRemoveEditText);
        searchEditText.addTextChangedListener(this);
        workers =new ArrayList<>();
        retrieve();
        adapter = new EmployeeListAdapter(workers , getContext());
        lv_employees1.setAdapter(adapter);

        return view ;
    }

    private void retrieve() {
        mainRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String job = dataSnapshot.child("job").getValue(String.class);
                String image =dataSnapshot.child("image").getValue(String.class);
                String phoneNo = dataSnapshot.child("phone").getValue(String.class);
                Worker worker = new Worker();
                worker.setImage(image);
                worker.setJob(job);
                worker.setName(name);
                worker.setNumber(phoneNo);
                worker.setDatasnapshot(dataSnapshot.getKey());
                workers.add(worker);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.adapter.getFilter().filter(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private class EmployeeListAdapter extends BaseAdapter implements Filterable
    {

        private ArrayList<Worker> newsArray  , tempArray ;
        private LayoutInflater inflater;
        private Context context;
        private EmployeeListAdapter.CustomFilter cs ;
        public EmployeeListAdapter(ArrayList<Worker> newsArray, Context context) {
            this.newsArray = newsArray;
            this.tempArray = newsArray ;
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return newsArray.size();
        }

        @Override
        public Worker getItem(int position) {
            return newsArray.get(getCount() - position - 1);
        }
        @Override
        public long getItemId(int position) {
            return position - 1;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_item_employee, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.tv_name_EmplyeeItem);
            ImageView img = (ImageView) convertView.findViewById(R.id.imgV_employeeImage_ItemEmployee);
            TextView job =(TextView) convertView.findViewById(R.id.tv_job_EmployeeItem);
            ImageButton imgBtn =(ImageButton)convertView.findViewById(R.id.imageButton);
            name.setText(getItem(position).getName());
            job.setText(getItem(position).getJob());
            final String datasnapShot = getItem(position).getDatasnapshot();
            Picasso.with(getContext()).load(getItem(position).getImage()).into(img);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!haspermissions(getContext() , Permissions)) {
                        ActivityCompat.requestPermissions(getActivity(), Permissions, Permission_All);
                    }else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + getItem(position).getNumber()));
                        startActivity(callIntent);
                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent  gotoEmp = new Intent(getActivity() ,WorkerActivity.class );
                    gotoEmp.putExtra("emp" ,datasnapShot );
                    startActivity(gotoEmp);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());
                    alertDialogBuilder.setTitle("حذف العامل .");
                    alertDialogBuilder
                            .setMessage("هل انت متأكد من حذف العامل ؟ ")
                            .setCancelable(false)
                            .setPositiveButton("نعم",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    DatabaseReference remove = mainRef.child(getItem(position).getDatasnapshot());
                                    remove.removeValue();
                                    Toast.makeText(getContext(), "تم حذف العامل  ", Toast.LENGTH_SHORT).show();
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


        @Override
        public Filter getFilter() {
            if (cs==null)
            {
                cs = new CustomFilter() ;

            }
            return cs;
        }
        class CustomFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults() ;
                if(constraint != null && constraint.length()>0) {
                    constraint = constraint.toString().toUpperCase();
                    ArrayList<Worker> filters = new ArrayList<>();

                    for (int i = 0; i < tempArray.size(); i++) {
                        if (tempArray.get(i).getJob().toUpperCase().contains(constraint)) {
                            Worker worker = new Worker(tempArray.get(i).getName(), tempArray.get(i).getJob()
                                    , tempArray.get(i).getImage(), tempArray.get(i).getNumber());
                            filters.add(worker);

                        }

                    }
                    results.count = filters.size();
                    results.values = filters;
                }else
                {

                    results.count =tempArray.size();
                    results.values = tempArray ;
                }




                return  results ;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newsArray = (ArrayList<Worker>)results.values ;
                notifyDataSetChanged();
            }
        }



    }


    class Worker {
        String name , job , image , number  , datasnapshot;
        public Worker(){}


        public Worker(String name, String job, String image, String number) {
            this.name = name;
            this.job = job;
            this.image = image;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;

        }
        public String getDatasnapshot() {
            return datasnapshot;
        }

        public void setDatasnapshot(String datasnapshot) {
            this.datasnapshot = datasnapshot;
        }
    }
    public static boolean  haspermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT>= M && context!=null && permissions!=null) {
            for (String permission: permissions){
                if (ActivityCompat.checkSelfPermission(context , permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return  true;
    }
}
