package com.example.dropball;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


public class Instructions2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions2);
    }

    public void goToInstructions3(View v){
        Intent i = new Intent(this, Instructions3.class);
        startActivity(i);
        finish();
    }

    public void goToInstructions(View v) {
        Intent i = new Intent(this, Instructions2.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Navigate back to MainActivity without showing the exit confirmation dialog
            goToInstructions(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
