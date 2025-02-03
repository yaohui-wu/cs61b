package gitlet;

import static gitlet.Utils.*;
import java.io.File;

public class Branch {
    public static final File BRANCHES_DIR = join(Repository.GITLET_DIR, "branches");

    public static void setCommitId(String branchName, String commitId) {
        File branchFile = join(BRANCHES_DIR, branchName);
        writeContents(branchFile, commitId);
    }

    public static String getCommitId(String branchName) {
        File branchFile = join(BRANCHES_DIR, branchName);
        if (branchFile.exists()) {
            return readContentsAsString(branchFile);
        }
        return null;
    }
}
