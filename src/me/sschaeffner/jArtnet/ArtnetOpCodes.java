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

import me.sschaeffner.jArtnet.packets.ArtDmxPacket;
import me.sschaeffner.jArtnet.packets.ArtPollPacket;
import me.sschaeffner.jArtnet.packets.ArtPollReplyPacket;
import me.sschaeffner.jArtnet.packets.ArtnetPacket;

import java.util.Arrays;

/**
 * A collection of all OpCodes of the Art-Net 3 specification.
 *
 * @author sschaeffner
 */
public final class ArtnetOpCodes {
    //each opcode is 2 bytes long

    public static final int OP_POLL = 0x2000;
    public static final int OP_POLL_REPLY = 0x2100;
    public static final int OP_DIAG_DATA = 0x2300;
    public static final int OP_COMMAND = 0x2400;
    public static final int OP_OUTPUT = 0x5000;
    public static final int OP_NZS = 0x5100;
    public static final int OP_SYNC = 0x5200;
    public static final int OP_ADDRESS = 0x6000;
    public static final int OP_INPUT = 0x7000;
    public static final int OP_TOD_REQUEST = 0x8000;
    public static final int OP_TOD_DATA = 0x8100;
    public static final int OP_TOD_CONTROL = 0x8200;
    public static final int OP_RDM = 0x8300;
    public static final int OP_RDM_SUB = 0x8400;
    public static final int OP_VIDEO_SETUP = 0xa010;
    public static final int OP_VIDEO_PALETTE = 0xa020;
    public static final int OP_VIDEO_DATA = 0xa040;
    public static final int OP_MAC_MASTER = 0xf000;
    public static final int OP_MAC_SLAVE = 0xf100;
    public static final int OP_FIRMWARE_REPLY = 0xf300;
    public static final int OP_FILE_TN_MASTER = 0xf400;
    public static final int OP_FILE_FN_MASTER = 0xf500;
    public static final int OP_FILE_FN_REPLY = 0xf600;
    public static final int OP_IP_PROG = 0xf800;
    public static final int OP_IP_PROG_REPLY = 0xf900;
    public static final int OP_MEDIA = 0x9000;
    public static final int OP_MEDIA_PATCH = 0x9100;
    public static final int OP_MEDIA_CONTROL = 0x9200;
    public static final int OP_MEDIA_CONTRL_REPLY = 0x9300;
    public static final int OP_TIME_CODE = 0x9700;
    public static final int OP_TIME_SYNC = 0x9800;
    public static final int OP_TRIGGER = 0x9900;
    public static final int OP_DIRECTORY = 0x9a00;
    public static final int OP_DIRECTORY_REPLY = 0x9b00;

    /**
     * Converts an OpCode integer into a byte array.
     *
     * Low byte is at position 0 and high byte at position 1 as to specification.
     *
     * @param opCode    OpCode to convert
     * @return          converted OpCode as byte array
     */
    public static byte[] toByteArray(int opCode) {
        byte lowByte = (byte) (opCode & 0xFF);
        byte highByte = (byte)((opCode >>> 8) & 0xFF);
        return new byte[]{lowByte, highByte};
    }

    /**
     * Converts the data of an Art-Net packet in an ArtnetPacket.
     *
     * @param bytes data of an Art-Net packet
     * @return      instance of ArtnetPacket
     */
    public static ArtnetPacket fromBytes(byte[] bytes) {
        //minimum data length
        //packet id -> 8
        //opcode    -> 2
        //some data -> 1+
        if (bytes.length > 10) {

            //check packet id
            byte[] packetId = new byte[8];
            System.arraycopy(bytes, 0, packetId, 0, 8);

            if(Arrays.equals(packetId, ArtnetPacket.ID)) {

                //check opcode
                byte opCodeLo = bytes[8];
                byte opCodeHi = bytes[9];

                int opCode = (opCodeHi << 8) + opCodeLo;

                switch (opCode) {
                    case ArtnetOpCodes.OP_OUTPUT:
                        System.out.println("OP_OUTPUT");
                        return ArtDmxPacket.fromBytes(bytes);

                    case ArtnetOpCodes.OP_POLL:
                        System.out.println("OP_POLL");
                        return ArtPollPacket.fromBytes(bytes);

                    case ArtnetOpCodes.OP_POLL_REPLY:
                        System.out.println("OP_POLL_REPLY");
                        return ArtPollReplyPacket.fromBytes(bytes);

                    default:
                        break;
                }
            } else {
                System.out.println("not artnet packet: wrong packet ID");
            }
        } else {//else cannot be Art-Net packet
            System.out.println("not artnet packet: too short");
        }
        return null;
    }
}
