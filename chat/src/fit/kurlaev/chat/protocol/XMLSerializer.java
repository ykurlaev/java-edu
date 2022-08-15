package fit.kurlaev.chat.protocol;

import fit.kurlaev.chat.protocol.message.*;
import fit.kurlaev.chat.protocol.message.Error;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

public class XMLSerializer implements Serializer
{
    private Marshaller marshaller = null;
    private Unmarshaller unmarshaller = null;

    public XMLSerializer() throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(Command.class, Error.class, Event.class, Success.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        unmarshaller = context.createUnmarshaller();
    }

    public byte[] serialize(Message message)
    {
        Logger.getAnonymousLogger().info("Serializing message: "+message);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try
        {
            marshaller.marshal(message,stream);
        }
        catch(JAXBException e)
        {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    public Message deserialize(byte[] byteArray)
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
        Message message = null;
        try
        {
            message = (Message)unmarshaller.unmarshal(stream);
        }
        catch(JAXBException e)
        {
            e.printStackTrace();
        }
        Logger.getAnonymousLogger().info("Deserialized message: "+message);
        return message;
    }
}
