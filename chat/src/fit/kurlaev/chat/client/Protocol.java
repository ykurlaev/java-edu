package fit.kurlaev.chat.client;

import java.io.IOException;
import java.net.InetAddress;

public interface Protocol
{
    public Connection connect(InetAddress address, int port, String username, String clientName) throws IOException;
}
