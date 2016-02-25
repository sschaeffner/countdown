/*
 * Copyright (C) 2015  Simon Schaeffner <simon.schaeffner@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.sschaeffner.jArtnet.test;

import me.sschaeffner.jArtnet.packets.ArtPollPacket;
import me.sschaeffner.jArtnet.ArtnetController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sschaeffner
 */
public class ControllerTest {

    ArtnetController controller;

    @Before
    public void setup() {
        controller = new ArtnetController();
    }

    @Test
    public void test() {
        ArtPollPacket pollPacket = new ArtPollPacket();
        for (byte b : pollPacket.getPackageBytes()) System.out.print(Byte.toUnsignedInt(b) + " "); System.out.println();
        controller.broadcastPacket(pollPacket);
        System.out.println("packet sent");
    }

    @After
    public void teardown() {
        controller.closeSocket();
    }
}
