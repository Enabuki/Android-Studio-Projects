package com.example.dropball;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class Results extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView resultTextView = findViewById(R.id.resultTextView);
        TextView attemptsTextView = findViewById(R.id.attemptsTextView);
        TextView scoreTextView = findViewById(R.id.scoreTextView);

        int totalAttempts = getIntent().getIntExtra("totalAttempts", 0);
        int totalScore = getIntent().getIntExtra("totalScore", 0); // Retrieve the score from intent extras

        if (totalAttempts > 0) {
            resultTextView.setText("Game Over!");
            attemptsTextView.setText("Total Attempts: " + totalAttempts);
            scoreTextView.setText("Total Score: " + totalScore); // Display the total score
        } else {
            // Handle unexpected case (no attempts)
            resultTextView.setText("No attempts recorded.");
            attemptsTextView.setText("");
            scoreTextView.setText("");
        }
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        dialogBuilder.setView(dialogView);

        // Button references
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOkay = dialogView.findViewById(R.id.btnOkay);

        // Create AlertDialog
        AlertDialog alertDialog = dialogBuilder.create();
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
                stopService(new Intent(Results.this, MusicService.class));
                finish();
            }
        });
    }


    public void goToMainActivity(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void goToExit(View v){
        stopService(new Intent(this, MusicService.class)); // Stop the music service
        finish();
    }
}
