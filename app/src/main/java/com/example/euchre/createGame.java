package com.example.euchre;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class createGame extends AppCompatActivity {
    private EditText createName;
    private Button createButton;
    private EditText joinName;
    private EditText roomId;
    private Button joinButton;
    private TextView playerList;
    private TextView roomIdShow;

    private String myName;

    private AlertDialog.Builder builder;

    private Socket socket;
    {
        try {
            socket = IO.socket(Constants.API_BASE);
        } catch (URISyntaxException e) {
            System.out.println(e + " What?");
        }
    }

    private Emitter.Listener onNewPlayer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            createGame.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    JSONArray players = new JSONArray();
                    String roomName = "";
                    try {
                        players = data.getJSONArray("players");
                        Log.v("ASD", players.toString());
                        playerList.setText("");
                        for (int i = 0; i < players.length(); i++) {
                            playerList.append("player" + new Integer(i + 1).toString() + ": " + players.get(i).toString() + "\n");
                        }
                        roomName = data.getString("room");
                        roomIdShow.setText("Room ID: " + roomName);
                    } catch (JSONException e) {
                        Log.v("Gosn", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            createGame.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String message = "what?";
                    try {
                        message = data.getString("message");
                        Log.v("SDS", "What???");
                        builder.setMessage(message);
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (JSONException e) {
                        Log.v("Gosn", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onGameCreated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            createGame.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String roomNum = "";
                    try {
                        roomNum = data.getString("room");
                        roomIdShow.setText("Room Id: " + roomNum);
                    } catch (JSONException e) {
                        Log.v("Gosn", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onGameStart = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            createGame.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        String myTeammate = data.getString("myTeammate");
                        String myTeam = data.getString("myTeam");
                        Intent intent = new Intent(createGame.this, GameScreen.class);
                        intent.putExtra("myName", myName);
                        intent.putExtra("myTeammate", myTeammate);
                        intent.putExtra("myTeam", myTeam);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Log.e("ASD", e.toString());
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        createName = findViewById(R.id.createName);
        createButton = findViewById(R.id.createButton);
        joinName = findViewById(R.id.joinName);
        roomId = findViewById(R.id.roomId);
        joinButton = findViewById(R.id.joinButton);
        playerList = findViewById(R.id.playerList);
        roomIdShow = findViewById(R.id.roomIdShow);

        builder = new AlertDialog.Builder(createGame.this);
        builder.setCancelable(true);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        socket.connect();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createName.getText().toString().trim().equals("")) {
                    builder.setMessage("Your name shouldn't be empty");
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    JSONObject o = new JSONObject();
                    try {
                        myName = createName.getText().toString();
                        o.put("name", myName);
                        socket.emit("createGame", o);
                    } catch (JSONException e) {
                        Log.e("ASD", "Json error" + e.toString());
                    }
                }
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinName.getText().toString().trim().equals("")
                        || roomId.getText().toString().trim().equals("")) {
                    builder.setMessage("Both your name and room name shouldn't be empty");
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    JSONObject o = new JSONObject();
                    try {
                        myName = joinName.getText().toString();
                        o.put("name", myName);
                        o.put("room", roomId.getText().toString());
                        socket.emit("joinGame", o);
                    } catch (JSONException e) {
                        Log.e("ASD", "Json error" + e.toString());
                    }
                }
            }
        });

        socket.on("gameCreated", onGameCreated);

        socket.on("newPlayer", onNewPlayer);

        socket.on("err", onError);

        socket.on("gameStart", onGameStart);
    }
}
