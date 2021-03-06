package com.xiaokun.aidldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //快捷键 logt
    private static final String TAG = "MainActivity";

    private Button mMessengerBtn;
    private Button mAidlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        mMessengerBtn = (Button) findViewById(R.id.messenger_btn);
        mAidlBtn = (Button) findViewById(R.id.aidl_btn);
        initListener(mAidlBtn, mMessengerBtn);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.messenger_btn:
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
                break;
            case R.id.aidl_btn:
                startActivity(new Intent(MainActivity.this, BookManagerActivity.class));
                break;
            default:
                break;
        }
    }

    private void serialization() throws IOException, ClassNotFoundException
    {
        //序列化过程
        User user = new User(0, "xiaokun", true);
        FileOutputStream fileOutputStream = new FileOutputStream("cache.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
        out.writeObject(user);
        out.close();

        //反序列化过程
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("cache.txt"));
        User newUser = (User) in.readObject();
        in.close();
    }

}
