package gitlet;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.time.Instant;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yaohui Wu
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    // The date of the commit.
    private Date date;

    /* TODO: fill in the rest of this class. */
    private Commit() {
        message = "initial commit";
        // Unix epoch, 00:00:00 UTC, Thursday, 1 January 1970.
        date = new Date(0);
    }

    private Commit(String msg) {
        message = msg;
        date = new Date();
    }
}
