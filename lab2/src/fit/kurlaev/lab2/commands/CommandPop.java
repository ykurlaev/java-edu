package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.util.logging.Logger;

public class CommandPop implements Command
{
    public void exec(Context context, String[] arguments) throws Calculator.CommandException
    {
        if(arguments.length>1)
        {
            Logger.getLogger("Calculator").info("CommandPop: extra arguments");
        }
        Double d = context.popFromStack();
        if(null==d)
        {
            Logger.getLogger("Calculator").warning("CommandPop: stack is empty");
            throw new Calculator.EmptyStackException();
        }
        if(arguments.length>=1)
        {
            if(!arguments[0].matches("[A-Za-z]\\w*"))
            {
                context.pushToStack(d);
                Logger.getLogger("Calculator").warning(String.format("CommandPop: '%s' is not valid variable name",arguments[0]));
                throw new Calculator.BadVariableNameException();
            }
            context.setVariable(arguments[0],d);
        }
        else
        {
            Logger.getLogger("Calculator").info("CommandPop: number has been popped, but hasn't been stored");
        }
    }
}
