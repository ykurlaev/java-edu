package fit.kurlaev.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Controller implements GUIController
{
    private class ConnectionListener implements fit.kurlaev.chat.client.ConnectionListener
    {
        private GUIView view;
        private Connection connection;

        public ConnectionListener(GUIView view, Connection connection)
        {
            Logger.getLogger("Client").info("Controller: Listener created");
            this.view = view;
            this.connection = connection;
        }

        public void newMessage(User user, String text)
        {
            Logger.getLogger("Client").info("Controller: new message: "+user.getUsername()+": "+text);
            view.newMessage(connection, user, text);
        }

        public void userConnected(String username)
        {
            Logger.getLogger("Client").info("Controller: user connected message: "+username);
            view.userConnected(connection, username);
        }

        public void userDisconnected(String username)
        {
            Logger.getLogger("Client").info("Controller: user disconnected message: "+username);
            view.userDisconnected(connection, username);
        }

        public void connecting()
        {
            Logger.getLogger("Client").info("Controller: connecting message");
            view.connecting(connection);
        }

        public void connected()
        {
            Logger.getLogger("Client").info("Controller: connected message");
            view.connected(connection);
        }

        public void disconnected()
        {
            Logger.getLogger("Client").info("Controller: disconnected message");
            connections.remove(connection);
            view.disconnected(connection);
        }

        public void error(String message)
        {
            Logger.getLogger("Client").info("Controller: error message: "+message);
            view.error(connection, message);
        }
    }

    private ArrayList<GUIView> guiViews = new ArrayList<GUIView>();
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private Protocol protocol;
    private ArrayList<ConnectionListener> listeners = new ArrayList<ConnectionListener>();
    private static final String CLIENT_ID = "client_id";

    public Controller(Protocol protocol)
    {
        this.protocol = protocol;
        Logger.getLogger("Client").info("Controller created");
    }

    public void addView(GUIView view)
    {
        guiViews.add(view);
        view.setController(this);
        Logger.getLogger("Client").info("Controller: view connected");
    }

    public void removeView(GUIView view)
    {
        Logger.getLogger("Client").info("Controller: view disconnected");
        guiViews.remove(view);
        view.setController(null);
    }

    public Connection[] getConnections()
    {
        if(connections.size()==0)
        {
            return null;
        }
        return (Connection[])connections.toArray();
    }

    public Connection connect(InetAddress address, int port, String nickname) throws IOException
    {
        Connection connection = protocol.connect(address,port,nickname,CLIENT_ID);
        connections.add(connection);
        Logger.getLogger("Client").info("Controller: connected");
        return connection;
    }

    public void register(GUIView view, Connection connection)
    {
        ConnectionListener listener = new ConnectionListener(view,connection);
        listeners.add(listener);
        connection.addListener(listener);
    }

    public void unregister(GUIView view, Connection connection)
    {
        for(ConnectionListener listener : listeners)
        {
            if(listener.view == view)
            {
                connection.removeListener(listener);
                listeners.remove(listener);
                break;
            }
        }
    }
}
