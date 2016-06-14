package com.fuck.wechatmsg;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView msgBox = (TextView) findViewById(R.id.msgBox);
        findViewById(R.id.readBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.mvDb();
                } catch (Exception e) {
                   msgBox.setText(e.getMessage());
                    return;
                }
                List<Message> list = Utils.getAllMessage();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                msgBox.setText(gson.toJson(list));
            }
        });

        findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Utils.clearDb();
                    msgBox.setText("Clear success");
                }catch (Exception e){
                    msgBox.setText(e.getMessage());
                }
            }
        });
    }


}
