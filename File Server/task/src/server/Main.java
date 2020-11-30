package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<String> files = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            String[] inputs = input.split(" ");
            switch (inputs[0]) {
                case "add":
                    if (files.contains(inputs[1]) || (!inputs[1].matches("file\\d|file10"))) {
                        System.out.println("Cannot add the file " + inputs[1]);
                    } else {
                        files.add(inputs[1]);
                        System.out.println("The file " + inputs[1] + " added successfully");
                    }
                    break;
                case "get":
                    if (files.contains(inputs[1])) {
                        System.out.println("The file " + inputs[1] + " was sent");
                    } else {
                        System.out.println("The file " + inputs[1] + " not found");
                    }
                    break;
                case "delete":
                    if (files.contains(inputs[1])) {
                        files.remove(inputs[1]);
                        System.out.println("The file " + inputs[1] + " was deleted");
                    } else {
                        System.out.println("The file " + inputs[1] + " not found");
                    }
                    break;
            }
            input = scanner.nextLine();
        }
    }
}