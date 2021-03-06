/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.liquid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewConfiguration;

public class GlowPadTorchHelper {

    private static final String TAG = "GlowPadTorchHelper";

    public final static int TORCH_TIMEOUT = ViewConfiguration.getLongPressTimeout(); //longpress glowpad torch
    public final static int TORCH_CHECK = 2000; //make sure torch turned off

    private GlowPadTorchHelper() {
    }

    public static int torchActive(Context mContext) {
        int torchActive = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.TORCH_STATE, 0);
        return torchActive;
    }

    public static void killTorch(Context mContext) {
        vibrate(mContext);
        torchOff(mContext, false);
    }

    public static boolean startTorch(Context mContext) {
        if (torchActive(mContext) == 0) {
            vibrate(mContext);
            Intent intent = new Intent("net.cactii.flash2.TOGGLE_FLASHLIGHT");
            intent.putExtra("bright", false);
            mContext.sendBroadcast(intent);
            return true;
        } else {
            return false;
        }
    }

    public static void torchOff(Context mContext, boolean logIt) {
        if (logIt) {
            Log.w(TAG, "Second Torch Temination Required");
        }
        Intent intent = new Intent("net.cactii.flash2.TOGGLE_FLASHLIGHT");
        intent.putExtra("bright", false);
        mContext.sendBroadcast(intent);
    }

    public static void vibrate(Context mContext) {
        if (Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.HAPTIC_FEEDBACK_ENABLED, 1, UserHandle.USER_CURRENT) != 0) {
            android.os.Vibrator vib = (android.os.Vibrator)mContext.getSystemService(
                    Context.VIBRATOR_SERVICE);
            if (vib != null) {
                vib.vibrate(25);
            }
        }
    }
}
