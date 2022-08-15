package fit.kurlaev.lab2;

public class Parser
{
    enum TokenType
    {
        Empty, Comment, Normal
    }

    public class Token
    {
        private Token(TokenType type, String name, String[] content)
        {
            _type=type;
            _name = name;
            _content=content;
        }

        public TokenType type()
        {
            return _type;
        }

        public String[] content()
        {
            return _content;
        }

        public String name()
        {
            return _name;
        }

        private TokenType _type;
        private String _name;
        private String[] _content;
    }

    public Parser()
    {
    }

    public Token ParseLine(String string)
    {
        TokenType type;
        String name;
        String[] content;
        String delimiter = "[ \t]+";
        if(string.matches(delimiter))
        {
            type = TokenType.Empty;
            name = null;
            content = null;
        }
        else if(string.startsWith("#"))
        {
            type = TokenType.Comment;
            name = null;
            content = new String[1];
            content[0] = string;
        }
        else
        {
            type = TokenType.Normal;
            String[] temp = string.split(delimiter);
            int index=1;
            if(temp[0].equals(""))
            {
                index+=1;
            }
            name=temp[index-1];
            content = new String[temp.length-index];
            for(int i=0;i<content.length;i++)
            {
                content[i]=temp[i+index];
            }
        }
        return new Token(type,name,content);
    }
}
