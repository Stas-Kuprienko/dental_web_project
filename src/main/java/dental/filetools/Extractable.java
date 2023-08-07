package dental.filetools;

import java.io.ObjectInputStream;

/**
 * Instances of an extractable classes can be {@linkplain ObjectReader#reading() read (extracted)}
 *  from a {@link java.io.File files} by {@linkplain ObjectInputStream} class.
 */
public interface Extractable {

    /**
     * This static method extracts an {@link Extractable} object from the {@link java.io.File file}.
     * @param reader The {@link ObjectReader object reader}
     *  for {@linkplain ObjectInputStream#readObject() reading} file.
     * @return The {@link Extractable} object from the file.
     */
    static Extractable extractFromFile(ObjectReader reader) {
        Extractable object;
        object = (Extractable) reader.reading();
        return object;
    }

}
