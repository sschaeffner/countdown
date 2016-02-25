package de.gymolching.Countdown;

import me.sschaeffner.jArtnet.ArtnetController;

import java.util.Arrays;

/**
 * @author sschaeffner
 */
public class CountdownArtnetController {

    private static CountdownArtnetController instance = null;

    public static CountdownArtnetController getInstance() {
        if (instance == null) instance = new CountdownArtnetController();
        return instance;
    }

    private final ArtnetController ac;

    private CountdownArtnetController() {
        ac = new ArtnetController();
        ac.discoverNodes();
        Arrays.asList(ac.getNodes()).stream().forEach(System.out::println);
    }

    public ArtnetController getAc() {
        return ac;
    }
}
