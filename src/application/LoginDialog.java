package application;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * Class implementing LoginDialog with user authentication and working environment selection.
 * @author Hubert Borkowski
 *
 */
public class LoginDialog extends Dialog<Pair<String, String>>
{
    private LoginLogic logic;
    private String environment;
    private String user;
    private String passwd;
    private Boolean envOk;
    private Boolean userOk;
    private Boolean passOk;
    private ChoiceBox<String> env;
    private ComboBox<String> username;
    private PasswordField password;
    private Node loginButton;
    private ButtonType cancelButtonType;
    private ButtonType loginButtonType;

    /**
     * Sets the correct values for dialog window operation.
     */
    LoginDialog()
    {
	logic = new LoginLogic(new DataHandler());
	envOk = false;
	userOk = false;
	passOk = false;
	env = new ChoiceBox<String>(FXCollections.observableArrayList("Deweloperskie", "Testowe", "Produkcyjne"));
	username = new ComboBox<String>();
	password = new PasswordField();
	loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
	cancelButtonType = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
    }

    /**
     * Sets value of environment property based on value x.
     * @param x value of the environment to be selected
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
     * Handles ChoiceBox event of selecting the working environment.
     * @param newValue value passed by ChoiceBox event
     */
    private void handleEnv(Number newValue)
    {
	selectEnvironment(newValue.intValue());
	userOk = false;
	passOk = false;
	envOk = true;
	username.getItems().clear();
	password.clear();
	username.getItems().addAll(logic.getUserList(environment));
	loginButton.setDisable(!(passOk && userOk && envOk));
    }

    /**
     * Handles ComboBox event of electing username.
     */
    private void handleUser()
    {
	user = username.getSelectionModel().getSelectedItem();
	passOk = false;
	userOk = true;
	password.clear();
	loginButton.setDisable(!(passOk && userOk && envOk));
    }

    /**
     * Handles PasswordField event of typing in a password.
     * @param newVal value passed by the PasswordField
     */
    private void handlePasswd(String newVal)
    {
	passwd = newVal;
	passOk = true;
	loginButton.setDisable(!(passOk && userOk && envOk));
    }

    /**
     * Converts the standard Dialog result to a Pair<String,String> type result.
     * @param dialogButton return value of Dialog showAndWait() method
     * @return environment, username Pair if user is in the database, else null
     */
    public Pair<String, String> resultConverter(ButtonType dialogButton)
    {
	if (dialogButton == loginButtonType)
	{
	    if (logic.isInData(new Account(user, passwd, environment), environment))
	    {
		return new Pair<String, String>(environment, user);
	    }
	}
	return null;
    }

    /**
     * Configures and runs the LoginDialog 
     * @return environment, username Pair if user is in database, else null
     */
    public Optional<Pair<String, String>> run()
    {
	logic.loadData();

	setTitle("Logowanie");
	setHeaderText("Logowanie do systemu");
	initStyle(StageStyle.UTILITY);

	File file = new File("login.png");
	Image image = new Image(file.toURI().toString());
	ImageView iv = new ImageView();
	iv.setImage(image);
	setGraphic(iv);

	getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

	GridPane grid = new GridPane();
	grid.setHgap(20);
	grid.setVgap(10);
	grid.setPadding(new Insets(20, 95, 10, 10));

	username.setEditable(true);

	password.setPromptText("Has³o");

	grid.add(new Label("Œrodowisko:"), 0, 0);
	grid.add(env, 1, 0);
	grid.add(new Label("U¿ytkownik:"), 0, 1);
	grid.add(username, 1, 1);
	grid.add(new Label("Has³o:"), 0, 2);
	grid.add(password, 1, 2);

	loginButton = getDialogPane().lookupButton(loginButtonType);
	loginButton.setDisable(true);

	env.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> handleEnv(newVal));

	username.setOnAction((e) -> handleUser());

	password.textProperty().addListener((observable, oldVal, newVal) -> handlePasswd(newVal));

	getDialogPane().setContent(grid);

	Platform.runLater(() -> env.requestFocus());

	setResultConverter(dialogButton -> resultConverter(dialogButton));

	return showAndWait();
    }
}
