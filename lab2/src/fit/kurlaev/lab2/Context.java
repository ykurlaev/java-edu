package fit.kurlaev.lab2;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;
import java.util.TreeMap;

public class Context
{
    public Context(PrintStream printStream)
    {
        _variables = new TreeMap<String,Double>();
        _stack = new Stack<Double>();
        _printStream = printStream;
    }

    public void setVariable(String name, double value)
    {
        _variables.put(name, value);
    }

    public Double getVariable(String name)
    {
        if(_variables.containsKey(name))
        {
            return new Double(_variables.get(name));
        }
        else
        {
            return null;
        }
    }

    public void pushToStack(double value)
    {
        _stack.push(value);
    }

    public Double popFromStack()
    {
        if(_stack.empty())
        {
            return null;
        }
        else
        {
            return new Double(_stack.pop());
        }
    }

    public void output(Double value) throws IOException
    {
        if(null!=_printStream)
        {
            _printStream.append(value+"\n");
        }
    }

    private TreeMap<String,Double> _variables;
    private Stack<Double> _stack;
    private PrintStream _printStream;
}
