package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a Gitlet commit object.
 * @author Yaohui Wu
 */
public class Commit implements Serializable {
    public static final File COMMITS = join(Repository.GITLET, "commits");
    // Message of the commit.
    private String message;
    // Timestamp of the commit.
    private String timestamp;
    // SHA-1 ID of the commit.
    private String id;
    // SHA-1 ID of the first parent reference.
    private String firstParent;
    // SHA-1 ID of the second parent reference for merges.
    private String secondParent;
    // Maps the file name to the blob ID.
    private Map<String, String> blobs;

    public Commit() {
        message = "initial commit";
        timestamp = timestamp(Instant.EPOCH);
        firstParent = null;
        secondParent = null;
        blobs = new TreeMap<>();
        id = hash();
    }

    public Commit(String msg, String firstParentId) {
        this();
        message = msg;
        timestamp = timestamp(Instant.now());
        firstParent = firstParentId;
        blobs = Commit.load(firstParentId).getBlobs();
        id = hash();
    }

    public Commit(String msg, String firstParentId, String secondParentId) {
        this(msg, firstParentId);
        secondParent = secondParentId;
        id = hash();
    }

    private String timestamp(Instant time) {
        DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z")
            .withZone(ZoneId.systemDefault());
        return formatter.format(time);
    }

    private String hash() {
        return sha1(serialize(this));
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getfirstParent() {
        return firstParent;
    }
    
    public Map<String, String> getBlobs() {
        return blobs;
    }

    public String getBlobId(String file) {
        return blobs.get(file);
    }

    public void save() {
        writeObject(join(COMMITS, id), this);
    }

    public static Commit load(String id) {
        if (id.length() < UID_LENGTH) {
            id = findCommit(id);
        }
        File file = join(COMMITS, id);
        if (!file.exists()) {
            return null;
        }
        return readObject(file, Commit.class);
    }

    private static String findCommit(String id) {
        for (String file : COMMITS.list()) {
            if (file.startsWith(id)) {
                return file;
            }
        }
        return null;
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
