package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
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
        TreeMap<String, String> addition = stagingArea.getAddition();
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
        TreeSet<String> removal = stagingArea.getRemoval();
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
        TreeMap<String, String> blob = commit.getBlob();
        TreeMap<String, String> addition = stagingArea.getAddition();
        // Updates the files staged for addition.
        for (Map.Entry<String, String> entry : addition.entrySet()) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();
            blob.put(fileName, blobId);
        }
        // Updates the files staged for removal.
        TreeSet<String> removal = stagingArea.getRemoval();
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

    /** Unstages the file if it is currently staged for addition. If the file
     *  is tracked in the current commit, stages it for removal and removes the
     *  file from the working directory.
     */
    public static void rm(String fileName) {
        String headId = Branch.getId(HEAD.getBranch());
        Commit head = Commit.load(headId);
        StagingArea stagingArea = StagingArea.load();
        TreeMap<String, String> addition = stagingArea.getAddition();
        boolean tracked = head.getBlob().containsKey(fileName);
        boolean staged = addition.containsKey(fileName);
        if (!tracked && !staged) {
            String message = "No reason to remove the file.";
            Main.exit(message);    
        }
        if (staged) {
            // Unstages the file from addition.
            addition.remove(fileName);
        }
        if (tracked) {
            // Stages the file for removal and removes it.
            TreeSet<String> removal = stagingArea.getRemoval();
            removal.add(fileName);
            restrictedDelete(fileName);
        }
        stagingArea.save();
    }

    /** Displays information about each commit in reverse chronological order
     *  until the initial commit following the first parent.
     */
    public static void log() {
        String commitId = Branch.getId(HEAD.getBranch());
        while (commitId != null) {
            Commit commit = Commit.load(commitId);
            System.out.println(commit);
            commitId = commit.getFirstParentId();
        }
    }

    /** Displays information about all commits. */
    public static void globalLog() {
        List<String> commitIds = plainFilenamesIn(Commit.COMMITS_DIR);
        for (String commitId : commitIds) {
            Commit commit = Commit.load(commitId);
            System.out.println(commit);
        }
    }

    /** Prints the IDs of all commits that have the given commit message. */
    public static void find(String message) {
        boolean found = false;
        List<String> commitIds = plainFilenamesIn(Commit.COMMITS_DIR);
        for (String commitId : commitIds) {
            Commit commit = Commit.load(commitId);
            if (message.equals(commit.getMessage())) {
                found = true;
                System.out.println(commit.getId());
            }
        }
        if (!found) {
            String msg = "Found no commit with that message.";
            Main.exit(msg);
        }
    }

    /** Displays the current branch, other branches, staged files for addition
     *  or removal, and untracked files.
     */
    public static void status() {
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(Branch.BRANCHES_DIR);
        Collections.sort(branches);
        for (String branch : branches) {
            if (branch.equals(HEAD.getBranch())) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        StagingArea stagingArea = StagingArea.load();
        TreeMap<String, String> addition = stagingArea.getAddition();
        for (String fileName : addition.keySet()) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        TreeSet<String> removal = stagingArea.getRemoval();
        for (String fileName : removal) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println()
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    private static List<String> getUntrackedFiles(String commitId) {
        Commit commit = Commit.load(commitId);
        TreeMap<String, String> blob = commit.getBlob();
        StagingArea stagingArea = StagingArea.load();
        TreeMap<String, String> addition  = stagingArea.getAddition();
        List<String> cwdFiles = plainFilenamesIn(CWD);
        List<String> untrackedFiles = new ArrayList<>();
        for (String fileName : cwdFiles) {
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
        TreeMap<String, String> blob = commit.getBlob();
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
        /*
         * Deletes files tracked in the current branch that are not present in
         * the checked out branch.
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

    /** Creates a new branch with the given name, and points it at the current head commit. */
    public static void branch(String name) {
        List<String> branches = plainFilenamesIn(Branch.BRANCHES_DIR);
        if (branches.contains(name)) {
            String message = "A branch with that name already exists.";
            Main.exit(message);
        }
        Branch.setId(name, Branch.getId(HEAD.getBranch()));
    }

    /** Deletes the branch with the given name. */
    public static void rmBranch(String name) {
        List<String> branches = plainFilenamesIn(Branch.BRANCHES_DIR);
        String message;
        if (!branches.contains(name)) {
            message = "A branch with that name does not exist.";
            Main.exit(message);
        }
        if (name.equals(HEAD.getBranch())) {
            message = "Cannot remove the current branch.";
            Main.exit(message);
        }
        File branch = join(Branch.BRANCHES_DIR, name);
        restrictedDelete(branch);
    }

    public static void reset() {
        reset(Branch.getId(HEAD.getBranch()));
    }

    /** Checks out all the files tracked by the given commit. Removes tracked
     *  files that are not present in that commit. Moves the current branch's
     *  head to that commit node.
     */
    public static void reset(String commitId) {
        List<String> commitIds = plainFilenamesIn(Commit.COMMITS_DIR);
        if (!commitIds.contains(commitId)) {
            String message = "No commit with that id exists.";
            Main.exit(message);
        }
        checkoutCommit(commitId);
    }

    /** Merges files from the given branch into the current branch. */
    public static void merge(String branch) {
        throw new UnsupportedOperationException();
    }
}
