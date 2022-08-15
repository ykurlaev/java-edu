package fit.kurlaev.chat.protocol.client;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import fit.kurlaev.chat.client.ConnectionListener;
import fit.kurlaev.chat.protocol.Message;
import fit.kurlaev.chat.protocol.Serializer;
import fit.kurlaev.chat.protocol.User;
import fit.kurlaev.chat.protocol.message.Command;
import fit.kurlaev.chat.protocol.message.Error;
import fit.kurlaev.chat.protocol.message.Event;
import fit.kurlaev.chat.protocol.message.Success;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class Connection implements fit.kurlaev.chat.client.Connection
{
    private Thread readThread = null;
    private Thread writeThread = null;
    private InetAddress address;
    private ArrayList<User> users;
    private int port ;
    private User myUser;
    final private ArrayList<ConnectionListener> listeners = new ArrayList<ConnectionListener>();
    private LinkedBlockingQueue<Command> toWrite = new LinkedBlockingQueue<Command>();
    private LinkedBlockingQueue<Message> responds = new LinkedBlockingQueue<Message>();
    private volatile boolean waitingRespond;
    private volatile boolean connected;
    private volatile Boolean userListIsCurrent = false;
    private String session;
    private Socket socket;
    private Serializer serializer;
    private InputStream inStream;
    private OutputStream outStream;

    public Connection(InetAddress address, int port, String username, String clientName, Serializer serializer) throws IOException
    {
        this.serializer = serializer;
        this.address = address;
        this.port = port;
        socket = new Socket();
        socket.connect(new InetSocketAddress(address,port));
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
        connected = true;
        this.myUser = new User(username,clientName);
        for(ConnectionListener listener : listeners)
        {
            listener.connecting();
        }
        readThread = new Thread(new Runnable()
        {
            public void run()
            {
                readLoop();
            }
        });
        readThread.start();
        writeThread = new Thread(new Runnable()
        {
            public void run()
            {
                writeLoop();
            }
        });
        writeThread.start();
        Command command = Command.login(username,clientName);
        try
        {
            toWrite.put(command);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void addListener(ConnectionListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(ConnectionListener listener)
    {
        listeners.remove(listener);
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public User getMyUser()
    {
        return myUser;
    }

    public User[] getUserList()
    {
        Logger.getLogger("Client").info("Connection: getting userlist");
        while(!userListIsCurrent)
        {
            try
            {
                synchronized(userListIsCurrent)
                {
                    userListIsCurrent.wait();
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        Logger.getLogger("Client").info("Connection: userlist updated");
        User[] userArray = new User[users.size()];
        int i = 0;
        for(User user : users)
        {
            userArray[i] = user;
            i++;
        }
        return userArray;
    }

    public void sendMessage(String text)
    {
        Logger.getLogger("Client").info("Connection: sending message: "+text);
        Command command = Command.message(text,session);
        try
        {
            toWrite.put(command);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        /*synchronized(listeners)
        {
            for(ConnectionListener listener : listeners)
            {
                listener.newMessage(myUser,text);
            }
        }*/
    }

    public void disconnect()
    {
        Logger.getLogger("Client").info("Connection: disconnecting");
        Command command = Command.logout(session);
        try
        {
            toWrite.put(command);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    private void readLoop()
    {
        try
        {
            for(;;)
            {
                Logger.getLogger("Client").info("Connection: readLoop iteration");
                byte[] lengthBuffer = new byte[4];
                inStream.read(lengthBuffer);
                DataInputStream stream =new DataInputStream(new ByteArrayInputStream(lengthBuffer));
                int length = stream.readInt();
                Logger.getLogger("Client").info("Connection: new message length: "+length);
                byte buffer[] = new byte[length];
                inStream.read(buffer);
                Message message = serializer.deserialize(buffer);
                Logger.getLogger("Client").info("Connection: new message "+message);
                if(message!=null)
                {
                    if(message instanceof Event)
                    {
                        switch(((Event) message).getType())
                        {
                            case USERLOGIN:
                                synchronized(listeners)
                                {
                                    for(ConnectionListener listener : listeners)
                                    {
                                        listener.userConnected(((Event) message).getUsername());
                                    }
                                }
                                break;
                            case USERLOGOUT:
                                synchronized(listeners)
                                {
                                    for(ConnectionListener listener : listeners)
                                    {
                                        listener.userDisconnected(((Event) message).getUsername());
                                    }
                                }
                                break;
                            case MESSAGE:
                                synchronized(listeners)
                                {
                                    for(ConnectionListener listener : listeners)
                                    {
                                        User sender = null;
                                        for(User user : users)
                                        {
                                            if(user.getUsername().equals(((Event) message).getUsername()))
                                            {
                                                sender = user;
                                            }
                                        }
                                        listener.newMessage(sender,((Event) message).getText());
                                    }
                                }
                                break;
                            case UNKNOWN:
                                error("Server protocol error");
                        }
                    }
                    else
                    {
                        if(waitingRespond)
                        {
                            responds.put(message);
                        }
                        else
                        {
                            error("Server protocol error");
                        }
                    }
                }
            }
        }
        catch(IOException e)
        {
            error(e.getLocalizedMessage());
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    private void writeLoop()
    {
        try
        {
            while(connected)
            {
                Logger.getLogger("Client").info("Connection: writeLoop iteration");
                Command command = toWrite.take();
                Logger.getLogger("Client").info("Connection: new message to write "+command);
                byte[] buffer = serializer.serialize(command);
                ByteOutputStream byteStream = new ByteOutputStream(4);
                DataOutputStream stream = new DataOutputStream(byteStream);
                try
                {
                    stream.writeInt(buffer.length);
                }
                catch(IOException e)
                {
                }
                outStream.write(byteStream.getBytes());
                outStream.write(buffer);
                Logger.getLogger("Client").info("Connection: wrote message, length: "+buffer.length);
                waitingRespond = true;
                Message respond = responds.take();
                Logger.getLogger("Client").info("Connection: respond received: "+respond);
                if(respond instanceof Success)
                {
                    if(command.getType()==Command.Type.LOGIN)
                    {
                        if(((Success) respond).getType()==Success.Type.SESSION)
                        {
                            session = ((Success) respond).getSession();
                            synchronized(listeners)
                            {
                                for(ConnectionListener listener : listeners)
                                {
                                    listener.connected();
                                }
                            }
                            try
                            {
                                toWrite.put(Command.list(session));
                            }
                            catch(InterruptedException e)
                            {
                                Thread.currentThread().interrupt();
                            }
                        }
                        else
                        {
                            error("Server protocol error");
                        }
                    }
                    else if(command.getType()==Command.Type.LIST)
                    {
                        if(((Success) respond).getType()==Success.Type.USERLIST)
                        {
                            users = new ArrayList<User>(Arrays.asList(((Success) respond).getUsers()));
                            synchronized(userListIsCurrent)
                            {
                                userListIsCurrent = true;
                                try
                                {
                                    userListIsCurrent.notify();
                                }
                                catch(IllegalMonitorStateException e) { }
                            }
                        }
                        else
                        {
                            error("Server protocol error");
                        }
                    }
                    else if(command.getType()==Command.Type.MESSAGE || command.getType()== Command.Type.LOGOUT)
                    {
                        if(((Success) respond).getType()!=Success.Type.EMPTY)
                        {
                            error("Server protocol error");
                        }
                    }
                }
                else
                {
                    String text;
                    if(respond instanceof Error)
                    {
                        text = ((Error) respond).getReason();
                    }
                    else
                    {
                        text = "Unknown error";
                    }
                    error(text);
                }
            }
        }
        catch(IOException e)
        {
            error(e.getLocalizedMessage());
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    private void error(String text)
    {
        synchronized(listeners)
        {
            for(ConnectionListener listener : listeners)
            {
                listener.error(text);
                listener.disconnected();
                connected = false;
            }
        }
    }
}
