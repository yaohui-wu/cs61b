package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

/** Represents the staging area in a Gitlet version control system. */
public class StagingArea implements Serializable {
    private static final File STAGE = join(Repository.GITLET, "stage");
    // Maps the name of the file staged for addition to the blob ID.
    private Map<String, String> addition;
    // Files staged for removal.
    private Set<String> removal;

    public StagingArea() {
        addition = new TreeMap<>();
        removal = new TreeSet<>();
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public Set<String> getRemoval() {
        return removal;
    }

    public void save() {
        writeObject(STAGE, this);
    }

    public static StagingArea load() {
        return readObject(STAGE, StagingArea.class);
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }
}
