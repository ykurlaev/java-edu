package fit.kurlaev.chat.protocol.message;

import fit.kurlaev.chat.protocol.Message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class Error extends Message
{
    @XmlElement(name = "message")
    private String reason;

    public String getReason()
    {
        return reason;
    }

    public static Error error(String reason)
    {
        Error error = new Error();
        error.reason = reason;
        return error;
    }

    @Override
    public String toString()
    {
        return "Error{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
