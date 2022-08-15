package fit.kurlaev.minesweeper;


import java.util.Random;

public class MinesweeperModel implements MinesweeperModelInterface
{
    private MinesweeperControllerInterface m_controller;
    private int m_X;
    private int m_Y;
    private int m_mines;
    private int m_flags;
    private int m_opened;
    private Field m_fields[][];

    public void startGame(int X, int Y, int mines)
    {
        if(X<0 || Y<0 || mines<0 || mines>X*Y)
        {
            return;
        }
        m_X = X;
        m_Y = Y;
        m_fields = new Field[X][Y];
        m_mines = mines;
        m_flags = 0;
        m_opened = 0;
        generateRandomField();
        m_controller.gameStarted();
    }

    private void generateRandomField()
    {
        Random random = new Random();
        boolean mines[][] = new boolean[m_X][m_Y];
        for(int i=0;i<m_X;i++)
        {
            for(int j=0;j<m_Y;j++)
            {
                mines[i][j] = false;
            }
        }
        for(int i=0;i<m_mines;i++)
        {
            int x, y;
            do
            {
                x = random.nextInt(m_X);
                y = random.nextInt(m_Y);
            }
            while(mines[x][y]);
            mines[x][y] = true;
        }
        for(int i=0;i<m_X;i++)
        {
            for(int j=0;j<m_Y;j++)
            {
                m_fields[i][j] = new Field(mines[i][j],
                                           (i<1?0:j<1?0:mines[i-1][j-1]?1:0)+(i<1?0:mines[i-1][j]?1:0)+(i<1?0:j>m_Y-2?0:mines[i-1][j+1]?1:0)+
                                           (j<1?0:mines[i][j-1]?1:0)+(j>m_Y-2?0:mines[i][j+1]?1:0)+
                                           (i>m_X-2?0:j<1?0:mines[i+1][j-1]?1:0)+(i>m_X-2?0:mines[i+1][j]?1:0)+(i>m_X-2?0:j>m_Y-2?0:mines[i+1][j+1]?1:0));
            }
        }
    }

    public void setController(MinesweeperControllerInterface controller)
    {
        m_controller = controller;
    }

    private void checkWin()
    {
        if(m_opened+m_mines==m_X*m_Y)
        {
            for(int i=0;i<m_X;i++)
            {
                for(int j=0;j<m_Y;j++)
                {
                    if(m_fields[i][j].isMine())
                    {
                        m_fields[i][j].flag();
                    }
                }
            }
            m_controller.gameOver(true);
        }
    }

    public void open(int x, int y)
    {
        if (!m_fields[x][y].isOpened() || x>=0 && x<m_X && y>=0 && y<m_Y)
        {
            openRecursive(x, y);
            if(m_fields[x][y].isMine())
            {
                for(int i=0;i<m_X;i++)
                {
                    for(int j=0;j<m_Y;j++)
                    {
                        openRecursive(i,j);
                    }
                }
                m_controller.gameOver(false);
            }
            else
            {
                checkWin();
            }
        }
        m_controller.endUpdate();
    }

    private void openRecursive(int x, int y)
    {
        if(!(x>=0 && x<m_X && y>=0 && y<m_Y) || m_fields[x][y].isOpened())
        {
            return;
        }
        if(m_fields[x][y].isFlagged())
        {
            unflag(x, y);
        }
        m_fields[x][y].open();
        m_opened++;
        m_controller.fieldUpdated(x,y);
        if(m_fields[x][y].getCount()==0 && !m_fields[x][y].isMine())
        {
            for(int i=-1;i<=1;i++)
            {
                for(int j=-1;j<=1;j++)
                {
                    if(i!=0||j!=0)
                    {
                        openRecursive(x+i,y+j);
                    }
                }
            }
        }
    }

    public void toggleFlag(int x, int y)
    {
        if(x>=0 && x<m_X && y>=0 && y<m_Y && !m_fields[x][y].isOpened())
        {
            if(!m_fields[x][y].isFlagged())
            {
                flag(x,y);
            }
            else
            {
                unflag(x,y);
            }
        }
        m_controller.endUpdate();
    }

    private void flag(int x, int y)
    {
        if(m_flags<m_mines)
        {
            m_fields[x][y].flag();
            m_flags++;
            m_controller.fieldUpdated(x,y);
        }
    }

    private void unflag(int x, int y)
    {
        if(m_flags>0)
        {
            m_fields[x][y].unflag();
            m_flags--;
            m_controller.fieldUpdated(x,y);
        }
    }

    public int getX() {
        return m_X;
    }

    public int getY() {
        return m_Y;
    }

    public int getMineCount() {
        return m_mines;
    }

    public int getFlagsCount() {
        return m_flags;
    }

    public Field getField(int x, int y) {
        return new Field(m_fields[x][y]);
    }
}
