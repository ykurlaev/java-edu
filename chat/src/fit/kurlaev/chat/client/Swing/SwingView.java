package fit.kurlaev.chat.client.Swing;

import fit.kurlaev.chat.client.Connection;
import fit.kurlaev.chat.client.GUIController;
import fit.kurlaev.chat.client.GUIView;
import fit.kurlaev.chat.client.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SwingView extends JFrame implements GUIView
{
    private GUIController controller = null;
    private Connection connection = null;
    private JTextField addressField;
    private JTextField portField;
    private JTextField nicknameField;
    private JLabel errorLabel;
    private JButton loginButton;
    private ChatFrame chatFrame;
    private String[] userList;

    private void initUI()
    {
        setTitle("Chat");
        addressField = new JTextField("localhost");
        addressField.setPreferredSize(new Dimension(250,30));
        portField = new JTextField("4444");
        portField.setPreferredSize(new Dimension(250, 30));
        nicknameField = new JTextField("user");
        nicknameField.setPreferredSize(new Dimension(250,30));
        errorLabel = new JLabel();
        errorLabel.setMaximumSize(new Dimension(250,30));
        loginButton = new JButton(loginButtonText());
        loginButton.setPreferredSize(new Dimension(70,30));
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0,90,0,90));
        final GUIView view = this;
        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                if(controller!=null)
                {
                    try
                    {
                        connection = controller.connect(InetAddress.getByName(addressField.getText()),
                                                        Integer.decode(portField.getText()),nicknameField.getText());
                        controller.register(view,connection);
                        Logger.getLogger("Client").info("View: connected");
                    }
                    catch(UnknownHostException e)
                    {
                        errorLabel.setText("Unknown host!");
                    }
                    catch(IOException e)
                    {
                        errorLabel.setText("Error connecting to host!");
                    }
                    catch(NumberFormatException e)
                    {
                        errorLabel.setText("Wrong port!");
                    }
                    if(connection!=null)
                    {
                        chatFrame = new ChatFrame(connection,view,controller);
                        setVisible(false);
                        chatFrame.setVisible(true);
                    }
                }
            }
        });
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(addressField);
        add(portField);
        add(nicknameField);
        add(errorLabel);
        panel.add(loginButton);
        add(panel);
        setResizable(false);
        pack();
    }

    public SwingView()
    {
        initUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() {
                setVisible(true);
            }
        });
        Logger.getLogger("Client").info("View created");
    }

    public void setController(GUIController controller)
    {
        this.controller = controller;
    }

    public void connecting(Connection connection)
    {
        Logger.getLogger("Client").info("View: connecting message");
        chatFrame.addMessage("* Connecting...");
    }

    public void connected(Connection connection)
    {
        Logger.getLogger("Client").info("View: connected message");
        //chatFrame.addMessage("* Connected");
    }

    public void newMessage(Connection connection, User user, String text)
    {
        String message = user.getUsername()+": "+text;
        Logger.getLogger("Client").info("View: new message: "+message);
        chatFrame.addMessage(message);
    }

    public void error(Connection connection, String text)
    {
        Logger.getLogger("Client").info("View: error message: "+text);
        chatFrame.addMessage("* Error: "+text);
    }

    public void userConnected(Connection connection, String username)
    {
        Logger.getLogger("Client").info("View: user connected message: "+username);
        chatFrame.addMessage("* " + username + " connected");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userConnected();
            }
        });
    }

    private void userConnected()
    {
        User[] users = connection.getUserList();
        userList = new String[users.length];
        for(int i=0; i<users.length; i++)
        {
            userList[i] = users[i].getUsername();
        }
        chatFrame.setUserList(userList);
    }

    public void userDisconnected(Connection connection, String username)
    {
        Logger.getLogger("Client").info("View: user disconnected message"+username);
        chatFrame.addMessage("* "+username+" disconnected");
        User[] users = connection.getUserList();
        userList = new String[users.length];
        for(int i=0; i<users.length; i++)
        {
            userList[i] = users[i].getUsername();
        }
        chatFrame.setUserList(userList);    }

    public void disconnected(Connection connection)
    {
        Logger.getLogger("Client").info("View: disconnected message");
        chatFrame.addMessage("* Disconnected");
    }

    private String loginButtonText()
    {
        return "Login";
    }
}
