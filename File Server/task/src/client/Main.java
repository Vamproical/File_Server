package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String path = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;
    private static boolean isPost = false;
    private static boolean isGet = false;
    private static boolean isDelete = false;
    private final static Scanner scanner = new Scanner(System.in);
    private static String file;

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 34522;
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            String request = createRequest();
            output.writeUTF(request);
            if (isPost) {
                try (FileInputStream fileInputStream = new FileInputStream(path + file);
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                    byte[] message = bufferedInputStream.readAllBytes();
                    output.writeInt(message.length);
                    output.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("The request was sent.");
            if (!request.equals("exit")) {
                String response = input.readUTF();
                processResponse(response, input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processResponse(String response, DataInputStream dataInputStream) {
        if (isPost) {
            if (response.contains("403")) {
                System.out.println("The response says that creating the file was forbidden!");
            } else {
                System.out.println("Response says that file is saved! ID = " + response.substring(4));
            }
        } else if (isGet) {
            if (response.contains("404")) {
                System.out.println("The response says that this file is not found!");
            } else {
                try {
                    System.out.println("Client");
                    int arraySize = Integer.parseInt(response.substring(4));
                    byte[] fileAsBytes = new byte[arraySize];
                    System.out.println(arraySize);
                    dataInputStream.readFully(fileAsBytes, 0, arraySize);
                    System.out.println(Arrays.toString(fileAsBytes));
                    System.out.print("The file was downloaded! Specify a name for it: ");
                    String fileName = scanner.nextLine();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
                        bufferedOutputStream.write(fileAsBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("File saved on the hard drive!");
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
        String action = scanner.nextLine();
        switch (action) {
            case "1":
                System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id): ");
                int option = Integer.parseInt(scanner.nextLine());
                String name;
                String output = "GET";
                if (option == 1) {
                    System.out.println("Enter name: ");
                    name = scanner.nextLine();
                    output = output + " BY_NAME " + name;
                } else {
                    System.out.println("Enter id:");
                    name = scanner.nextLine();
                    output = output + " BY_ID " + name;
                }
                isGet = true;
                return output;
            case "2":
                System.out.println("Enter name of the file: ");
                file = scanner.nextLine();
                System.out.println("Enter name of the file to be saved on server: ");
                String nameOnServer = scanner.nextLine();
                isPost = true;
                return "PUT " + (!nameOnServer.equals("") ? nameOnServer : file);
            case "3":
                System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
                int optionDel = Integer.parseInt(scanner.nextLine());
                String filenameDel;
                String outputDel = "DELETE";
                if (optionDel == 1) {
                    System.out.println("Enter name: ");
                    filenameDel = scanner.nextLine();
                    outputDel = outputDel + " BY_NAME " + filenameDel;
                } else {
                    System.out.println("Enter id:");
                    filenameDel = scanner.nextLine();
                    outputDel = outputDel + " BY_ID " + filenameDel;
                }
                isDelete = true;
                return outputDel;
            default:
                return "exit";
        }
    }
}
