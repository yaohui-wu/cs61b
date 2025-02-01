package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yaohui Wu
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */
    private void init() {
        validateRepo();
        GITLET_DIR.mkdir();
        Commit initCommit = new Commit();
    }

    private void validateRepo() {
        if (GITLET_DIR.exists()) {
            String message = """
                    A Gitlet version-control system already exists in the
                    current directory.
                    """;
            Main.exit(message);
        }
    }

    private void add(String fileName) {}

    private void commit(String message) {}

    private void checkout() {}

    private void log() {}
}
