package com.project.android.photo_journal_android;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessageService;

public class MessageService extends HmsMessageService {

    private final String TAG = "PUSH_TOKEN_LOG";

    @Override
    public void onNewToken(String token, Bundle bundle) {
        // Obtain a push token.
        Log.i(TAG, "have received refresh token " + token);

        // Check whether the token is null.
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

}
