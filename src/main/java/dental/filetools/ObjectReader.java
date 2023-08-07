package dental.filetools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * The inner private class, has {@link FileInputStream} and {@link ObjectInputStream}.
 * By constructor, with {@link File} as the parameter,
 *  class creates needed objects for {@linkplain ObjectInputStream#readObject() reading} a files.
 * Made just for usability.
 */
public class ObjectReader {

    public final FileInputStream fileInput;
    public final ObjectInputStream objectInput;

    /**
     * Create the {@link ObjectReader ObjectReader} for {@linkplain ObjectInputStream#readObject() reading} an objects in a file.
     * @param file   The {@link File file} from that to read the object.
     * @throws IOException If an I/O error occurs while writing stream header
     */
    private ObjectReader(File file) throws IOException {
        this.fileInput = new FileInputStream(file);
        this.objectInput = new ObjectInputStream(fileInput);
    }

    /**
     * This method is actually wrapper with {@code try(resource)/catch,}
     *  of {@linkplain ObjectInputStream#readObject()} method,
     *  just for usability.
     * @return The {@linkplain ObjectInputStream#readObject() readable} object,
     *  or null if thrown exception.
     */
    public Object reading() {
        try (this.fileInput; this.objectInput) {
            return objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}