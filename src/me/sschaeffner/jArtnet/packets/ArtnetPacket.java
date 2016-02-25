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
package me.sschaeffner.jArtnet.packets;

import me.sschaeffner.jArtnet.ArtnetNode;

/**
 * This is an Artnet package.
 *
 * @author sschaeffner
 */
public abstract class ArtnetPacket {
    public static final int UDP_PORT = 0x1936;//6454
    public static final byte[] ID = new byte[]{'A', 'r', 't', '-', 'N', 'e', 't', 0x00};

    protected ArtnetNode sender;

    public void setSender(ArtnetNode sender) {
        this.sender = sender;
    }

    /**
     * Returns the packet's sender.
     * @return  the packet's sender
     */
    public ArtnetNode getSender() {
        return this.sender;
    }

    /**
     * Returns the whole package's data as byte array.
     *
     * @return the package's data as byte array
     */
    public abstract byte[] getPackageBytes();
}
