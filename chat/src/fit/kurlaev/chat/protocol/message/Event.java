package fit.kurlaev.chat.protocol.message;

import fit.kurlaev.chat.protocol.Message;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class Event extends Message
{
    @XmlAttribute(name = "name", required = true)
    private String eventType = null;
    @XmlElement(name = "message")
    private String text = null;
    @XmlElement(name = "name", required = true)
    private String username = null;

    public enum Type
    {
        UNKNOWN, MESSAGE, USERLOGIN, USERLOGOUT
    }

    public Type getType()
    {
        if(eventType!=null && username!=null)
        {
            if(eventType.equals("message") && text!=null)
            {
                return Type.MESSAGE;
            }
            if(eventType.equals("userlogin"))
            {
                return Type.USERLOGIN;
            }
            if(eventType.equals("userlogout"))
            {
                return Type.USERLOGOUT;
            }
        }
        return Type.UNKNOWN;
    }

    public String getText()
    {
        return text;
    }

    public String getUsername()
    {
        return username;
    }

    static public Event message(String text, String username)
    {
        Event event = new Event();
        event.eventType = "message";
        event.text = text;
        event.username = username;
        return event;
    }

    static public Event userlogin(String username)
    {
        Event event = new Event();
        event.eventType = "userlogin";
        event.username = username;
        return event;
    }

    static public Event userlogout(String username)
    {
        Event event = new Event();
        event.eventType = "userlogout";
        event.username = username;
        return event;
    }

    @Override
    public String toString()
    {
        return "Event{" +
                "type='" + getType() + '\'' +
                ", username='" + username + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
