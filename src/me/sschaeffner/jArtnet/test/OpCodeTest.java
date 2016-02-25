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

import me.sschaeffner.jArtnet.ArtnetOpCodes;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sschaeffner
 */
public class OpCodeTest {

    @Test
    public void test1() {
        int i = 0xFF00;
        byte[] ba = ArtnetOpCodes.toByteArray(i);
        byte[] eq = new byte[]{(byte) 0x00, (byte) 0xFF};

        Assert.assertArrayEquals(eq, ba);
    }

    @Test
    public void test2() {
        int i = 0xABCD;
        byte[] ba = ArtnetOpCodes.toByteArray(i);
        byte[] eq = new byte[]{(byte) 0xCD, (byte) 0xAB};

        Assert.assertArrayEquals(eq, ba);
    }

}
