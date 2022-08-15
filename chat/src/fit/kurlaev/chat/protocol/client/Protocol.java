package fit.kurlaev.chat.protocol.client;

import fit.kurlaev.chat.protocol.Serializer;
import fit.kurlaev.chat.protocol.XMLSerializer;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.InetAddress;

public class Protocol implements fit.kurlaev.chat.client.Protocol
{
    private Serializer serializer = null;

    public Protocol(Serializer serializer)
    {
        this.serializer = serializer;
    }

    public Connection connect(InetAddress address, int port, String username, String clientName) throws IOException
    {
        try
        {
            serializer = new XMLSerializer();
        }
        catch(JAXBException e)
        {
            e.printStackTrace();
        }
        return new Connection(address, port, username, clientName, serializer);
    }
}
