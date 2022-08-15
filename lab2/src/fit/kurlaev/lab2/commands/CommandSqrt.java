package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.util.logging.Logger;

public class CommandSqrt implements Command
{
    public void exec(Context context, String[] arguments) throws Calculator.CommandException
    {
        Double d = context.popFromStack();
        if(null==d)
        {
            Logger.getLogger("Calculator").warning("CommandSqrt: stack is empty");
            throw new Calculator.EmptyStackException();
        }
        if(d<0)
        {
            Logger.getLogger("Calculator").warning("CommandSqrt: square root from negative value");
            throw new Calculator.NotRealResultException();
        }
        d = Math.sqrt(d);
        context.pushToStack(d);
    }
}
