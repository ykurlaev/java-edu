package fit.kurlaev.lab2;

import java.io.*;

public class Main
{
    public static void main(String args[])
    {
        Reader reader;
        boolean streamIsStdIn = true;
        InputStream stream = null;
        if(args.length>=1)
        {
            streamIsStdIn = false;
            try
            {
                stream = new FileInputStream(args[0]);
            }
            catch(FileNotFoundException e)
            {
                System.err.print("Can't open input file"+e.getLocalizedMessage());
            }
        }
        else
        {
            stream = System.in;
        }
        try
        {
            reader = new InputStreamReader(stream);
            try
            {
                try
                {
                    Calculator calculator = new Calculator(reader,System.out);
                    while(true)
                    {
                        try
                        {
                            calculator.doNext();
                        }
                        catch(Calculator.UnknownCommandException e)
                        {
                            System.err.print("No such command! Try again\n");
                        }
                        catch(Calculator.BadArgumentsException e)
                        {
                            System.err.print("Bad command arguments! Try again\n");
                        }
                        catch(Calculator.UnknownVariableException e)
                        {
                            System.err.print("No such variable! Try again\n");
                        }
                        catch(Calculator.DivisionByZeroException e)
                        {
                            System.err.print("Division by zero! Try again\n");
                        }
                        catch(Calculator.NotRealResultException e)
                        {
                            System.err.print("Result is not real number! Try again\n");
                        }
                        catch(Calculator.EmptyStackException e)
                        {
                            System.err.print("Stack is empty! Try again\n");
                        }
                        catch(Calculator.CalculatorException e)
                        {
                            System.err.print("Error! Try again\n");
                        }
                    }
                }
                catch(Calculator.InitializationException e)
                {
                    System.err.print("Error initializing calculator\n");
                }
            }
            catch(IOException e)
            {
                System.err.print("IOError" + e.getLocalizedMessage());
            }
            finally
            {
                if(!streamIsStdIn)
                {
                    reader.close();
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(!streamIsStdIn)
            {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
