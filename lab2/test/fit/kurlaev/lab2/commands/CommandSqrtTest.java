package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CommandSqrtTest
{
    @Test
    public void sqrtOk() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandSqrt();
        String[] arguments = { };
        context.pushToStack(4);
        command.exec(context, arguments);
        double a = context.popFromStack();
        assertTrue(2==a);
    }

    @Test
    public void sqrtNegative() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandSqrt();
        String[] arguments = { };
        context.pushToStack(-4);
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.NotRealResultException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void sqrtEmpty() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandSqrt();
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
