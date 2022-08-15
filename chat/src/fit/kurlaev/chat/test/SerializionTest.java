package fit.kurlaev.chat.test;

import fit.kurlaev.chat.protocol.*;
import fit.kurlaev.chat.protocol.message.*;

import javax.xml.bind.JAXBException;

public class SerializionTest
{
    public static void main(String[] args)
    {
        Serializer serializer = null;
        try
        {
            String id = "000000";
            Message login = Command.login("MyUsername", "MyClient");
            Message session = Success.session(id);
            Message listUsers = Command.list(id);
            User[] users = {new User("User1","Client1"), new User("User2","Client2"),
                            new User("MyUsername","MyClient"), new User("ЙЦУКЕН","侲")};
            Message userList = Success.userlist(users);
            Message emptyUserList = Success.userlist(new User[0]);
            Message outMessage = Command.message("This is the message from client to server",id);
            Message success = Success.empty();
            Message error = fit.kurlaev.chat.protocol.message.Error.error("An error occured!");
            Message inMessage = Event.message("This is message from server to client","User2");
            Message userLogin = Event.userlogin("User3");
            Message userLogout = Event.userlogout("User1");
            Message logout = Command.logout(id);
            Message bad = Event.userlogin(null);
            Message[] messages = {login, session, listUsers, userList, emptyUserList, outMessage,
                                  success, error, inMessage, userLogin, userLogout, logout, bad};
            System.out.println("XML:");
            serializer = new XMLSerializer();
            for(Message message : messages)
            {
                System.out.println("Before: "+message);
                byte[] byteArray = serializer.serialize(message);
                String s = new String(byteArray);
                System.out.println("In between: "+s);
                Message after = serializer.deserialize(byteArray);
                System.out.println("After: "+after);
            }
            byte[] bytes = "<success></success>".getBytes();
            Message msg = serializer.deserialize(bytes);
            System.out.println(msg);
            System.out.println("ObjectStream");
            serializer = new ObjectStreamSerializer();
            for(Message message : messages)
            {
                System.out.println("Before: "+message);
                byte[] byteArray = serializer.serialize(message);
                Message after = serializer.deserialize(byteArray);
                System.out.println("After: "+after);
            }
        }
        catch(JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
