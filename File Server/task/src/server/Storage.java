package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Storage {
    private static final String path = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;
    private final HashMap<String, Integer> storageName;
    private final static File nameFile = new File(path + "names.txt");

    public Storage() {
        storageName = new HashMap<>();
        if (nameFile.exists()) {
            loadMap();
        }
    }

    public int putFile(byte[] content, String name) {
        if (writeFile(content, name)) {
            int id = getNextId();
            storageName.put(name, id);
            saveMap();
            return id;
        } else return -1;
    }

    private int getNextId() {
        int id = 10;
        for (Integer integer : storageName.values()) {
            if (integer > id) {
                id = integer;
            }
        }
        return ++id;
    }

    public boolean writeFile(byte[] content, String name) {
        File file = new File(path + name);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
            bufferedOutputStream.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] readFile(String name) {
        File file = new File(path + name);
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                return bufferedInputStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] readFile(Integer id) {
        for (Map.Entry<String, Integer> currentEntry : storageName.entrySet()) {
            String currentName = currentEntry.getKey();
            Integer currentId = currentEntry.getValue();
            if (currentId.equals(id)) {
                File file = new File(path + currentName);
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                    return bufferedInputStream.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean deleteFile(String name) {
        if (storageName.containsKey(name)) {
            File file = new File(path + name);
            file.delete();
            storageName.remove(name);
            saveMap();
            return true;
        }
        return false;
    }

    public boolean deleteFile(Integer id) {
        for (Map.Entry<String, Integer> currentEntry : storageName.entrySet()) {
            String currentName = currentEntry.getKey();
            Integer currentId = currentEntry.getValue();
            if (currentId.equals(id)) {
                File file = new File(path + currentName);
                file.delete();
                storageName.remove(id);
                return true;
            }
        }
        return false;
    }

    private void loadMap() {
        try (Scanner scanner = new Scanner(nameFile)) {
            while (scanner.hasNext()) {
                String[] data = scanner.nextLine().split(" ");
                storageName.put(data[0], Integer.parseInt(data[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + nameFile);
        }
    }

    protected void saveMap() {
        nameFile.delete();
        try (PrintWriter printWriter = new PrintWriter(nameFile)) {
            for (var map : storageName.entrySet()) {
                printWriter.write(map.getKey() + " " + map.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }
}
