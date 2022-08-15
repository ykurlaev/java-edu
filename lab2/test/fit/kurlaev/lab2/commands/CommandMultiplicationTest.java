package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class CommandMultiplicationTest
{
        @Test
        public void multiplicationOk() throws Calculator.CommandException, IOException
        {
            Context context = new Context(null);
            Command command = new CommandMultiplication();
            String[] arguments = { };
            context.pushToStack(4);
            context.pushToStack(2);
            command.exec(context, arguments);
            double a = context.popFromStack();
            assertTrue(8==a);
        }

        @Test
        public void multiplicationEmptyOne() throws Calculator.CommandException, IOException
        {
            Context context = new Context(null);
            Command command = new CommandMultiplication();
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
        public void multiplicationEmptyBoth() throws Calculator.CommandException, IOException
        {
            Context context = new Context(null);
            Command command = new CommandMultiplication();
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
