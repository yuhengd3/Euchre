package com.example.euchre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button = findViewById(R.id.button333);
//        final TextView text = findViewById(R.id.text333);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text.append("what??? dsadfjklasjsj\nwhat???sakjdfksaj \n");
//            }
//        });
//        text.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = new Intent(this, createGame.class);
        startActivity(intent);
        finish();
    }
}
