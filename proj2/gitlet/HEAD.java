package gitlet;

import java.io.File;
import static gitlet.Utils.*;

public class HEAD {
    public static final File HEAD = join(Repository.GITLET_DIR, "HEAD");

    public static void setBranchName(String branchName) {
        writeContents(HEAD, branchName);
    }

    public static String getBranchName() {
        return readContentsAsString(HEAD);
    }
}
