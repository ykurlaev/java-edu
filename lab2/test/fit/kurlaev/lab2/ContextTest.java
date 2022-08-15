package fit.kurlaev.lab2;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.junit.Assert.assertTrue;

public class ContextTest
{
    @Test
    public void nullVariable()
    {
        Context context = new Context(null);
        Double a = context.getVariable("aaa");
        assertTrue(null==a);
    }

    @Test
    public void variable()
    {
        Context context = new Context(null);
        context.setVariable("aaa",3);
        Double a = context.getVariable("aaa");
        assertTrue(3==a.doubleValue());
    }

    @Test
    public void nullStack()
    {
        Context context = new Context(null);
        Double a = context.popFromStack();
        assertTrue(null==a);
    }

    @Test
    public void stack()
    {
        Context context = new Context(null);
        context.pushToStack(3);
        Double a = context.popFromStack();
        assertTrue(3==a.doubleValue());
    }

    @Test
    public void outputNull() throws IOException
    {
        Context context = new Context(null);
        context.output(3.);
    }

    @Test
    public void output() throws IOException
    {
        Context context = new Context(System.out);
        context.output(3.);
    }
}
