package fit.kurlaev.lab2;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void ParseEmpty()
    {
        Parser parser = new Parser();
        Parser.Token token = parser.ParseLine("     ");
        assertTrue(token.content()==null);
        assertTrue(token.name()==null);
        assertTrue(token.type()==Parser.TokenType.Empty);
    }

    @Test
    public void ParseComment()
    {
        Parser parser = new Parser();
        Parser.Token token = parser.ParseLine("#abcdefghij");
        assertTrue(token.name()==null);
        assertTrue(token.content()[0].equals("#abcdefghij"));
        assertTrue(token.type()==Parser.TokenType.Comment);
    }

    @Test
    public void ParseNormal()
    {
        Parser parser = new Parser();
        Parser.Token token = parser.ParseLine("abc def ghi jkl");
        String[] content = {"def", "ghi", "jkl"};
        for(int i=0;i<content.length;i++)
        {
            assertTrue(token.content()[i].equals(content[i]));
        }
        assertTrue(token.name().equals("abc"));
        assertTrue(token.type()==Parser.TokenType.Normal);
    }
}
