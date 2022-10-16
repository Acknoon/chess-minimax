package finalVar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * ClientData class provides the information about the link between wither the
 * server or the client that they are connected. The method that is used to
 * transfer the information between the ClientServer is the Socket. A Socket is
 * like a station that provides information to transfer data. The ClientData
 * class contains 2 types of Sockets one that sends information messages and one
 * that sends commands. ClientData is a sensitive class from all of the project
 * as it handles the communication in the network.
 *
 * @auther Ariel Mazar (mazarariel@gmail.com)
 */
public class ClientData implements Serializable {

    private String id;  //Name of the Client, used to differentiate between other clients. 
    private String type;    //Defines if the Client is a guest or a logged in user that exists in the database.
    private Socket socketMsg;   //A Socket station that provides information of the board game play.
    private Socket socketCmd;  //A secondary socket that provides essential information to be transfered like commands.
    private ObjectInputStream isMsg;    //Input variable that extracts information from the socket message related to the board game from the Server.
    private ObjectInputStream isCmd;    //Input vairable that extracts informaton from the Command socket that is related to the network from the Server.
    private ObjectOutputStream osMsg;   //Output information formatted into an object such as Message to the Server.
    private ObjectOutputStream osCmd;   //Output critical information formatted into an object such as Message to the Server.

    /**
     * Constructor of the ClientData class, the creation of the a object that
     * stores information about the connections. The connection information that
     * is stored in ClientData is relevant to transfer the data of the game, or
     * important commands to notify the user the connection problems occur.
     *
     * @param socketMsg Defined socket known as an endpoint of the client which
     * creates a link to the server to transfer data of the game.
     * @param socketCmd Defined socket known as an endpoint of the client which
     * creates a link to the server to transfers commands, or critical
     * information out of the game.
     */
    public ClientData(Socket socketMsg, Socket socketCmd) {
        this.socketMsg = socketMsg;
        this.socketCmd = socketCmd;
        try {
            /*This creates a link with messages that are sent*/
            this.osMsg = new ObjectOutputStream(socketMsg.getOutputStream());
            this.isMsg = new ObjectInputStream(socketMsg.getInputStream());

            /*This creates a link with commands that are sent*/
            this.osCmd = new ObjectOutputStream(socketCmd.getOutputStream());
            this.isCmd = new ObjectInputStream(socketCmd.getInputStream());
        } catch (IOException ex) {
            Utils.debug(ex);
        }
    }

    /**
     * Constructor of the ClientData class, the creation of the a object that
     * stores information about the connections. The connection information that
     * is stored in ClientData is relevant to transfer the data of the game, or
     * important commands to notify the user the connection problems occur.
     *
     * @param socketMsg Defined socket that is a station to transfer data of the
     * game.
     * @param socketCmd Defined socket that is similar to a station which
     * transfers commands, or critical information out of the game.
     * @param id New name of the Client that recently joined or made a login to
     * the server.
     */
    public ClientData(Socket socketMsg, Socket socketCmd, String id) {
        this(socketMsg, socketCmd);
        this.id = id;
    }

    /**
     * A link to the server that outputs information that is formatted as a
     * Message. Message is a complex Object that contains specific information
     * that is related to the game.
     *
     * @see Const.java
     * @param msg A unique structure of information related to the game that is
     * being transfered to the server.
     */
    public void writeMessage(Message msg) {
        try {
            osMsg.writeObject(msg);
            osMsg.flush();
        } catch (IOException ex) {
        }
    }

    /**
     * A link to the server that outputs information that is a simple plain
     * text, the text is a command that is stored in the mutual class that both
     * server and client contain. The command that is being transfered is only
     * related to the game.
     *
     * @param text simple plain text or a command sent to the server related to
     * the game.
     */
    public void writeMessage(String text) {
        writeMessage(new Message(text));
    }

    /**
     * An input data from the server that is related to the game. This blocks
     * the further code compilation as it awaits instructions from the server.
     * As the information being sent and transfered this extracts data and makes
     * sure the data is correct and wasn't corrupted. The information sent are
     * commands that notifies the client about the state of the game.
     *
     * @return The extracted data from server transfer. Checks weather the
     * information isn't corrupted.
     */
    public Message readMessage() {
        Message data = null;
        try {
            data = (Message) isMsg.readObject();
        } catch (IOException | ClassNotFoundException ex) {
        }
        return data;
    }

    /**
     * A link to the server that outputs information that is formatted as a
     * Message. Message is a complex Object that contains specific information
     * to administrate or control the clients from the server. The information
     * that is sent are commands or templates to show the user functionality of
     * the UI.
     *
     * @see Const.java
     * @param cmd A unique structure of information non-related to the game that
     * is being transfered to the server.
     */
    public void writeCommand(Message cmd) {
        try {
            osCmd.writeObject(cmd);
            osCmd.flush();
        } catch (IOException ex) {
        }
    }

    /**
     * A link to the server that outputs information that is a simple plain
     * text, the text is a command that is stored in the mutual class that both
     * server and client contain. The command that is being transfered is not
     * related to the game at all.
     *
     * @param cmd simple plain text or a command sent to the server not related
     * to the game.
     */
    public void writeCommand(String cmd) {
        writeCommand(new Message(cmd));
    }

    /**
     * An input data from the server. This blocks the further code compilation
     * as it awaits instructions from the server. As the information being sent
     * and transfered it gets extracted into fixed data of which isn't
     * corrupted. The information sent are commands of whom notifies the client
     * with critical messages.
     *
     * @return The extracted data from server transfer. Checks weather the
     * information isn't corrupted.
     */
    public Message readCommand() {
        Message cmd = null;
        try {
            cmd = (Message) isCmd.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            //ex.printStackTrace();
        }
        return cmd;
    }

    /**
     * Direct access to the name or special identity of the client.
     *
     * @return the name of the client.
     */
    public String getID() {
        return id;
    }

    /**
     * Direct access to change the name of the Client that is stored in this
     * Object.
     *
     * @param id New name to change the current name, the old name will be
     * replaced and deleted.
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Cuts the link between client and the server, the data will no longer
     * transfer after that action. This is with the intentions of closing the
     * connection between client and server to prevent network gap or opening.
     */
    public void close() {
        try {
            socketMsg.close();  // will close the msg socket and is & os streams 
            socketCmd.close();  // will close the cmd socket and is & os streams 
        } catch (IOException ex) {
        }
    }

    /**
     * Gives access to current IP address used to surf the network.
     *
     * @return A string that contains the IP address of the user.
     */
    public String getSocketAddress() {
        return socketMsg.getRemoteSocketAddress().toString().substring(1);
    }

    /**
     * Action that notifies and checks if the current used sockets of the
     * ClientData are functioning and operating fully. As the used sockets are
     * the link to the server from the client, the cmdSocket and msgSocket.
     *
     * @return True if the sockets are functional and operating fully. Returns
     * False if one of the sockets isn't functioning and operating.
     */
    public boolean isOpen() {
        return socketMsg != null && socketCmd != null && socketMsg.isClosed() && socketCmd.isClosed();
    }

    /**
     * Function that gives access to view what type the client is.
     *
     * @return weather the client is a guest registered user or a logged in user
     * that is known
     */
    public String getType() {
        return type;
    }

    /**
     * Changes the Client identity type to be Registered as.
     *
     * @param type is weather a Guest or an existing user from the database.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Simple String that reports the Information that the ClientData stores,
     * living to it's name "the data of the client".
     *
     * @return A string that fully shows the information needed of a this
     * client.
     */
    @Override
    public String toString() {
        return "ClientData{" + "id=" + id + ", msgSocket=" + socketMsg + ", cmdSocket=" + socketCmd + ", isMsg=" + isMsg + ", isCmd=" + isCmd + ", osMsg=" + osMsg + ", osCmd=" + osCmd + ", partner=" + '}';
    }
}
