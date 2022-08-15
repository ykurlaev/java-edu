package fit.kurlaev.chat.client;

public interface ConnectionListener
{
    public void newMessage(User user, String text);
    public void userConnected(String username);
    public void userDisconnected(String username);
    public void connecting();
    public void connected();
    public void disconnected();
    public void error(String message);
}
