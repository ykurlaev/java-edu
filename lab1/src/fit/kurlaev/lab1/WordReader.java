package fit.kurlaev.lab1;

import java.io.IOException;
import java.io.Reader;

public class WordReader
{
    final private Reader reader;

    public WordReader(Reader in)
    {
        if(null==in)
        {
            throw new NullPointerException();
        }
        reader = in;
    }

    public String readNext() throws IOException
    {
        final StringBuilder builder = new StringBuilder("");
        final char buffer[] = new char[1];
        Character next;
        if(reader.ready())
        {
            do
            {
                reader.read(buffer);
                next = buffer[0];
            }
            while(!Character.isLetterOrDigit(next) && reader.ready());
            if(!reader.ready())
            {
                return null;
            }
            do
            {
                builder.append(next);
                reader.read(buffer);
                next = buffer[0];
            }
            while(Character.isLetterOrDigit(next) && reader.ready());
            return builder.toString();
        }
        return null;
    }
}
