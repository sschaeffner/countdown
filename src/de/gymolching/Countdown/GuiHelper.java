package de.gymolching.Countdown;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * A collection of various useful helper functions for swing.
 *
 * @author sschaeffner
 */
public class GuiHelper {
    /**
     * Calculates the maximum font size to display a time in the given format
     * on a screen with a certain width and height.
     *
     * @param screenWidth   screen width
     * @param screenHeight  screen height
     * @param fontName      font to use
     * @return              maximum font size
     */
    public static Font maxFontSize(int screenWidth, int screenHeight, String fontName, String timeFormat) {
        Font font;
        int fontSize = screenHeight;

        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);

        while (fontSize > 0) {
            font = new Font(fontName, Font.PLAIN, fontSize);
            Rectangle2D size = font.getStringBounds(timeFormat, frc);
            if (size.getWidth() < screenWidth) {
                return font;
            } else {
                fontSize--;
            }
        }
        return null;
    }

    /**
     * Returns the size of a text in a given font.
     *
     * @param text  text to use
     * @param font  font to use
     * @return      size of the text in the font
     */
    public static Rectangle2D getTextSize(String text, Font font) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);

        return font.getStringBounds(text, frc);
    }

    /**
     * Calculates the y-coordinate of the baseline for a text.
     *
     * @param text          text to calculate with
     * @param font          font to calculate with
     * @param screenHeight  screen height
     * @return              y-coordinate of the baseline
     */
    public static int getTextYForCenter(String text, Font font, double screenHeight) {
        double textHeight = getTextSize(text, font).getHeight();

        double textY = (screenHeight / 2) + (textHeight / 4);
        return (int)Math.round(textY);
    }
}
