package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class CommandAdditionTest
{
    @Test
    public void additionOk() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandAddition();
        String[] arguments = { };
        context.pushToStack(4);
        context.pushToStack(2);
        command.exec(context, arguments);
        double a = context.popFromStack();
        assertTrue(6==a);
    }

    @Test
    public void additionEmptyOne() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandAddition();
        String[] arguments = { };
        context.pushToStack(4);
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.EmptyStackException e)
        {
            assertTrue(4==context.popFromStack());
        }
    }

    @Test
    public void additionEmptyBoth() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandAddition();
        String[] arguments = { };
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.EmptyStackException e)
        {
            assertTrue(true);
        }
    }
}
