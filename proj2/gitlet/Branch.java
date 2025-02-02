package gitlet;

import java.io.File;
import static gitlet.Utils.*;


public class Branch {
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    private Branch() {
        File master = join(BRANCHES_DIR, "master");
    }
}
