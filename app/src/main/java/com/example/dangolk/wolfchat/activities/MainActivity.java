package com.example.dangolk.wolfchat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dangolk.wolfchat.Constants;
import com.example.dangolk.wolfchat.R;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mSocket = IO.socket(Constants.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(MainActivity.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        mSocket.connect();
        Toast.makeText(this, "Connected to: " + Constants.IP_LOCAL_HOST, Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onDestroy() {
        mSocket.disconnect();
        super.onDestroy();
    }
}

