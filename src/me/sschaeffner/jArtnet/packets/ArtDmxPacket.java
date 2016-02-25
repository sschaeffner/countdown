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

import me.sschaeffner.jArtnet.ArtnetOpCodes;

/**
 * @author sschaeffner
 */
public class ArtDmxPacket extends ArtnetPacket {
    public static final byte protVerHi = 3;
    public static final byte protVerLo = 14;

    private final byte sequence;
    private final byte physical;
    private final byte subUni, net;
    private final byte lengthHi, length;
    private final byte[] data;

    /**
     * Constructs a new instance of this class.
     */
    public ArtDmxPacket(byte sequence, byte physical, byte subUni, byte net, byte lengthHi, byte length, byte[] data) {
        this.sequence = sequence;
        this.physical = physical;
        this.subUni = subUni;
        this.net = net;
        this.lengthHi = lengthHi;
        this.length = length;
        this.data = data;
    }

    /**
     * Constructs a new instance of this class.
     */
    public ArtDmxPacket(byte sequence, byte physical, byte subUni, byte net, byte[] data) {
        this.sequence = sequence;
        this.physical = physical;
        this.subUni = subUni;
        this.net = net;
        int length = data.length;
        this.lengthHi = (byte)((length >>> 8) & 0xFF);
        this.length = (byte)(length & 0xFF);
        this.data = data;
    }

    /**
     * Returns the whole package's data as byte array.
     *
     * @return the package's data as byte array
     */
    @Override
    public byte[] getPackageBytes() {
        int length = (this.lengthHi << 8) + this.length;

        int byteArrayLength = ArtnetPacket.ID.length + 2 + 1+1 + 1 + 1 + 1 + 1 + 1+1 + length;
        byte[] bytes = new byte[byteArrayLength];

        //Art-Net package ID
        System.arraycopy(ArtnetPacket.ID, 0, bytes, 0, ArtnetPacket.ID.length);

        //opcode
        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_OUTPUT);
        System.arraycopy(opCode, 0, bytes, ArtnetPacket.ID.length, 2);

        bytes[10] = protVerHi;
        bytes[11] = protVerLo;

        bytes[12] = this.sequence;

        bytes[13] = this.physical;

        bytes[14] = this.subUni;
        bytes[15] = this.net;

        bytes[16] = this.lengthHi;
        bytes[17] = this.length;

        if (bytes.length >= length + 18) {
            System.arraycopy(data, 0, bytes, 18, length);
        } else {
            throw new IllegalStateException("cannot get packet bytes for ArtDmxPacket: not enough data for length available");
        }

        return bytes;
    }

    public static ArtDmxPacket fromBytes(byte[] bytes) {

        //check for correct opcode
        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_OUTPUT);

        byte rOpCodeLo = bytes[8];
        byte rOpCodeHi = bytes[9];
        if (rOpCodeLo != opCode[0] || rOpCodeHi != opCode[1]) {
            throw new IllegalArgumentException("cannot construct ArtDmxPacket from data: wrong opcode");
        }

        //check protocol version
        byte rProtVerHi = bytes[10];
        byte rProtVerLo = bytes[11];
        if (rProtVerHi < protVerHi || rProtVerLo < protVerLo) {
            throw new IllegalArgumentException("cannot construct ArtPollPacket from data: protVer not compatible");
        }

        //read information
        byte sequence = bytes[12];

        byte physical = bytes[13];

        byte subUni = bytes[14];
        byte net = bytes[15];

        byte lengthHi = bytes[16];
        byte length = bytes[17];

        int lengthI = (lengthHi << 8) + length;

        byte[] data = new byte[lengthI];
        if (bytes.length >= lengthI + 18) {
            System.arraycopy(bytes, 18, data, 0, lengthI);
        } else {
            throw new IllegalStateException("cannot construct ArtDmxPacket from bytes: data too short (is " + (bytes.length - 18)
                    + "; should be " + lengthI + ")");
        }

        return new ArtDmxPacket(sequence, physical, subUni, net, lengthHi, length, data);
    }

    public byte getSequence() {
        return sequence;
    }

    public byte getPhysical() {
        return physical;
    }

    public byte getSubUni() {
        return subUni;
    }

    public byte getNet() {
        return net;
    }

    public byte getLengthHi() {
        return lengthHi;
    }

    public byte getLength() {
        return length;
    }

    public byte[] getData() {
        return data;
    }
}
