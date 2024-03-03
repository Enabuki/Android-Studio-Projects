package com.example.dropball;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
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
}
