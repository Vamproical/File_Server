package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static boolean isPost = false;
    private static boolean isGet = false;
    private static boolean isDelete = false;
    private final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            String request = createRequest();
            System.out.println("The request was sent.");
            output.writeUTF(request);
            if (!request.equals("exit")) {
                String response = input.readUTF();
                processResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processResponse(String response) {
        if (isPost) {
            if (response.contains("403")) {
                System.out.println("The response says that creating the file was forbidden!");
            } else {
                System.out.println("The response says that the file was created!");
            }
        } else if (isGet) {
            if (response.contains("404")) {
                System.out.println("The response says that the file was not found!");
            } else {
                System.out.println("The content of the file is: " + response.substring(4));
            }
        } else if (isDelete) {
            if (response.contains("404")) {
                System.out.println("The response says that the file was not found!");
            } else {
                System.out.println("The response says that the file was successfully deleted!");
            }
        }
    }

    private static String createRequest() {
        System.out.println("Enter action (1 - get the file, 2 - create a file, 3 - delete the file):");
        String action = scanner.next();
        switch (action) {
            case "1":
                System.out.println("Enter filename: ");
                String filename = scanner.next();
                scanner.nextLine();
                isGet = true;
                return "GET " + filename;
            case "2":
                System.out.println("Enter filename: ");
                String file = scanner.next();
                scanner.nextLine();
                System.out.println("Enter file content: ");
                String content = scanner.nextLine().trim();
                isPost = true;
                return "PUT " + file + " " + content;
            case "3":
                System.out.println("Enter filename: ");
                String name = scanner.next();
                scanner.nextLine();
                isDelete = true;
                return "DELETE " + name;
            default:
                return "exit";
        }
    }
}
