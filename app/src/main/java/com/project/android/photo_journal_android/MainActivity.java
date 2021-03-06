package com.project.android.photo_journal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String PUSH_TAG = "Token";
    protected AuthAccount account;
    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParams;

    private static final int REQUEST_CODE_SIGN_IN = 16587;
    private static final String TAG = "Account";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navLogin:
                silentSignIn();
                return true;
            case R.id.navRegister:
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://id5.cloud.huawei.com/CAS/portal/userRegister/regbyemail.html"));
                startActivity(urlIntent);
                return true;
            case R.id.navLogout:
                signOut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (account == null) {
            inflater.inflate(R.menu.menu_account, menu);
        } else {
            inflater.inflate(R.menu.menu_account_guest, menu);
        }

        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (account == null) {
            silentSignIn();
        }

        getToken();

        Button buttonAddEntry = findViewById(R.id.buttonAddEntry);

        buttonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account == null) {
                    Toast.makeText(MainActivity.this, "Please log in or register to continue.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                    intent.putExtra("User ID", account.getUnionId());
                    startActivity(intent);
                }
            }
        });
    }

    private void signIn() {
        mAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();

        AccountAuthService mAuthManager = AccountAuthManager.getService(MainActivity.this, mAuthParams);

        startActivityForResult(mAuthManager.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                account = authAccountTask.getResult();
                getUserEntries(account.getUnionId());
                invalidateOptionsMenu();

                Log.i(TAG, "Login succeeded: " + REQUEST_CODE_SIGN_IN);
            } else {
                Log.e(TAG, "Login failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
                Toast.makeText(this, "Login failed, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void silentSignIn() {
        mAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();

        mAuthService = AccountAuthManager.getService(this, mAuthParams);

        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                account = authAccount;
                getUserEntries(account.getUnionId());
                invalidateOptionsMenu();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    signIn();
                }
            }
        });
    }

    private void signOut() {
        mAuthService.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Log.i(TAG, "Logout complete");
                findViewById(R.id.rvEntries).setVisibility(View.GONE);
            }
        });

        mAuthService.cancelAuthorization().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "You have logged out.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Authorization revoked");
                    account = null;
                    invalidateOptionsMenu();
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        int statusCode = ((ApiException) exception).getStatusCode();
                        Log.i(TAG, "Authorization revocation failed: " + statusCode);
                    }
                }
            }
        });
    }

    private void getUserEntries(String userId) {
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        TextView textEmpty = findViewById(R.id.textEmpty);
        RecyclerView rvEntries = findViewById(R.id.rvEntries);

        ArrayList<Entry> entries = db.getEntries(userId);

        if (entries.isEmpty()) {
            textEmpty.setText("No entries found");
        } else {
            EntriesAdapter entriesAdapter = new EntriesAdapter(MainActivity.this, entries);
            rvEntries.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rvEntries.setAdapter(entriesAdapter);
        }
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // App ID from agconnect-services.json
                    String appId = "105334565";

                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, tokenScope);

                    Log.i(PUSH_TAG, "Get token: " + token);

                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e(PUSH_TAG, "Get token failed: " + e);
                }
            }
        }.start();
    }

    private void sendRegTokenToServer(String token) {
        Log.i(PUSH_TAG, "Sending token to server: " + token);
    }

    private void refreshedTokenToServer(String token) {
        Log.i(PUSH_TAG, "Sending token to server:" + token);
    }

    private void deleteToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = "105334565";

                    String tokenScope = "HCM";
                    HmsInstanceId.getInstance(MainActivity.this).deleteToken(appId, tokenScope);

                    Log.i(PUSH_TAG, "token deleted successfully");
                } catch (ApiException e) {
                    Log.e(PUSH_TAG, "deleteToken failed." + e);
                }
            }
        }.start();
    }
}