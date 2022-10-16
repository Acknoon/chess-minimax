package finalVar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * The main run file for the client to play the game as, this class handles and
 * manages the connection it has with the server.
 *
 * @author : Ariel Mazar (mazarariel@gmail.com)
 */
public class ClientChess {
    // constatns

    // for client connection with server
    private static int serverPort;//Server port that is automated and trying to connect to the server with it
    private static String serverIP;//Server IP trying to make a link towards it
    private static ClientData clientData;//Stored information that contain to transfer messages during the entire connection.

    private static View guiView;//The UI of the client
    private static String playerUserName, sign;//a starter sign that plays as and the username.

    // main
    public static void main(String[] args) {
        Utils.loadAssets();
        guiView = new View();
        setupServerAddress();
        connectToServer();
        runClient();
        scanCmdSocket();
    }

    /**
     * Function that runs the client, begging.
     */
    private static void runClient() {
        new Thread(() -> {
            while (true) {
                // wait for a message from the server or null if socket closed
                Message msg = clientData.readMessage();

                // check if error msg
                if (msg == null) {
                    guiView.connectionToServerLost();
                    break;
                }
                // proccess the message from the server
                processMsgFromServer(msg);
            }
            // close the client
            closeClient();
        }).start();
    }

    /**
     * Main function from the client to process messages incoming from the server as instruction to play the game.
     * @param msg the message received from server
     */
    private static void processMsgFromServer(Message msg) {
        String msgServer = msg.getMsg();
        String[] msgSubjParams = msgServer.split(" ");
        System.out.println(">>> proccessMsgFromServer() " + msg + " >>> Params: " + Arrays.toString(msgSubjParams));

        if (msgServer.startsWith(Const.LOGIN)) {
            // #login <attempts> <error-message>
            guiView.login();
            login();
            return;
        }

        if (msgServer.equals(Const.AI_REQUEST)) {
            String res = guiView.dialogAIRequest();
            clientData.writeMessage(res);
        }

        if (msgServer.startsWith(Const.WELCOME)) {
            // #welcome <user-name> <player-sign>
            playerUserName = msgSubjParams[1];
            sign = msgSubjParams[2];
            guiView.welcome(playerUserName, sign);
            return;
        }

        if (msgServer.equals(Const.WAIT_FOR_PARTNER)) {
            guiView.waitForPartner();
            return;
        }

        if (msgServer.equals(Const.MAKE_MOVE)) {
            guiView.setOriginalBGColor();
            guiView.makeMove(msg.getPos(), msg.getDestinationPos(), msg.getPieceID());
            return;
        }

        if (msgServer.equals(Const.PLAY_TURN)) {
            guiView.playTurn(msg.getList());
            return;
        }

        if (msgServer.equals(Const.CHECK)) {
            guiView.playTurn(msg.getList());
            return;
        }

        if (msgServer.equals(Const.SELECT_PIECE)) {
            guiView.setHighlightSquare(msg.getPos());
            return;
        }

        if (msgServer.equals(Const.SELECT_SQUARE)) {
            guiView.lockBoardButtons(msg.getReserveList());
            guiView.unlockBoardButtons(msg.getList());
            guiView.setHighlightOn(msg.getList());
            return;
        }

        if (msgServer.equals(Const.WAIT_TURN)) {
            guiView.waitTurn();
            return;
        }

        if (msgServer.equals(Const.DEFEATE)) {
            guiView.gameOver("Defeated");// will count down
            clientData.writeMessage(Const.COUNTDOWN_FINISHED);
            return;
        }

        if (msgServer.equals(Const.VICTORY)) {
            guiView.gameOver("Victorious");// will count down
            clientData.writeMessage(Const.COUNTDOWN_FINISHED);
            return;
        }

        if (msgServer.equals(Const.CHECK)) {
            String info = "You are under Check!";
            guiView.inCheck(info);
        }
//        if (msgServer.equals(Const.GAME_OVER)) {
//            guiView.gameOver();    // will count down
//            clientData.writeMessage(Const.COUNTDOWN_FINISHED);
//            return;
        if (msgServer.startsWith(Const.START_NEW_GAME)) {
            guiView.startNewGame();
            return;
        }

        if (msgServer.equals(Const.PARTNER_EXIT)) {
            showClientDisconnectedMSG();
        }

        if (msgServer.equals(Const.PROMOTION)) {
            guiView.showPromotionDialog();
        }
    }

    /**
     * Function that checks if there is still connection with the server.
     */
    private static void scanCmdSocket() {
        new Thread(() -> {
            while (true) {
                // wait for a message from the server or null if socket closed
                Message msg = clientData.readCommand();

                // check if error msg
                if (msg == null) {
                    guiView.connectionToServerLost();
                    break;
                }
                processCmdFromServer(msg);
            }
            // close the client
            closeClient();
        }).start();
    }

    /**
     * Process the commands that are sent from the server to avoid the interrupt on the game that is being played between 2 players.
     * @param msg commands that are received from the server
     */
    private static void processCmdFromServer(Message msg) {
        String cmdServer = msg.getMsg();

        if (cmdServer.equals(Const.PARTNER_EXIT)) {
            showClientDisconnectedMSG();
        }

        if (cmdServer.equals(Const.STATISTICS)) {
            guiView.showStatisticsDialog(msg.getStats());
        }
    }

    /**
     * A channeled information to server to inform of the situation or requests.
     * @param msg a generated message that this client sends to the server
     */
    public static void writeRequestsPermissions(String msg) {
        clientData.writeCommand(msg);
    }

    /**
     * Sends a message to the server the coordinates that been selected on the chess board.
     * @param row row location of the board
     * @param col colum location of the board
     */
    public static void squareSelected(int row, int col) {
        clientData.writeMessage(new Message(new Square(row, col)));
    }

    /**
     * A piece promotion function
     * @param translatePieceValue a id of a piece
     */
    public static void promotePiece(String translatePieceValue) {
        clientData.writeMessage(translatePieceValue);
    }

    /**
     * Login dialog presented to the user
     */
    private static void login() {
        String un = "", pw = "";
        JTextField unField = new JTextField(un);   // לקליטת שם המשתמש
        JTextField pwField = new JTextField(pw);   // לקליטת הסיסמה
        Object[] inputFields
                = {
                    "Enter User Name",
                    unField,
                    "Enter Password",
                    pwField,};

        // show Login dialog
        String[] buttonsNames = {"Login", "Guest", "Cancle & Exit"};
        JOptionPane optionPane = new JOptionPane(inputFields, -1, 1, null, buttonsNames);
        Utils.showWin(guiView.getWin());
        JDialog dialog = optionPane.createDialog("Player Login");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);  // show dialog - BLOCKING method
        dialog.dispose();

        String status = (String) optionPane.getValue();

        switch (status) {
            case "Login":
                un = unField.getText();
                pw = pwField.getText();
                if (un.isEmpty() || pw.isEmpty()) {
                    //
                    login();
                } else {
                    // send user name & password to server
                    clientData.writeMessage(un);
                    clientData.writeMessage(pw);
                }
                break;

            case "Guest":
                // send EMPTY user name & password to server - GUEST
                clientData.writeMessage("");
                clientData.writeMessage("");
                break;

            case "Cancle & Exit":
                clientData.writeCommand(Const.CLIENT_EXIT);
                closeClient();
                break;
        }
    }

    /**
     * function that tries to connect to the server.
     */
    private static void connectToServer() {
        try {

            guiView.setStatus("Try connect to server...");

            // Create TWO sockets - one for messages & one for commands
            Socket msgSocket = new Socket(serverIP, serverPort);
            Socket cmdSocket = new Socket(serverIP, serverPort);

            clientData = new ClientData(msgSocket, cmdSocket);
        } catch (IOException ex) {
            //Utils.debug(ex);
            String msg = "Can't connect to Server (" + serverIP + ":" + serverPort + ")\n";
            msg += "First run the Server, then try again...";
            guiView.errorMessage(msg);
            closeClient();
        }
    }

    /**
     * Interrupts the client and closes it.
     */
    private static void stopClient() {
        clientData.close(); // will throw 'SocketException' and unblock I/O. see close() API
    }

    /**
     * Shows the client that it's partner in the game left the game or the server.
     */
    private static void showClientDisconnectedMSG() {
        String msg = "Your Partner Disconnected!\nClient will be close...";
        guiView.errorMessage(msg);
        closeClient();
    }

//    private static void showServerDisconnectedMSG() {
//        String msg = "The connection with the Server is LOST!\nClient will be close...";
//        guiView.errorMessage(msg);
//        closeClient();
//    }

    /**
     * Closes the client completely
     */
    private static void closeClient() {
        if (clientData != null && clientData.isOpen()) {
            stopClient();
        }
        guiView.close();
        System.exit(0);
    }

    /**
     * sets up an address for the client
     */
    private static void setupServerAddress() {
        try {
            // get the Computer IP on this machine
            serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            String msg = "Can't get Computer IP!";
            guiView.errorMessage(msg);
            System.exit(0);
        }
        serverPort = Const.DEFAULT_SERVER_PORT;
        String serverAddress = guiView.getInputPort(serverIP, serverPort);
        if (serverAddress != null) {
            serverAddress = serverAddress.replace(" ", ""); // remove all spaces
            serverIP = serverAddress.substring(0, serverAddress.indexOf(":"));
            serverPort = Integer.parseInt(serverAddress.substring(serverAddress.indexOf(":") + 1));
        }
    }

    /**
     * Action that closes the window if needed.
     */
    public static void closeAndExitGame() {
        clientData.writeCommand(Const.CLIENT_EXIT);
        stopClient();
        // close GUI
        guiView.close();
        // close client
        System.exit(0);

    }

}
