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

import me.sschaeffner.jArtnet.ArtnetStyleCodes;
import me.sschaeffner.jArtnet.packets.ArtPollReplyPacket;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author sschaeffner
 */
public class PollReplyPacketTest {

    @Test
    public void test() throws UnknownHostException {
        ArtPollReplyPacket p = new ArtPollReplyPacket(InetAddress.getLocalHost(), (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'}, new byte[64], new byte[64], (byte)0, (byte)0, new byte[4], new byte[4], new byte[4], new byte[4], new byte[4], (byte)0, (byte)0, (byte)1, ArtnetStyleCodes.ST_CONTROLLER, new byte[6], new byte[4], (byte)0, (byte)255);
        Assert.assertNotNull(p);

        byte[] data = p.getPackageBytes();
        Assert.assertEquals(239, data.length);
    }

    @Test
    public void test2() throws UnknownHostException {
        ArtPollReplyPacket pOrig = new ArtPollReplyPacket(InetAddress.getByAddress(InetAddress.getLocalHost().getAddress()), (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'}, new byte[64], new byte[64], (byte)0, (byte)0, new byte[4], new byte[4], new byte[4], new byte[4], new byte[4], (byte)0, (byte)0, (byte)1, ArtnetStyleCodes.ST_CONTROLLER, new byte[6], new byte[4], (byte)0, (byte)255);

        byte[] data = pOrig.getPackageBytes();
        ArtPollReplyPacket p = ArtPollReplyPacket.fromBytes(data);

        //TODO add all options
        Assert.assertEquals(pOrig.getAddress(), p.getAddress());
        Assert.assertEquals(pOrig.getBindIndex(), p.getBindIndex());
        Assert.assertEquals(pOrig.getStatus2(), p.getStatus2());
    }
}
