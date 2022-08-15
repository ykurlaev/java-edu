package fit.kurlaev.chat.client;

import java.io.IOException;
import java.net.InetAddress;

public interface GUIController
{
    public void addView(GUIView view);
    public void removeView(GUIView view);
    public Connection[] getConnections();
    public Connection connect(InetAddress address, int port, String nickname) throws IOException;
    public void register(GUIView view, Connection connection);
    public void unregister(GUIView view, Connection connection);
}
