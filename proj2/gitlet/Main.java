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
        if (argsNum == 0) {
            String message = "Please enter a command.";
            exit(message);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                int operandsNum = 2;
                validateOperands(operandsNum, argsNum);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                int operandsNum = 2;
                validateOperands(operandsNum, argsNum);
                break;
            case "checkout":
                break;
            case "log":
                break;
            default:
                // Input command does not exist.
                String message = "No command with that name exists."
                exit(message);
        }
    }

    /** Prints an error message and exits the program. */
    private void exit(String message) {
        // Prints the error message.
        Utils.message(message);
        // Exits the program with error code 0 immediately.
        System.exit(0);
    }

    /** Validates a command has the correct number and format of operands. */
    private void validateOperands(int num, int argsNum) {
        if (num != argsNum) {
            String message = "Incorrect operands.";
            exit(message);
        }
    }
}
