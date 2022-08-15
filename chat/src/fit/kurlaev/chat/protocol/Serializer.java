package fit.kurlaev.chat.protocol;

public interface Serializer
{
    public byte[] serialize(Message message);
    public Message deserialize(byte[] byteArray);
}
