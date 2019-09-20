package me.gfred.hotelng;

import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

class FirebaseUtil {

    static final int RC_SIGN_IN = 123;
    private static FirebaseUtil firebaseUtil;
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static MainActivity caller;

    private FirebaseUtil() {
    }

    static void openReference(final MainActivity callerActivity) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            firebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            authStateListener = firebaseAuth -> {
                if (firebaseAuth.getCurrentUser() == null) {
                    FirebaseUtil.signIn();
                } else {
                    Toast.makeText(caller.getBaseContext(),
                            "Welcome back", Toast.LENGTH_SHORT).show();
                }
            };
        }
    }

    private static void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.activity_welcome)
                .setEmailButtonId(R.id.welcomeButton)
                .setGoogleButtonId(R.id.dummy)
                .build();

        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    static void attachListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    static void detachListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
