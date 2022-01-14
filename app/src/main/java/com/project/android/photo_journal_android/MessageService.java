package com.project.android.photo_journal_android;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;

public class MessageService extends HmsMessageService {

    private final String TAG = "PUSH_TOKEN_LOG";

    @Override
    public void onNewToken(String token, Bundle bundle) {
        // Obtain push token
        Log.i(TAG, "Received refresh token: " + token);

        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "Sending token to server: " + token);
    }
}
