// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.
/*
1. 핸들러 생성: onCreate 메서드에서 UI 작업을 처리할 핸들러(mUIHandler)를 생성하고, onPostCreate 메서드에서는 백그라운드 작업을 처리할 핸들러(mBackgroundHandler)를 생성한다
이 두 핸들러는 각각 메인 스레드와 별도의 백그라운드 스레드에서 작동합니다.

2. 백그라운드 스레드 관리: startBackgroundThread 메서드와 stopBackgroundThread 메서드를 통해 백그라운드 스레드를 시작하거나 종료할 수 있다
onDestroy 메서드에서는 액티비티가 파괴될 때 백그라운드 스레드를 안전하게 종료한다

추상 클래스가 아니므로, 그 자체로 인스턴스화할 수 있습니다.
그러나 이 클래스는 기본적인 기능만을 제공하므로, 실제 앱에서는 이 클래스를 상속받아 필요한 기능을 추가로 구현할 것이다

AbstractCameraXActivity 클래스를 보면, BaseModuleActivity 클래스를 상속받아 카메라 관련 기능을 추가로 구현한 것을 볼 수 있습니다.
 */
package org.pytorch.demo.objectdetection;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseModuleActivity extends AppCompatActivity {
    protected HandlerThread mBackgroundThread;
    protected Handler mBackgroundHandler;
    protected Handler mUIHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUIHandler = new Handler(getMainLooper());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startBackgroundThread();
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("ModuleActivity");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        stopBackgroundThread();
        super.onDestroy();
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e("Object Detection", "Error on stopping background thread", e);
        }
    }
}
