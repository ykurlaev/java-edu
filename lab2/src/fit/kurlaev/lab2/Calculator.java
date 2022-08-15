package fit.kurlaev.lab2;

import java.io.*;
import java.util.logging.Logger;

public class Calculator
{
    static public class CalculatorException extends Throwable {};
    static public class InitializationException extends CalculatorException {};
    static public class UnknownCommandException extends CalculatorException {};
    static public class CommandException extends CalculatorException {};
    static public class BadArgumentsException extends CommandException {};
    static public class NotEnoughArgumentsException extends BadArgumentsException {};
    static public class BadVariableNameException extends BadArgumentsException {};
    static public class BadNumberException extends BadArgumentsException {};
    static public class UnknownVariableException extends CommandException {};
    static public class DivisionByZeroException extends CommandException {};
    static public class NotRealResultException extends CommandException {};
    static public class EmptyStackException extends CommandException {};

    public Calculator(Reader reader,PrintStream printStream) throws IOException, InitializationException
    {
        Logger.getLogger("Calculator").fine("Creating factory");
        try
        {
            _commandsFactory = new CommandsFactory();
        }
        catch(CommandsFactory.ConfigParserException e)
        {
            Logger.getLogger("Calcualtor").severe("Factory config parser error");
            InitializationException t = new InitializationException();
            t.initCause(e);
            throw t;
        }
        Logger.getLogger("Calculator").fine("Creating parser");
        _parser = new Parser();
        Logger.getLogger("Calculator").fine("Creating context");
        _context = new Context(printStream);
        _reader = new BufferedReader(reader);
    }

    public boolean doNext() throws IOException, CalculatorException
    {
        String string = _reader.readLine();
        if(null==string)
        {
            return false;
        }
        Parser.Token token = _parser.ParseLine(string);
        if(Parser.TokenType.Normal==token.type())
        {
            Command command = _commandsFactory.create(token.name());
            if(null==command)
            {
                throw new UnknownCommandException();
            }
            command.exec(_context, token.content());
        }
        return true;
    }

    private CommandsFactory _commandsFactory;
    private Parser _parser;
    private Context _context;
    private BufferedReader _reader;
}
