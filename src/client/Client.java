
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    //constructor
    public Client(String host) { //pass the ip adress of server
        super("CLIENT");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendMessage(event.getActionCommand());
                userText.setText("");
            }
        }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    //connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eofException) {
            showMessage("\n Client terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeCrap();
        }
    }

    //connect to server
    private void connectToServer() throws IOException {
        showMessage("Attemption connection.... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    //set up streams to send and recieve messages
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage(" \n Dude your streams are now good to go!");
    }

    //while chatting with server
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotfoundException) {
                showMessage("\n ARE YOU SURE ABOUT THAT?");
            }
        } while (!message.equals("SERVER - END"));
    }

    //close the streams and sockets
    private void closeCrap() {
        showMessage("\n Closing crap down...");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //send messages to server
    private void sendMessage(String message) {
        try {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\n CLIENT-" + message);
        } catch (IOException ioException) {
            chatWindow.append("\n something messed up sending message!");
        }
    }

    //chage/update chatWindow
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                chatWindow.append(m);
            }
        }
        );
    }

    //gives user permission to type crap into the text box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() {
                userText.setEditable(tof);
            }
        }
        );
    }
}

