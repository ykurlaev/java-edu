package fit.kurlaev.lab1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class StringContainer
{
    private class FrequencyElement implements Element, Comparable<FrequencyElement>
    {
        private final int count;

        private final String string;

        private FrequencyElement(String _string,int _count)
        {
            count = _count;
            string = _string;
        }

        private FrequencyElement add()
        {
            return new FrequencyElement(string,count+1);
        }

        private Float frequency()
        {
            if(0!=allCount)
            {
                return (float)count/allCount;
            }
            else
            {
                return 1.0F;
            }
        }

        public int fieldCount()
        {
            return 3;
        }

        public String getField(int index)
        {
            switch(index)
            {
                case 0:
                    return string;
                case 1:
                    return String.format("%f",frequency());
                case 2:
                    return String.format("%.2f%%",frequency()*100);
                default:
                    return null;
            }
        }

        public int compareTo(FrequencyElement o)
        {
            if(this==o)
            {
                return 0;
            }
            if(null==string)
            {
                return 1;
            }
            if(null==o || null==o.string)
            {
                return -1;
            }
            int c=frequency().compareTo(o.frequency());
            if(0!=c)
            {
                return c;
            }
            return string.compareTo(o.string);
        }
    }

    private int allCount;
    private final HashMap<String,FrequencyElement> map;

    public StringContainer()
    {
        map = new HashMap<String,FrequencyElement>();
        allCount = 0;
    }

    public void add(String string)
    {
        if(null==string)
        {
            return;
        }
        allCount+=1;
        if(!map.containsKey(string))
        {
            map.put(string,new FrequencyElement(string,1));
        }
        else
        {
            final FrequencyElement element = map.remove(string);
            map.put(string,element.add());
        }
    }

    public Iterator<Element> getAscending()
    {
        final ArrayList<FrequencyElement> array = new ArrayList<FrequencyElement>(map.values());
        Collections.sort(array);
        final ArrayList<Element> elementArray = new ArrayList<Element>(array);
        return elementArray.iterator();
    }

    public Iterator<Element> getDescending()
    {
        final ArrayList<FrequencyElement> array = new ArrayList<FrequencyElement>(map.values());
        Collections.sort(array,Collections.<FrequencyElement>reverseOrder());
        final ArrayList<Element> elementArray = new ArrayList<Element>(array);
        return elementArray.iterator();
    }
}
