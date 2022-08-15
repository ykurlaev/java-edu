package fit.kurlaev.lab2.commands;

import fit.kurlaev.lab2.Calculator;
import fit.kurlaev.lab2.Command;
import fit.kurlaev.lab2.Context;

import java.io.IOException;
import java.util.logging.Logger;

public class CommandDefine implements Command
{
    public void exec(Context context, String[] arguments) throws IOException, Calculator.CommandException
    {
        if(arguments.length<2)
        {
            Logger.getLogger("Calculator").warning("CommandDefine: not enough arguments");
            throw new Calculator.NotEnoughArgumentsException();
        }
        if(arguments.length>2)
        {
            Logger.getLogger("Calculator").info("CommandDefine: extra arguments");
        }
        double d;
        try
        {
            d = Double.parseDouble(arguments[1]);
        }
        catch(NumberFormatException e)
        {
            Logger.getLogger("Calculator").warning(String.format("CommandDefine: '%s' is not valid number",arguments[1]));
            throw new Calculator.BadNumberException();
        }
        if(!arguments[0].matches("[A-Za-z]\\w*"))
        {
            Logger.getLogger("Calculator").warning(String.format("CommandDefine: '%s' is not valid variable name",arguments[0]));
            throw new Calculator.BadVariableNameException();
        }
        context.setVariable(arguments[0],d);
    }
}
