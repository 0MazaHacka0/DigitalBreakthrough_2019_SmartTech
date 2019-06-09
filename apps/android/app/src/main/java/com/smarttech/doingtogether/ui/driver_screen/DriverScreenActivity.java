package com.smarttech.doingtogether.ui.driver_screen;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smarttech.doingtogether.R;

import org.w3c.dom.Text;

public class DriverScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Информация");

        findViewById(R.id.pick_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {
//                        mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        ((TextView) findViewById(R.id.driver_free_seats)).setText("Свободных мест: 2");
                        Toast.makeText(getApplicationContext(), "Водитель принял вашу заявку!", Toast.LENGTH_LONG).show();
                    }
                }.start();
            }
        });
    }

}
