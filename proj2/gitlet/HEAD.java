package gitlet;

import java.io.File;
import static gitlet.Utils.*;

public class HEAD {
    public static final File HEAD = join(Repository.GITLET_DIR, "head");

    public void setBranchName(String branchName) {
        writeContents(HEAD, branchName);
    }

    public String getBranchName() {
        return readContentsAsString(HEAD);
    }
}
