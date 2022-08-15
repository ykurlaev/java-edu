package fit.kurlaev.lab1;

import java.io.*;
import java.util.Iterator;

public class Main
{
    private static StringContainer buildContainer(WordReader reader) throws IOException
    {
        if(null==reader)
        {
            throw new NullPointerException();
        }
        final StringContainer container = new StringContainer();
        String next;
        do
        {
            next = reader.readNext();
            if(null!=next)
            {
                container.add(next);
            }
        }
        while(null!=next);
        return container;
    }

    private static void writeCSV(StringContainer container,Writer writer,String delimiter,String stringDelimiter)
            throws IOException
    {
        if(null==container || null==writer || null==delimiter)
        {
            throw new NullPointerException();
        }
        final CSVFormatter formatter = new CSVFormatter(delimiter,stringDelimiter);
        final Iterator<Element> elements = container.getDescending();
        while(elements.hasNext())
        {
            String next = formatter.format(elements.next());
            writer.write(next);
        }
    }

    public static void main(String args[])
    {
        if(null==args)
        {
            return;
        }
        String filenames[];
        if(0==args.length)
        {
            filenames = new String[1]; // :?
        }
        else
        {
            filenames = args;
        }
        final Writer writer = new OutputStreamWriter(System.out);
        try
        {
            for(String filename : filenames)
            {
                InputStream stream;
                StringContainer container = null;
                if(null!=filename)
                {
                    stream = new FileInputStream(filename);
                }
                else
                {
                    stream = System.in;
                }
                try
                {
                    final InputStreamReader streamReader = new InputStreamReader(stream);
                    try
                    {
                        final WordReader wordReader = new WordReader(streamReader);
                        container = buildContainer(wordReader);
                    }
                    finally
                    {
                        try
                        {
                            streamReader.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace(System.err);
                        }
                    }
                }
                catch(IOException e)
                {
                    System.err.println("Error reading input file: " + e.getLocalizedMessage());
                    continue;
                }
                finally
                {
                    try
                    {
                        stream.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace(System.err);
                    }
                }
                writeCSV(container,writer," ",System.getProperty("line.separator"));
            }
        }
        catch(IOException e)
        {
            System.err.println("Error writing output file: " + e.getLocalizedMessage());
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
        }
    }
}
