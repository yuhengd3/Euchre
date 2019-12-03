package com.example.euchre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final TextView text = findViewById(R.id.text1);
//        Button button = findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.API_BASE + '/',
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                text.setText(response);
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        text.setText("That didn't work!");
//                        System.out.println(error + "what!");
//                    }
//                });
//                queue.add(stringRequest);
//            }
//        });
    }
}
