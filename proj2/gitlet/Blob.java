package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;

/** Represents a blob in a Gitlet repository. */
public class Blob implements Serializable {
    public static final File BLOBS = join(Repository.GITLET, "blobs");

    private byte[] contents;
    private final String id;

    public Blob(byte[] fileContents) {
        contents = fileContents;
        id = hash();
    }

    private String hash() {
        return sha1(serialize(contents));
    }

    public String getId() {
        return id;
    }

    public void save() {
        writeContents(join(BLOBS, id), (Object) contents);
    }
}
