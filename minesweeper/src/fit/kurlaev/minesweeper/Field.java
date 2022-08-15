package fit.kurlaev.minesweeper;

public class Field
{
    private boolean m_mined;
    private boolean m_flagged;
    private boolean m_opened;
    private int m_count;

    public Field(boolean mine, int count)
    {
        assert(count>0 && count<8);
        m_count = count;
        m_mined = mine;
    }

    public Field(Field another)
    {
        m_mined = another.m_mined;
        m_flagged = another.m_flagged;
        m_opened = another.m_opened;
        m_count = another.m_count;
    }

    public int getCount()
    {
        return m_count;
    }

    public void open()
    {
        m_opened = true;
    }

    public boolean isOpened()
    {
        return m_opened;
    }

    public void flag()
    {
        m_flagged = true;
    }

    public void unflag()
    {
        m_flagged = false;
    }

    boolean isFlagged()
    {
        return m_flagged;
    }

    public boolean isMine()
    {
        return m_mined;
    }
}
