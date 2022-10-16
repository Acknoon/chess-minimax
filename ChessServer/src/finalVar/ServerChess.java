package finalVar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Document : Chess, Server
 *
 * Date : 18/10/2019
 *
 * Author : Ariel Mazar (mazarariel@gmail.com)
 */
public class ServerChess {

    /*==============================Server setup==============================*/
    private static String serverIP;//The server IP to be able to access and enter the server
    private static int serverPort;//The server used port to make connections with clients.
    private static int autoGuestID = 1;//Automated number to generate if the user log in are guests.
    private static ServerSocket serverSocket;//A server socket that creates link to new clients
    private static ArrayList<Player> listPlayers;//list of players that are currently connected to the server
    private static Player soloPlayer;// A player that is waiting for partner.

    /*==============================GUI Content==============================*/
    private static JFrame win;//window of the server
    private static JTextArea logArea;//the text field
    private static JButton showPlayers;//button to show every player.

    /*==============================Main==============================*/
    /**
     * The run file to run the server
     */
    public static void main(String args[]) {
        createServerGUI();
        setupServerAddress();
        createServerSocket();
        runServer();
    }

    /*==============================set up and startGame server==============================*/
    /**
     * A function that runs the server.
     */
    public static void runServer() {
        log("Server start RUNNING on " + serverIP + ":" + serverPort + "\n");
        listPlayers = new ArrayList<>();
        while (true) {
            // wait for a client to connect - blocking.
            // if Ok, return client socket. else return null.
            ClientData connector = waitForAClient();
            // if client1 is null serverSocket was closed!
            if (connector == null) {
                log("Problem with server socket! server will close...");
                break;
            }
            // create a thread to handle game Session between two clients (pair).
            handleClient(connector);
        }
        // close the server
        closeServer();
    }

    /**
     * A function that is inside a thread to handle a client that connected to
     * the server.
     *
     * @param client newly entered client
     */
    private static void handleClient(ClientData client) {
        // handle client login with thread
        new Thread(() -> {
            // check login. BLOCKING!
            if (clientLogin(client)) {
//                listPlayers.add(client);
                transformClientToPlayer(client);
                String choice = isPlayedWithComputer(client);
                tryToStartGameSession(choice);
            }
        }).start();
    }

    /**
     * Transformation of the client that entered the server into a player that
     * plays games.
     *
     * @param client the client which made connections with
     */
    private static void transformClientToPlayer(ClientData client) {
        PlayerNetwork newPlayer = new PlayerNetwork(client, giveSign(listPlayers));
        listPlayers.add(newPlayer);

        new Thread(() -> {
            while (true) {
                // wait for a command from client
                Message cmd = client.readCommand();
                // check if error msg
                if (cmd == null) {
                    break;
                }
                // proccess command from the client
                proccessCmdFromPlayer(newPlayer, cmd);
            }
        }).start();
    }

    /**
     * A processor that handles the critical network communication if one of the
     * players connection fell.
     *
     * @param player the player that runs the process on
     * @param cmd messages that being transfered.
     */
    private static void proccessCmdFromPlayer(PlayerNetwork player, Message cmd) {
        System.out.println(">>> proccessCmdFromClient() " + player.getNameID() + " say: " + cmd);
        System.out.println("the updated cmd is active indeed");
        String msgSubj = cmd.getMsg();

        if (msgSubj.equals(Const.CLIENT_EXIT)) {
            disconnectPlayer(player);

            if (player.getPartner() != null && player.getPartner() instanceof PlayerNetwork) {

                ((PlayerNetwork) player.getPartner()).writeCommand(Const.PARTNER_EXIT);
                disconnectPlayer((PlayerNetwork) player.getPartner());
                System.out.println("should've disconnected the partner by now");
            }
        }

        if (msgSubj.equals(Const.STATISTICS)) {
            String stats = DB.getDataGamesFromPlayer(player.getNameID());
            player.writeCommand(new Message(Const.STATISTICS, stats));

        }
    }

    /**
     * Function which handles the client to login as a registered user.
     *
     * @param client the client that entered the server
     * @return true if the client managed to login or enter as a guest false if
     * there was a failure in connection.
     */
    private static boolean clientLogin(ClientData client) {
        int attempts = 1;
        String errMsg = " ";

        while (true) {
            client.writeMessage(new Message(Const.LOGIN + " " + attempts + " " + errMsg));

            Message msg1 = client.readMessage();  // read User Name from client
            Message msg2 = client.readMessage();  // read Password from client

            // check if connection to client is lost
            if (msg1 == null || msg2 == null) {
                return false;
            }

            String un = msg1.getMsg();  // get username
            String pw = msg2.getMsg();  // get password

            // check if row with user name & password exists in DB
            if (un.isEmpty() || DB.isUserExists(un, pw) && !isUserLogedIn(un)) {
//                String inputOnlineMember = playerID + " " + client.getSocketAddress();
                client.setType(Const.PLAYER);

                // check if this is a GUEST
                if (un.isEmpty()) {
                    un += Const.GUEST + "#" + autoGuestID++;
                    client.setType(Const.GUEST);
                }

                client.setID(un);

                log(client.getID() + " " + client.getSocketAddress() + " Login!");
                break;
            } else {
                if (DB.isUserExists(un, pw) && isUserLogedIn(un)) {
                    errMsg = "This user already LOGED !";
                } else {
                    errMsg = "User Name or Password INCORRECT !";
                }
                attempts++;
            }
        }
        return true;
    }

    /**
     * Function that checks if the client decided to play with a computer or a
     * human.
     *
     * @param client the client that chooses the decision.
     * @return AI if the client chose to play with a computer, Human if the
     * client chose not to.
     */
    public static String isPlayedWithComputer(ClientData client) {
        client.writeMessage(Const.AI_REQUEST);

        Message msg = client.readMessage();

        if (msg == null) {
            return null;
        }
        return msg.getMsg();
    }

    /**
     * Function which attempts to start a match with clients
     *
     * @param choice choice that was made by a client to play against an
     * opponent.
     */
    public static void tryToStartGameSession(String choice) {
        if (choice == null) {
//            disconnectPlayer((PlayerNetwork) listPlayers.get(listPlayers.size() - 1));
            return;
        }

        switch (choice) {
            case Const.AI_CHOICE:
                PlayerNetwork playerSeeker = (PlayerNetwork) listPlayers.get(listPlayers.size() - 1);
                PlayerAI computerPlayer = new PlayerAI(giveSign(listPlayers));
                listPlayers.add(computerPlayer);

                playerSeeker.writeMessage(Const.WELCOME + " " + playerSeeker.getNameID() + " " + Const.WHITE);
                playerSeeker.setPartner(computerPlayer);
                computerPlayer.setPartner(playerSeeker);
                startGameSession(playerSeeker, computerPlayer);
                break;
            case Const.HUMAN_CHOICE:
                if (listPlayers.size() % 2 != 0) {
                    soloPlayer = listPlayers.get(listPlayers.size() - 1);
                    if (soloPlayer instanceof PlayerNetwork) {
                        ((PlayerNetwork) soloPlayer).writeMessage(Const.WELCOME + " " + soloPlayer.getNameID() + " " + Const.WHITE);
                        ((PlayerNetwork) soloPlayer).writeMessage(Const.WAIT_FOR_PARTNER);
                    }

                } else {
                    //can start a New Game with the last two clients
                    PlayerNetwork firstPlayer = (PlayerNetwork) soloPlayer;
                    PlayerNetwork secondPlayer = (PlayerNetwork) listPlayers.get(listPlayers.size() - 1);

                    secondPlayer.writeMessage(Const.WELCOME + " " + secondPlayer.getNameID() + " " + Const.BLACK);
                    firstPlayer.setPartner(secondPlayer);
                    secondPlayer.setPartner(firstPlayer);
                    startGameSession(firstPlayer, secondPlayer);
                }
        }
    }

    /**
     * Starts a game between 2 players on a different thread.
     *
     * @param player1 the first player that connected and waited.
     * @param player2 second player which could be a human or an AI
     */
    private static void startGameSession(Player player1, Player player2) {
        new Thread(() -> {
            if (player1 instanceof PlayerNetwork && player2 instanceof PlayerNetwork) {
                log("Game Session: (" + player1.getNameID() + " & " + player2.getNameID() + ") STARTED!");
                Game session = new Game(player1, player2);
                session.startGame();
                disconnectPlayer((PlayerNetwork)player1);
                disconnectPlayer((PlayerNetwork)player2);
                log("Game Session: (" + player1.getNameID() + " & " + player2.getNameID() + ") STOPPED!");
            } else {
                if (player1 instanceof PlayerNetwork) {
                    log("Game Session: (" + player1.getNameID() + " & " + "AI" + ") STARTED!");
                    Game session = new Game(player1, player2);
                    session.startGame();
                    log("Game Session: (" + player1.getNameID() + " & " + "AI" + ") STOPPED!");
                }
            }
        }).start();
    }

    /**
     * Server waiting for the client to accept and connect to.
     *
     * @return New client that entered.
     */
    private static ClientData waitForAClient() {
        ClientData client = null;
        try {
            // Wait for a new client to connect. return client socket.
            // Need accept TWO sockets - one for messages & one for commands
            Socket msgSocket = serverSocket.accept();
            Socket cmdSocket = serverSocket.accept();

            // create ClientData object to the new client
            client = new ClientData(msgSocket, cmdSocket);
            log("Client " + client.getSocketAddress() + " Connected!");
        } catch (IOException ex) {
            /*Utils.debug(ex);*/        }
        return client;
    }

    /**
     * Disconnects the player from the server.
     *
     * @param player the player that gets disconnected
     */
    private static void disconnectPlayer(PlayerNetwork player) {
        if (player.getNameID() == null) // not loged & not a gesust
        {
            log("Client " + player.getLinkAddress() + " Disconected!");
        } else {
            log(player.getNameID() + " Disconnected!");
        }
        listPlayers.remove(player);
        player.exit();
    }

    /**
     * Function that disconnects the client from the server
     *
     * @param client the client that gets disconnected
     */
    private static void disconnectClient(ClientData client) {
        if (client.getID() == null) // not loged & not a gesust
        {
            log("Client " + client.getSocketAddress() + " Disconected!");
        } else {
            log(client.getID() + " Disconnected!");
        }
        client.close();
    }

    /**
     * Exits the server.
     */
    private static void exitServer() {
        int option = JOptionPane.showConfirmDialog(win, "Do you really want to Exit?", "Server Exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            stopServer();
        }
    }

    /**
     * Function that halts and stops the server running putting it to the end
     * and closes it.
     */
    private static void stopServer() {
        try {
            // This will throw cause an Exception on serverSocket.accept() in waitForClient() method
            serverSocket.close();

            // close all clients socket. will cause the Thread session to stop
            for (int i = 0; i < listPlayers.size(); i++) {
                if (listPlayers.get(i) instanceof PlayerNetwork) {
                    ((PlayerNetwork) listPlayers.get(i)).exit();
                }
            }

            log("Server Stopped!");
        } catch (IOException ex) {
            Utils.debug(ex);
        }
    }

    /**
     * Closes the server completely and locks the server socket.
     */
    private static void closeServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            stopServer();
        }
        win.dispose();
        log("Server Closed!");
        System.exit(0);
    }

    /**
     * Creates a server socket for clients to connect
     */
    private static void createServerSocket() {
        try {
            serverSocket = new ServerSocket(serverPort);
            win.setTitle("Chess Server (" + serverIP + ":" + serverPort + ")");
        } catch (IOException ex) {
            //Utils.debug(ex);
            String errMsg = "Can't run another server on (" + serverIP + ":" + serverPort + ")\n";
            errMsg += "Check if server allready running...";
            Utils.showErrMsgToUser(win, "Game Server Error", errMsg);
            closeServer();
        }
    }

    /**
     * Sets up an IP address with avaliable port to connect to the server.
     */
    private static void setupServerAddress() {
        try {
            // get the Computer IP on this machine
            serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Utils.showErrMsgToUser(win, "Game Server Error", "Can't get Computer IP!");
            System.exit(0);
        }

        serverPort = Const.DEFAULT_SERVER_PORT;
        String port = null;

        while (port == null) {
            port = JOptionPane.showInputDialog(win, "Enter Server PORT Number [1024-65535]", serverPort);
            if (port != null) {
                port = port.replace(" ", ""); // remove all spaces
                try {
                    serverPort = Integer.parseInt(port);
                    if (serverPort < 1024 || serverPort > 65535) {
                        log("Port number is NOT between 1024-65535 !");
                        serverPort = Const.DEFAULT_SERVER_PORT;
                        port = null;
                    }
                } catch (NumberFormatException ex) {
                    log("Port number is NOT between 1024-65535 !");
                    port = null;
                }
            }
        }
    }

    /**
     * Creates a Small terminal for the server to easily inspect of new users.
     */
    private static void createServerGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
        }

        win = new JFrame();
        win.setSize(Const.WIN_SIZE);
        win.setTitle("Game Server");
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        win.setAlwaysOnTop(true);
        win.setResizable(false);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitServer();
                //System.exit(0);
            }
        });

        // create displayArea
        logArea = new JTextArea();
        logArea.setLineWrap(true);
        logArea.setFont(Const.LOG_FONT);
        logArea.setMargin(new Insets(5, 5, 5, 5));
        logArea.setBackground(Const.LOG_BG_COLOR);
        logArea.setForeground(Const.LOG_FONT_COLOR);
        logArea.setEditable(false);

        showPlayers = new JButton("Show Online Players");
        showPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                showOnlinePlayers();
            }
        });

        // add all components to window
        win.add(new JScrollPane(logArea), BorderLayout.CENTER);
        win.add(showPlayers, BorderLayout.SOUTH);

        // show window
        win.setVisible(true);
    }

    /**
     * function that shows online members that play.
     */
    private static void showOnlinePlayers() {
        log("\nOnline Players:");
        String onlinePlayer = "";
        int num = 1;
        sortListNames();
        for (int i = 0; i < listPlayers.size(); i++) {
            if (listPlayers.get(i) instanceof PlayerNetwork) {
                if (listPlayers.get(i).getPartner() != null) {
                    onlinePlayer = (num + i) + " " + ((PlayerNetwork) listPlayers.get(i)).getAddressSocket() + " "
                            + listPlayers.get(i).getNameID() + " " + listPlayers.get(i).getPartner().getNameID();
                } else {
                    onlinePlayer = (num + i) + " " + ((PlayerNetwork) listPlayers.get(i)).getAddressSocket() + " "
                            + listPlayers.get(i).getNameID() + " ?";
                }
            }
            log(onlinePlayer);
        }
    }

    /**
     * Sorts the players that are online based on the alphabetical order.
     */
    private static void sortListNames() {
        Collections.sort(listPlayers, (Player p1, Player p2) -> {
            return p1.getNameID().compareTo(p2.getNameID());
        });
    }

    /**
     * Gives a player the sign a piece set to play as
     *
     * @param list which player deserves from the list to play as
     * @return the index of a sign that played as piece set, white pieces or
     * black pieces.
     */
    private static int giveSign(ArrayList<Player> list) {
        return list.isEmpty() ? Const.PLAYER_WHITE : (list.size() % 2 == 0 ? Const.PLAYER_WHITE : Const.PLAYER_BLACK);
    }

    /**
     * Function that checks if a user is logged in
     *
     * @param un username
     * @return true if the user is logged in, false if not.
     */
    private static boolean isUserLogedIn(String un) {
        for (int i = 0; i < listPlayers.size(); i++) {
            if (listPlayers.get(i) instanceof PlayerNetwork) {
                if (((PlayerNetwork) listPlayers.get(i)).getNameID().equals(un)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * appends messages inside the GUI for the server administrator to view.
     *
     * @param msg A message to post.
     */
    private static void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        System.out.println(msg);
    }

}
