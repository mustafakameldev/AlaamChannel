package com.simplexo.alaamchannel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    TextView message  , title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        title=(TextView)findViewById(R.id.testTitle);
        message=(TextView)findViewById(R.id.testMessage);



        if(getIntent().getExtras() !=null)
        {
            for (String key:getIntent().getExtras().keySet())
            {

                if (key.equals(title))
                    title.setText(getIntent().getExtras().getString(key));
                else if (key.equals("message"))
                    message.setText(getIntent().getExtras().getString(key));

            }
        }
    }
}
