package com.example.dropball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import java.util.Random;

public class Game extends AppCompatActivity {

    private ImageView[] boxes = new ImageView[6];
    private Button[] guessButtons = new Button[6];  // Changed to Button
    private Button startButton;

    private int ballPosition;
    private boolean animationInProgress = false;
    private int remainingAttempts = 3;

    private ImageView ballImageView;

    private int totalScore = 0;

    private int selectedGuess = -1; // -1 indicates no guess selected

    private int currentBallPosition; // Add this variable
    private Button selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startButton = findViewById(R.id.startButton);

        // Initialize image views for boxes
        for (int i = 0; i < 6; i++) {
            int boxId = getResources().getIdentifier("box" + (i + 1), "id", getPackageName());
            boxes[i] = findViewById(boxId);
        }

        // Initialize guess buttons
        for (int i = 0; i < 6; i++) {
            int buttonId = getResources().getIdentifier("guessButton" + (i + 1), "id", getPackageName());
            guessButtons[i] = findViewById(buttonId);
        }

        resetAttempts();
        ballImageView = findViewById(R.id.ballImageView);

    }

    public void goToInstructions4(View v) {
        Intent i = new Intent(this, Instructions4.class);
        startActivity(i);
        finish();
    }

    public void startGame(View view) {
        if (!animationInProgress) {
            // Check if at least one guess button is selected
            boolean isAnyButtonSelected = false;
            for (Button button : guessButtons) {
                if (button.isSelected()) {
                    isAnyButtonSelected = true;
                    break;
                }
            }

            if (!isAnyButtonSelected) {
                showPopupMessage("Please select a guess before starting the game.");
                return;
            }

            // Reset UI and start the ball animation
            resetUI();
            animateBall();
        }
    }

    private void resetUI() {
        // Reset UI elements to their initial state
        int[] boxImages = {R.drawable.meme1, R.drawable.meme2, R.drawable.meme3, R.drawable.meme4, R.drawable.meme5, R.drawable.meme6};

        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setImageResource(boxImages[i]);
        }

        // Reset guess buttons (changed to Button)
        for (Button button : guessButtons) {
            button.setSelected(false);
            button.setEnabled(true);
        }
    }

    private void animateBall() {
        // Simulate ball movement and set the final position
        Random random = new Random();
        ballPosition = random.nextInt(6);
        currentBallPosition = ballPosition; // Store the initial ball position

        int animationDuration = 2000; // 2 seconds (adjust as needed)
        int numberOfDisappearances = 5;

        // Calculate the interval between each disappearance and reappearance
        int interval = animationDuration / (numberOfDisappearances * 2);

        // Simulate a series of disappearances and reappearances
        for (int i = 0; i < numberOfDisappearances; i++) {
            final int finalI = i;

            // Disappear
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ballImageView.setVisibility(View.INVISIBLE);
                }
            }, interval * (2 * finalI));

            // Reappear
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetUI();
                    currentBallPosition = random.nextInt(6); // Use a new random position for each reappearance
                    ballImageView.setImageResource(getImageResourceByPosition(currentBallPosition));
                    setBallPosition(); // Set the position programmatically
                    ballImageView.setVisibility(View.VISIBLE);
                }
            }, interval * (2 * finalI + 1));
        }

        // Reset the ball's visibility at the end of the animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ballImageView.setVisibility(View.INVISIBLE);
                resetUI();
                showResult();
                animationInProgress = false;
            }
        }, animationDuration);

        animationInProgress = true;
    }


    // Modify setBallPosition method to use currentBallPosition
    private void setBallPosition() {
        int[] boxLeftMargins = {13, 150, 250, 350, 450, 550}; // Adjust these margins as needed
        int leftMargin = boxLeftMargins[currentBallPosition];
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ballImageView.getLayoutParams();
        params.leftMargin = leftMargin;
        ballImageView.setLayoutParams(params);
    }

    // Helper method to get the image resource based on ball position
    private int getImageResourceByPosition(int position) {
        // Add your logic to return the appropriate image resource based on the position
        // For example: return R.drawable.image1 for position 0, R.drawable.image2 for position 1, and so on.
        return R.drawable.bola; // Replace this with your actual logic
    }

    private void resetAttempts() {
        remainingAttempts = 3;
    }

    private void showResult() {
        // Display the result only if the animation was in progress
        if (animationInProgress) {
            if (currentBallPosition >= 0 && currentBallPosition < 6) {
                // Set the image directly for the box that corresponds to the ball position
                boxes[currentBallPosition].setImageResource(R.drawable.bola);

                boolean isCorrectGuess = selectedGuess == currentBallPosition;

                // Check which guess button is selected
                for (int i = 0; i < 6; i++) {
                    if (guessButtons[i].isSelected()) {
                        // Compare the index of the selected button with the ball position
                        isCorrectGuess = i + 1 == currentBallPosition + 1; // Add 1 to align with box indices
                        break;
                    }
                }

                // Enable all guess buttons before showing the result
                for (Button button : guessButtons) {
                    button.setEnabled(true);
                }

                // Check if the selected guess button matches the ball position
                if (isCorrectGuess) {
                    // You win! Implement the winning logic (e.g., show a message)
                    showPopupMessage("Congratulations! You guessed correctly!");
                } else {
                    // Implement the logic for when the guessed position is incorrect
                    showPopupMessage("Sorry! You guessed incorrectly.");
                }

                // Calculate the score
                int score = calculateScore(isCorrectGuess, currentBallPosition);
                totalScore += score; // Increment the total score
                remainingAttempts--; // Decrease remaining attempts

                if (remainingAttempts <= 0) {
                    // Launch NextActivity with the total attempts and score
                    launchNextActivity(totalScore);
                } else {
                    // Show the remaining attempts
                    Toast.makeText(Game.this, "Remaining Attempts: " + remainingAttempts, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // Modify the calculateScore method to take a boolean parameter indicating correct guess
    private int calculateScore(boolean isCorrectGuess, int ballPosition) {
        // Adjust your scoring logic as needed
        return isCorrectGuess ? 1 : 0;
    }

    private void launchNextActivity(int score) {
        Intent intent = new Intent(Game.this, Results.class);
        intent.putExtra("totalAttempts", 3); // Always send the total attempts as 3
        intent.putExtra("totalScore", score); // Pass the score as an extra
        startActivity(intent);
        finish(); // Optional: Finish the current activity to prevent going back.
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
                stopService(new Intent(Game.this, MusicService.class));
                finish();
            }
        });
    }

    private void showPopupMessage(String message) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.pop_up_message, null);
        dialogBuilder.setView(dialogView);

        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogMessage.setText(message);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOkay = dialogView.findViewById(R.id.btnOkay);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        btnOkay.setOnClickListener(v -> alertDialog.dismiss()); // Modify this based on what "Okay" should do
    }


    public void onGuessButtonClick(View view) {
        Button button = (Button) view;

        // Reset the state of all guess buttons and find which button was clicked
        for (int i = 0; i < guessButtons.length; i++) {
            guessButtons[i].setSelected(false); // You might not need to visually deselect, but ensure consistent logic
            guessButtons[i].setBackgroundResource(android.R.color.transparent);

            if (view.getId() == guessButtons[i].getId()) {
                selectedGuess = i; // Store the index of the selected button
                selectedButton = guessButtons[i]; // Store the reference to the selected button
                selectedButton.setBackgroundResource(R.drawable.button_selected_background);
                view.setSelected(true); // This is for your logic, might not change visual state
            }

            button.setBackgroundTintList(null);
        }
    }


}
