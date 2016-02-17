package de.gymolching.Countdown;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Controls the countdown gui.
 *
 * @author sschaeffner
 */
public class CountdownController implements InputHandler {

    //time the countdown starts from
    private int countdownTime;
    //background color the countdown starts with
    private final Color startBackgroundColor = new Color(0, 0, 0);
    //background color for the last x seconds of the countdown
    private final Color endBackgroundColor = new Color(255, 0, 0);

    //reference to the gui object
    private GUI gui;

    //ScheduledExecutorService reference
    private final ScheduledExecutorService scheduler;
    //the current ScheduledFuture that sets the countdown's time
    private ScheduledFuture scheduled;

    //the current countdown time
    private int time;
    //whether blackout is currently enabled
    private boolean blackoutEnabled;
    //whether whiteout is currently enabled
    private boolean whiteoutEnabled;

    /**
     * Initializes a CountdownController object. This also creates its own GUI object.
     *
     * @param countdownTime the time the countdown starts from
     * @param screen        number of the screen the countdown be displayed on
     * @param fontName      name of the font to use for displaying the countdown
     */
    public CountdownController(int countdownTime, int screen, String fontName) {
        this.countdownTime = countdownTime;

        int minuteCharAmount = ((countdownTime / 60) + "").length();
        this.gui = new GUI(this, screen, minuteCharAmount, fontName);

        this.scheduler = Executors.newScheduledThreadPool(1);
        this.time = 0;
        this.blackoutEnabled = false;
        this.whiteoutEnabled = false;
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        switch(e.getKeyChar()) {
            case 's'://start
                start();
                break;
            case 'k'://kill
                kill();
                break;
            case 'b'://black out
                blackOut();
                break;
            case 'w'://white out
                whiteOut();
                break;
            case 'Q'://quit
                System.exit(0);
                break;
        }
    }

    /**
     * Starts the countdown. Uses a ScheduledFuture.
     */
    private void start() {
        if (this.scheduled == null) {
            blackoutEnabled = false;
            whiteoutEnabled = false;
            this.gui.setBlackOut(false);
            this.gui.setWhiteOut(false);

            this.time = countdownTime;
            this.gui.setTime(this.time);
            this.gui.setBackgroundColor(startBackgroundColor);

            this.scheduled = this.scheduler.scheduleAtFixedRate(() -> {
                if (this.time > 0) {
                    this.time--;
                    this.gui.setTime(this.time);

                    if (this.time == 10) {
                        this.gui.setBackgroundColor(endBackgroundColor);
                    }

                } else {
                    ScheduledFuture s = this.scheduled;
                    this.scheduled = null;
                    s.cancel(true);
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Kills the countdown. Displays 0:00 on the screen.
     */
    private void kill() {
        if (this.scheduled != null) {
            this.scheduled.cancel(false);
            this.scheduled = null;
            this.gui.setTime(0);
        }
    }

    /**
     * Toggles blackout of the screen.
     */
    private void blackOut() {
        blackoutEnabled = !blackoutEnabled;
        whiteoutEnabled = false;
        this.gui.setBlackOut(blackoutEnabled);
        this.gui.setWhiteOut(false);
    }

    /**
     * Toggles whiteout of the screen.
     */
    private void whiteOut() {
        whiteoutEnabled = !whiteoutEnabled;
        blackoutEnabled = false;
        this.gui.setWhiteOut(whiteoutEnabled);
        this.gui.setBlackOut(false);
    }
}