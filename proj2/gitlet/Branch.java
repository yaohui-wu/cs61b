package gitlet;

import static gitlet.Utils.*;
import java.io.File;

/** Branches of the repository. */
public class Branch {
    public static final File BRANCHES = join(Repository.GITLET, "branches");
    
    public static void setId(String branch, String id) {
        writeContents(join(BRANCHES, branch), id);
    }

    public static String getId(String branch) {
        File file = join(BRANCHES, branch);
        if (file.exists()) {
            return readContentsAsString(file);
        }
        return null;
    }
}
