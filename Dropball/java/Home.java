package com.example.dropball;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void goToDevelopers(View v){
        Intent i = new Intent(this, Developers.class);
        startActivity(i);
        finish();
    }

    public void goToInstructions(View v){
        Intent i = new Intent(this, Instructions.class);
        startActivity(i);
        finish();
    }

    public void goToGame(View v){
        Intent i = new Intent(this, Game.class);
        startActivity(i);
        finish();
    }
    public void goToMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goToMain(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
