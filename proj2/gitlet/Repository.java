package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Gitlet repository.
 * @author Yaohui Wu
 */
public class Repository {
    // Current working directory.
    public static final File CWD = new File(System.getProperty("user.dir"));
    // The .gitlet directory.
    public static final File GITLET = join(CWD, ".gitlet");
    private static final String DEFAULT_BRANCH = "master";

    /**
     * Creates a new Gitlet version-control system in the current directory.
     */
    public static void init() {
        validateRepo();
        GITLET.mkdir();
        Commit.COMMITS.mkdir();
        Branch.BRANCHES.mkdir();
        Blob.BLOBS.mkdir();
        StagingArea stage = new StagingArea();
        stage.save();
        Commit commit = new Commit();
        commit.save();
        Branch.setId(DEFAULT_BRANCH, commit.getId());
        HEAD.setBranch(DEFAULT_BRANCH);
    }

    private static void validateRepo() {
        if (GITLET.exists()) {
            String error = "A Gitlet version-control system already exists "
                + "in the current directory.";
            Main.exit(error);
        }
    }

    /** Returns the name of the current branch. */
    private static String getBranch() {
        return HEAD.getBranch();
    }

    /** Returns the ID of the current commit in the current branch. */
    private static String getId() {
        return Branch.getId(getBranch());
    }

    /** Returns the current commit in the current branch. */
    private static Commit getCommit() {
        return getCommit(getId());
    }

    /** Returns the commit with the given ID. */
    private static Commit getCommit(String id) {
        return Commit.load(id);
    }

    /** Returns a list of all commit IDs. */
    private static List<String> getIds() {
        return Arrays.asList(Commit.COMMITS.list());
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

    /**
     * Returns a list of all the files present in the working directory but
     * neither staged for addition nor tracked.
     */
    private static List<String> getUntrackedFiles() {
        List<String> untrackedFiles = new ArrayList<>();
        for (String file : plainFilenamesIn(CWD)) {
            if (!isTracked(file) && !isStaged(file)) {
                untrackedFiles.add(file);
            }
        }
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
        StagingArea stage = StagingArea.load();
        Map<String, String> addition = stage.getAddition();
        /*
         * If the current working version of the file is identical to the
         * version in the current commit, do not stage it to be added, and
         * remove it from the staging area if it is already there.
         */
        if (blobId.equals(getCommit().getBlobId(fileName))) {
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

    /** 
     * Creates a new commit with the given log message. Updates and saves
     * files staged for addition and removal.
     */
    public static void commit(String message) {
        commit(message, getId(), null);
    }
    
    private static void commit(
        String message,
        String firstParent,
        String secondParent) {
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
        Commit commit;
        if (secondParent == null) {
            commit = new Commit(message, firstParent);
        } else {
            commit = new Commit(message, firstParent, secondParent);
        }
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
        Branch.setId(getBranch(), commit.getId());
        // The staging area is cleared after a commit.
        stage.clear();
        stage.save();
        commit.save();
    }

    /** 
     * Unstages the file if it is currently staged for addition. If the file
     * is tracked in the current commit, stages it for removal and removes the
     * file from the working directory.
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

    /**
     * Displays information about each commit in reverse chronological order
     * until the initial commit following the first parent.
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

    /**
     * Displays the current branch, other branches, staged files for addition
     * or removal, and untracked files.
     */
    public static void status() {
        printBranches();
        printStage();
        printModifiedFiles();
        printUntrackedFiles();
    }

    /** Displays the branches of the repository. */
    private static void printBranches() {
        System.out.println("=== Branches ===");
        for (String branch : getBranches()) {
            if (branch.equals(getBranch())) {
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

    /** Displays the modified files not staged for commit. */
    private static void printModifiedFiles() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        StagingArea stage = StagingArea.load();
        Map<String, String> blobs = getCommit().getBlobs();
        for (String file : blobs.keySet()) {
            File currentFile = join(CWD, file);
            boolean modified = false;
            boolean deleted = false;
            boolean tracked = isTracked(file);
            boolean staged = isStaged(file);
            if (currentFile.exists()) {
                Blob blob = new Blob(readContents(currentFile));
                String blobId = blob.getId();
                /*
                 * Tracked in the current commit, changed in the working
                 * directory, but not staged.
                 */
                if (tracked) {
                    modified = !blobId.equals(blobs.get(file));
                }
                /*
                 * Staged for addition, but with different contents than in
                 * the working directory.
                 */
                if (staged) {
                    Map<String, String> addition = stage.getAddition();
                    File stagedFile = join(Blob.BLOBS, addition.get(file));
                    Blob stagedBlob = new Blob(readContents(stagedFile));
                    modified = !blobId.equals(stagedBlob.getId());
                }
            } else {
                /*
                 * Not staged for removal, but tracked in the current commit
                 * and deleted from the working directory.
                 */
                deleted = tracked && !stage.getRemoval().contains(file);
                // Staged for addition, but deleted in the working directory.
                if (staged) {
                    deleted = true;
                }
            }
            if (modified) {
                System.out.println(file + " (modified)");
            } else if (deleted) {
                System.out.println(file + " (deleted)");
            }
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

    /** 
     * Takes the version of the file as it exists in the commit with the given
     * ID, and puts it in the working directory, overwriting the version of
     * the file that's already there if there is one. The new version of the
     * file is not staged.
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

    /** 
     * Takes all files in the commit at the head of the given branch, and puts
     * them in the working directory, overwriting the versions of the files
     * that are already there if they exist.
     */
    private static void checkoutBranch(String branch) {
        File branchFile = join(Branch.BRANCHES, branch);
        String error;
        if (!branchFile.exists()) {
            error = "No such branch exists.";
            Main.exit(error);
        }
        if (branch.equals(getBranch())) {
            error = "No need to checkout the current branch.";
            Main.exit(error);
        }
        checkoutCommit(Branch.getId(branch));
        // The given branch is now the current branch.
        HEAD.setBranch(branch);
    }

    private static void checkoutCommit(String id) {
        StagingArea stage = StagingArea.load();
        Map<String, String> blobs = getCommit(id).getBlobs();
        Set<String> fileNames = blobs.keySet();
        for (String fileName : fileNames) {
            File file = join(CWD, fileName);
            boolean staged = isStaged(fileName);
            boolean tracked = isTracked(fileName);
            if (file.exists() && !staged && !tracked) {
                /*
                 * A working file is untracked in the current branch and would
                 * be overwritten.
                 */
                String error = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
                Main.exit(error);
            }
        }
        // Checks out all the files in the given commit.
        for (String fileName : fileNames) {
            File file = join(CWD, fileName);
            String blobId = blobs.get(fileName);
            byte[] contents = readContents(join(Blob.BLOBS, blobId));
            writeContents(file, (Object) contents);
        }
        /*
         * Deletes files tracked in the current branch that are not present in
         * the checked out branch.
         */
        for (String fileName : getCommit().getBlobs().keySet()) {
            if (!fileNames.contains(fileName)) {
                restrictedDelete(join(CWD, fileName));
            }
        }
        stage.clear();
        stage.save();
    }

    /**
     * Creates a new branch with the given name, and points it at the current
     * head commit.
     */
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
        validateBranch(branch);
        if (branch.equals(getBranch())) {
            error = "Cannot remove the current branch.";
            Main.exit(error);
        }
        branchFile.delete();
    }

    private static void validateBranch(String branch) {
        if (!join(Branch.BRANCHES, branch).exists()) {
            String error = "A branch with that name does not exist.";
            Main.exit(error);
        }
    }

    public static void reset() {
        reset(getId());
    }

    /**
     * Checks out all the files tracked by the given commit. Removes tracked
     * files that are not present in that commit. Moves the current branch's
     * head to that commit node.
     */
    public static void reset(String id) {
        if (!getIds().contains(id)) {
            String error = "No commit with that id exists.";
            Main.exit(error);
        }
        checkoutCommit(id);
        Branch.setId(getBranch(), id);
    }

    /** Merges files from the given branch into the current branch. */
    public static void merge(String branch) {
        validateMerge(branch);
        // Latest common ancestor of the current and given branches.
        String givenId = Branch.getId(branch);
        String splitId = findSplit(branch);
        if (splitId.equals(givenId)) {
            // The split point is the same commit as the given branch.
            String msg = "Given branch is an ancestor of the current branch.";
            System.out.println(msg);
            return;
        }
        String currentId = getId();
        if (splitId.equals(currentId)) {
            // The split point is the current branch.
            // Checks out the given branch.
            checkoutBranch(branch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        boolean conflict = mergeBranch(currentId, givenId, splitId);
        String message = "Merged " + branch + " into " + getBranch() + ".";
        commit(message, currentId, givenId);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void validateMerge(String branch) {
        StagingArea stage = StagingArea.load();
        if (!stage.isEmpty()) {
            // There are staged additions or removals present.
            String error = "You have uncommitted changes.";
            Main.exit(error);
        }
        validateBranch(branch);
        if (branch.equals(getBranch())) {
            String error = "Cannot merge a branch with itself.";
            Main.exit(error);
        }
        Commit given = getCommit(Branch.getId(branch));
        for (String file : getUntrackedFiles()) {
            /*
             * An untracked file in the current commit would be overwritten or
             * deleted by the merge.
             */
            if (given.getBlobs().containsKey(file)) {
                String error = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
                Main.exit(error);
            }
        }
    }

    private static String findSplit(String branch) {
        Commit current = getCommit();
        Commit given = getCommit(Branch.getId(branch));
        List<String> currentAncestors = getAncestors(current);
        List<String> givenAncestors = getAncestors(given);
        for (String id : currentAncestors) {
            if (givenAncestors.contains(id)) {
                return id;
            }
        }
        return null;
    }

    private static List<String> getAncestors(Commit commit) {
        List<String> ancestors = new ArrayList<>();
        String id = commit.getId();
        while (id != null) {
            ancestors.add(id);
            id = getCommit(id).getfirstParent();
        }
        return ancestors;
    }

    /**
     * Merges files from the given branch into the current branch. Returns
     * true if a merge conflict occurs.
     */
    private static boolean mergeBranch(
        String current,
        String given,
        String split) {
        boolean hasConflict = false;
        Map<String, String> currentBlobs = getCommit(current).getBlobs();
        Map<String, String> givenBlobs = getCommit(given).getBlobs();
        Map<String, String> splitBlobs = getCommit(split).getBlobs();
        for (String file : givenBlobs.keySet()) {
            boolean conflict = false;
            String currentBlobId = currentBlobs.get(file);
            String givenBlobId = givenBlobs.get(file);
            String splitBlobId = splitBlobs.get(file);
            boolean modifiedCurrent = false;
            boolean modifiedGiven = false;
            if (splitBlobId != null && currentBlobId != null) {
                modifiedCurrent = !splitBlobId.equals(currentBlobId);
                modifiedGiven = !splitBlobId.equals(givenBlobId);
            }
            if (!modifiedCurrent && modifiedGiven
                || currentBlobId == null && splitBlobId == null) {
                /*
                 * File was modified in the given branch but not the current
                 * branch.
                 */
                // File is present only in the given branch.
                checkoutFile(given, file);
                add(file);
            } else if (splitBlobId != null && currentBlobId == null) {
                conflict = !splitBlobId.equals(givenBlobId);
            }
            if (conflict) {
                writeContents(join(CWD, file), conflictContent(
                    file, currentBlobs, givenBlobs));
                add(file);
                hasConflict = true;
            }
        }
        for (String file : currentBlobs.keySet()) {
            boolean conflict = false;
            String currentBlobId = currentBlobs.get(file);
            String givenBlobId = givenBlobs.get(file);
            String splitBlobId = splitBlobs.get(file);
            if (splitBlobId != null
                && currentBlobId.equals(splitBlobId)
                && givenBlobId == null) {
                /*
                 * File was present at the split point, is unmodified in the
                 * current branch, and is absent in the given branch.
                 */
                rm(file);
            } else if (givenBlobId != null) {
                /*
                 * Contents of both are changed and different from other.
                 */
                conflict = !currentBlobId.equals(givenBlobId);
            } else if (splitBlobId != null && givenBlobId == null) {
                /*
                 * Contents of one are changed and the other file is deleted.
                 */
                conflict = !splitBlobId.equals(currentBlobId);
            }
            if (conflict) {
                writeContents(join(CWD, file), conflictContent(
                    file, currentBlobs, givenBlobs));
                add(file);
                hasConflict = true;
            }
        }
        return hasConflict;
    }

    private static String conflictContent(
        String file,
        Map<String, String> currentBlobs,
        Map<String, String> givenBlobs) {
        String content = "<<<<<<< HEAD\n";
        if (currentBlobs.containsKey(file)) {
            content += readContentsAsString(
                join(Blob.BLOBS, currentBlobs.get(file))
            );
        }
        content += "=======\n";
        if (givenBlobs.containsKey(file)) {
            content += readContentsAsString(
                join(Blob.BLOBS, givenBlobs.get(file))
            );
        }
        content += ">>>>>>>\n";
        return content;
    }
}
