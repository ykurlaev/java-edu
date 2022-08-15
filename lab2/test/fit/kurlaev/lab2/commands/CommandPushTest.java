package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class CommandPushTest
{
    @Test
    public void pushNumber() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPush();
        String[] arguments = { "2" };
        command.exec(context, arguments);
        double a = context.popFromStack();
        assertTrue(2==a);
    }

    @Test
    public void pushVariable() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPush();
        String[] arguments = { "a" };
        context.setVariable("a",2);
        command.exec(context, arguments);
        double a = context.popFromStack();
        assertTrue(2==a);
    }

    @Test
    public void pushVarFail() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPush();
        String[] arguments = { "a" };
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.UnknownVariableException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void pushEpicFail() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPush();
        String[] arguments = { "#!@" };
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
    public void pushNotEnoughArgs() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPush();
        String[] arguments = { };
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
