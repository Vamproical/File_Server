package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private final List<String> files;
    private final Scanner scanner;
    public Server() {
        files = new ArrayList<>();
        scanner = new Scanner(System.in);
    }
    public void run() {
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            chooseAction(input);
            input = scanner.nextLine();
        }
    }

    private void chooseAction(String input) {
        String[] inputs = input.split(" ");
        switch (inputs[0]) {
            case "add":
                add(inputs[1]);
                break;
            case "get":
                get(inputs[1]);
                break;
            case "delete":
                delete(inputs[1]);
                break;
        }
    }

    private void add(String file) {
        if (files.contains(file) || (!file.matches("file\\d|file10"))) {
            System.out.println("Cannot add the file " + file);
        } else {
            files.add(file);
            System.out.println("The file " + file + " added successfully");
        }
    }

    private void get(String file) {
        if (files.contains(file)) {
            System.out.println("The file " + file + " was sent");
        } else {
            System.out.println("The file " + file + " not found");
        }
    }

    private void delete(String file) {
        if (files.contains(file)) {
            files.remove(file);
            System.out.println("The file " + file + " was deleted");
        } else {
            System.out.println("The file " + file + " not found");
        }
    }
}
