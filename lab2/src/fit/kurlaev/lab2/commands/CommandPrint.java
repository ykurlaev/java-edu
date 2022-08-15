package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.io.IOException;
import java.util.logging.Logger;

public class CommandPrint implements Command
{
    public void exec(Context context, String[] arguments) throws Calculator.CommandException, IOException
    {
        if(arguments.length>0)
        {
            Logger.getLogger("Calculator").info("CommandPrint: extra arguments");
        }
        Double d = context.popFromStack();
        if(null==d)
        {
            Logger.getLogger("Calculator").warning("CommandPrint: stack is empty");
            throw new Calculator.EmptyStackException();
        }
        context.pushToStack(d);
        context.output(d);
    }
}
