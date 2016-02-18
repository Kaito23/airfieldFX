package airfield.fx;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import airfield.application.Test;
import airfield.fx.components.RepoPane;
import airfield.secure.Generator;
import airfield.secure.SignChecker;
import airfield.secure.Signer;

/**
 * View for signing, keygeneration and commiting.
 * 
 * @author koetter
 */
public class AirfieldView extends Stage {

	private RepoPane repoPane;
	/** Local folder of the app */
	private TextField textFieldLocalFolder;

	/** Displays the takeoff screen. */
	public AirfieldView() {
		super();
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setTitle("TakeOff");
		this.setResizable(false);
		this.sizeToScene();
		this.repoPane = new RepoPane();
		final Scene scene = new Scene(getBPRoot());
		this.setScene(scene);
	}

	private BorderPane getBPRoot() {
		BorderPane root = new BorderPane();

		root.setTop(getHead());
		root.setBottom(repoPane);
		root.setCenter(getTabPane());

		return root;
	}

	private HBox getHead() {
		HBox head = new HBox(SPACE);
		head.setPadding(new Insets(SPACE));

		textFieldLocalFolder = new TextField();
		Button buttonChooseLocalFolder = new Button("Wählen");
		buttonChooseLocalFolder.setOnAction(event -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Zielordner wählen");
			File selectedDirectory = directoryChooser.showDialog(this);
			textFieldLocalFolder.setText(selectedDirectory.getAbsolutePath());
		});

		head.getChildren().addAll(new Label("Ordner"), textFieldLocalFolder, buttonChooseLocalFolder);

		return head;
	}

	private TabPane getTabPane() {
		TabPane tabPane = new TabPane();

		Tab tabInit = new Tab("Init");
		tabInit.setContent(getInitTab());
		tabPane.getTabs().add(tabInit);
		tabInit.setClosable(false);

		Tab tab = new Tab("Sign");
		tab.setContent(getSignGrid());
		tabPane.getTabs().add(tab);
		tab.setClosable(false);

		Tab tabCP = new Tab("C & P");
		tabCP.setContent(getCPTab());
		tabPane.getTabs().add(tabCP);
		tabCP.setClosable(false);

		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(final ObservableValue<? extends Tab> observable, final Tab oldTab, final Tab newTab) {
				if (newTab == tabCP || newTab == tabInit) {
					repoPane.setVisible(true);
					// repoPane.getTextFieldTarget().setText("");
				} else {
					repoPane.setVisible(false);
					// repoPane.getTextFieldTarget().setText("");
				}
			}
		});
		return tabPane;
	}

	private GridPane getCPTab() {
		Button buttonGO = new Button("C & P");
		buttonGO.setOnAction(event -> {
			// TODO
			System.out.println("TODO");
		});

		GridPane grid = new GridPane();
		// grid.setHgap(TEN);
		grid.setVgap(TEN);
		grid.setPadding(new Insets(TEN));

		Text category = new Text("Commit & Push:");
		category.setFont(Font.font("Arial", FontWeight.BOLD, TWENTY));
		grid.add(category, 0, 0);

		grid.add(new Label("Kommentar"), 0, 1);
		grid.add(new TextArea(), 0, 2);

		HBox checkboxbox = new HBox(TEN);

		checkboxbox.getChildren().addAll(new Label("Signieren?"), new CheckBox());
		grid.add(checkboxbox, 0, 3);
		grid.add(buttonGO, 0, 4);

		return grid;
	}

	private GridPane getSignGrid() {
		GridPane grid = new GridPane();
		grid.setHgap(TEN);
		grid.setVgap(TEN);
		grid.setPadding(new Insets(INSETS));

		grid.add(new Label("Signkey Private"), 0, 0);
		TextField textFieldPrivateKeyPath = new TextField();
		grid.add(textFieldPrivateKeyPath, 1, 0);
		grid.add(new Button("Key wählen"), 2, 0);

		grid.add(new Label("Signkey Public"), 0, 1);
		TextField textFieldPublicKey = new TextField();
		grid.add(textFieldPublicKey, 1, 1);
		grid.add(new Button("Key wählen"), 2, 1);

		Button buttonCreateKey = new Button("Keypaar erstellen");
		buttonCreateKey.setOnAction(event -> {
			if (!textFieldLocalFolder.getText().isEmpty()) {
				Generator generator = new Generator();
				File keyFolder = generator.generateKeypair(this);
				if (keyFolder != null) {
					textFieldPrivateKeyPath.setText(keyFolder.getAbsolutePath() + File.separator + "priv");
					textFieldPublicKey.setText(keyFolder.getAbsolutePath() + File.separator + "pub");
					grid.add(new Label("Keypair erstellt!"), 1, 3);
				}
			} else {
				textFieldLocalFolder.setStyle("-fx-border-color: red;");
			}
		});

		grid.add(buttonCreateKey, 0, 3);
		Button buttonTakeOff = new Button("Signieren");
		
		Label labelSignMsg = new Label();
		grid.add(labelSignMsg, 1, 5);
		
		Label labelTest = new Label();
		
		Label labelSignOk = new Label();
		grid.add(labelSignOk, 1, 4);
	
		buttonTakeOff.setOnAction(event -> {
			String privateKeyFolderPath = textFieldPrivateKeyPath.getText();
			String publicKeyFolderPath = textFieldPublicKey.getText();

			Signer signer = new Signer();
			signer.createSignFile(privateKeyFolderPath, publicKeyFolderPath, textFieldLocalFolder.getText());

			System.out.println("signing complete! start checking ...");
			labelSignOk.setText("Signiert!");
			System.out.println("publicKeyFolderPath > " + publicKeyFolderPath);
			SignChecker signChecker = new SignChecker();
			boolean verified = signChecker.verify(publicKeyFolderPath, textFieldLocalFolder.getText());
			if (verified) {
				labelTest.setText("Erfolgreich!");
			} else {
				labelTest.setText("Fehlerhaft!");
			}
			System.out.println("checking complete");
		});
		grid.add(buttonTakeOff, 0, 4);
		grid.add(labelTest, 1, 5);
		
		Button buttonTestSignature = new Button("Test signature");
		buttonTestSignature.setOnAction(event -> {
				SignChecker signChecker = new SignChecker();
				boolean verified = signChecker.verify(textFieldPublicKey.getText(), textFieldLocalFolder.getText());
				if (verified) {
					labelTest.setText("Test erfolgreich!");
				} else {
					labelTest.setText("Fehler beim Testen!");
				}
			});
		grid.add(buttonTestSignature, 0, 5);

		return grid;
	}

	private GridPane getInitTab() {
		Button buttonGO = new Button("Go");
		buttonGO.setOnAction(event -> {
			Test test = new Test();
			// test.test(pwField.getText()); TODO
		});

		GridPane grid = new GridPane();
		grid.setHgap(TEN);
		grid.setVgap(TEN);
		grid.setPadding(new Insets(TEN));

		Text category = new Text("Init a repo:");
		category.setFont(Font.font("Arial", FontWeight.BOLD, TWENTY));
		grid.add(category, 0, 0);

		Label labelExplain = new Label("Lädt initial das Git-Repo herunter.");
		grid.add(labelExplain, 0, 1);
		grid.add(buttonGO, 0, 2);

		return grid;
	}

	/** 5 */
	private static final int INSETS = 15;
	/** 3 */
	private static final int THREE = 3;
	/** 10 */
	private static final int TEN = 10;
	/** 20 */
	private static final int TWENTY = 20;
	/** Space 5 */
	private static final int SPACE = 5;

}
