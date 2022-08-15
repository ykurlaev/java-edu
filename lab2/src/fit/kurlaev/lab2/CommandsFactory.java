package fit.kurlaev.lab2;

import java.io.*;
import java.util.HashMap;

public class CommandsFactory
{
    private HashMap<String,String> availableCommands;

    public class ConfigParserException extends Throwable {};

    private HashMap<String,String> parseConfig(BufferedReader reader) throws IOException,ConfigParserException
    {
        HashMap<String,String> map = new HashMap<String,String>();
        while(reader.ready())
        {
            String string = reader.readLine();
            String strings[] = string.split(" ");
            if(2!=strings.length)
            {
                throw new ConfigParserException();
            }
            map.put(strings[0],strings[1]);
        }
        return map;
    }

    public CommandsFactory() throws IOException,ConfigParserException
    {
        InputStream stream = getClass().getResourceAsStream("commands.cfg");
        try
        {
            Reader reader = new InputStreamReader(stream);
            try
            {
                BufferedReader buffered = new BufferedReader(reader);
                availableCommands = parseConfig(buffered);
            }
            finally
            {
                reader.close();
            }
        }
        finally
        {
          stream.close();
        }
    }

    public Command create(String key)
    {
        String className = availableCommands.get(key);
        if(null==className)
        {
            return null;
        }
        Class commandClass;
        try
        {
            commandClass = Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            return null;
        }
        Command command = null;
        try
        {
            command = (Command)commandClass.newInstance();
        }
        catch(InstantiationException e)
        {
            return null;
        }
        catch(IllegalAccessException e)
        {
            return null;
        }
        return command;
    }
}
