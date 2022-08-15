package fit.kurlaev.chat.protocol.message;

import fit.kurlaev.chat.protocol.Message;
import fit.kurlaev.chat.protocol.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Arrays;

@XmlRootElement(name = "success")
public class Success extends Message
{
    @XmlType
    static private class UserList implements Serializable
    {
        @XmlElement(name = "user")
        private User[] users = null;

        public UserList() { }

        public UserList(User[] users)
        {
            this.users = users;
        }

        public User[] getUsers()
        {
            return users;
        }

        @Override
        public String toString()
        {
            return "UserList{" +
                    "users=" + (users == null ? null : Arrays.asList(users)) +
                    '}';
        }
    }

    @XmlElement(name = "session")
    private String session = null;
    @XmlElement(name = "listusers")
    private UserList userList = null;

    public enum Type
    {
        UNKNOWN, EMPTY, SESSION, USERLIST
    }

    public Type getType()
    {
        if(session==null && userList==null)
        {
            return Type.EMPTY;
        }
        if(session!=null && userList==null)
        {
            return Type.SESSION;
        }
        if(session==null && userList!=null)
        {
            return Type.USERLIST;
        }
        return Type.UNKNOWN;
    }

    public String getSession()
    {
        return session;
    }

    public User[] getUsers()
    {
        if(userList == null)
        {
            return null;
        }
        return userList.getUsers();
    }

    static public Success empty()
    {
        Success success = new Success();
        return success;
    }

    static public Success session(String session)
    {
        Success success = new Success();
        success.session = session;
        return success;
    }

    static public Success userlist(User users[])
    {
        Success success = new Success();
        success.userList = new UserList(users);
        return success;
    }

    @Override
    public String toString()
    {
        return "Success{" +
                "type='" + getType() + '\'' +
                ", session='" + session + '\'' +
                ", userList=" + userList +
                '}';
    }
}
