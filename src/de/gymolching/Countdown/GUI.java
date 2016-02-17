package de.gymolching.Countdown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Displays a countdown.
 *
 * @author sschaeffner
 */
public class GUI {
    //listener for keyTyped events on the gui
    private final InputHandler inputHandler;
    //the amount of characters for the minutes
    private int minuteCharAmount;
    //time format (e.g. XX:XX)
    private String timeFormat = "";
    //whether blackout is currently enabled
    private boolean blackOutEnabled;
    //whether whiteout is currently enabled
    private boolean whiteOutEnabled;

    //swing frame
    private final JFrame frame;

    //the currently displayed countdown time
    private int time;
    //current background and text color
    private Color backgroundColor, textColor;
    //font to use for displaying the countdown
    private Font font;
    //the name of the font
    private final String fontName;

    //y-coordinate for the baseline of the text
    private int textY;

    /**
     * Initializes a GUI object that displays a countdown on a screen.
     *
     * @param inputHandler      keyEvent handler
     * @param screen            number of the screen the countdown be displayed on
     * @param minuteCharAmount  the amount of characters the minutes need
     * @param fontName          name of the font to use for displaying the countdown
     */
    public GUI(InputHandler inputHandler, int screen, int minuteCharAmount, String fontName) {
        this.inputHandler = inputHandler;
        this.minuteCharAmount = minuteCharAmount;
        for (int i = 0; i < this.minuteCharAmount; i++) timeFormat += "X";
        timeFormat += ":XX";

        this.backgroundColor = new Color(0, 0, 0);
        this.textColor = new Color(255, 255, 255);
        this.font = null;
        this.fontName = fontName;

        this.textY = -1;

        this.frame = new JFrame("Countdown");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setContentPane(new CountdownPanel());
        this.frame.setResizable(false);
        this.frame.setUndecorated(true);
        this.frame.setCursor(createBlankCursor());
        GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screen].setFullScreenWindow(this.frame);

        this.frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                inputHandler.onKeyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        this.frame.setVisible(true);
    }

    private Cursor createBlankCursor() {
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        return Toolkit.getDefaultToolkit().createCustomCursor(bi, new Point(0, 0), "blank cursor");
    }

    public class CountdownPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;

                if (blackOutEnabled) {
                    //background
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                } else if (whiteOutEnabled) {
                    //background
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                } else {
                    //background
                    g2d.setColor(backgroundColor);
                    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

                    //text
                    if (font == null) font = GuiHelper.maxFontSize(frame.getWidth(), frame.getHeight(), fontName, timeFormat);
                    if (textY == -1) textY = GuiHelper.getTextYForCenter(timeFormat, font, frame.getHeight());
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setFont(font);
                    g2d.setColor(textColor);
                    g2d.drawString(timeToString(time), 0, textY);
                }
            }
        }
    }

    /**
     * Set the time in seconds.
     *
     * @param time the time in seconds.
     */
    public void setTime(int time) {
        this.time = time;
        this.frame.repaint();
    }

    /**
     * Converts an int time in seconds into a human readable format (minutes:seconds).
     *
     * @param time  time in seconds
     * @return      time in human readable format
     */
    private String timeToString(int time) {
        int min = time / 60;
        int sec = time % 60;

        String minS = min + "";
        int minSLength = minS.length();
        for (int i = 0; i < minuteCharAmount - minSLength; i++) minS = "0" + minS;

        String secS;
        if (sec < 10) {
            secS = "0" + sec;
        } else secS = sec + "";

        return minS + ":" + secS;
    }

    public void setBlackOut(boolean blackOutEnabled) {
        this.blackOutEnabled = blackOutEnabled;
        this.frame.repaint();
    }

    public void setWhiteOut(boolean whiteOutEnabled) {
        this.whiteOutEnabled = whiteOutEnabled;
        this.frame.repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }
}