package dental.app.filetools;

import java.io.File;

public class FileSearcher {

    public static File[] findFiles(String pathToDir, String extens) {
        //TODO correct the paths and extensions.
        File dir = new File(pathToDir);
        return dir.listFiles((dir1, name1) ->
                name1.endsWith(extens));
    }

}
