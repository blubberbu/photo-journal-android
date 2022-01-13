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


    //PUT EVERYTHING BELOW INSIDE MainActivity.java
    //Call getToken() method inside MainActivity onCreate

//    private void getToken() {
//        // Create a thread.
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    // Obtain the app ID from the agconnect-services.json file.
//                    String appId = "105334565";
//
//                    // Set tokenScope to HCM.
//                    String tokenScope = "HCM";
//                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, tokenScope);
//                    Log.i(TAG, "get token: " + token);
//
//                    // Check whether the token is null.
//                    if (!TextUtils.isEmpty(token)) {
//                        sendRegTokenToServer(token);
//                    }
//                } catch (ApiException e) {
//                    Log.e(TAG, "get token failed, " + e);
//                }
//            }
//        }.start();
//    }
//
//    private void sendRegTokenToServer(String token) {
//        Log.i(PUSH_TAG, "sending token to server. token:" + token);
//    }
//
//    private void refreshedTokenToServer(String token) {
//        Log.i(PUSH_TAG, "sending token to server. token:" + token);
//    }
//
//    private void deleteToken() {
//        // Create a thread.
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    // Obtain the app ID from the agconnect-service.json file.
//                    String appId = "105334565";
//
//                    // Set tokenScope to HCM.
//                    String tokenScope = "HCM";
//                    // Delete the token.
//                    HmsInstanceId.getInstance(MainActivity.this).deleteToken(appId, tokenScope);
//                    Log.i(TAG, "token deleted successfully");
//                } catch (ApiException e) {
//                    Log.e(TAG, "deleteToken failed." + e);
//                }
//            }
//        }.start();
//    }

}
