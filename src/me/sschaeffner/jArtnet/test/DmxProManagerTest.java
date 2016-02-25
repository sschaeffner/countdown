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

import me.sschaeffner.jArtnet.ArtnetController;
import me.sschaeffner.jArtnet.ArtnetPacketListener;
import me.sschaeffner.jArtnet.packets.ArtDmxPacket;
import me.sschaeffner.jArtnet.packets.ArtnetPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author sschaeffner
 */
public class DmxProManagerTest implements ArtnetPacketListener {
    public static void main(String[] args) {
        DmxProManagerTest t = new DmxProManagerTest();
        t.sendData();
        t.controller.discoverNodes();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*try {
            t.in();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        t.fade();

        t.controller.closeSocket();
    }

    private ArtnetController controller;

    public DmxProManagerTest() {
        controller = new ArtnetController();
    }


    public void sendData() {
        byte sequence = 0;
        byte physical = 0;
        byte subUni = 0;
        byte net = 0;
        byte[] data = new byte[]{(byte)0, (byte)0, (byte)0};

        ArtDmxPacket dmxPacket = new ArtDmxPacket(sequence, physical, subUni, net, data);
        controller.broadcastPacket(dmxPacket);
        System.out.println("ArtDmx packet send");
    }

    public void in() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String in;
        System.out.print("r: ");
        while (!(in = reader.readLine()).equals("exit")) {
            int r = Integer.valueOf(in);
            System.out.print("g: ");
            int g = Integer.valueOf(reader.readLine());
            System.out.print("b: ");
            int b = Integer.valueOf(reader.readLine());
            ArtDmxPacket dmxPacket = new ArtDmxPacket((byte)0, (byte)0, (byte)0, (byte)0, new byte[]{(byte)r, (byte)g, (byte)b});
            controller.broadcastPacket(dmxPacket);

            System.out.print("r: ");
        }
    }

    public void fade() {
        int rM = 1;
        int gM = 0;
        int bM = 0;

        for (int o = 0; o < 9; o++) {
            if (rM == 1) {
                rM = 0;
                gM = 1;
            } else if (gM == 1) {
                gM = 0;
                bM = 1;
            } else if (bM == 1) {
                bM = 0;
                rM = 1;
            }

            for (int i = 0; i < 256; i += 4) {
                int r = i * rM;
                int g = i * gM;
                int b = i * bM;
                ArtDmxPacket dmxPacket = new ArtDmxPacket((byte)0, (byte)0, (byte)0, (byte)0, new byte[]{(byte)r, (byte)g, (byte)b});
                controller.broadcastPacket(dmxPacket);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 254; i >= 0; i -= 4) {
                int r = i * rM;
                int g = i * gM;
                int b = i * bM;
                ArtDmxPacket dmxPacket = new ArtDmxPacket((byte)0, (byte)0, (byte)0, (byte)0, new byte[]{(byte)r, (byte)g, (byte)b});
                controller.broadcastPacket(dmxPacket);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ArtDmxPacket dmxPacket = new ArtDmxPacket((byte)0, (byte)0, (byte)0, (byte)0, new byte[]{(byte)0, (byte)0, (byte)0});
        controller.broadcastPacket(dmxPacket);
    }

    @Override
    public void onArtnetPacketReceive(ArtnetPacket packet) {
        System.out.println("packet from: " + packet.getSender());
    }
}
