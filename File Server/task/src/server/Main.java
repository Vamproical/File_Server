package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        System.out.println("Server started!");
        boolean isRun = true;
        while (isRun)
            try (
                    ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
                    Socket socket = server.accept();
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                String response = input.readUTF();
                if (response.equals("exit")) {
                    isRun = false;
                } else {
                    String request = processResponse(response);
                    assert request != null;
                    output.writeUTF(request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static String processResponse(String response) {
        String path = "C:\\Users\\Никита\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data";
        String[] data = response.split(" ");
        File file = new File(path + "\\" + data[1]);
        if (response.startsWith("GET")) {
            if (!isCreated(file)) {
                return "404";
            } else {
                try (Scanner scanner = new Scanner(file)) {
                    return "200 " + scanner.nextLine();
                } catch (FileNotFoundException e) {
                    System.out.println("No file found: " + path);
                }
            }
        } else if (response.startsWith("PUT")) {
            if (isCreated(file)) {
                return "403";
            } else {
                try (FileWriter writer = new FileWriter(file)) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < data.length; i++) {
                        builder.append(data[i]).append(" ");
                    }
                    writer.write(builder.toString().trim());
                    return "200";
                } catch (IOException e) {
                    System.out.printf("An exception occurs %s", e.getMessage());
                }
            }
        } else {
            if (!isCreated(file)) {
                return "404";
            } else {
                if (file.delete()) {
                    return "200";
                } else {
                    return "404";
                }

            }
        }
        return null;
    }

    private static boolean isCreated(File file) {
        try {
            return !(file.createNewFile());
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
            return false;
        }
    }
}