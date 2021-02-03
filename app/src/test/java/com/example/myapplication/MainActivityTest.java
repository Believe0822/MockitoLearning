package com.example.myapplication;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myservice.IMyService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.LEGACY)
public class MainActivityTest {
    ActivityController<MainActivity> controller;
    MainActivity activity;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @After
    public void tearDown() {
        ShadowLog.stream = null;
    }

    @Test
    public void onCreate() {
        {
            controller = buildActivity(MainActivity.class);
            activity = controller.get();
            final IMyService myService = mock(IMyService.class);
            try (final MockedStatic<IMyService.Stub> ignore = mockStatic(IMyService.Stub.class, invocation -> myService)) {
                controller.create();
            }
            controller.destroy();
        }
        {
            controller = buildActivity(MainActivity.class);
            activity = controller.get();
            final IMyService myService = mock(IMyService.class);
            try (final MockedStatic<IMyService.Stub> ignore = mockStatic(IMyService.Stub.class, invocation -> myService)) {
                doReturn(true).when(myService).isSuccess();
                controller.create();
            } catch (final RemoteException e) {
                e.printStackTrace();
                fail();
            }
            controller.destroy();
        }
        {
            controller = buildActivity(MainActivity.class);
            activity = controller.get();
            final IMyService myService = mock(IMyService.class);
            try (final MockedStatic<IMyService.Stub> ignore = mockStatic(IMyService.Stub.class, invocation -> myService)) {
                doThrow(RemoteException.class).when(myService).Function(anyInt());
                controller.create();
            } catch (final RemoteException e) {
                e.printStackTrace();
                fail();
            }
            controller.destroy();
        }
        {
            controller = buildActivity(MainActivity.class);
            activity = spy(controller.get());
            doReturn(false).when(activity).bindService(any(Intent.class), any(ServiceConnection.class), anyInt());
            activity.onCreate(new Bundle());
        }
    }
}