package com.fuck.wechatmsg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

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
                DBOpenHelper db = new DBOpenHelper(v.getContext(),"m.db");
                List<Message> list = db.getAllMessage();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                msgBox.setText(gson.toJson(list));
            }
        });
    }
}
