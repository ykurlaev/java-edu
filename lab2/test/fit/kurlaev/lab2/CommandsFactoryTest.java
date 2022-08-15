package fit.kurlaev.lab2;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CommandsFactoryTest
{
    @Test
    public void testCreateFail() throws IOException, CommandsFactory.ConfigParserException
    {
        CommandsFactory factory;
        factory = new CommandsFactory();
        Command command = factory.create("LOL");
        assertTrue(null==command);
    }

    @Test
    public void testCreate() throws IOException, CommandsFactory.ConfigParserException
    {
        CommandsFactory factory;
        factory = new CommandsFactory();
        Command command = factory.create("PRINT");
        assertTrue(null!=command);
    }
}
