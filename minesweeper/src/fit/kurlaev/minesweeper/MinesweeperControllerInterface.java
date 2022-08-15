package fit.kurlaev.minesweeper;

public interface MinesweeperControllerInterface
{
    public void setModel(MinesweeperModelInterface model);
    public void addView(MinesweeperViewInterface view);
    //events from model
    public void fieldUpdated(int x, int y);
    public void endUpdate();
    public void gameStarted();
    public void gameOver(boolean win);
    //events from view
    public void startGame(int X, int Y, int mines);
    public void open(int x, int y);
    public void toggleFlag(int x, int y);
    //model accessors
    int getX();
    int getY();
    int getMineCount();
    int getFlagsCount();
    Field getField(int x, int y);
}
