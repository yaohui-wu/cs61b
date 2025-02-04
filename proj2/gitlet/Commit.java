package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 *
 *  @author Yaohui Wu
 */
public class Commit implements Serializable {
    public static final File COMMITS_DIR = join(Repository.GITLET_DIR, "commits");
    // Message of the commit.
    private String message;
    // Timestamp of the commit.
    private String timestamp;
    // SHA-1 ID of the commit.
    private String id;
    // SHA-1 ID of the first parent commit.
    private String firstParentId;
    // SHA-1 ID of the second parent commit.
    private String secondParentId;
    // Maps the file name to the blob ID.
    private HashMap<String, String> blob;

    public Commit() {
        message = "initial commit";
        timestamp = timestamp(Instant.EPOCH);
        id = hash();
        firstParentId = null;
        blob = new HashMap<>();
    }

    public Commit(String msg, String firstParent) {
        message = msg;
        timestamp = timestamp(Instant.now());
        id = hash();
        firstParentId = firstParent;
        blob = new HashMap<>();
    }

    private String timestamp(Instant time) {
        DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z")
            .withZone(ZoneId.systemDefault());
        String timestamp = formatter.format(time);
        return timestamp;
    }

    private String hash() {
        return Utils.sha1(Utils.serialize(this));
    }

    public String getId() {
        return id;
    }

    public String getFirstParentId() {
        return firstParentId;
    }
    
    public HashMap<String, String> getBlob() {
        return blob;
    }

    public String getBlobId(String fileName) {
        return blob.get(fileName);
    }

    public void save() {
        File file = join(COMMITS_DIR, id);
        writeObject(file, this);
    }

    public static Commit load(String id) {
        File file = join(COMMITS_DIR, id);
        if (!file.exists()) {
            return null;
        }
        return readObject(file, Commit.class);
    }

    @Override
    public String toString() {
        String headerString = "===\n";
        String idString = "commit " + id + "\n";
        String timestampString = "Date: " + timestamp + "\n";
        String messageString = message + "\n";
        return headerString + idString + timestampString + messageString;
    }
}
