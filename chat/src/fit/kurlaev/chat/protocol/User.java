package fit.kurlaev.chat.protocol;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType
public class User implements fit.kurlaev.chat.client.User, Serializable
{
    @XmlElement(name = "name", required = true)
    private String username = null;
    @XmlElement(name = "type", required = true)
    private String clientName = null;

    public User() { }

    public User(String username, String clientName)
    {
        this.username = username;
        this.clientName = clientName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getClientName()
    {
        return clientName;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "username='" + username + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
