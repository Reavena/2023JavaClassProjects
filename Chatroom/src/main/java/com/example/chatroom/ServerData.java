package com.example.chatroom;
//CHAT WORKS WITH UDP (User Datagram Protocol)

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerData {
    private static byte[] incoming = new byte [256];
    private static final int PORT = 8000;

    private static DatagramSocket socket; //we dont have to worry how the OS wiRll send my packages

    //initialize DatagramSocket on this PORT
    static{
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e){
            throw new RuntimeException(e);
        }
    }

    // List of Users
    private static ArrayList<Integer> users = new ArrayList();
    // Local Host address
    private static final InetAddress address;

    static{
        try{
            address = InetAddress.getByName("localhost");
        }catch (UnknownHostException e){
            throw new RuntimeException(e);
        }
    }
//Associate Users Port with the chat room they are in
    //private static final Map<Integer, String> userRoom = new HashMap<>();
//Associate a chat room with a list of users
    private static final Map<String, ArrayList<Integer>> rooms = new HashMap<>();


    public static void main(String[] args){
        System.out.println(("SERVER started on PORT " + PORT));

        //Client requests loop

        while(true){
            //for every iteration we prepare a packet
            DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
            try{//listening, receiving a packet if there is one
                socket.receive(packet);
            }catch (IOException e){
                throw new RuntimeException(e);
            }

            //Extract the data of that packet
            String message = new String(packet.getData(),0,packet.getLength());
            System.out.println("Server  has received" + message);

            // if new user, add their port to users list
            if(message.contains("init_user;")){
                users.add(packet.getPort());
            }
            // if normal message
            else{
                // get user port from the packet , getPort is a DatagramSocket function
                int userPort = packet.getPort();
                //we can only send bytes, convert string to bytes
                byte[] byteMessage = message.getBytes();

                //forward to all other users (except the one who sent it out)
                for(int forward_port : users){
                    if(forward_port != userPort){
                        //create the packet and use socket.send
                        DatagramPacket forward = new DatagramPacket(byteMessage, byteMessage.length,address,forward_port);
                        try{
                            socket.send(forward);
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }

                }
            }

        }
    }
}
