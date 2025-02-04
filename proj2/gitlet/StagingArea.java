package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;

/**  */
public class StagingArea implements Serializable {
    private static final File STAGED_FILES = join(Repository.GITLET_DIR, "staging_area");
    // Maps the name of the file staged for addition to the blob ID.
    private TreeMap<String, String> addition;
    // Files staged for removal.
    private TreeSet<String> removal;

    public StagingArea() {
        addition = new TreeMap<>();
        removal = new TreeSet<>();
    }

    public TreeMap<String, String> getAddition() {
        return addition;
    }

    public TreeSet<String> getRemoval() {
        return removal;
    }

    public void save() {
        writeObject(STAGED_FILES, this);
    }

    public static StagingArea load() {
        return readObject(STAGED_FILES, StagingArea.class);
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }
}
