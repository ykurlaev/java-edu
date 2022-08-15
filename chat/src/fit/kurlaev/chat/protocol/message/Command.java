package fit.kurlaev.chat.protocol.message;

import fit.kurlaev.chat.protocol.Message;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "command")
public class Command extends Message
{
    @XmlAttribute(name = "name", required = true)
    private String commandType = null;
    @XmlElement(name = "name")
    private String username = null;
    @XmlElement(name = "type")
    private String clientName = null;
    @XmlElement(name = "message")
    private String text = null;
    @XmlElement(name = "session")
    private String session = null;

    public enum Type
    {
        UNKNOWN, LOGIN, LIST, MESSAGE, LOGOUT
    }

    public Type getType()
    {
        if(commandType!=null)
        {
            if(commandType.equals("login") && username != null && clientName != null)
            {
                return Type.LOGIN;
            }
            if(commandType.equals("list") && session != null)
            {
                return Type.LIST;
            }
            if(commandType.equals("message") && text != null && session != null)
            {
                return Type.MESSAGE;
            }
            if(commandType.equals("logout") && session != null)
            {
                return Type.LOGOUT;
            }
        }
        return Type.UNKNOWN;
    }

    public String getUsername()
    {
        return username;
    }

    public String getClientName()
    {
        return clientName;
    }

    public String getSession()
    {
        return session;
    }

    public String getText()
    {
        return text;
    }

    static public Command login(String username, String clientName)
    {
        Command command = new Command();
        command.commandType = "login";
        command.username = username;
        command.clientName = clientName;
        return command;
    }

    static public Command list(String session)
    {
        Command command = new Command();
        command.commandType = "list";
        command.session = session;
        return command;
    }

    static public Command message(String text, String session)
    {
        Command command = new Command();
        command.commandType = "message";
        command.text = text;
        command.session = session;
        return command;
    }

    static public Command logout(String session)
    {
        Command command = new Command();
        command.commandType = "logout";
        command.session = session;
        return command;
    }

    @Override
    public String toString()
    {
        return "Command{" +
                "type='" + getType() + '\'' +
                ", username='" + username + '\'' +
                ", clientName='" + clientName + '\'' +
                ", session='" + session + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
