package com.simplexo.alaamchannel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import static android.os.Build.VERSION_CODES.M;
public class ChooseActioActivity extends AppCompatActivity implements View.OnClickListener ,
        AddEventFragment.OnFragmentInteractionListener  , AddEmployeeActivity.OnFragmentInteractionListener
{
    private Button btn_news , btn_events , btn_employee , btn_removeEvent , btn_removeNews , btn_removeWorker ;
    private FrameLayout fragmentContainer ;
    int Permission_All = 1;
    String[]  Permissions = { android.Manifest.permission.CALL_PHONE} ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_actio);
        declare() ;
    }
    void declare ()
    {
        btn_employee =(Button)findViewById(R.id.btn_employee_ChooseActivity) ;
        btn_events =(Button)findViewById(R.id.btn_events_ChooseActivity);
        btn_news =(Button)findViewById(R.id.btn_news_ChooseActivity) ;
        btn_news.setOnClickListener(this);
        fragmentContainer =(FrameLayout)findViewById(R.id.fragment_container);
        btn_events.setOnClickListener(this);
        btn_employee.setOnClickListener(this);
        btn_removeEvent=(Button)findViewById(R.id.btn_removeEvent_ChooseActivity);
        btn_removeEvent.setOnClickListener(this);
        btn_removeNews=(Button)findViewById(R.id.btn_removeNews_ChooseActivity);
        btn_removeNews.setOnClickListener(this);
        btn_removeWorker=(Button)findViewById(R.id.btn_removeEmployee_ChooseActivity);
        btn_removeWorker.setOnClickListener(this);

        String[] Permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE } ;
        if (!haspermissions(this , Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }
    }
    @Override
    public void onClick(View v) {
        if(v== btn_news)
        {
            startActivity(new Intent(ChooseActioActivity.this , AddNewsActivity.class));
        }if(v==btn_events )
        {
            openEventsFrag();
        }else if (v== btn_employee)
        {
            openAddEmployee();
        }else if(v== btn_removeWorker)
        {
            openRemoveWorker();
        }else if(v== btn_removeNews)
        {
            openRemoveNews();

        }else if (v== btn_removeEvent)
        {
            openRemoveEvent();
        }
    }
    private void openAddEmployee() {
        AddEmployeeActivity fragment = AddEmployeeActivity.newInstance() ;
        FragmentManager manager = getSupportFragmentManager() ;
        FragmentTransaction trans = manager.beginTransaction() ;
        trans.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_from_left ,
                R.anim.enter_from_right ,R.anim.exit_from_left) ;
        btn_news.setVisibility(View.GONE);
        btn_employee.setVisibility(View.GONE);
        btn_events.setVisibility(View.GONE);
        btn_removeEvent.setVisibility(View.GONE);
        btn_removeNews.setVisibility(View.GONE);
        btn_removeWorker.setVisibility(View.GONE);
        trans.add(R.id.fragment_container, fragment ).commit() ;
    }
    private void openEventsFrag() {
        AddEventFragment fragment = AddEventFragment.newInstance() ;
        FragmentManager manager = getSupportFragmentManager() ;
        FragmentTransaction trans = manager.beginTransaction() ;

        trans.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_from_left ,
                R.anim.enter_from_right ,R.anim.exit_from_left) ;
        btn_news.setVisibility(View.GONE);
        btn_employee.setVisibility(View.GONE);
        btn_events.setVisibility(View.GONE);
        btn_removeEvent.setVisibility(View.GONE);
        btn_removeNews.setVisibility(View.GONE);
        btn_removeWorker.setVisibility(View.GONE);
        trans.add(R.id.fragment_container, fragment ).commit() ;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        onBackPressed();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChooseActioActivity.this , MainActivity.class));

    }
    private void openRemoveNews() {
        btn_news.setVisibility(View.GONE);
        btn_employee.setVisibility(View.GONE);
        btn_events.setVisibility(View.GONE);
        btn_removeEvent.setVisibility(View.GONE);
        btn_removeNews.setVisibility(View.GONE);
        btn_removeWorker.setVisibility(View.GONE);
        RemoveNewsFragment fragment =new RemoveNewsFragment() ;
        FragmentManager manager = getSupportFragmentManager() ;
        FragmentTransaction trans = manager.beginTransaction() ;
        trans.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_from_left ,
                R.anim.enter_from_right ,R.anim.exit_from_left) ;
        trans.add(R.id.fragment_container, fragment ).commit() ;
    }
    private void openRemoveEvent() {
        btn_news.setVisibility(View.GONE);
        btn_employee.setVisibility(View.GONE);
        btn_events.setVisibility(View.GONE);
        btn_removeEvent.setVisibility(View.GONE);
        btn_removeNews.setVisibility(View.GONE);
        btn_removeWorker.setVisibility(View.GONE);
        RemoveEventFragment fragment =new RemoveEventFragment() ;
        FragmentManager manager = getSupportFragmentManager() ;
        FragmentTransaction trans = manager.beginTransaction() ;
        trans.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_from_left ,
                R.anim.enter_from_right ,R.anim.exit_from_left) ;
        trans.add(R.id.fragment_container, fragment ).commit() ;
    }
    private void openRemoveWorker() {
        btn_news.setVisibility(View.GONE);
        btn_employee.setVisibility(View.GONE);
        btn_events.setVisibility(View.GONE);
        btn_removeEvent.setVisibility(View.GONE);
        btn_removeNews.setVisibility(View.GONE);
        btn_removeWorker.setVisibility(View.GONE);
        RemoveEmployeeFraagment fragment =new RemoveEmployeeFraagment() ;
        FragmentManager manager = getSupportFragmentManager() ;
        FragmentTransaction trans = manager.beginTransaction() ;
        trans.setCustomAnimations(R.anim.enter_from_right , R.anim.exit_from_left ,
                R.anim.enter_from_right ,R.anim.exit_from_left) ;
        trans.add(R.id.fragment_container, fragment ).commit() ;
    }

}
