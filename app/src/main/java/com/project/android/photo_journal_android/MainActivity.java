package com.project.android.photo_journal_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AuthAccount account;
    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParams;

    private static final int REQUEST_CODE_SIGN_IN = 16587;
    private static final String TAG = "Account";

    Button buttonAddEntry;
    RecyclerView rvEntries;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri registerUrl = Uri.parse("https://id5.cloud.huawei.com/CAS/portal/userRegister/regbyemail.html");

        switch (item.getItemId()) {
            case R.id.navLogin:
                signIn();
                return true;
            case R.id.navRegister:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        silentSignIn();

        MenuInflater inflater = getMenuInflater();

        if (account == null) {
            inflater.inflate(R.menu.menu_account, menu);
        } else {
            inflater.inflate(R.menu.menu_account_guest, menu);
        }

        findViewById(R.id.navLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddEntry = findViewById(R.id.buttonAddEntry);

        buttonAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                startActivity(intent);
            }
        });

        rvEntries = findViewById(R.id.rvEntries);
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);

        ArrayList<Entry> entries = db.getEntries();

        EntriesAdapter entriesAdapter = new EntriesAdapter(MainActivity.this, entries);
        rvEntries.setLayoutManager(new LinearLayoutManager(this));
        rvEntries.setAdapter(entriesAdapter);
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
                dealWithResultOfSignIn(account);

                Log.i(TAG, "Login succeeded: " + REQUEST_CODE_SIGN_IN);
            } else {
                Log.e(TAG, "Login failed: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void silentSignIn() {
        mAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();

        mAuthService = AccountAuthManager.getService(this, mAuthParams);

        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                dealWithResultOfSignIn(authAccount);
                account = authAccount;
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
        Task<Void> task = mAuthService.signOut();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Logout succeeded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "Logout failed");
            }
        });
    }

    // change method name
    private void dealWithResultOfSignIn(AuthAccount authAccount) {
        Toast.makeText(this, "Welcome back, " + authAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}