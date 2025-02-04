package gitlet;

import static gitlet.Utils.*;
import java.io.File;

/** Branches of the repository. */
public class Branch {
    public static final File BRANCHES_DIR = join(Repository.GITLET_DIR, "branches");
    
    public static void setId(String branch, String id) {
        File file = join(BRANCHES_DIR, branch);
        writeContents(file, id);
    }

    public static String getId(String branch) {
        File file = join(BRANCHES_DIR, branch);
        if (file.exists()) {
            return readContentsAsString(file);
        }
        return null;
    }
}
