package gitlet;

import static gitlet.Util.*;
import java.util.HashMap;

public class StagingArea {
    private static final File STAGE_FILES = join(Repository.GITLET_DIR, "staging_area");
    private HashMap<String, String> addition;
    private HashSet<String> removal;

    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashMap<>();
    }

    public HashMap<String, String> getAddition() {
        return addition;
    }

    public HashMap<String, String> getRemoval() {
        return removal;
    }

    public void save() {
        writeContens(STAGE_FILES, this);
    }

    public StagingArea load() {
        readObject(STAGE_FILES, StagingArea.class);
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }
}
