package fit.kurlaev.lab2;

import java.io.IOException;

public interface Command
{
    void exec(Context context, String[] arguments) throws IOException, Calculator.CommandException;
}
