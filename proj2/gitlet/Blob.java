package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;

/** Blobs: the saved contents of files. */
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
        File file = join(BLOBS, id);
        writeContents(file, (Object) contents);
    }
}
