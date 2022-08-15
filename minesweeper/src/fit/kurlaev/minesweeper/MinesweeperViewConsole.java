package fit.kurlaev.minesweeper;

import java.io.*;

public class MinesweeperViewConsole implements MinesweeperViewInterface
{
    private MinesweeperControllerInterface m_controller;
    private char m_fields[][];
    private boolean m_gameRunning;
    private PrintStream m_writer;
    private BufferedReader m_reader;
    private String frame;
    private int m_X;
    private int m_Y;

    public MinesweeperViewConsole(PrintStream writer, Reader reader)
    {
        m_gameRunning = false;
        m_writer = writer;
        m_reader = new BufferedReader(reader);
    }

    public void startGame()
    {
        int X=-1, Y=-1, mines=-1;
        try
        {
            boolean success = false;
            while(!success)
            {
                String line = m_reader.readLine();
                String[] words = line.split(" +");
                try
                {
                    X = Integer.parseInt(words[0]);
                    Y = Integer.parseInt(words[1]);
                    mines = Integer.parseInt(words[2]);
                    success = X>0 && Y>0 && mines>0 && mines<=X*Y;
                }
                catch(NumberFormatException e)
                {
                    success = false;
                }
                catch(IndexOutOfBoundsException e)
                {
                    success = false;
                }
                if(!success)
                {
                    m_writer.print("Error! Try again\n");
                }
            }
            m_controller.startGame(X,Y,mines);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setController(MinesweeperControllerInterface controller)
    {
        m_controller = controller;
    }

    public void fieldUpdated(int x, int y)
    {
        setFieldSymbol(x,y);
    }

    public void endUpdate()
    {
        nextMove();
    }

    public void gameStarted() {
        m_X = m_controller.getX();
        m_Y = m_controller.getY();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<m_X+2;i++)
        {
            builder.append('#');
        }
        builder.append('\n');
        frame = builder.toString();
        m_fields = new char[m_X][m_Y];
        for(int i=0;i<m_X;i++)
        {
            for(int j=0;j<m_Y;j++)
            {
                setFieldSymbol(i,j);
            }
        }
        m_gameRunning = true;
        nextMove();
    }

    public void gameOver(boolean win)
    {
        printField();
        if(win)
        {
            m_writer.print("You win!\n");
        }
        else
        {
            m_writer.print("Game over\n");
        }
        m_gameRunning = false;
    }

    private void setFieldSymbol(int x, int y)
    {
        Field field = m_controller.getField(x,y);
        if(field.isOpened())
        {
            if(field.isMine())
            {
                m_fields[x][y]='*';
            }
            else if(field.getCount()==0)
            {
                m_fields[x][y]='.';
            }
            else
            {
                m_fields[x][y]=(new Integer(field.getCount())).toString().charAt(0);
            }
        }
        else
        {
            if(field.isFlagged())
            {
                m_fields[x][y]='F';
            }
            else
            {
                m_fields[x][y]=' ';
            }
        }
    }

    private void printField()
    {
            m_writer.print(frame);
            for(int i=0;i<m_X;i++)
            {
                m_writer.print('#');
                m_writer.print(m_fields[i]);
                m_writer.print("#\n");
            }
            m_writer.print(frame);
    }

    private void nextMove()
    {
        if(!m_gameRunning)
        {
            return;
        }
        try
        {
            printField();
            for(;;)
            {
                boolean success;
                int x, y;
                String line = m_reader.readLine();
                String words[] = line.split(" +");
                try
                {

                    if(words[0].equals("F"))
                    {
                        x = Integer.parseInt(words[1]);
                        y = Integer.parseInt(words[2]);
                        if(x>0 && y>0 && x<=m_X && y <= m_Y)
                        {
                            m_controller.toggleFlag(x-1,y-1);
                            success = true;
                        }
                        else
                        {
                            success = false;
                        }
                    }
                    else
                    {
                        x = Integer.parseInt(words[0]);
                        y = Integer.parseInt(words[1]);
                        if(x>0 && y>0 && x<=m_X && y <= m_Y)
                        {
                            m_controller.open(x-1,y-1);
                            success = true;
                        }
                        else
                        {
                            success = false;
                        }
                    }
                }
                catch(NumberFormatException e)
                {
                    success = false;
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    success = false;
                }
                if(success)
                {
                    break;
                }
                else
                {
                    m_writer.print("Error, try again\n");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
