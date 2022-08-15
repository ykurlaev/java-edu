package fit.kurlaev.chat.client;

import java.net.InetAddress;

public interface Connection
{
    public void addListener(ConnectionListener listener);
    public void removeListener(ConnectionListener listener);
    public InetAddress getAddress();
    public int getPort();
    public User getMyUser();
    public User[] getUserList();
    public void sendMessage(String text);
    public void disconnect();
}
