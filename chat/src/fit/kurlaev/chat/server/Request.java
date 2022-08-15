package fit.kurlaev.chat.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request
{
    private Client client;
    private byte[] byteArray;
    private boolean toDisconnect = false;
    int lengthBytes = 0;
    byte[] lengthBuffer = new byte[4];
    int already = 0;

    public Request(Client client)
    {
        this.client = client;
        this.byteArray =null;
    }

    public Request(Client client, byte[] byteArray)
    {
        this.client = client;
        this.byteArray = byteArray;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(4);
        DataOutputStream stream = new DataOutputStream(byteStream);
        try
        {
            stream.writeInt(byteArray.length);
            lengthBuffer = byteStream.toByteArray();
        }
        catch(IOException e)
        {
            e.printStackTrace(); //cannot...
        }
    }

    public void setByteArray(byte[] byteArray)
    {
        this.byteArray = byteArray;
    }

    public byte[] getByteArray()
    {
        return byteArray;
    }

    public Client getClient()
    {
        return client;
    }

    public boolean isToDisconnect()
    {
        return toDisconnect;
    }

    public void setToDisconnect()
    {
        this.toDisconnect = true;
    }
}
