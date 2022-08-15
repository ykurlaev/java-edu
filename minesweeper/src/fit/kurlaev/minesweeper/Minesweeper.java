package fit.kurlaev.minesweeper;

import java.io.IOException;
import java.io.InputStreamReader;

public class Minesweeper
{
    public static void main(String args[]) throws IOException
    {
        MinesweeperControllerInterface controller = new MinesweeperController();
        MinesweeperModelInterface model = new MinesweeperModel();
        MinesweeperViewInterface view;
        if(args.length>1 && args[1].equals("-c"))
        {
             view = new MinesweeperViewConsole(System.out,new InputStreamReader(System.in));
        }
        else
        {
            view = new MinesweeperViewGUI();
        }
        controller.setModel(model);
        controller.addView(view);
        view.startGame();
    }
}
