package com.dnaanalyzer.util;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dnaanalyzer.DnaApplication;
import com.dnaanalyzer.LoginActivity;
import com.dnaanalyzer.R;
import com.dnaanalyzer.UploadActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginUtils {
    public static void loginAs(@NonNull Activity activity, @Nullable FirebaseUser user) {
        if (user == null) {
            return;
        }
        DnaApplication.get(activity).setUid(user.getUid());

        Intent intent = new Intent(activity, UploadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        activity.startActivity(intent);
    }

    public static void logout(@NonNull Activity activity) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        LoginUtils.getGoogleSignInClient(activity).signOut();

        Toast.makeText(activity, "Successfully Logout!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static GoogleSignInClient getGoogleSignInClient(@NonNull Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(activity, gso);
    }
}
