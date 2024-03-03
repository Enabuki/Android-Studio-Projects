package com.example.dropball;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MusicService.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitConfirmationDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitConfirmationDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        dialogBuilder.setView(dialogView);

        // Button references
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOkay = dialogView.findViewById(R.id.btnOkay);

        // Create AlertDialog
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Set click listener for the cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Dismiss the dialog
            }
        });

        // Set click listener for the okay button
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MusicService.class));
                finish();
            }
        });
    }

    // Method to handle okay button click
    public void okayExit(View view) {
        stopService(new Intent(MainActivity.this, MusicService.class));
        finish();
    }

    // Method to handle cancel button click
    public void cancelExit(View view) {
        alertDialog.dismiss(); // Dismiss the dialog
    }

    public void goToHome(View v){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

    public void goToDevelopers(View v){
        Intent i = new Intent(this, Developers.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }
}
