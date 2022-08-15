package fit.kurlaev.minesweeper;

import java.util.ArrayList;

public class MinesweeperController implements MinesweeperControllerInterface
{
    private MinesweeperModelInterface m_model;
    private ArrayList<MinesweeperViewInterface> m_views = new ArrayList<MinesweeperViewInterface>();

    public void setModel(MinesweeperModelInterface model)
    {
        m_model = model;
        model.setController(this);
    }

    public void addView(MinesweeperViewInterface view) {
        m_views.add(view);
        view.setController(this);
    }

    public void fieldUpdated(int x, int y)
    {
        for(MinesweeperViewInterface view : m_views)
        {
            view.fieldUpdated(x,y);
        }
    }

    public void endUpdate()
    {
        for(MinesweeperViewInterface view : m_views)
        {
            view.endUpdate();
        }
    }

    public void gameStarted()
    {
        for(MinesweeperViewInterface view : m_views)
        {
            view.gameStarted();
        }
    }

    public void gameOver(boolean win)
    {
        for(MinesweeperViewInterface view : m_views)
        {
            view.gameOver(win);
        }
    }

    public void startGame(int X, int Y, int mines)
    {
        m_model.startGame(X,Y,mines);
    }

    public void open(int x, int y)
    {
        m_model.open(x,y);
    }

    public void toggleFlag(int x, int y) {
        m_model.toggleFlag(x,y);
    }

    public int getX() {
        return m_model.getX();
    }

    public int getY() {
        return m_model.getY();
    }

    public int getMineCount() {
        return m_model.getMineCount();
    }

    public int getFlagsCount() {
        return m_model.getFlagsCount();
    }

    public Field getField(int x, int y) {
        return m_model.getField(x,y);
    }
}
