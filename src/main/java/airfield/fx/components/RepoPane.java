package airfield.fx.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Getter;

/**
 * TODO
 * @author koetter
 */
public class RepoPane extends GridPane {

	@Getter
	private PasswordField pwField;

	@Getter
	private TextField textFieldRepo;

	@Getter
	private TextField textFieldUsername;

	@Getter
	private TextField textFieldPrivateKey;
	
	@Getter
	private TextField textFieldTarget;

	public RepoPane() {
		pwField = new PasswordField();

		this.setHgap(DISTANCES);
		this.setVgap(DISTANCES);
		this.setPadding(new Insets(DISTANCES));

		Text category = new Text("Repo:");
		category.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
		this.add(category, 0, 0);

		this.add(new Label("Repo"), 0, 1);
		textFieldRepo = new TextField();
		this.add(textFieldRepo, 1, 1);

		this.add(new Label("Username"), 0, 2);
		textFieldUsername = new TextField();
		this.add(textFieldUsername, 1, 2);

		this.add(new Label("Passwort"), 0, 3);
		textFieldTarget = new TextField();
		this.add(pwField, 1, 3);

		this.add(new Label("Privatekey"), 0, 4);

		textFieldPrivateKey = new TextField();
		this.add(textFieldPrivateKey, 1, 4);
		this.add(new Button("Wählen"), 2, 4);

	}

	/** 10 */
	private static final int DISTANCES = 10;
	/** 20 */
	private static final int FONT_SIZE = 20;
}
