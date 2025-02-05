package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yaohui Wu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        int argsNum = args.length; // Number of input arguments.
        String error; // Error message.
        if (argsNum == 0) {
            error = "Please enter a command.";
            exit(error);
        }
        switch(args[0]) {
            case "init":
                // Handles the `init` command.
                validateArgs(1, argsNum);
                Repository.init();
                break;
            case "add":
                // Handles the `add [filename]` command.
                validateArgs(2, argsNum);
                Repository.add(args[1]);
                break;
            case "commit":
                // Handles the `commit [message]` command.
                validateArgs(2, argsNum);
                Repository.commit(args[1]);
                break;
            case "rm":
                // Handles the `rm [file name]` command.
                validateArgs(2, argsNum);
                Repository.rm(args[1]);
                break;
            case "log":
                // Handles the `log` command.
                validateArgs(1, argsNum);
                Repository.log();
                break;
            case "global-log":
                // Handles the `global-log` command.
                validateArgs(1, argsNum);
                Repository.globalLog();
                break;
            case "find":
                // Handles the `find [commit message]` command.
                validateArgs(2, argsNum);
                Repository.find(args[1]);
                break;
            case "status":
                // Handles the `status` command.
                validateArgs(1, argsNum);
                Repository.status();
                break;
            case "checkout":
                Repository.checkout(args);
                break;
            case "branch":
                // Handles the `branch [branch name]` command.
                validateArgs(2, argsNum);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                // Handles the `rm-branch [branch name]` command.
                validateArgs(2, argsNum);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                // Handles the `reset [commit id]` command.
                validateArgs(2, argsNum);
                Repository.reset(args[1]);
                break;
            case "merge":
                // Handles the `merge [branch name]` command.
                validateArgs(2, argsNum);
                Repository.merge(args[1]);
                break;
            default:
                // Input command does not exist.
                error = "No command with that name exists.";
                exit(error);
        }
    }

    /** Prints an error message and exits the program. */
    public static void exit(String error) {
        // Prints the error message.
        System.out.println(error);
        // Exits the program with error code 0 immediately.
        System.exit(0);
    }

    /** Validates a command has the correct number of arguments. */
    private static void validateArgs(int num, int argsNum) {
        if (num != argsNum) {
            String error = "Incorrect operands.";
            exit(error);
        }
    }
}
