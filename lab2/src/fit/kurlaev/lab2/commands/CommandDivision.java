package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.util.logging.Logger;

public class CommandDivision implements Command
{
    public void exec(Context context, String[] arguments) throws Calculator.CommandException
    {
        if(0<arguments.length)
        {
            Logger.getLogger("Calculator").info("CommandsDivision: extra arguments");
        }
        Double a = context.popFromStack();
        if(null==a)
        {
            Logger.getLogger("Calculator").warning("CommandDivision: stack is empty");
            throw new Calculator.EmptyStackException();
        }
        Double b = context.popFromStack();
        if(null==b)
        {
            context.pushToStack(a);
            Logger.getLogger("Calculator").warning("CommandDivision: stack is empty");
            throw new Calculator.EmptyStackException();
        }
        Double d;
        d = a/b;
        if(d.isInfinite() || d.isNaN())
        {
            Logger.getLogger("Calculator").warning("CommandDivision: division by zero");
            throw new Calculator.DivisionByZeroException();
        }
        context.pushToStack(d);
    }
}
