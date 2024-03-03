package com.example.dropball;

import android.content.DialogInterface;
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
    private Button[] guessButtons = new Button[6];
    private Button startButton;

    private int selectedCount = 0; // Tracks the number of selected guess buttons

    private int remainingAttempts = 3;

    private Button selectedButton = null;

    private int ballPosition;
    private boolean animationInProgress = false;

    private ImageView ballImageView;

    private int currentBallPosition;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        startButton = findViewById(R.id.startButton);

        for (int i = 0; i < 6; i++) {
            int boxId = getResources().getIdentifier("box" + (i + 1), "id", getPackageName());
            boxes[i] = findViewById(boxId);

            int buttonId = getResources().getIdentifier("guessButton" + (i + 1), "id", getPackageName());
            guessButtons[i] = findViewById(buttonId);
            guessButtons[i].setTag(false); // Initialize button tag to false
        }

        ballImageView = findViewById(R.id.ballImageView);
    }


    public void goToInstructions4(View v) {
        Intent i = new Intent(this, Instructions4.class);
        startActivity(i);
        finish();
    }

    public void startGame(View view) {
        if (!animationInProgress) {
            if (selectedButton != null) {
                resetUI();
                animateBall();
            } else {
                showPopupMessage("Please select a guess before starting the game.");
            }
        }
    }


    private int countSelectedGuesses() {
        int count = 0;
        for (Button button : guessButtons) {
            boolean isSelected = (boolean) button.getTag();
            if (isSelected) {
                count++;
            }
        }
        return count;
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

        int animationDuration = 5000; // 5 seconds
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

                boolean isCorrectGuess = false;

                // Check which guess button is selected
                for (int i = 0; i < 6; i++) {
                    if (guessButtons[i].isSelected()) {
                        isCorrectGuess = i == currentBallPosition;
                        break;
                    }
                }

                // Check if the selected guess button matches the ball position
                if (isCorrectGuess) {
                    // You win! Implement the winning logic (e.g., show a message)
                    showPopupMessage("Congratulations! You guessed correctly!");
                } else {
                    // Implement the logic for when the guessed position is incorrect
                    showPopupMessage("Sorry! You guessed incorrectly.");
                }

                // Enable all guess buttons after showing the result
                for (Button button : guessButtons) {
                    button.setEnabled(true);
                }

                // Calculate the score
                int score = calculateScore(isCorrectGuess, currentBallPosition);
                remainingAttempts--;

                if (remainingAttempts <= 0) {
                    // Launch NextActivity with the total attempts and score
                    launchNextActivity(score);
                } else {
                    // Show the remaining attempts
                    showPopupMessage("Remaining Attempts: " + remainingAttempts);
                }
            }
        }
    }

    // Modify the calculateScore method to take a boolean parameter indicating correct guess
    private int calculateScore(boolean isCorrectGuess, int ballPosition) {
        // Implement your scoring logic here
        // For example, you can deduct points for each incorrect guess
        // and provide bonus points for correct guesses.
        // Adjust this logic based on your game's scoring rules.
        // For simplicity, let's assume each correct guess gives 10 points.
        return isCorrectGuess ? 10 : 0;
    }

    private void launchNextActivity(int score) {
        Intent intent = new Intent(Game.this, Results.class);
        intent.putExtra("totalAttempts", 3); // Always send the total attempts as 3
        intent.putExtra("totalScore", score);
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

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOkay = dialogView.findViewById(R.id.btnOkay);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        btnOkay.setOnClickListener(v -> {
            finish();
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
        if (!animationInProgress) {
            if (selectedButton == null) {
                // No button selected, select the clicked button
                selectedButton = button;
                selectedButton.setBackgroundResource(R.drawable.button_selected_background);
                selectedButton.setTag(true);
                selectedCount++;
            } else if (selectedButton.getId() == button.getId()) {
                // Deselect the clicked button
                selectedButton.setBackgroundResource(android.R.color.transparent);
                selectedButton.setTag(false);
                selectedButton = null;
                selectedCount--;
            } else {
                // Another button is already selected, show message
                showPopupMessage("You have already selected a guess.");
            }

            // Remove background tint
            button.setBackgroundTintList(null);

        }

    }
}
