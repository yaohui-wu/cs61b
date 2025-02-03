package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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
    public static void init() {
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

    private static void validateRepo() {
        if (GITLET_DIR.exists()) {
            String message = "A Gitlet version-control system already exists "
                + "in the current directory.";
            Main.exit(message);
        }
    }

    public static void add(String fileName) {
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

    public static void commit(String message) {
        String commitId = Branch.getCommitId(HEAD.getBranchName());
        commit(message, commitId);
    }
    
    private static void commit(String message, String commitId) {
        StagingArea stagingArea = StagingArea.load();
        String msg;
        if (stagingArea.isEmpty()) {
            msg = "No changes added to the commit.";
            Main.exit(msg);
        }
        if (message.isEmpty()) {
            msg = "Please enter a commit message.";
            Main.exit(msg);
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

    public static void checkout(String[] args) {
        int argsNum = args.length;
        if (argsNum == 3 &&  args[1].equals("--")) {
            String headId = Branch.getCommitId(HEAD.getBranchName());
            checkoutFile(headId, args[2]);
            return;
        } else if (argsNum == 4 && args[2].equals("--")) {
            checkoutFile(args[1], args[3]);
            return;
        } else if (argsNum == 2) {
            checkoutBranch(args[1]);
            return;
        } else {
            String message = "Incorrect operands.";
            Main.exit(message);
        }
    }

    private static void checkoutFile(String commitId, String fileName) {
        Commit commit = Commit.load(commitId);
        String message;
        if (commit == null) {
            message = "No commit with that id exists.";
            Main.exit(message);
        }
        String blobId = commit.getBlobId(fileName);
        if (blobId == null) {
            message = "File does not exist in that commit.";
            Main.exit(message);
        }
        byte[] contents = readContents(join(Blob.BLOBS_DIR, blobId));
        writeContents(join(CWD, fileName), (Object) contents);
    }

    private static void checkoutBranch(String branchName) {
        File branch = join(Branch.BRANCHES_DIR, branchName);
        String message;
        if (!branch.exists()) {
            message = "No such branch exists.";
            Main.exit(message);
        }
        if (branchName.equals(HEAD.getBranchName())) {
            message = "No need to checkout the current branch.";
            Main.exit(message);
        }
        String commitId = Branch.getCommitId(branchName);
        checkoutCommit(commitId);
        HEAD.setBranchName(branchName);
    }

    private static void checkoutCommit(String commitId) {
        StagingArea stagingArea = StagingArea.load();
        Commit commit = Commit.load(commitId);
        List<String> untrackedFiles = getUntrackedFiles(commitId);
        HashMap<String, String> blob = commit.getBlob();
        Set<String> fileNames = blob.keySet();
        for (String fileName : fileNames) {
            if (untrackedFiles.contains(fileName)) {
                String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
                Main.exit(message);
            }
        }
        for (String fileName : fileNames) {
            String blobId = blob.get(fileName);
            byte[] contents = readContents(join(Blob.BLOBS_DIR, blobId));
            writeContents(join(CWD, fileName), (Object) contents);
        }
        Commit head = Commit.load(Branch.getCommitId(HEAD.getBranchName()));
        for (String fileName : head.getBlob().keySet()) {
            if (!fileNames.contains(fileName)) {
                join(CWD, fileName).delete();
            }
        }
        stagingArea.clear();
        stagingArea.save();
    }

    public static List<String> getUntrackedFiles(String commitId) {
        Commit commit = Commit.load(commitId);
        HashMap<String, String> blob = commit.getBlob();
        StagingArea stagingArea = StagingArea.load();
        HashMap<String, String> addition  = stagingArea.getAddition();
        List<String> CWDfileNames = plainFilenamesIn(CWD);
        List<String> untrackedFiles = new ArrayList<>();
        for (String fileName : CWDfileNames) {
            boolean tracked = blob.containsKey(fileName);
            boolean staged = addition.containsKey(fileName);
            if (!tracked && !staged) {
                untrackedFiles.add(fileName);
            }
        }
        Collections.sort(untrackedFiles);
        return untrackedFiles;
    }

    public static void log() {
        String commitId = Branch.getCommitId(HEAD.getBranchName());
        while (commitId != null) {
            Commit commit = Commit.load(commitId);
            System.out.println(commit);
            commitId = commit.getFirstParent();
        }
    }
}
