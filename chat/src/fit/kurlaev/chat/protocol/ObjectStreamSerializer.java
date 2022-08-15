package fit.kurlaev.chat.protocol;

import java.io.*;

public class ObjectStreamSerializer implements Serializer
{
    public byte[] serialize(Message message)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream stream = new ObjectOutputStream(byteStream);
            stream.writeObject(message);
        }
        catch(IOException e)
        {
            e.printStackTrace(); //Is it right?..
        }
        return byteStream.toByteArray();
    }

    public Message deserialize(byte[] byteArray)
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
        try
        {
            ObjectInputStream stream = new ObjectInputStream(byteStream);
            Object o = stream.readObject();
            if(o instanceof Message)
            {
                return (Message)o;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace(); //?..
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
