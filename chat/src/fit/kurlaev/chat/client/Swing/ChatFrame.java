package fit.kurlaev.chat.client.Swing;

import fit.kurlaev.chat.client.Connection;
import fit.kurlaev.chat.client.GUIController;
import fit.kurlaev.chat.client.GUIView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

public class ChatFrame extends JFrame
{
    private JTextArea chatArea;
    private JScrollPane chatAreaScrollPane;
    private JList userList;
    private JScrollPane userListScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private Connection connection;
    private GUIView view;
    private GUIController controller;

    public ChatFrame(Connection connection, GUIView view, GUIController controller)
    {
        this.connection = connection;
        this.view = view;
        this.controller = controller;
        initUI();
        addWindowListener(new WindowListener()
        {
            public void windowOpened(WindowEvent e) { }

            public void windowClosing(WindowEvent e) { }

            public void windowClosed(WindowEvent e)
            {
                closing();
            }

            public void windowIconified(WindowEvent e) { }

            public void windowDeiconified(WindowEvent e) { }

            public void windowActivated(WindowEvent e) { }

            public void windowDeactivated(WindowEvent e) { }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Logger.getLogger("Client").info("ChatFrame created");
    }

    private void closing()
    {
        connection.disconnect();
        controller.unregister(view,connection);
        controller.removeView(view);
        System.exit(0); //I know, that it is bad
    }

    private void initUI()
    {
        setTitle("Chat");
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatAreaScrollPane = new JScrollPane(chatArea);
        chatAreaScrollPane.setPreferredSize(new Dimension(400,400));
        chatAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        userList = new JList();
        userListScrollPane = new JScrollPane(userList);
        userListScrollPane.setPreferredSize(new Dimension(200,400));
        userListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageField = new JTextField();
        messageField.addKeyListener(new KeyListener()
        {
            public void keyTyped(KeyEvent event)
            {
                if(event.getKeyChar()=='\n')
                {
                    send();
                }
            }
            public void keyPressed(KeyEvent e) { }
            public void keyReleased(KeyEvent e) { }
        });
        sendButton = new JButton(sendButtonText());
        sendButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                send();
            }
        });
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        topPanel.add(chatAreaScrollPane);
        topPanel.add(userListScrollPane);
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        add(topPanel);
        add(bottomPanel);
        pack();
    }

    private String sendButtonText()
    {
        return "Send"; //add localization
    }

    private void send()
    {
        String message = messageField.getText();
        Logger.getLogger("Client").info("View: sending message: "+message);
        connection.sendMessage(message);
        messageField.setText("");
    }

    public void addMessage(final String text)
    {
        Logger.getLogger("Client").info("View: new message: "+text);
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                chatArea.append(text+"\n");
            }
        });
    }

    public void setUserList(final String[] users)
    {
        Logger.getLogger("Client").info("Userlist updated");
        userList.setListData(users);
    }
}
