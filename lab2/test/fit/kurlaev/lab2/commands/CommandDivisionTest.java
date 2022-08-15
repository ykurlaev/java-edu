package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class CommandDivisionTest
{
    @Test
    public void divisionOk() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDivision();
        String[] arguments = { };
        context.pushToStack(4);
        context.pushToStack(2);
        command.exec(context, arguments);
        double a = context.popFromStack();
        assertTrue(0.5==a);
    }

    @Test
    public void divisionEmptyOne() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDivision();
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
    public void divisionEmptyBoth() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDivision();
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

    @Test
    public void divisionByZero() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandDivision();
        String[] arguments = { };
        context.pushToStack(0);
        context.pushToStack(2);
        try
        {
            command.exec(context, arguments);
        }
        catch(Calculator.DivisionByZeroException e)
        {
            assertTrue(true);
        }
    }
}
