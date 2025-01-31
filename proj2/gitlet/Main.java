package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yaohui Wu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        // No input arguments.
        if (args.length == 0) {
            String message = "Please enter a command.";
            // Prints the error message.
            System.out.println(message);
            // Exits the program immediately.
            System.exit(0);
        }
        String firstArg = args[0];
        if (firstArg == "init") {
            // Handles the "init" command.
            break;
        }
        // Current working directory
        String cwd = System.getProperty("usr.dir");
        File gitlet = Utils.join(cwd, ".gitlet");
        if (!gitlet.exists()) {
            String message = "Not in an initialized Gitlet directory.";
            System.out.println(message);
            System.exit(0);
        }
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "commit":
                break;
            case "checkout":
                break;
            case "log":
                break;
            default:
                // Input command does not exist.
                String message = "No command with that name exists."
                System.out.println(message);
                System.exit(0);
        }
    }
}
