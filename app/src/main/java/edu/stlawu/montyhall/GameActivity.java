package edu.stlawu.montyhall;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ImageButton doorOne;
    private ImageButton doorTwo;
    private ImageButton doorThree;
    private int goat1;
    private int goat2;
    private int car;
    private int round = 0;  // keeps track of if they've already chosen one door or not
    private int revealed = -1;
    private int wins;
    private int losses;
    private int total_wins;
    private int total_losses;
    private int choice;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        doorOne = findViewById(R.id.door1);
        doorTwo = findViewById(R.id.door2);
        doorThree = findViewById(R.id.door3);

        assign();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button19);

        doorOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                doorOne.setImageResource(R.drawable.closed_door_chosen);
                switch (revealed) {
                    case 1:
                        doorThree.setImageResource(R.drawable.closed_door);
                        break;
                    case 2:
                        doorTwo.setImageResource(R.drawable.closed_door);
                        break;
                }
                doorOne.setEnabled(false);
                doorTwo.setEnabled(false);
                doorThree.setEnabled(false);
                choice = 0;

                countdown(view);
            }
        });

        doorTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                doorTwo.setImageResource(R.drawable.closed_door_chosen);
                switch (revealed) {
                    case 2:
                        doorOne.setImageResource(R.drawable.closed_door);
                        break;
                    case 0:
                        doorThree.setImageResource(R.drawable.closed_door);
                        break;
                }
                doorOne.setEnabled(false);
                doorTwo.setEnabled(false);
                doorThree.setEnabled(false);
                choice = 1;

                countdown(view);
            }
        });

        doorThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                doorThree.setImageResource(R.drawable.closed_door_chosen);
                switch (revealed) {
                    case 1:
                        doorOne.setImageResource(R.drawable.closed_door);
                        break;
                    case 0:
                        doorTwo.setImageResource(R.drawable.closed_door);
                        break;
                }
                doorOne.setEnabled(false);
                doorTwo.setEnabled(false);
                doorThree.setEnabled(false);
                choice = 2;

                countdown(view);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        total_wins = getPreferences(MODE_PRIVATE).getInt("TOTAL_WINS", 0);
        total_losses = getPreferences(MODE_PRIVATE).getInt("TOTAL_LOSSES", 0);

        TextView total_win_text = findViewById(R.id.total_wins);
        total_win_text.setText("  " + Integer.toString(total_wins));

        TextView total_loss_text = findViewById(R.id.total_losses);
        total_loss_text.setText("  " + Integer.toString(total_losses));

        boolean new_clicked = getSharedPreferences(
                "MontyHall", Context.MODE_PRIVATE).getBoolean(
                        "NEWCLICKED", false);

        if (new_clicked) {
            wins = 0;
            losses = 0;
            choice = -1;
            revealed = -1;
            round = 0;
        } else {
            wins = getPreferences(MODE_PRIVATE).getInt("WINS", 0);
            losses = getPreferences(MODE_PRIVATE).getInt("LOSSES", 0);
            round = getPreferences(MODE_PRIVATE).getInt("ROUND", 0);
            revealed = getPreferences(MODE_PRIVATE).getInt("REVEALED", -1);
            goat1 = getPreferences(MODE_PRIVATE).getInt("GOAT1", -1);
            goat2 = getPreferences(MODE_PRIVATE).getInt("GOAT2", -1);
            car = getPreferences(MODE_PRIVATE).getInt("CAR", -1);
            choice = getPreferences(MODE_PRIVATE).getInt("CHOICE", -1);
            switch (round) {
                case 0:
                    assign();
                    choice = -1;
                    break;
                case 1:
                    switch (revealed) {
                        case 0:
                            doorOne.setImageResource(R.drawable.goat);
                            doorOne.setEnabled(false);
                            break;
                        case 1:
                            doorTwo.setImageResource(R.drawable.goat);
                            doorTwo.setEnabled(false);
                            break;
                        case 2:
                            doorThree.setImageResource(R.drawable.goat);
                            doorThree.setEnabled(false);
                            break;
                    }

                    switch (choice) {
                        case 0:
                            doorOne.setImageResource(R.drawable.closed_door_chosen);
                            break;
                        case 1:
                            doorTwo.setImageResource(R.drawable.closed_door_chosen);
                            break;
                        case 2:
                            doorThree.setImageResource(R.drawable.closed_door_chosen);
                            break;
                    }

                    TextView view = findViewById(R.id.conclusion);
                    view.setText("Switch doors?");
                    break;
                case 2:
                    if (choice == car) {
                        wins--;
                        total_wins--;
                    } else {
                        losses--;
                        total_losses--;
                    }
                    revealAnswer(choice);
            }
        }

        TextView win_count = findViewById(R.id.win_count);
        win_count.setText("  " + Integer.toString(wins) + "   ");

        TextView loss_count = findViewById(R.id.loss_count);
        loss_count.setText("  " + Integer.toString(losses) + "   ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        getPreferences(MODE_PRIVATE).edit().putInt("WINS", this.wins).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("LOSSES", this.losses).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("TOTAL_WINS", this.total_wins).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("TOTAL_LOSSES", this.total_losses).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("ROUND", this.round).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("REVEALED", this.revealed).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("GOAT1", this.goat1).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("GOAT2", this.goat2).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("CAR", this.car).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("CHOICE", this.choice).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPreferences(MODE_PRIVATE).edit().putInt("WINS", this.wins).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("LOSSES", this.losses).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("TOTAL_WINS", this.total_wins).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("TOTAL_LOSSES", this.total_losses).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("ROUND", this.round).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("REVEALED", this.revealed).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("GOAT1", this.goat1).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("GOAT2", this.goat2).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("CAR", this.car).apply();
        getPreferences(MODE_PRIVATE).edit().putInt("CHOICE", this.choice).apply();

        SharedPreferences.Editor pref_ed =
                this.getSharedPreferences(
                        "MontyHall", Context.MODE_PRIVATE).edit();
        pref_ed.putBoolean("NEWCLICKED", false).apply();
    }

    // countdown animation for reveal
    private void countdown(View view) {

        TextView goatView = findViewById(R.id.conclusion);
        goatView.setText("");

        final ImageView myImageView3 = findViewById(R.id.three);
        final ImageView myImageView2 = findViewById(R.id.two);
        final ImageView myImageView1 = findViewById(R.id.one);
        final Animation myFadeInAnimation3 = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);
        final Animation myFadeInAnimation2 = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);
        final Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);

        myFadeInAnimation3.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                myImageView2.startAnimation(myFadeInAnimation2);

            }
        });

        myFadeInAnimation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                myImageView1.startAnimation(myFadeInAnimation1);

            }
        });

        myFadeInAnimation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (round == 0) {
                    revealGoat(choice);
                } else {
                    revealAnswer(choice);
                }
            }
        });

        myImageView3.startAnimation(myFadeInAnimation3);
    }

    private void revealGoat(int choice) {
        int reveal;
        if (goat1 == choice) {
            // reveal goat2
            reveal = goat2;
        } else {
            // reveal goat1
            reveal = goat1;
        }

        switch (reveal) {
            case 0:
                doorOne.setImageResource(R.drawable.goat);
                doorTwo.setEnabled(true);
                doorThree.setEnabled(true);
                revealed = 0;
                break;
            case 1:
                doorTwo.setImageResource(R.drawable.goat);
                doorOne.setEnabled(true);
                doorThree.setEnabled(true);
                revealed = 1;
                break;
            case 2:
                doorThree.setImageResource(R.drawable.goat);
                doorOne.setEnabled(true);
                doorTwo.setEnabled(true);
                revealed = 2;
                break;
        }

        TextView view = findViewById(R.id.conclusion);
        view.setText("Switch doors?");
        round++;
    }

    private void revealAnswer(int choice) {
        doorOne.setEnabled(false);
        doorTwo.setEnabled(false);
        doorThree.setEnabled(false);
        switch (car) {
            case 0:
                doorOne.setImageResource(R.drawable.car);
                doorTwo.setImageResource(R.drawable.goat);
                doorThree.setImageResource(R.drawable.goat);
                break;
            case 1:
                doorOne.setImageResource(R.drawable.goat);
                doorTwo.setImageResource(R.drawable.car);
                doorThree.setImageResource(R.drawable.goat);
                break;
            case 2:
                doorOne.setImageResource(R.drawable.goat);
                doorTwo.setImageResource(R.drawable.goat);
                doorThree.setImageResource(R.drawable.car);
                break;
        }

        if (choice == car) {
            wins++;
            total_wins++;

            TextView win_count = findViewById(R.id.win_count);
            win_count.setText("  " + Integer.toString(wins));

            TextView total_win_text = findViewById(R.id.total_wins);
            total_win_text.setText("  " + Integer.toString(total_wins));

            TextView view = findViewById(R.id.conclusion);
            view.setText("You won a car!");
        } else {
            losses++;
            total_losses++;

            TextView loss_count = findViewById(R.id.loss_count);
            loss_count.setText("  " + Integer.toString(losses));

            TextView total_loss_text = findViewById(R.id.total_losses);
            total_loss_text.setText("  " + Integer.toString(total_losses));

            TextView view = findViewById(R.id.conclusion);
            view.setText("You lost.");
        }

        round = 2;

        SharedPreferences.Editor pref_ed =
                this.getSharedPreferences(
                        "MontyHall", Context.MODE_PRIVATE).edit();
        pref_ed.putBoolean("NEWCLICKED", false).apply();

        Button again = findViewById(R.id.again);
        again.setVisibility(View.VISIBLE);
        again.setEnabled(true);
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                view.setVisibility(View.INVISIBLE);
                view.setEnabled(false);
            }
        });
    }

    public void reset() {
        choice = -1;
        round = 0;
        revealed = -1;
        assign();
        doorOne.setImageResource(R.drawable.closed_door);
        doorOne.setEnabled(true);
        doorTwo.setImageResource(R.drawable.closed_door);
        doorTwo.setEnabled(true);
        doorThree.setImageResource(R.drawable.closed_door);
        doorThree.setEnabled(true);
        TextView view = findViewById(R.id.conclusion);
        view.setText("");
    }

    // assign the goats/car
    private void assign() {
        Random random = new Random();
        goat1 = random.nextInt(3);
        goat2 = goat1;

            while (goat2 == goat1) {
            goat2 = random.nextInt(3);
        }

        car = goat2;
            while (car == goat2 || car == goat1) {
            car = random.nextInt(3);
        }
    }

}
