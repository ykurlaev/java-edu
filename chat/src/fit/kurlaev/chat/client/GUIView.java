package fit.kurlaev.chat.client;

public interface GUIView
{
    public void setController(GUIController controller);
    public void connecting(Connection connection);
    public void connected(Connection connection);
    public void newMessage(Connection connection, User user, String text);
    public void error(Connection connection, String text);
    public void userConnected(Connection connection, String username);
    public void userDisconnected(Connection connection, String username);
    public void disconnected(Connection connection);
}
