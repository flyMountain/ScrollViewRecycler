package com.flymountain.scrollviewrecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by admin on 2016/10/12.
 */

public class MainActivity extends AppCompatActivity {
    Button list_btn;
    Button gird_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        list_btn = (Button) findViewById(R.id.list_btn);
        gird_btn = (Button) findViewById(R.id.gird_btn);
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        });

        gird_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GridViewActivity.class));
            }
        });
    }
}
