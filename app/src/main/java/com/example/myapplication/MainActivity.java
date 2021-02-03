package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.myservice.IMyService;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private IMyService mMyService;
    final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            mMyService = IMyService.Stub.asInterface(service);
            if (mMyService != null) {
                try {
                    mMyService.Function(0);
                    if (mMyService.isSuccess()) {
                        Log.v(TAG, "is Success.");
                    } else {
                        Log.v(TAG, "is Fail.");
                    }
                } catch (final RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mMyService = null;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // サービスとbind
        final Intent intent = new Intent(IMyService.class.getName()).setPackage(IMyService.class.getPackage().getName());
        if (!bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            Log.e(TAG, "bindService fail.");
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}