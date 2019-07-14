package com.dnaanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dnaanalyzer.util.LoginUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginCodeActivity extends BaseActivity {
    private static final String TAG = "PhoneLoginCodeActivity";

    private EditText editTextCode;
    private Button buttonConfirm;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Callbacks callbacks = new Callbacks();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_code);

        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        buttonConfirm = findViewById(R.id.btnConfirmCode);
        buttonConfirm.setOnClickListener(__ -> confirmCode());

        requestOtpCode();
    }

    private void confirmCode() {
        String code = editTextCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Code must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mVerificationId == null) {
            Log.d(TAG, "mVerificationId == null");
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void requestOtpCode() {
        String phone = getIntent().getStringExtra("phone");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,            // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        buttonConfirm.setEnabled(false);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoginUtils.loginAs(PhoneLoginCodeActivity.this, mAuth.getCurrentUser());
                        } else {
                            buttonConfirm.setEnabled(true);
                            String message = "Something is wrong, we will fix it soon...";
                            if (task.getException() != null && task.getException().getMessage() != null) {
                                message = task.getException().getMessage();
                            }

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(PhoneLoginCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class Callbacks extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);
            String code = credential.getSmsCode();
            if (code != null) {
                editTextCode.setText(code);
            }

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            String error = "";
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                error = "Error: invalid request";

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                error = "Error: The SMS quota for the project has been exceeded";
            }

            // Show a message and update the UI
            // ...
            Toast.makeText(PhoneLoginCodeActivity.this, error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    }
}
