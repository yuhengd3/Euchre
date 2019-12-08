package com.example.euchre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class GameScreen extends AppCompatActivity {
    private Card firstCard = new Card(-1, -1);
    private String myName;
    private TextView messages;
    private CardButton[] myCards = new CardButton[5];
    private TextView scores;

    private class CardButton {
        private Card card;
        private Button button;
        public CardButton(int id, JSONObject c) {
            button = findViewById(id);
            card = new Card(-1, -1);
            try {
                card = new Card(c.getInt("suit"), c.getInt("rank"));
            } catch (JSONException e) {
                Log.v("ASD", e.toString());
            }
            button.setText(card.toString());
        }

        public void setCard(Card newC) {
            card = newC;
            button.setText(card.toString());
        }

        public void cardsToClick() {
            button.setClickable(true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("player", myName);
                        obj.put("suit", card.getSuit());
                        obj.put("rank", card.getRank());
                        socket.emit("cardPlayed", obj);
                        Log.d("ASD", "play " + obj.toString());
                        for (CardButton c : myCards) {
                            c.disable();
                        }
                        button.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Log.e("ASD", e.toString());
                    }
                }
            });
        }
        public void disable() { button.setClickable(false); }
        public void enable() { button.setClickable(true);}
        public void changeCard() {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("suit", card.getSuit());
                        obj.put("rank", card.getRank());
                        socket.emit("changeCard", obj);
                        Log.d("ASD", "change card " + card.toString());
                        setCard(firstCard);
                        for (CardButton c : myCards) {
                            c.disable();
                        }
                    } catch (JSONException e) {
                        Log.e("ASD", e.toString());
                    }
                }
            });
        }
    }

    class Card {
        private int suit;
        private int rank;

        public Card(int s, int r) {
            suit = s;
            rank = r;
        }
        public int getSuit() { return suit; }
        public int getRank() { return rank; }

        public String toString() {
            return getResources().getStringArray(R.array.suits)[suit] + " "
                    + getResources().getStringArray(R.array.ranks)[rank - 9];
        }
    }

    private Socket socket;
    {
        try {
            socket = IO.socket(Constants.API_BASE);
        } catch (URISyntaxException e) {
            Log.v("createGame", e.toString());
        }
    }

    private Emitter.Listener onDeal = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    //JSONArray cards = new JSONArray();
                    //Card theCard = new Card(-1, -1);
                    Log.d("ASD", "run: deal");
                    try {
                        JSONArray cards = data.getJSONArray("cards");
                        Log.v("DEAL", cards.toString());
                        myCards[0] = new CardButton(R.id.button1, cards.getJSONObject(0));
                        myCards[1] = new CardButton(R.id.button2, cards.getJSONObject(1));
                        myCards[2] = new CardButton(R.id.button3, cards.getJSONObject(2));
                        myCards[3] = new CardButton(R.id.button4, cards.getJSONObject(3));
                        myCards[4] = new CardButton(R.id.button5, cards.getJSONObject(4));
                        JSONObject obbj = data.getJSONObject("card");
                        firstCard = new Card(obbj.getInt("suit"), obbj.getInt("rank"));
                        String dealer = data.getString("dealer");
                        messages.setText("GAME START!!!\n");
                        messages.append("The dealer is " + dealer + ".\n");
                        messages.append("The first card is " + firstCard.toString() + ".\n");
                        for (CardButton c : myCards) {
                            c.enable();
                        }
                    } catch (JSONException e) {
                        Log.v("Gosn", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onOrder = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Make it Trump", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("player", myName);
                                o.put("result", true);
                                o.put("trump", firstCard.getSuit());
                                socket.emit("order.", o);
                            } catch (JSONException e){
                                Log.v("ASD", e.toString());
                            }
                        }
                    });
                    builder.setNegativeButton("Pass", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                JSONObject o = new JSONObject();
                                o.put("player", myName);
                                o.put("result", false);
                                socket.emit("order.", o);
                            } catch (JSONException e){
                                Log.v("ASD", e.toString());
                            }
                        }
                    });
                    builder.setMessage("Do you want to make "
                            + getResources().getStringArray(R.array.suits)[firstCard.getSuit()] + " the Trump suit?");
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    };

    private Emitter.Listener onTrump = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        int trumpSuit = data.getInt("trump");
                        String player = data.getString("player");
                        messages.append(player + " makes " + getResources().getStringArray(R.array.suits)[trumpSuit] + "the Trump suit!\n");
                        TextView trumpText = findViewById(R.id.trump);
                        trumpText.setText("Trump: " + getResources().getStringArray(R.array.suits)[trumpSuit]);
                    } catch (JSONException e) {
                        Log.v("ASD", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onPickUp = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.v("ASD", "pick up");
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.v("ASD", "dealer pick up ");
                    Toast.makeText(GameScreen.this, "Select a card to get rid of.",
                            Toast.LENGTH_LONG).show();
                    for (CardButton c : myCards) {
                        c.changeCard();
                    }
                }
            });
        }
    };

    private Emitter.Listener onPickTrumpSuit = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
                    LayoutInflater inflater = GameScreen.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.choose_trump_suit, null);
                    builder.setView(dialogView);
                    builder.setCancelable(false);
                    final AlertDialog dialog = builder.create();
                    Button[] buttons = new Button[3];
                    buttons[0] = dialogView.findViewById(R.id.trump0);
                    buttons[1] = dialogView.findViewById(R.id.trump1);
                    buttons[2] = dialogView.findViewById(R.id.trump2);
                    int j = 0;
                    for (int i = 0; i < 4; i++) {
                        if (i == firstCard.getSuit()) continue;
                        final int c = i;
                        buttons[j].setText(getResources().getStringArray(R.array.suits)[i]);
                        buttons[j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                JSONObject o = new JSONObject();
                                try {
                                    o.put("result", true);
                                    o.put("player", myName);
                                    o.put("trump", c);
                                    socket.emit("select suit", o);
                                    dialog.dismiss();
                                } catch (JSONException e) {
                                    Log.e("ASD", "onClick: " + e.toString());
                                }
                            }
                        });
                        j++;
                    }
                    Button passButton = dialogView.findViewById(R.id.pass);
                    passButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject o = new JSONObject();
                            try {
                                o.put("result", false);
                                o.put("player", myName);
                                socket.emit("select suit", o);
                                dialog.dismiss();
                            } catch (JSONException e) {
                                Log.e("ASD", "onClick: " + e.toString());
                            }
                        }
                    });
                    dialog.show();
                }
            });
        }
    };

    private Emitter.Listener onPlayCard = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.v("ASD", "pick up");
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (CardButton c : myCards) {
                        c.cardsToClick();
                    }
                    Toast.makeText(GameScreen.this, "Your Turn!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onCardsUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        Card newCard = new Card(data.getInt("suit"), data.getInt("rank"));
                        messages.append(data.getString("player") + "played " + newCard.toString() + ".\n");
                    } catch (JSONException e) {
                        Log.v("Gosn", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onTrick = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String player = data.getString("player");
                        messages.append(player + " gets a trick!!!\n");
                    } catch (JSONException e) {
                        Log.v("ASD", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onScoreUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        int oneScore = data.getInt("teamOne");
                        int twoScore = data.getInt("teamTwo");
                        String message = data.getString("message");
                        messages.append(message + "\n");
                        scores.setText("TeamOne: " + oneScore + "\nTeamTwo: " + twoScore);
                    } catch (JSONException e) {
                        Log.v("ASD", e.toString());
                    }
                }
            });
        }
    };

    private Emitter.Listener onGameOver = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GameScreen.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String winner = data.getString("winner");
                        String player1 = data.getString("player1");
                        String player2 = data.getString("player2");
                        messages.append(winner + " wins the game!!\n");
                        String message;
                        if (myName.equals(player1) || myName.equals(player2)) {
                            message = "Congratulations! You Win!";
                        } else {
                            message = "Sorry, you lost.";
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                socket.disconnect();
                                Intent intent = new Intent(GameScreen.this, createGame.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } catch (JSONException e) {
                        Log.v("ASD", e.toString());
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        Intent intent = getIntent();
        myName = intent.getStringExtra("myName");

        messages = findViewById(R.id.messages);
        scores = findViewById(R.id.scores);

        socket.connect();
        socket.on("deal", onDeal);
        socket.on("order?", onOrder);
        socket.on("make trump", onTrump);
        socket.on("pick up", onPickUp);
        socket.on("pick trump suit", onPickTrumpSuit);
        socket.on("play card", onPlayCard);
        socket.on("cardsUpdate", onCardsUpdate);
        socket.on("trick", onTrick);
        socket.on("scoreUpdate", onScoreUpdate);
        socket.on("gameOver", onGameOver);
    }
}
