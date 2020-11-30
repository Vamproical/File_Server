package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        System.out.println("Server started!");
        try (
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        Socket socket = server.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output  = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Received: " + input.readUTF());
            System.out.println("Sent: All files were sent!");
            output.writeUTF("All files were sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}