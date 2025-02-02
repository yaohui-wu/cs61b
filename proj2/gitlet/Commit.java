package gitlet;

// TODO: any imports you need here

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Serializable;
import java.io.File;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yaohui Wu
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The message of this Commit. */
    private String message;
    // The date of the commit.
    private String timestamp;
    private String hash;
    private String firstParent;
    private String secondParent;
    private HashMap<String, String> fileToBlob;

    /* TODO: fill in the rest of this class. */
    private Commit() {
        message = "initial commit";
        timestamp = timestamp(Instant.EPOCH);
        hash = hash();
        firstParent = null;
        secondParent = null;
        fileToBlob = new HashMap<>();
    }

    private Commit(String msg, String firstParentId, String secondParentId) {
        message = msg;
        timestamp = timestamp(Instant.now());
        hash = hash();
        firstParent = firstParentId;
        secondParent = secondParentId;
        fileToBlob = new HashMap<>();
    }

    private String timestamp(Instant time) {
        DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z")
            .withZone(ZoneId.systemDefault());
        String timestamp = formatter.format(time);
        return timestamp;
    }

    private int hash() {
        return Utils.sha1(Utils.serialize(this));
    }

    private void save() {
        File file = join(COMMITS_DIR, hash);
        writeObject(file, this);
    }

    @Override
    private String toString() {
        return "===\n" + "commit " + hash + "\n" + "Date: " + timestamp + "\n"
            + message + "\n";
    }
}
