package fit.kurlaev.minesweeper;

public interface MinesweeperModelInterface
{
    public void startGame(int X, int Y, int mines);
    public void setController(MinesweeperControllerInterface controller);
    public void open(int x, int y);
    public void toggleFlag(int x, int y);
    int getX();
    int getY();
    int getMineCount();
    int getFlagsCount();
    Field getField(int x, int y);
}
