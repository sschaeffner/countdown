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
package me.sschaeffner.jArtnet;

import me.sschaeffner.jArtnet.packets.ArtPollReplyPacket;

import java.net.InetAddress;

/**
 * @author sschaeffner
 */
public class ArtnetNode {
    //ip address
    private final InetAddress inetAddress;

    //type
    private final byte styleCode;

    //ArtPollReply packet
    private ArtPollReplyPacket artPollReplyPacket;

    //name
    private String shortName, longName;

    /**
     * Constructs a new instance of this class.
     *
     * @param inetAddress   the node's address
     * @param styleCode     the node's styleCode
     */
    public ArtnetNode(InetAddress inetAddress, byte styleCode, String shortName, String longName) {
        this.inetAddress = inetAddress;
        this.styleCode = styleCode;
    }

    /**
     * Constructs a new instance of this class from an ArtPollReply packet.
     * @param inetAddress           the node's address
     * @param artPollReplyPacket    ArtPollReplyPacket instance
     */
    public ArtnetNode(InetAddress inetAddress, ArtPollReplyPacket artPollReplyPacket) {
        this.inetAddress = inetAddress;
        this.artPollReplyPacket = artPollReplyPacket;
        this.styleCode = artPollReplyPacket.getStyle();
        this.shortName = new String(artPollReplyPacket.getShortName()).trim();
        this.longName = new String(artPollReplyPacket.getLongName()).trim();
    }

    @Override
    public String toString() {
        if (longName != null) {
            return "ArtnetNode{" + this.longName + "; " + inetAddress.getHostAddress() + "; " + ArtnetStyleCodes.toName(styleCode) + "}";
        } else {
            return "ArtnetNode{" + inetAddress.getHostAddress() + "; " + ArtnetStyleCodes.toName(styleCode) + "}";
        }
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getNodeReport() {
        if (artPollReplyPacket != null) {
            return new String(artPollReplyPacket.getNodeReport());
        } else {
            return null;
        }
    }

    public int getEstaMan() {
        if (artPollReplyPacket != null) {
            return artPollReplyPacket.getEstaMan();
        } else {
            return 0;
        }
    }

    public byte getStyleCode() {
        return styleCode;
    }

    public String getStlyeAsString() {
        return ArtnetStyleCodes.toName(styleCode);
    }

    public void setArtPollReplyPacket(ArtPollReplyPacket artPollReplyPacket) {
        this.artPollReplyPacket = artPollReplyPacket;
    }

    public ArtPollReplyPacket getArtPollReplyPacket() {
        return artPollReplyPacket;
    }
}
