package fit.kurlaev.lab1;

public class CSVFormatter
{
    private final String delimiter;
    private final String stringDelimiter;

    public CSVFormatter(String _delimiter,String _stringDelimiter)
    {
        if(null==_delimiter)
        {
            delimiter=" ";
        }
        else
        {
            delimiter=_delimiter;
        }
        if(null==_stringDelimiter)
        {
            stringDelimiter=" ";
        }
        else
        {
            stringDelimiter=_stringDelimiter;
        }
    }

    public String format(Element element)
    {
        final StringBuilder sb = new StringBuilder();
        final int fieldCount = element.fieldCount();
        for(int i=0;i<fieldCount;++i)
        {
            String nextField=element.getField(i);
            if(null==nextField)
            {
                nextField="";
            }
            sb.append(nextField);
            if(i!=fieldCount-1)
            {
                sb.append(delimiter);
            }
        }
        sb.append(stringDelimiter);
        return sb.toString();
    }
}
