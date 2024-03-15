package com.example.chatroom;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientData extends Application{

    private static final String LOG_FILE_PATH = "log.txt";
    private static final DatagramSocket socket;

    static{
        try{
            socket = new DatagramSocket();//initializes to any available port
        }catch (SocketException e){
            throw new RuntimeException(e);
        }
    }

    //address stays the  same
    private static final InetAddress address;

    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static String identifier = "Client";

    private static final int SERVER_PORT = 8000; //send to server


    @FXML
    private TextField usernameField;

    @FXML
    private static final TextArea messageArea = new TextArea();
@FXML
    private static final TextField inputBox = new TextField();

    public static void main (String[] args) throws IOException{

        //make a new thread for receiving messages
        ClientThread clientThread = new ClientThread(socket, messageArea);
        clientThread.start();

        //send initialization message to server
        byte[] uuid = ("init_user;" + identifier).getBytes();
        DatagramPacket initialize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        socket.send(initialize);

        launch();

    }
    //TO DO: Add a method to send a room creation request

    @Override
    public void start(Stage primaryStage)  {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username Input");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your username:");

        dialog.showAndWait().ifPresent(username -> {
            identifier = username;
            System.out.println("User : " + identifier + " has joined the chat!");


            messageArea.setMaxWidth(500);
            messageArea.setEditable(false);

            inputBox.setMaxWidth(500);
            inputBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String temp = identifier + " : " + inputBox.getText(); // message to send
                    String logEntry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                            + " - " + temp;

                    //add to txt file
                    try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                        writer.println(logEntry);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Update UI
                    messageArea.setText(messageArea.getText() + inputBox.getText() + "\n"); // update messages on screen
                    byte[] msg = temp.getBytes(); // convert to bytes
                    inputBox.setText(""); // remove text from input box

                    // create a packet & send
                    DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                    try {
                        socket.send(send);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // put everything on screen
            Scene scene = new Scene(new VBox(35, messageArea, inputBox), 500, 300);
            primaryStage.setTitle("Chatroom");
            primaryStage.setScene(scene);
            primaryStage.show();
        });

    }
}