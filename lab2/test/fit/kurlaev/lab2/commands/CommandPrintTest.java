package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CommandPrintTest
{
    @Test
    public void printOk() throws Calculator.CommandException, IOException
    {
        Context context = new Context(System.out);
        context.pushToStack(4);
        String[] arguments = { };
        Command command = new CommandPrint();
        command.exec(context, arguments);
    }

    @Test
    public void printEmpty() throws Calculator.CommandException, IOException
    {
        Context context = new Context(System.out);
        String[] arguments = { };
        Command command = new CommandPrint();
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
