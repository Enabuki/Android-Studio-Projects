package com.example.dropball;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    public void goToInstructions2(View v){
        Intent i = new Intent(this, Instructions2.class);
        startActivity(i);
        finish();
    }

    public void goToHome(View v) {
        Intent i = new Intent(this, Instructions2.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Navigate back to MainActivity without showing the exit confirmation dialog
            goToHome(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
