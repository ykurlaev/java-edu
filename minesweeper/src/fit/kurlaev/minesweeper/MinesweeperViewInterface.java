package fit.kurlaev.minesweeper;

public interface MinesweeperViewInterface
{
    public void setController(MinesweeperControllerInterface controller);;
    public void fieldUpdated(int x, int y);
    public void endUpdate();
    public void gameStarted();
    public void gameOver(boolean win);
    public void startGame();
}
