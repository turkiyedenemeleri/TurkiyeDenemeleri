package com.turkiyedenemeleri;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.turkiyedenemeleri.base.BaseActivity;
import com.turkiyedenemeleri.customviews.TDTextView;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import butterknife.BindView;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.TDtv_link) TDTextView link;

    @BindView(R.id.auth_button) DigitsAuthButton digitsButton;
    private static final String TWITTER_KEY = "nIhyru1O7xMYNo41wwja2Jxwo";
    private static final String TWITTER_SECRET = "1paz0cu4JKx4f7BObdJ0b8Wrj7gmz4CTziuXlOxpcafPKd1znG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Digits.Builder digitsBuilder = new Digits.Builder();
        Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build());
        Digits.logout();

    }

    @Override
    protected void setInitialValues() {
        link.setMovementMethod(LinkMovementMethod.getInstance());

        digitsButton.setBackgroundResource(R.drawable.btn_accept);
        digitsButton.setText(R.string.accaptandcontinue);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(DigitsException exception) {
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnClick(R.id.regisbutton)
    public void regisButtonClick(){
        WelcomeActivityPermissionsDispatcher.showSmsWithCheck(WelcomeActivity.this);
    }
    @Override
    protected void initViews() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }



    @NeedsPermission(Manifest.permission.RECEIVE_SMS)
    void showSms() {
        digitsButton.performClick();
    }

    @OnShowRationale(Manifest.permission.RECEIVE_SMS)
    void showRationaleForSms(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_sms_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.RECEIVE_SMS)
    void receiveSmsDenied(){
        digitsButton.performClick();
    }

}