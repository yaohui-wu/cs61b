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
        int argsNum = args.length; // Number of input arguments.
        String message;
        if (argsNum == 0) {
            message = "Please enter a command.";
            exit(message);
        }
        String firstArg = args[0];
        int num; // Number of required arguments for a command.
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                num = 2;
                validateArgs(num, argsNum);
                String fileName = args[1];
                Repository.add(fileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                num = 2;
                validateArgs(num, argsNum);
                message = args[1];
                Repository.commit(message);
                break;
            case "checkout":
                Repository.checkout(args);
                break;
            case "log":
                Repository.log();
                break;
            default:
                // Input command does not exist.
                message = "No command with that name exists.";
                exit(message);
        }
    }

    /** Prints an error message and exits the program. */
    public static void exit(String message) {
        // Prints the error message.
        System.out.println(message);
        // Exits the program with error code 0 immediately.
        System.exit(0);
    }

    /** Validates a command has the correct number and format of operands. */
    private static void validateArgs(int num, int argsNum) {
        if (num != argsNum) {
            String message = "Incorrect operands.";
            exit(message);
        }
    }
}
