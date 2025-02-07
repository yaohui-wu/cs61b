package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/** Represents a Gitlet repository.
 *  @author Yaohui Wu
 */
public class Repository {
    // Current working directory.
    public static final File CWD = new File(System.getProperty("user.dir"));
    // The .gitlet directory.
    public static final File GITLET = join(CWD, ".gitlet");
    private static final String DEFAULT_BRANCH = "master";

    /** Creates a new Gitlet version-control system in the current directory. */
    public static void init() {
        validateRepo();
        GITLET.mkdir();
        Commit.COMMITS.mkdir();
        Branch.BRANCHES.mkdir();
        Blob.BLOBS.mkdir();
        StagingArea stage = new StagingArea();
        stage.save();
        Commit initCommit = new Commit();
        initCommit.save();
        Branch.setId(DEFAULT_BRANCH, initCommit.getId());
        HEAD.setBranch(DEFAULT_BRANCH);
    }

    private static void validateRepo() {
        if (GITLET.exists()) {
            String error = "A Gitlet version-control system already exists "
                + "in the current directory.";
            Main.exit(error);
        }
    }

    /** Returns the ID of the current commit in the current branch. */
    private static String getId() {
        return Branch.getId(HEAD.getBranch());
    }

    /** Returns the current commit in the current branch. */
    private static Commit getCommit() {
        return getCommit(getId());
    }

    /** Returns the commit with the given ID. */
    private static Commit getCommit(String id) {
        return Commit.load(id);
    }

    /** Returns the list of all commit IDs. */
    private static List<String> getIds() {
        return plainFilenamesIn(Commit.COMMITS);
    }

    /** Returns a list of all the branches of the repository. */
    private static List<String> getBranches() {
        return plainFilenamesIn(Branch.BRANCHES);
    }

    /** Returns true if the file is tracked. */
    private static boolean isTracked(String file) {
        return getCommit().getBlobs().containsKey(file);
    }

    /** Returns true if the file is staged for addition. */
    private static boolean isStaged(String file) {
        return StagingArea.load().getAddition().containsKey(file);
    }

    /** Returns a list of all the untracked files.  */
    private static List<String> getUntrackedFiles() {
        Commit commit = getCommit();
        StagingArea stage = StagingArea.load();
        List<String> untrackedFiles = new ArrayList<>();
        for (String file : plainFilenamesIn(CWD)) {
            if (!isTracked(file) && !isStaged(file)) {
                untrackedFiles.add(file);
            }
        }
        Collections.sort(untrackedFiles);
        return untrackedFiles;
    }

    /** Adds a copy of the file to the staging area. */
    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            String error = "File does not exist.";
            Main.exit(error);
        }
        Blob blob = new Blob(readContents(file));
        String blobId = blob.getId();
        Commit current = getCommit();
        StagingArea stage = StagingArea.load();
        Map<String, String> addition = stage.getAddition();
        /*
         * If the current working version of the file is identical to the
         * version in the current commit, do not stage it to be added, and
         * remove it from the staging area if it is already there.
         */
        if (blobId.equals(current.getBlobId(fileName))) {
            addition.remove(fileName);
        } else {
            /*
             * Adds the file to the staging area for addition and overwrites
             * it if it is already staged.
             */
            addition.put(fileName, blobId);
        }
        // File is no longer staged for removal if it was.
        stage.getRemoval().remove(fileName);
        blob.save();
        stage.save();
    }

    /** Creates a new commit with the given log message. Updates and saves
     *  files staged for addition and removal.
     */
    public static void commit(String message) {
        commit(message, getId());
    }
    
    private static void commit(String message, String firstParent) {
        StagingArea stage = StagingArea.load();
        String error;
        if (stage.isEmpty()) {
            error = "No changes added to the commit.";
            Main.exit(error);
        }
        if (message.isEmpty()) {
            error = "Please enter a commit message.";
            Main.exit(error);
        }
        Commit commit = new Commit(message, firstParent);
        Map<String, String> blobs = commit.getBlobs();
        Map<String, String> addition = stage.getAddition();
        // Updates the files staged for addition.
        for (Map.Entry<String, String> entry : addition.entrySet()) {
            blobs.put(entry.getKey(), entry.getValue());
        }
        // Updates the files staged for removal.
        for (String file : stage.getRemoval()) {
            blobs.remove(file);
        }
        // The new commit becomes the current commit.
        Branch.setId(HEAD.getBranch(), commit.getId());
        // The staging area is cleared after a commit.
        stage.clear();
        stage.save();
        commit.save();
    }

    /** Unstages the file if it is currently staged for addition. If the file
     *  is tracked in the current commit, stages it for removal and removes the
     *  file from the working directory.
     */
    public static void rm(String file) {
        Commit current = getCommit();
        StagingArea stage = StagingArea.load();
        Map<String, String> addition = stage.getAddition();
        boolean tracked = isTracked(file);
        boolean staged = isStaged(file);
        if (!tracked && !staged) {
            String error = "No reason to remove the file.";
            Main.exit(error);    
        }
        if (staged) {
            // Unstages the file from addition.
            addition.remove(file);
        }
        if (tracked) {
            // Stages the file for removal and removes it.
            stage.getRemoval().add(file);
            restrictedDelete(file);
        }
        stage.save();
    }

    /** Displays information about each commit in reverse chronological order
     *  until the initial commit following the first parent.
     */
    public static void log() {
        String id = getId();
        while (id != null) {
            Commit commit = getCommit(id);
            System.out.println(commit);
            id = commit.getfirstParent();
        }
    }

    /** Displays information about all commits. */
    public static void globalLog() {
        for (String id : getIds()) {
            Commit commit = getCommit(id);
            System.out.println(commit);
        }
    }

    /** Prints the IDs of all commits that have the given commit message. */
    public static void find(String message) {
        boolean found = false;
        for (String id : getIds()) {
            Commit commit = getCommit(id);
            if (message.equals(commit.getMessage())) {
                found = true;
                System.out.println(commit.getId());
            }
        }
        if (!found) {
            String error = "Found no commit with that message.";
            Main.exit(error);
        }
    }

    /** Displays the current branch, other branches, staged files for addition
     *  or removal, and untracked files.
     */
    public static void status() {
        printBranches();
        printStage();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        printUntrackedFiles();
    }

    /** Displays the branches of the repository. */
    private static void printBranches() {
        System.out.println("=== Branches ===");
        for (String branch : getBranches()) {
            if (branch.equals(HEAD.getBranch())) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
    }

    /** Displays the staged files for addition and removal. */
    private static void printStage() {
        System.out.println("=== Staged Files ===");
        StagingArea stage = StagingArea.load();
        for (String file : stage.getAddition().keySet()) {
            System.out.println(file);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String file : stage.getRemoval()) {
            System.out.println(file);
        }
        System.out.println();
    }

    /** Displays the untracked files. */
    private static void printUntrackedFiles() {
        System.out.println("=== Untracked Files ===");
        for (String file : getUntrackedFiles()) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void checkout(String[] args) {
        int argsNum = args.length;
        if (argsNum == 3 &&  args[1].equals("--")) {
            // Handles the `checkout -- [file name]` command.
            checkoutFile(getId(), args[2]);
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
            String error = "Incorrect operands.";
            Main.exit(error);
        }
    }

    /** Takes the version of the file as it exists in the commit with the
     *  given ID, and puts it in the working directory, overwriting the
     *  version of the file that's already there if there is one. The new
     *  version of the file is not staged.
     */
    private static void checkoutFile(String id, String file) {
        Commit commit = getCommit(id);
        String error;
        if (commit == null) {
            error = "No commit with that id exists.";
            Main.exit(error);
        }
        String blobId = commit.getBlobId(file);
        if (blobId == null) {
            error = "File does not exist in that commit.";
            Main.exit(error);
        }
        byte[] contents = readContents(join(Blob.BLOBS, blobId));
        writeContents(join(CWD, file), (Object) contents);
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
        File branchFile = join(Branch.BRANCHES, branch);
        String error;
        if (!branchFile.exists()) {
            error = "No such branch exists.";
            Main.exit(error);
        }
        if (branch.equals(HEAD.getBranch())) {
            error = "No need to checkout the current branch.";
            Main.exit(error);
        }
        checkoutCommit(Branch.getId(branch));
        HEAD.setBranch(branch);
    }

    private static void checkoutCommit(String id) {
        StagingArea stage = StagingArea.load();
        Map<String, String> blobs = getCommit(id).getBlobs();
        Set<String> files = blobs.keySet();
        for (String file : getUntrackedFiles()) {
            if (files.contains(file)) {
                /*
                * A working file is untracked in the current branch and would be
                * overwritten.
                */
                String error = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
                Main.exit(error);
            }
        }
        // Checks out all the files in the given commit.
        for (String file : files) {
            byte[] contents = readContents(join(Blob.BLOBS, blobs.get(file)));
            writeContents(join(CWD, file), (Object) contents);
        }
        /*
         * Deletes files tracked in the current branch that are not present in
         * the checked out branch.
         */
        for (String file : getCommit().getBlobs().keySet()) {
            if (!files.contains(file)) {
                restrictedDelete(join(CWD, file));
            }
        }
        stage.clear();
        stage.save();
    }

    /** Creates a new branch with the given name, and points it at the current head commit. */
    public static void branch(String branch) {
        if (join(Branch.BRANCHES, branch).exists()) {
            String error = "A branch with that name already exists.";
            Main.exit(error);
        }
        Branch.setId(branch, getId());
    }

    /** Deletes the branch with the given name. */
    public static void rmBranch(String branch) {
        File branchFile = join(Branch.BRANCHES, branch);
        String error;
        if (!branchFile.exists()) {
            error = "A branch with that name does not exist.";
            Main.exit(error);
        }
        if (branch.equals(HEAD.getBranch())) {
            error = "Cannot remove the current branch.";
            Main.exit(error);
        }
        branchFile.delete();
    }

    public static void reset() {
        reset(getId());
    }

    /** Checks out all the files tracked by the given commit. Removes tracked
     *  files that are not present in that commit. Moves the current branch's
     *  head to that commit node.
     */
    public static void reset(String id) {
        if (!getIds().contains(id)) {
            String error = "No commit with that id exists.";
            Main.exit(error);
        }
        checkoutCommit(id);
        Branch.setId(HEAD.getBranch(), id);
    }

    /** Merges files from the given branch into the current branch. */
    public static void merge(String branch) {
        throw new UnsupportedOperationException();
    }
}
