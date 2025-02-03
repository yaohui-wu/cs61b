package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class StagingArea implements Serializable {
    private static final File STAGED_FILES = join(Repository.GITLET_DIR, "staging_area");
    private HashMap<String, String> addition;
    private HashSet<String> removal;

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashSet<>();
    }

    public HashMap<String, String> getAddition() {
        return addition;
    }

    public HashSet<String> getRemoval() {
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
