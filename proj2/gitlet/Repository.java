package gitlet;

import java.io.File;
import static gitlet.Utils.*;
import java.util.HashMap;

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
        Commit.COMMITS_DIR.mkdir();
        Branch.BRANCHES_DIR.mkdir();
        Blob.BLOBS_DIR.mkdir();
        StagingArea stagingArea = new StagingArea();
        stagingArea.save();
        Commit initCommit = new Commit();
        initCommit.save();
        Branch.setCommitId("master", initCommit.getId());
        HEAD.setBranchName("master");
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

    private void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            String message = "File does not exist.";
            Main.exit(message);
        }
        Blob blob = new Blob(readContents(file));
        String blobId = blob.getId();
        Commit head = Commit.load(Branch.getCommitId(HEAD.getBranchName()));
        StagingArea stagingArea = StagingArea.load();
        if (blobId.equals(head.getBlobId(fileName))) {
            stagingArea.clear();
            stagingArea.save();
            return;
        }
        HashMap<String, String> addition = stagingArea.getAddition();
        if (blobId.equals(addition.get(fileName))) {
            addition.remove(fileName);
        }
        addition.put(fileName, blobId);
        HashSet<String> removal = stagingArea.getRemoval();
        removal.remove(fileName);
        blob.save();
        stagingArea.save();
    }

    private void commit(String message) {
        String commitId = Branch.getCommitId(HEAD.getBranchName());
        commit(message, commitId);
    }
    
    private void commit(String message, String commitId) {
        StagingArea stagingArea = StagingArea.load();
        if (stagingArea.isEmpty()) {
            String message = "No changes added to the commit."
            Main.exit(message);
        }
        if (message.isEmpty()) {
            String message = "Please enter a commit message."
            Main.exit(message);
        }
        Commit commit = new Commit(message, commitId);
        HashMap<String, String> blob = commit.getBlob();
        HashMap<String, String> addition = stagingArea.getAddition();
        for (Map.Entry<String, String> entry : addition.entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();
            blob.put(fileName, blobId);
        }
        HashSet<String> removal = stagingArea.getRemoval();
        for (String fileName : removal) {
            blob.remove(fileName);
        }
        Branch.setCommitId(HEAD.getBranchName(), commit.getId());
        stagingArea.clear();
        stagingArea.save();
        commit.save();
    }

    private void checkout() {}

    private void log() {}
}
