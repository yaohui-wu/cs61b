package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/** Represents a Gitlet repository.
 *
 *  @author Yaohui Wu
 */
public class Repository {
    // Current working directory.
    public static final File CWD = new File(System.getProperty("user.dir"));
    // The .gitlet directory.
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    private static final String DEFAULT_BRANCH = "master";

    /** Creates a new Gitlet version-control system in the current directory. */
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
        Branch.setId(DEFAULT_BRANCH, initCommit.getId());
        HEAD.setBranch(DEFAULT_BRANCH);
    }

    private static void validateRepo() {
        if (GITLET_DIR.exists()) {
            String message = "A Gitlet version-control system already exists "
                + "in the current directory.";
            Main.exit(message);
        }
    }

    /** Adds a copy of the file to the staging area. */
    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            String message = "File does not exist.";
            Main.exit(message);
        }
        Blob blob = new Blob(readContents(file));
        String blobId = blob.getId();
        Commit head = Commit.load(Branch.getId(HEAD.getBranch()));
        StagingArea stagingArea = StagingArea.load();
        HashMap<String, String> addition = stagingArea.getAddition();
        /*
         * If the current working version of the file is identical to the
         * version in the current commit, do not stage it to be added, and
         * remove it from the staging area if it is already there.
         */
        if (blobId.equals(head.getBlobId(fileName))) {
            addition.remove(fileName);
        } else {
            /*
             * Adds the file to the staging area for addition and overwrites
             * it if it is already staged.
             */
            addition.put(fileName, blobId);
        }
        // File is no longer staged for removal if it was.
        HashSet<String> removal = stagingArea.getRemoval();
        removal.remove(fileName);
        blob.save();
        stagingArea.save();
    }

    /** Creates a new commit with the given log message. Updates and saves
     *  files staged for addition and removal.
     */
    public static void commit(String message) {
        String headId = Branch.getId(HEAD.getBranch());
        commit(message, headId);
    }
    
    private static void commit(String message, String firstParentId) {
        StagingArea stagingArea = StagingArea.load();
        String msg; // Error message.
        if (stagingArea.isEmpty()) {
            msg = "No changes added to the commit.";
            Main.exit(msg);
        }
        if (message.isEmpty()) {
            msg = "Please enter a commit message.";
            Main.exit(msg);
        }
        Commit commit = new Commit(message, firstParentId);
        HashMap<String, String> blob = commit.getBlob();
        HashMap<String, String> addition = stagingArea.getAddition();
        // Updates the files staged for addition.
        for (Map.Entry<String, String> entry : addition.entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();
            blob.put(fileName, blobId);
        }
        // Updates the files staged for removal.
        HashSet<String> removal = stagingArea.getRemoval();
        for (String fileName : removal) {
            blob.remove(fileName);
        }
        // Points HEAD to the new commit.
        Branch.setId(HEAD.getBranch(), commit.getId());
        // The staging area is cleared after a commit.
        stagingArea.clear();
        stagingArea.save();
        commit.save();
    }

    /** Displays information about each commit backwards along the commit tree
     *  until the initial commit, following the first parent commit links.
     */
    public static void log() {
        String commitId = Branch.getId(HEAD.getBranch());
        while (commitId != null) {
            Commit commit = Commit.load(commitId);
            System.out.println(commit);
            commitId = commit.getFirstParentId();
        }
    }

    private static List<String> getUntrackedFiles(String commitId) {
        Commit commit = Commit.load(commitId);
        HashMap<String, String> blob = commit.getBlob();
        StagingArea stagingArea = StagingArea.load();
        HashMap<String, String> addition  = stagingArea.getAddition();
        List<String> CWDfiles = plainFilenamesIn(CWD);
        List<String> untrackedFiles = new ArrayList<>();
        for (String fileName : CWDfiles) {
            boolean tracked = blob.containsKey(fileName);
            boolean staged = addition.containsKey(fileName);
            if (!tracked && !staged) {
                untrackedFiles.add(fileName);
            }
        }
        Collections.sort(untrackedFiles);
        return untrackedFiles;
    }

    public static void checkout(String[] args) {
        int argsNum = args.length;
        if (argsNum == 3 &&  args[1].equals("--")) {
            // Handles the `checkout -- [file name]` command.
            String headId = Branch.getId(HEAD.getBranch());
            checkoutFile(headId, args[2]);
            return;
        } else if (argsNum == 4 && args[2].equals("--")) {
            // Handles the `checkout [commit id] -- [file name]` command.
            checkoutFile(args[1], args[3]);
            return;
        } else if (argsNum == 2) {
            // Handles the `checkout [branch name]` command.
            checkoutBranch(args[1]);
            return;
        } else {
            String message = "Incorrect operands.";
            Main.exit(message);
        }
    }

    /** Takes the version of the file as it exists in the commit with the
     *  given ID, and puts it in the working directory, overwriting the
     *  version of the file that's already there if there is one. The new
     *  version of the file is not staged.
     */
    private static void checkoutFile(String commitId, String fileName) {
        Commit commit = Commit.load(commitId);
        String message; // Error message.
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

    /** Takes all files in the commit at the head of the given branch, and
     *  puts them in the working directory, overwriting the versions of the
     *  files that are already there if they exist. Also, at the end of this
     *  command, the given branch will now be considered the current branch
     *  (HEAD). Any files that are tracked in the current branch but are not
     *  present in the checked out branch are deleted. The staging area is
     *  cleared, unless the checked out branch is the current branch.
     */
    private static void checkoutBranch(String branch) {
        File branchFile = join(Branch.BRANCHES_DIR, branch);
        String message;
        if (!branchFile.exists()) {
            message = "No such branch exists.";
            Main.exit(message);
        }
        if (branch.equals(HEAD.getBranch())) {
            message = "No need to checkout the current branch.";
            Main.exit(message);
        }
        String commitId = Branch.getId(branch);
        checkoutCommit(commitId);
        HEAD.setBranch(branch);
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
        // Checks out all the files in the given commit.
        for (String fileName : fileNames) {
            String blobId = blob.get(fileName);
            byte[] contents = readContents(join(Blob.BLOBS_DIR, blobId));
            writeContents(join(CWD, fileName), (Object) contents);
        }
        /** Deletes files tracked in the current branch that are not present
         *  in the checked out branch.
         */
        Commit head = Commit.load(Branch.getId(HEAD.getBranch()));
        for (String fileName : head.getBlob().keySet()) {
            if (!fileNames.contains(fileName)) {
                join(CWD, fileName).delete();
            }
        }
        stagingArea.clear();
        stagingArea.save();
    }
}
