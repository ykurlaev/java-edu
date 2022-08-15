package fit.kurlaev.chat.server;

import fit.kurlaev.chat.protocol.Message;
import fit.kurlaev.chat.protocol.User;
import fit.kurlaev.chat.protocol.message.Command;
import fit.kurlaev.chat.protocol.message.Error;
import fit.kurlaev.chat.protocol.message.Event;
import fit.kurlaev.chat.protocol.message.Success;

import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

public class Client
{
    User user;
    private boolean loggedIn = false;
    private String session;
    private Collection<User> users;
    private Worker worker;
    private SocketChannel channel;

    public Client(Worker worker, Collection<User> users, SocketChannel channel)
    {
        this.worker = worker;
        this.users = users;
        this.channel = channel;
    }

    public SocketChannel getChannel()
    {
        return channel;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public Message processMessage(Message message)
    {
        Logger.getLogger("Server").info("New message: "+message);
        if(!(message instanceof Command))
        {
            if(loggedIn)
            {
                return Error.error("Protocol error");
            }
        }
        Command command = (Command)message;
        switch(command.getType())
        {
            case LOGIN:
                if(loggedIn)
                {
                    return Error.error("Already logged in");
                }
                String username = replaceSpecialChars(command.getUsername());
                String clientName = replaceSpecialChars(command.getClientName());
                if(!checkUsernameUnique(username))
                {
                    return Error.error("Username is already in use");
                }
                loggedIn = true;
                user = new User(username,clientName);
                users.add(user);
                session = UUID.randomUUID().toString();
                worker.broadcast(Event.userlogin(username));
                return Success.session(session);
           case LIST:
                if(!loggedIn)
                {
                    return null;
                }
                if(!command.getSession().equals(session))
                {
                    return Error.error("Incorrect session ID");
                }
                User[] userArray = new User[users.size()];
                int i = 0;
                for(User user : users)
                {
                    userArray[i] = user;
                    i++;
                }
                return Success.userlist(userArray);
           case MESSAGE:
               if(!loggedIn)
               {
                   return null;
               }
               worker.broadcast(Event.message(command.getText(),user.getUsername()));
               return Success.empty();
           case LOGOUT:
               if(!loggedIn)
               {
                   return null;
               }
               loggedIn = false;
               users.remove(user);
               worker.broadcast(Event.userlogout(user.getUsername()));
               return Success.empty();
            default:
            case UNKNOWN:
                if(loggedIn)
                {
                    return Error.error("Protocol error");
                }
                return null;
        }
    }

    private String replaceSpecialChars(String string)
    {
        return string.replaceAll("[\n\r]+"," ");
    }

    private boolean checkUsernameUnique(String username)
    {
        for(User user : users)
        {
            if(user.getUsername().equals(username))
            {
                return false;
            }
        }
        return true;
    }
}
