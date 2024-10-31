package tolobekov.library.database;

import java.io.*;

public class SerializationHelper {

    public static void serialize(Object obj, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(obj);
        }
    }

    public static Object deserialize(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return in.readObject();
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, return null or initialize an empty state as
            // appropriate.
            return null;
        }
    }
}
