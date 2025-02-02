package gitlet;

import java.io.File;
import static gitlet.Utils.*;
import java.io.Serializable;

public class Blob implements Serializable {
    public static final File BLOBS_DIR = join(Repository.GITLET_DIR, "blobs");

    private byte[] contents;
    private String id;

    public Blob(String blobContents) {
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
