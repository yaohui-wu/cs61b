package gitlet;

import static gitlet.Utils.*;
import java.io.File;
import java.io.Serializable;

/** Blobs: the saved contents of files. */
public class Blob implements Serializable {
    public static final File BLOBS_DIR = join(Repository.GITLET_DIR, "blobs");

    private byte[] contents;
    private final String id;

    public Blob(byte[] blobContents) {
        contents = blobContents;
        id = hash();
    }

    private String hash() {
        return sha1(serialize(contents));
    }

    public String getId() {
        return id;
    }

    public void save() {
        File blobFile = join(BLOBS_DIR, id);
        writeContents(blobFile, (Object) contents);
    }
}
