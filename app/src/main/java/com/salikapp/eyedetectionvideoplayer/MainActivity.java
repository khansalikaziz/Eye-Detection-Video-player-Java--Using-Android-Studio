package com.salikapp.eyedetectionvideoplayer;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CancellationSignal cancellationSignal = null;

    // create an authenticationCallback
    private BiometricPrompt.AuthenticationCallback authenticationCallback;
    Button btn1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.btn);
        btn1.setVisibility(View.INVISIBLE);

        authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(
                    int errorCode, CharSequence errString)
            {
                startActivity(new Intent(MainActivity.this,Splash.class));

                super.onAuthenticationError(errorCode, errString);
                notifyUser("Authentication Error : " + errString);
            }
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result)
            {
                btn1.setVisibility(View.VISIBLE);
                super.onAuthenticationSucceeded(result);
                notifyUser("Authentication Succeeded");
                // or start a new Activity
            }
        };

        checkBiometricSupport();

        BiometricPrompt biometricPrompt = new BiometricPrompt
                .Builder(getApplicationContext())
                .setTitle("Sign in using fingerprint")
                .setSubtitle("")
                .setDescription("")
                .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void
                    onClick(DialogInterface dialogInterface, int i)
                    {
                        startActivity(new Intent(MainActivity.this,Splash.class));
                        Toast.makeText(MainActivity.this, "Please authenticate to move further", Toast.LENGTH_SHORT).show();
                        notifyUser("Authentication Cancelled");
                    }
                }).build();


        biometricPrompt.authenticate(
                getCancellationSignal(),
                getMainExecutor(),
                authenticationCallback);



    }


    public void pick(View view) {


     startActivity(new Intent(MainActivity.this,Trial.class));
     finish();

    }








    private CancellationSignal getCancellationSignal()
    {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(
                new CancellationSignal.OnCancelListener() {
                    @Override public void onCancel()
                    {

                        notifyUser("Authentication was Cancelled by the user");
                    }
                });
        return cancellationSignal;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private Boolean checkBiometricSupport()
    {
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isDeviceSecure()) {
            notifyUser("Fingerprint authentication has not been enabled in settings");
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled");
            return false;
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return true;
        }
        else
            return true;
    }


    private void notifyUser(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
