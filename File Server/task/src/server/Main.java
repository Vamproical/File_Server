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
        int port = 34522;
        System.out.println("Server started!");
        Storage storage = new Storage();
        boolean isRun = true;
        while (isRun) {
            try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
                 Socket socket = server.accept();
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                String input = dataInputStream.readUTF();
                String[] request = input.split(" ");
                if ("exit".equals(input)) {
                    storage.saveMap();
                    isRun = false;
                } else {
                    switch (request[0]) {
                        case "GET":
                            byte[] content;
                            if (request[1].equals("BY_NAME")) {
                                content = storage.readFile(request[2]);
                            } else {
                                content = storage.readFile(Integer.parseInt(request[2]));
                            }
                            if (content != null) {
                                dataOutputStream.writeUTF("200 " + content.length);
                                dataOutputStream.write(content);
                            } else {
                                dataOutputStream.writeUTF("404");
                            }
                            break;
                        case "PUT":
                            int resultingID;
                            String desiredName = request[1];
                            int size = dataInputStream.readInt();
                            resultingID = storage.putFile(dataInputStream.readNBytes(size), desiredName);
                            if (resultingID == -1) {
                                dataOutputStream.writeUTF("404");
                            } else {
                                dataOutputStream.writeUTF("200 " + resultingID);
                            }
                            break;
                        case "DELETE":
                            boolean result;
                            if (request[1].equals("BY_NAME")) {
                                result = storage.deleteFile(request[2]);
                            } else {
                                result = storage.deleteFile(Integer.parseInt(request[2]));
                            }
                            dataOutputStream.writeUTF(result ? "200" : "404");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}