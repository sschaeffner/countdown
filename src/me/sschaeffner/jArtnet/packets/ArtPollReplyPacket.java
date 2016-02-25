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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An ArtPollReply packet.
 *
 * @author sschaeffner
 */
public class ArtPollReplyPacket extends ArtnetPacket {

    private final InetAddress address;
    private final byte versInfoH;
    private final byte versInfoL;
    private final byte netSwitch, subSwitch;
    private final byte oemHi, oem;
    private final byte ubeaVersion;
    private final byte status1;
    private final byte estaManLo, estaManHi;
    private final byte[] shortName;
    private final byte[] longName;
    private final byte[] nodeReport;
    private final byte numPortsHi, numPortsLo;
    private final byte[] portTypes;
    private final byte[] goodInput, goodOutput;
    private final byte[] swIn, swOut;
    private final byte swVideo;
    private final byte swMacro;
    private final byte swRemote;
    private final byte style;
    private final byte[] mac;
    private final byte[] bindIp;
    private final byte bindIndex;
    private final byte status2;

    /**
     * Constructs a new instance of this class.
     */
    public ArtPollReplyPacket(InetAddress address, byte versInfoH, byte versInfoL, byte netSwitch, byte subSwitch,
                              byte oemHi, byte oem, byte ubeaVersion, byte status1, byte estaManLo, byte estaManHi,
                              byte[] shortName, byte[] longName, byte[] nodeReport, byte numPortsHi, byte numPortsLo,
                              byte[] portTypes, byte[] goodInput, byte[] goodOutput, byte[] swIn, byte[] swOut,
                              byte swVideo, byte swMacro, byte swRemote, byte style, byte[] mac, byte[] bindIp,
                              byte bindIndex, byte status2) {
        this.address = address;
        this.versInfoH = versInfoH;
        this.versInfoL = versInfoL;
        this.netSwitch = netSwitch;
        this.subSwitch = subSwitch;
        this.oemHi = oemHi;
        this.oem = oem;
        this.ubeaVersion = ubeaVersion;
        this.status1 = status1;
        this.estaManLo = estaManLo;
        this.estaManHi = estaManHi;
        if (shortName.length != 18) throw new IllegalArgumentException("shortName has to be 18 bytes long");
        this.shortName = shortName;
        if (longName.length != 64) throw new IllegalArgumentException("longName has to be 64 bytes long");
        this.longName = longName;
        if (nodeReport.length != 64) throw new IllegalArgumentException("nodeReport has to be 4 bytes long");
        this.nodeReport = nodeReport;
        this.numPortsHi = numPortsHi;
        this.numPortsLo = numPortsLo;
        if (portTypes.length != 4) throw new IllegalArgumentException("portTypes has to be 4 bytes long");
        this.portTypes = portTypes;
        if (goodInput.length != 4) throw new IllegalArgumentException("goodInput has to be 4 bytes long");
        this.goodInput = goodInput;
        if (goodOutput.length != 4) throw new IllegalArgumentException("goodOutput has to be 4 bytes long");
        this.goodOutput = goodOutput;
        if (swIn.length != 4) throw new IllegalArgumentException("swIn has to be 4 bytes long");
        this.swIn = swIn;
        if (swOut.length != 4) throw new IllegalArgumentException("swOut has to be 4 bytes long");
        this.swOut = swOut;
        this.swVideo = swVideo;
        this.swMacro = swMacro;
        this.swRemote = swRemote;
        this.style = style;
        if (mac.length != 6) throw new IllegalArgumentException("mac has to be 6 bytes long");
        this.mac = mac;
        if (bindIp.length != 4) throw new IllegalArgumentException("bindIp has to be 4 bytes long");
        this.bindIp = bindIp;
        this.bindIndex = bindIndex;
        this.status2 = status2;
    }

    /**
     * Returns the whole package's data as byte array.
     *
     * @return the package's data as byte array
     */
    @Override
    public byte[] getPackageBytes() {
        int byteArrayLength = ArtnetPacket.ID.length + 2 + 4 + 2 + 2 + 1+1 + 1+1 + 1 + 1 + 1+1 + 18 + 64 + 64 + 1+1 + 4 + 4+4 + 4+4 + 1 + 1 + 1 + 3 + 1 + 6 + 4 + 1 + 1 + 26;
        byte[] bytes = new byte[byteArrayLength];

        //Art-Net package ID
        System.arraycopy(ArtnetPacket.ID, 0, bytes, 0, ArtnetPacket.ID.length);

        //opcode
        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_POLL_REPLY);
        System.arraycopy(opCode, 0, bytes, ArtnetPacket.ID.length, 2);

        //address
        byte[] addressBytes = address.getAddress();
        System.arraycopy(addressBytes, 0, bytes, 10, 4);

        //port (0x1936)
        byte portLo = (byte) 0x36;
        byte portHi = (byte) 0x19;
        bytes[14] = portLo;
        bytes[15] = portHi;

        //versInfo
        bytes[16] = versInfoH;
        bytes[17] = versInfoL;

        //netswitch
        bytes[18] = netSwitch;
        //subswitch
        bytes[19] = subSwitch;

        //oemHi
        bytes[20] = oemHi;
        //oem
        bytes[21] = oem;

        //ubea version
        bytes[22] = ubeaVersion;

        //status1
        bytes[23] = status1;

        //esta man lo
        bytes[24] = estaManLo;
        //esta man hi
        bytes[25] = estaManHi;

        //short name
        System.arraycopy(shortName, 0, bytes, 26, 18);

        //long name
        System.arraycopy(longName, 0, bytes, 44, 64);

        //node report
        System.arraycopy(nodeReport, 0, bytes, 108, 64);

        //num ports hi
        bytes[172] = numPortsHi;
        //num ports lo
        bytes[173] = numPortsLo;

        //port types
        System.arraycopy(portTypes, 0, bytes, 174, 4);

        //good input
        System.arraycopy(goodInput, 0, bytes, 178, 4);
        //good output
        System.arraycopy(goodOutput, 0, bytes, 182, 4);

        //swIn
        System.arraycopy(swIn, 0, bytes, 186, 4);
        //swOut
        System.arraycopy(swOut, 0, bytes, 190, 4);

        //swVideo
        bytes[194] = swVideo;

        //swMacro
        bytes[195] = swMacro;

        //swRemote
        bytes[196] = swRemote;

        //spare
        bytes[197] = 0;
        bytes[198] = 0;
        bytes[199] = 0;

        //style
        bytes[200] = style;

        //mac
        System.arraycopy(mac, 0, bytes, 201, 6);

        //bind ip
        System.arraycopy(bindIp, 0, bytes, 207, 4);

        //bind index
        bytes[211] = bindIndex;

        //status 2
        bytes[212] = status2;

        //filler
        for (int i = 0; i < 26; i++) {
            bytes[213 + i] = 0;
        }

        return bytes;
    }

    /**
     * Constructs a new instance of this class from received bytes.
     *
     * @param bytes  received bytes
     * @return      new instance
     */
    public static ArtPollReplyPacket fromBytes(byte[] bytes) {
        int byteArrayLength = ArtnetPacket.ID.length + 2 + 4 + 2 + 2 + 1+1 + 1+1 + 1 + 1 + 1+1 + 18 + 64 + 64 + 1+1 + 4 + 4+4 + 4+4 + 1 + 1 + 1 + 3 + 1 + 6 + 4 + 1 + 1 + 26;
        if (bytes.length < byteArrayLength) {
            throw new IllegalArgumentException("cannot construct ArtPollReplyPacket from bytes: bytes length not compatible");
        }

        byte[] opCode = ArtnetOpCodes.toByteArray(ArtnetOpCodes.OP_POLL_REPLY);

        byte rOpCodeLo = bytes[8];
        byte rOpCodeHi = bytes[9];

        if (rOpCodeLo != opCode[0] || rOpCodeHi != opCode[1]) {
            throw new IllegalArgumentException("cannot construct ArtPollReplyPacket from bytes: wrong opcode");
        }

        byte[] addressBytes = new byte[4];
        System.arraycopy(bytes, 10, addressBytes, 0, 4);
        InetAddress address;
        try {
            address = InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("cannot construct ArtPollReplyPacket from bytes: wrong address");
        }

        byte versInfoH = bytes[16];
        byte versInfoL = bytes[17];

        byte netSwitch = bytes[18];
        byte subSwitch = bytes[19];

        byte oemHi = bytes[20];
        byte oem = bytes[21];

        byte ubeaVersion = bytes[22];

        byte status1 = bytes[23];

        byte estaManLo = bytes[24];
        byte estaManHi = bytes[25];

        byte[] shortName = new byte[18];
        System.arraycopy(bytes, 26, shortName, 0, 18);

        byte[] longName = new byte[64];
        System.arraycopy(bytes, 44, longName, 0, 64);

        byte[] nodeReport = new byte[64];
        System.arraycopy(bytes, 108, nodeReport, 0, 64);

        byte numPortsHi = bytes[172];
        byte numPortsLo = bytes[173];

        byte[] portTypes = new byte[4];
        System.arraycopy(bytes, 174, portTypes, 0, 4);

        byte[] goodInput = new byte[4];
        System.arraycopy(bytes, 178, goodInput, 0, 4);
        byte[] goodOutput = new byte[4];
        System.arraycopy(bytes, 182, goodOutput, 0, 4);

        byte[] swIn = new byte[4];
        System.arraycopy(bytes, 186, swIn, 0, 4);
        byte[] swOut = new byte[4];
        System.arraycopy(bytes, 190, swOut, 0, 4);

        byte swVideo = bytes[194];

        byte swMacro = bytes[195];

        byte swRemote = bytes[196];

        byte style = bytes[200];

        byte[] mac = new byte[6];
        System.arraycopy(bytes, 201, mac, 0, 6);

        byte[] bindIp = new byte[4];
        System.arraycopy(bytes, 207, bindIp, 0, 4);

        byte bindIndex = bytes[211];

        byte status2 = bytes[212];

        return new ArtPollReplyPacket(address, versInfoH, versInfoL, netSwitch, subSwitch, oemHi, oem, ubeaVersion,
                status1, estaManLo, estaManHi, shortName, longName, nodeReport, numPortsHi, numPortsLo, portTypes,
                goodInput, goodOutput, swIn, swOut, swVideo, swMacro, swRemote, style, mac, bindIp, bindIndex, status2);
    }



    public InetAddress getAddress() {
        return address;
    }

    public byte getVersInfoH() {
        return versInfoH;
    }

    public byte getVersInfoL() {
        return versInfoL;
    }

    public byte getNetSwitch() {
        return netSwitch;
    }

    public byte getSubSwitch() {
        return subSwitch;
    }

    public byte getOemHi() {
        return oemHi;
    }

    public byte getOem() {
        return oem;
    }

    public byte getUbeaVersion() {
        return ubeaVersion;
    }

    public byte getStatus1() {
        return status1;
    }

    public byte getEstaManLo() {
        return estaManLo;
    }

    public byte getEstaManHi() {
        return estaManHi;
    }

    public int getEstaMan() {
        return ((estaManHi & 0xFF) << 8) + (estaManLo & 0xFF);
    }

    public byte[] getShortName() {
        return shortName;
    }

    public byte[] getLongName() {
        return longName;
    }

    public byte[] getNodeReport() {
        return nodeReport;
    }

    public byte getNumPortsHi() {
        return numPortsHi;
    }

    public byte getNumPortsLo() {
        return numPortsLo;
    }

    public byte[] getPortTypes() {
        return portTypes;
    }

    public byte[] getGoodInput() {
        return goodInput;
    }

    public byte[] getGoodOutput() {
        return goodOutput;
    }

    public byte[] getSwIn() {
        return swIn;
    }

    public byte[] getSwOut() {
        return swOut;
    }

    public byte getSwVideo() {
        return swVideo;
    }

    public byte getSwMacro() {
        return swMacro;
    }

    public byte getSwRemote() {
        return swRemote;
    }

    public byte getStyle() {
        return style;
    }

    public byte[] getMac() {
        return mac;
    }

    public byte[] getBindIp() {
        return bindIp;
    }

    public byte getBindIndex() {
        return bindIndex;
    }

    public byte getStatus2() {
        return status2;
    }
}
