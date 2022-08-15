package fit.kurlaev.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConsoleView implements GUIView
{
    volatile private boolean hasController = false;
    private Boolean connected = false;
    private GUIController controller;
    private Thread thread;
    private User myUser;
    volatile private User[] users;
    private Connection connection;
    private PrintStream out;
    private BufferedReader in;

    private static String lineStart = "\033[G";
    private static String prompt = "> ";

    public ConsoleView()
    {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = System.out;
        thread = new Thread(new Runnable()
        {
            public void run()
            {
                inputLoop();
            }
        });
        thread.start();
    }

    public void setController(GUIController controller)
    {
        this.controller = controller;
        hasController = true;
        synchronized(this)
        {
            notify();
        }
    }

    public void connecting(Connection connection)
    {
        if(connection==this.connection)
        {
            out.println("Connecting...");
        }
    }

    public void connected(Connection connection)
    {
        if(connection==this.connection)
        {
            out.println("Connected to "+connection.getAddress()+":"+connection.getPort());
            users = connection.getUserList();
            printUsers();
            synchronized(connected)
            {
                connected = true;
                connected.notify();
            }
        }
    }

    private void printUsers()
    {
        out.println(lineStart+users.length+" users online:");
        for(User user : users)
        {
            out.println(user.getUsername()+" ("+user.getClientName()+")"+(user==myUser?" *":""));
        }
        out.print(prompt);
    }

    public void newMessage(Connection connection, User user, String text)
    {
        if(connection==this.connection)
        {
            out.println(lineStart+user.getUsername()+": "+text);
            out.print(prompt);
        }
    }

    public void error(Connection connection, String text)
    {
        if(connection==this.connection)
        {
            out.println(lineStart+"* Error: "+text);
            out.print(prompt);
        }
    }

    public void userConnected(Connection connection, String username)
    {
        if(connection==this.connection)
        {
            out.println(lineStart+"* "+username+" connected");
            out.print(prompt);
            users = connection.getUserList();
        }
    }

    public void userDisconnected(Connection connection, String username)
    {
        if(connection==this.connection)
        {
            out.println(lineStart+"* Error: "+username+" connected");
            out.print(prompt);
            users = connection.getUserList();
        }
    }

    public void disconnected(Connection connection)
    {
        out.println("Disconnected");
        controller.unregister(this,connection);
        connection = null;
    }

    private void inputLoop()
    {
        try
        {
            synchronized(this)
            {
                while(!hasController)
                {
                    wait();
                }
            }
            Connection[] connections = controller.getConnections();
            if(connections==null || connections.length<1 || connections[0]==null)
            {
                connection = askForConnection();
            }
            else
            {
                connection = connections[0];
                users = connection.getUserList();
                connected = true;
            }
            controller.register(this,connection);
            myUser = connection.getMyUser();
            synchronized(connected)
            {
                while(!connected)
                {
                    connected.wait();
                }
            }
            for(;;)
            {
                String message = in.readLine();
                if(!connected)
                {
                    break;
                }
                if(message.equals("/quit"))
                {
                    controller.unregister(this,connection);
                    break;
                }
                if(message.equals("/users"))
                {
                    printUsers();
                }
                if(message.equals("/disconnect"))
                {
                    connection.disconnect();
                    controller.unregister(this,connection);
                }
                connection.sendMessage(message);
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        catch(IOException e)
        {
            if(controller!=null)
            {
                controller.removeView(this);
                if(connection!=null)
                {
                    controller.unregister(this,connection);
                }
            }
        }
        if(controller!=null)
        {
            controller.removeView(this);
        }
    }

    private Connection askForConnection() throws IOException
    {
        out.print("Enter server address: ");
        InetAddress address = null;
        while(address==null)
        {
            try
            {
                address = InetAddress.getByName(in.readLine());
            }
            catch(UnknownHostException e)
            {
                out.print("Wrong server address. Try again: ");
                address = null;
            }
        }
        out.print("Enter server port: ");
        int port = 0;
        while(port==0)
        {
            boolean correct;
            try
            {
                port = Integer.parseInt(in.readLine());
                correct = (port>0 && port<65536);
            }
            catch(NumberFormatException e)
            {
                correct = false;
            }
            if(!correct)
            {
                out.println("Wrong server port. Try again: ");
            }
        }
        out.print("Enter your nickname: ");
        String nickname = in.readLine();
        Connection connection = controller.connect(address,port,nickname);
        return connection;
    }
}
