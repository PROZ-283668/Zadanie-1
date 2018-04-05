package application;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.*;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Implements login dialog based on JavaFX Dialog.
 * 
 * @author Hubert Borkowski
 *
 */
public class LoginDialog extends Application
{
    private String environment;
    private String user;
    private String passwd;
    private Boolean envOk;
    private Boolean userOk;
    private Boolean passOk;

    /**
     * Sets the environment variable.
     * 
     * @param x
     *            index from ChoiceBox env array
     */
    private void selectEnvironment(int x)
    {
	if (x == 0)
	{
	    environment = "development";
	} else if (x == 1)
	{
	    environment = "testing";
	} else
	{
	    environment = "production";
	}
    }

    /**
     * Displays JavaFX Dialog and controls it's behaviour.
     */
    @Override
    public void start(Stage primaryStage)
    {
	LoginLogic logic = new LoginLogic(new DataHandler());
	logic.loadData();

	Dialog<Pair<String, String>> dialog = new Dialog<>();
	dialog.setTitle("Logowanie");
	dialog.setHeaderText("Logowanie do systemu");
	dialog.initStyle(StageStyle.UTILITY);

	File file = new File("login.png");
	Image image = new Image(file.toURI().toString());
	ImageView iv = new ImageView();
	iv.setImage(image);
	dialog.setGraphic(iv);

	ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
	ButtonType cancelButtonType = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

	GridPane grid = new GridPane();
	grid.setHgap(20);
	grid.setVgap(10);
	grid.setPadding(new Insets(20, 95, 10, 10));

	ChoiceBox<String> env = new ChoiceBox<String>(
		FXCollections.observableArrayList("Deweloperskie", "Testowe", "Produkcyjne"));

	ComboBox<String> username = new ComboBox<String>();
	username.setEditable(true);

	PasswordField password = new PasswordField();
	password.setPromptText("Has³o");

	grid.add(new Label("Œrodowisko:"), 0, 0);
	grid.add(env, 1, 0);
	grid.add(new Label("U¿ytkownik:"), 0, 1);
	grid.add(username, 1, 1);
	grid.add(new Label("Has³o:"), 0, 2);
	grid.add(password, 1, 2);

	Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
	loginButton.setDisable(true);

	env.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
	    selectEnvironment(newValue.intValue());
	    userOk = false;
	    passOk = false;
	    envOk = true;
	    username.getItems().clear();
	    password.clear();
	    username.getItems().addAll(logic.getUserList(environment));
	    loginButton.setDisable(!(passOk && userOk && envOk));
	});

	username.setOnAction((e) -> {
	    user = username.getSelectionModel().getSelectedItem();
	    passOk = false;
	    userOk = true;
	    password.clear();
	    loginButton.setDisable(!(passOk && userOk && envOk));
	});

	password.textProperty().addListener((observable, oldVal, newVal) -> {
	    passwd = newVal.toString();
	    passOk = true;
	    loginButton.setDisable(!(passOk && userOk && envOk));
	});

	dialog.getDialogPane().setContent(grid);

	Platform.runLater(() -> env.requestFocus());

	dialog.setResultConverter(dialogButton -> {
	    if (dialogButton == loginButtonType)
	    {
		if (logic.isInData(new Account(user, passwd, environment), environment))
		{
		    return new Pair<>(environment, user);
		}
	    }
	    return null;
	});

	Optional<Pair<String, String>> result = dialog.showAndWait();

	result.ifPresent(envUser -> {
	    System.out.println("Environment=" + envUser.getKey() + ", User=" + envUser.getValue());
	});
    }

    public static void main(String[] args)
    {
	launch(args);
    }
}
