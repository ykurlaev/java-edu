package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CommandPopTest
{
    @Test
    public void popToVariable() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPop();
        String[] arguments = { "a" };
        context.pushToStack(2);
        command.exec(context, arguments);
        double a = context.getVariable("a");
        assertTrue(2==a);
    }

    @Test
    public void popToVoid() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPop();
        String[] arguments = { };
        context.pushToStack(2);
        command.exec(context, arguments);
        assertTrue(true);
    }

    @Test
    public void popVarFail() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPop();
        String[] arguments = { "#%!" };
        context.pushToStack(2);
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
    public void popEmpty() throws Calculator.CommandException, IOException
    {
        Context context = new Context(null);
        Command command = new CommandPop();
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
