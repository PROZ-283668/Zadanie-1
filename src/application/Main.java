package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Used to show LoginDialog working in a sample application.
 * 
 * @author Hubert Borkowski
 *
 */
public class Main extends Application
{
    /**
     * Displays JavaFX Dialog.
     */
    @Override
    public void start(Stage primaryStage)
    {
	LoginDialog login = new LoginDialog();

	Optional<Pair<String, String>> result = login.run();

	result.ifPresent(envUser -> {
	    System.out.println("Environment=" + envUser.getKey() + ", User=" + envUser.getValue());
	});
    }

    public static void main(String[] args)
    {
	launch(args);
    }
}
