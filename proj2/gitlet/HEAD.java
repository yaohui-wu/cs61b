package gitlet;

import static gitlet.Utils.*;
import java.io.File;

/** The HEAD file stores the name of the currently checked out branch. */
public class HEAD {
    public static final File HEAD = join(Repository.GITLET_DIR, "HEAD");

    public static void setBranch(String branch) {
        writeContents(HEAD, branch);
    }

    public static String getBranch() {
        return readContentsAsString(HEAD);
    }
}
