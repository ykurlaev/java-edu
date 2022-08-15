package fit.kurlaev.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinesweeperViewGUI implements MinesweeperViewInterface
{
    private int m_X = 10, m_Y = 10;
    private MinesweeperControllerInterface m_controller;
    private MinesweeperGUI m_gui;
    private boolean m_gameStarted = false;
    private Field fields[][];

    private class MinesweeperGUI extends JFrame implements ActionListener
    {
        private JPanel grid;

        public MinesweeperGUI()
        {
            initUI();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        public void initUI()
        {
            setLocationRelativeTo(null);
            grid = new JPanel();
            grid.setLayout(new GridLayout(10, 10));
            for(int i=0;i<m_X;i++)
            {
                for(int j=0;j<m_Y;j++)
                {
                    JButton button = new JButton();
                    button.setMnemonic(i*m_X+j);
                    button.addActionListener(this);
                    grid.add(button);
                }
            }
            add(grid, BorderLayout.CENTER);
            setResizable(false);
            setTitle("Minesweeper");
        }

        public void actionPerformed(ActionEvent e)
        {
            if(!(e.getSource() instanceof JButton))
            {
                return;
            }
            JButton button = (JButton)e.getSource();
            int x = button.getMnemonic() % m_X;
            int y = button.getMnemonic() / m_X;
            m_controller.open(x,y);
        }
    }

    public MinesweeperViewGUI()
    {
        try {
            if (System.getProperty("os.name").equals("Windows"))
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            else
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
        }
        catch (IllegalAccessException ignored) {}
        catch (InstantiationException ignored) {}
        catch (UnsupportedLookAndFeelException ignored) {}
        catch (ClassNotFoundException ignored) {}
        m_X = 10;
        m_Y = 10;
    }

    public void setController(MinesweeperControllerInterface controller)
    {
        m_controller = controller;
    }

    public void fieldUpdated(int x, int y)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void endUpdate()
    {

    }

    public void gameStarted() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void gameOver(boolean win)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startGame()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() {
                m_gui = new MinesweeperGUI();
                m_gui.setVisible(true);
            }
        });
        m_gameStarted = true;
    }
}

