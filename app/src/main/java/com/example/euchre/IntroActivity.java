package com.example.euchre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addSlide(com.amqtech.opensource.appintroexample.util.SampleSlide.newInstance(R.layout.slide_1));

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("WELCOME TO EUCHRE");
        sliderPage.setDescription("Euchre (and its variations) is the reason why modern card decks were first packaged with jokers, a card originally designed to act as the right and left \"bowers\" (high trumps). Although later eclipsed by Bridge (as with so many other games of this type), Euchre is still well known in America and is an excellent social game.\n" +
                "\n" +
                "The game is best for four participants, playing two against two as partners. Therefore, the rules for the four-hand version are given first.");
        //sliderPage.setImageDrawable();
        sliderPage.setBgColor(Color.parseColor("#3F51B5"));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        setFlowAnimation();

        SliderPage sliderPages = new SliderPage();
        sliderPages.setTitle("Rules of Euchre");
        sliderPages.setDescription("RANK OF CARDS\n" +
                "The highest trump is the jack of the trump suit, called the \"right bower.\" The second-highest trump is the jack of the other suit of the same color called the \"left bower.\" (Example: If diamonds are trumps, the right bower is J♦ and left bower is J♥.) The remaining trumps, and also the plain suits, rank as follows: A (high), K, Q, J, 10, 9, 8, 7. If a joker has been added to the pack, it acts as the highest trump.\n" +
                "\n" +
                "CARD VALUES/SCORING\n" +
                "The following shows all scoring situations:\n" +
                "\n" +
                "Partnership making trump wins 3 or 4 tricks – 1 point\n" +
                "Partnership making trump wins 5 tricks – 2 points\n" +
                "Lone hand wins 3 or 4 tricks – 1 point\n" +
                "Lone hand wins 5 tricks – 4 points\n" +
                "Partnership or lone hand is euchred, opponents score 2 points\n" +
                "\n" +
                "The first player or partnership to score 5, 7 or 10 points, as agreed beforehand, wins the game. In the 5-point game, a side is said to be \"at the bridge\" when it has scored 4 and the opponents have scored 2 or less.");
        //sliderPage.setImageDrawable();
        sliderPages.setBgColor(Color.parseColor("#3F51B5"));
        addSlide(AppIntroFragment.newInstance(sliderPages));
        setFlowAnimation();


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intent = new Intent(this, createGame.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent = new Intent (this, createGame.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
