package gitlet;

import static gitlet.Utils.*;
import java.io.File;

/** Represents a reference to the current commit in the Gitlet repository. */
public class HEAD {
    public static final File HEAD = join(Repository.GITLET, "HEAD");

    public static void setBranch(String branch) {
        writeContents(HEAD, branch);
    }

    public static String getBranch() {
        return readContentsAsString(HEAD);
    }
}
