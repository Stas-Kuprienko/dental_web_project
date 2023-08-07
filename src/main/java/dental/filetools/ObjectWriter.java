package dental.filetools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

/**
 * The class for {@linkplain #isWritten() writing} an objects in a file. It is generic class,
 *  can write any non-primitive object. Has the fields - {@code <T>object}, {@link FileOutputStream} and {@link ObjectOutputStream}.
 * Its constructor take {@link File file} and writable {@link Object}, then execute {@linkplain #isWritten() writing}
 *  of the object in the given {@link File file}, return true if it was successful.
 * @param <T> The type of object than is needed to write.
 */
public class ObjectWriter <T> {

    /**
     * The {@link Object} for writing. Its {@linkplain #hashCode() hashcode} must be correct,
     * cause by it create {@link File file} name.
     */
    protected T object;

    public final FileOutputStream fileOutput;
    public final ObjectOutputStream objectOutput;

    /**
     * Create the {@link ObjectWriter ObjectsWriter} for {@linkplain #isWritten() writing} an objects in a file.
     * @param file   The {@link File file} in that {@linkplain #isWritten() to write} the object.
     * @param object The writable {@link Object}. Its {@linkplain #hashCode() hashcode} must be correct,
     *                cause by it create {@link File file} name.
     * @throws IOException If an I/O error occurs while writing stream header
     */
    public ObjectWriter(File file, T object) throws IOException {
        this.object = object;
        this.fileOutput = new FileOutputStream(file);
        this.objectOutput = new ObjectOutputStream(fileOutput);
    }

    /**
     * To write the given to class constructor object in the file.
     * This method is actually wrapper of {@linkplain ObjectOutputStream#writeObject(Object)} method,
     *  with {@code try(resource)/catch,} just for usability.
     * @return True if it was successful, or false if not.
     */
    public boolean isWritten() {
        try (this.objectOutput) {
            objectOutput.writeObject(this.object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * White an object to the file. The object {@linkplain #hashCode() hashcode} must be correct,
     *  cause by it create {@link File file} name.
     * @param object The {@link Object} for {@linkplain ObjectWriter#isWritten() writing}.
     * @return True if writing was successful.
     * @param <T> The type of object than is needed to write.
     */
    public static <T> boolean writeObjectToFile(T object, String extens, String dir) {
        //TODO correct the paths and extensions.
        Path path = Path.of(dir);
        File file = new File(path + "/" + object.hashCode() + extens);
        try {
            return new ObjectWriter<T>(file, object).isWritten();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
