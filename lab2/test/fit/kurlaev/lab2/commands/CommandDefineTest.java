package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class CommandDefineTest
{
    @Test
    public void defineOk() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDefine();
        String[] arguments = {"a","2"};
        command.exec(context, arguments);
        double a = context.getVariable("a");
        assertTrue(2==a);
    }

    @Test
    public void defineBadVarName() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDefine();
        String[] arguments = {"3","2"};
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.BadVariableNameException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void defineBadNumber() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDefine();
        String[] arguments = {"a","a"};
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.BadNumberException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void defineNotEnoughArgs() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDefine();
        String[] arguments = {"a"};
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.NotEnoughArgumentsException e)
        {
            assertTrue(true);
        }
    }
}
