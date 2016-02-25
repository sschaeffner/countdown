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


import me.sschaeffner.jArtnet.ArtNetPriorityCodes;
import me.sschaeffner.jArtnet.ArtnetOpCodes;

/**
 * An ArtPoll packet.
 *
 * @author sschaeffner
 */
public class ArtPollPacket extends ArtnetPacket {
    public static final byte protVerHi = 3;
    public static final byte protVerLo = 14;

    private final byte talkToMe;
    private final byte priority;

    /**
     * Constructs a new instance of this class.
     *
     * Uses the following defaults:
     * * talkToMe
     *      * Send ArtPollReply whenever Node conditions change
     *      * Send me diagnostics messages
     *      * Diagnostics messages are broadcast
     * * priority
     *      * critical
     */
    public ArtPollPacket() {
        this((byte) 0b00000110, ArtNetPriorityCodes.DP_CRITICAL);
    }

    /**
     * Constructs a new instance of this class.
     *
     * @param talkToMe  TalkToMe behaviour of nodes
     * @param priority  lowest priority of diagnostic message to be sent
     */
    public ArtPollPacket(byte talkToMe, byte priority) {
        this.talkToMe = talkToMe;
        this.priority = priority;
    }

    /**
     * Returns the whole package's data as byte array.
     *
     * @return the package's data as byte array
     */
    @Override
    public byte[] getPackageBytes() {
        int byteArrayLength = ArtnetPacket.ID.length + 2 + 1+1 + 1 + 1;
        byte[] bytes = new byte[byteArrayLength];

        //Art-Net package ID
        System.arraycopy(ArtnetPacket.ID, 0, bytes, 0, ArtnetPacket.ID.length);

        //op code
        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_POLL);
        System.arraycopy(opCode, 0, bytes, ArtnetPacket.ID.length, 2);

        //protVer
        bytes[ArtnetPacket.ID.length + 2] = protVerHi;
        bytes[ArtnetPacket.ID.length + 2 + 1] = protVerLo;

        //talk to me
        bytes[ArtnetPacket.ID.length + 2 + 1 + 1] = this.talkToMe;

        //priority
        bytes[ArtnetPacket.ID.length + 2 + 1 + 1 + 1] = this.priority;

        return bytes;
    }

    /**
     * Constructs a new instance of this class from received bytes.
     *
     * @param bytes  received bytes
     * @return      new instance
     */
    public static ArtPollPacket fromBytes(byte[] bytes) {
        int byteArrayLength = ArtnetPacket.ID.length + 2 + 1+1 + 1 + 1;
        if (bytes.length < byteArrayLength) {
            throw new IllegalArgumentException("cannot construct ArtPollPacket from bytes: bytes length not compatible");
        }

        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_POLL);

        byte rOpCodeLo = bytes[8];
        byte rOpCodeHi = bytes[9];
        if (rOpCodeLo != opCode[0] || rOpCodeHi != opCode[1]) {
            throw new IllegalArgumentException("cannot construct ArtPollReplyPacket from bytes: wrong opcode");
        }

        byte rProtVerHi = bytes[10];
        byte rProtVerLo = bytes[11];
        if (rProtVerHi < protVerHi || rProtVerLo < protVerLo) {
            throw new IllegalArgumentException("cannot construct ArtPollPacket from bytes: protVer not compatible");
        }

        //no exceptions
        byte talkToMe = bytes[12];
        byte priority = bytes[13];
        return new ArtPollPacket(talkToMe, priority);
    }

    public byte getTalkToMe() {
        return talkToMe;
    }

    public byte getPriority() {
        return priority;
    }
}
