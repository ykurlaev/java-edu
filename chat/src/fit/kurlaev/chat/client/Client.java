package fit.kurlaev.chat.client;

import fit.kurlaev.chat.client.Swing.SwingView;
import fit.kurlaev.chat.protocol.XMLSerializer;
import fit.kurlaev.chat.protocol.client.Protocol;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.util.logging.Logger;

public class Client
{
    public static void main(String args[]) throws JAXBException
    {
        try
        {
            if (System.getProperty("os.name").equals("Windows"))
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            else
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
        }
        catch (IllegalAccessException e) { }
        catch (InstantiationException e) { }
        catch (UnsupportedLookAndFeelException e) { }
        catch (ClassNotFoundException e) { }
        GUIView view = new SwingView();
        Protocol protocol = new Protocol(new XMLSerializer());
        Controller controller = new Controller(protocol);
        controller.addView(view);
        Logger.getLogger("Client").info("Client initialized");
    }
}
