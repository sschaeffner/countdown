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

import me.sschaeffner.jArtnet.packets.ArtPollPacket;
import me.sschaeffner.jArtnet.packets.ArtPollReplyPacket;
import me.sschaeffner.jArtnet.packets.ArtnetPacket;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * An Art-Net Controller.
 *
 * This is typically a lighting console.
 * May be used to send Art-Net packets.
 *
 * @author sschaeffner
 */
public class ArtnetController {

    //controller style code
    private static final int STYLE_CODE = ArtnetStyleCodes.ST_CONTROLLER;

    //list of all connected nodes
    private final ArrayList<ArtnetNode> nodes;

    //server socket to send and receive Art-Net packets with
    private final DatagramSocket socket;

    //broadcast address
    private BroadcastAddress broadcastAddress;

    //receiver thread listening for Art-Net packets
    private final Thread receiverThread;

    //list of listeners for received Art-Net packets
    private final ArrayList<ArtnetPacketListener> listeners;

    //whether to ignore packets sent from this controller
    private boolean ignoreOwnPackets = true;

    //whether the controller is currently running
    private boolean running = true;

    /**
     * Constructs a new instance of this class.
     */
    public ArtnetController() {
        this.nodes = new ArrayList<>();
        this.listeners = new ArrayList<>();

        BroadcastAddress[] bca = getBroadcastAddresses();

        if (bca.length > 0) {
            System.out.println("using " + bca[0]);
            broadcastAddress = bca[0];
        } else System.err.println("no broadcast address available");

        //open udp socket
        try {
            socket = new DatagramSocket(ArtnetPacket.UDP_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("cannot start ArtnetController: cannot open socket");
        }

        //start receiver thread
        this.receiverThread = new Thread(() -> {
            try {
                System.out.println("Listening on " + InetAddress.getLocalHost().getHostAddress() + ":" + ArtnetPacket.UDP_PORT);

                byte[] receiveData = new byte[600];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, 0, receiveData.length);

                while (running) {
                    if (!socket.isClosed()) {
                        try {
                            socket.receive(receivePacket);
                            byte[] data = receivePacket.getData();
                            InetAddress sender = receivePacket.getAddress();
                            int port = receivePacket.getPort();
                            onPacketReceive(data, sender, port);
                        } catch (SocketException e) {
                            //do nothing as the socket is just closed
                        }
                    }
                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
        this.receiverThread.start();
    }

    /**
     * Tries to discover nodes by sending an ArtPoll packet.
     */
    public void discoverNodes() {
        ArtPollPacket pollPacket = new ArtPollPacket();
        broadcastPacket(pollPacket);

        //answer itself
        broadcastPacket(constructArtPollReplyPacket());
    }

    /**
     * Constructs an ArtPollReply packet matching this controller.
     *
     * @return an ArtPollReplyPacket matching this controller
     */
    private ArtPollReplyPacket constructArtPollReplyPacket() {
        InetAddress address = broadcastAddress.getInterfaceAddress().getAddress();
        byte versInfoH = (byte) 0;
        byte versInfoL = (byte) 1;
        byte netSwitch = 0, subSwitch = 0;
        byte oemHi = (byte)0xff, oem = (byte)0xFF;
        byte ubeaVersion = 0;
        byte status1 = (byte) 0b00110000;
        byte estaManLo = 0, estaManHi = 0;
        byte[] shortName = new byte[]{'j', 'A', 'r', 't', 'n', 'e', 't', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 0};
        byte[] longName = new byte[]{'j', 'A', 'r', 't', 'n', 'e', 't', ' ', '-', ' ', 'A', 'n', ' ', 'A', 'r', 't', '-',
                                     'N', 'e', 't', ' ', 'l', 'i', 'b', 'r', 'a', 'r', 'y', ' ', 'f', 'o', 'r', ' ', 'J',
                                     'a', 'v', 'a', ' ', 'b', 'y', ' ', 'S', 'i', 'm', 'o', 'n', ' ', 'S', 'c', 'h', 'a',
                                     'e', 'f', 'f', 'n', 'e', 'r', ' ', ' ', ' ', ' ', ' ', ' ', 0};
        byte[] nodeReport = new byte[]{'r', 'e', 'a', 'd', 'y', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                                       ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                                       ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                                       ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 0};
        byte numPortsHi = 0, numPortsLo = 1;
        byte[] portTypes = new byte[]{0b001000101, 0b001000101, 0b001000101, 0b001000101};
        byte[] goodInput = new byte[]{0b0000, 0b0000, 0b0000, 0b0000}, goodOutput = new byte[]{(byte)0b10000000, (byte)0b10000000, (byte)0b10000000, (byte)0b10000000};
        byte[] swIn = new byte[]{0, 0, 0, 0}, swOut = new byte[]{0, 0, 0, 0};
        byte swVideo = 0;
        byte swMacro = 0;
        byte swRemote = 0;
        byte style = STYLE_CODE;
        byte[] mac = new byte[6];
        try {
            mac = broadcastAddress.getNetworkInterface().getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] bindIp = new byte[]{0, 0, 0, 0};
        byte bindIndex = 0;
        byte status2 = 0b00001110;

        return new ArtPollReplyPacket(address, versInfoH, versInfoL, netSwitch, subSwitch, oemHi, oem, ubeaVersion,
                status1, estaManLo, estaManHi, shortName, longName, nodeReport, numPortsHi, numPortsLo, portTypes,
                goodInput, goodOutput, swIn, swOut, swVideo, swMacro, swRemote, style, mac, bindIp, bindIndex, status2);
    }

    /**
     * Sends an Art-Net packet to a single node.
     *
     * @param artnetPacket packet to send
     */
    public void unicastPacket(ArtnetPacket artnetPacket, ArtnetNode node) {
        if (socket != null) {
            InetAddress nodeAddress = node.getInetAddress();
            if (nodeAddress != null) {

                byte[] data = artnetPacket.getPackageBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, nodeAddress, ArtnetPacket.UDP_PORT);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends an Art-Net packet to all nodes.
     *
     * @param artnetPacket packet to send
     */
    public void broadcastPacket(ArtnetPacket artnetPacket) {
        if (socket != null) {
            if (broadcastAddress != null) {
                byte[] data = artnetPacket.getPackageBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, broadcastAddress.getBroadcastAddress(), ArtnetPacket.UDP_PORT);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("no broadcast address available");
            }
        } else {
            System.err.println("no socket available to broadcast");
        }
    }

    /**
     * Returns all available broadcast addresses.
     *
     * @return all available broadcast addresses
     */
    private BroadcastAddress[] getBroadcastAddresses() {
        ArrayList<BroadcastAddress> bcAddresses = new ArrayList<>();

        //iterate trough all network interfaces
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface nwi = networkInterfaceEnumeration.nextElement();

                //check whether the network interface is a loopback type interface
                if (!nwi.isLoopback()) {

                    //get the interface's addresses
                    for (InterfaceAddress interfaceAddress : nwi.getInterfaceAddresses()) {

                        //get the address's broadcast address
                        InetAddress bcAddress = interfaceAddress.getBroadcast();

                        //check if broadcast address is available
                        if (bcAddress != null) {
                            bcAddresses.add(new BroadcastAddress(nwi, interfaceAddress, bcAddress));
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return bcAddresses.toArray(new BroadcastAddress[bcAddresses.size()]);
    }

    /**
     * Manages what to do when a packet is received.
     *
     * @param bytes     received data
     * @param sender    InetAddress of the packet's sender
     * @param port      packet sender's port
     */
    private void onPacketReceive(byte[] bytes, InetAddress sender, int port) {

        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //ignore packets sent from this controller
        if (!(ignoreOwnPackets && (broadcastAddress.equals(sender) || (localhost != null && localhost.equals(sender))))) {
            ArtnetPacket artnetPacket = ArtnetOpCodes.fromBytes(bytes);
            if (artnetPacket != null) {

                //if ArtPollReply is sent, add all new nodes to list
                if (artnetPacket instanceof ArtPollReplyPacket) {
                    ArtnetNode senderNode = isNodeRegistered(sender);

                    if (senderNode == null) {
                        //create new node and add it to the list
                        ArtnetNode node = new ArtnetNode(sender, (ArtPollReplyPacket) artnetPacket);
                        nodes.add(node);
                        System.out.println("new node: " + node);
                    } else {
                        //update ArtnetNode information
                        senderNode.setArtPollReplyPacket((ArtPollReplyPacket) artnetPacket);
                    }
                } else {

                    //set sender node for other packets
                    ArtnetNode senderNode = getNodeFromInetAddress(sender);
                    if (senderNode != null) artnetPacket.setSender(null);

                    //inform listeners
                    listeners.forEach(listener -> listener.onArtnetPacketReceive(artnetPacket));
                }
            }
        }
    }

    /**
     * Checks whether a node with a given InetAddress is already registered.
     *
     * @param sender    InetAddress to check
     * @return          whether a node is already registered
     */
    private ArtnetNode isNodeRegistered(InetAddress sender) {
        for (ArtnetNode node : nodes) {
            if (node.getInetAddress().equals(sender)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns the node with a specific InetAddress.
     *
     * If no node is registered, null is returned.
     *
     * @param nodeAddress   the node's address
     * @return              node with matching InetAddress
     */
    private ArtnetNode getNodeFromInetAddress(InetAddress nodeAddress) {
        for (ArtnetNode node : nodes) if (node.getInetAddress().equals(nodeAddress)) return node;
        return null;
    }

    /**
     * Closes the server socket.
     *
     * Blocking until the receiver thread is dead.
     */
    public void closeSocket() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            if (socket.isConnected()) socket.disconnect();
            socket.close();
        }
        try {
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an ArtnetPacketListener.
     *
     * @param listener ArtnetPacketListener instance
     */
    public void addArtnetPacketListener(ArtnetPacketListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns all registered nodes.
     *
     * @return all registered nodes
     */
    public ArtnetNode[] getNodes() {
        return nodes.toArray(new ArtnetNode[nodes.size()]);
    }

    /**
     * Removes an ArtnetPacketListener.
     *
     * @param listener  ArtnetPacketListener instance
     * @return <tt>true</tt> if the listener was registered and successfully removed
     */
    public boolean removeArtnetPacketListener(ArtnetPacketListener listener) {
        return listeners.remove(listener);
    }

    public void setIgnoreOwnPackets(boolean ignoreOwnPackets) {
        this.ignoreOwnPackets = ignoreOwnPackets;
    }

    /**
     * An InetAddress that is combined with its InterfaceAddress.
     */
    public class BroadcastAddress {
        private final NetworkInterface networkInterface;
        private final InterfaceAddress interfaceAddress;
        private final InetAddress broadcastAddress;

        /**
         * Constructs a new instance of this class.
         */
        public BroadcastAddress(NetworkInterface networkInterface, InterfaceAddress interfaceAddress, InetAddress broadcastAddress) {
            this.networkInterface = networkInterface;
            this.interfaceAddress = interfaceAddress;
            this.broadcastAddress = broadcastAddress;
        }

        @Override
        public String toString() {
            return "BroadcastAddress: " + interfaceAddress.getAddress().getHostAddress() + " => " + broadcastAddress.getHostAddress();
        }

        /**
         * Returns the NetworkInterface.
         *
         * @return NetworkInterface instance
         */
        public NetworkInterface getNetworkInterface() {
            return networkInterface;
        }

        /**
         * Returns the InterfaceAddress.
         *
         * @return InterfaceAddress instance
         */
        public InterfaceAddress getInterfaceAddress() {
            return interfaceAddress;
        }

        /**
         * Returns the broadcast address.
         *
         * @return broadcast address
         */
        public InetAddress getBroadcastAddress() {
            return broadcastAddress;
        }
    }
}
