package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.util.logging.Logger;

public class CommandPush implements Command
{
    public void exec(Context context, String[] arguments) throws Calculator.CommandException
    {
        if(arguments.length>1)
        {
            Logger.getLogger("Calculator").info("CommandPush: extra arguments");
        }
        if(arguments.length<1)
        {
            Logger.getLogger("Calculator").warning("CommandPush: not enough arguments");
            throw new Calculator.NotEnoughArgumentsException();
        }
        Double d;
        if(arguments[0].matches("[A-Za-z]\\w*"))
        {
            d = context.getVariable(arguments[0]);
            if(null == d)
            {
                Logger.getLogger("Calculator").warning(String.format("CommandPush: no such variable '%s'", arguments[0]));
                throw new Calculator.UnknownVariableException();
            }
        }
        else
        {
            try
            {
                d = Double.parseDouble(arguments[0]);
            }
            catch(NumberFormatException e)
            {
                Logger.getLogger("Calculator").warning(String.format("CommandPush: '%s' is not valid number or variable name",arguments[0]));
                throw new Calculator.BadNumberException();
            }
        }
        context.pushToStack(d);
    }
}
