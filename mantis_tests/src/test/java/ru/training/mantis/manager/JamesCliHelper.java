package ru.training.mantis.manager;

import org.openqa.selenium.io.CircularOutputStream;
import org.openqa.selenium.os.CommandLine;

public class JamesCliHelper extends HelperBase {

    public JamesCliHelper(ApplicationManager manager) {
        super(manager);
    }

    public void addUser(String email, String password) {
        // в лекции говорится, что нужно обрамлять дополнительными кавычками параметр "james-server-jpa-app.lib/*"
        // вот таким образом "\"james-server-jpa-app.lib/*\""
        // однако при запуске в linux эти кавычки как раз мешают, поэтому здесь их нет
        CommandLine cmd = new CommandLine(
                "java", "-cp", "james-server-jpa-app.lib/*",
                "org.apache.james.cli.ServerCmd",
                "AddUser", email, password);
        cmd.setWorkingDirectory(manager.property("james.workingDir"));
        CircularOutputStream out = new CircularOutputStream();
        cmd.copyOutputTo(out);
        cmd.execute();
        cmd.waitFor();
        System.out.println(out);
    }
}
