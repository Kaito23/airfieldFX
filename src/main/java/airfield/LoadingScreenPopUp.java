package airfield;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A popup displaying a loading screen @author koetter
 */
public class LoadingScreenPopUp extends Stage {
	/** Text in Progressbar */
	private final Text text;
	/** The progressbar */
	private ProgressBar pbs;

	/**
	 * Displays a LoadingScreen.
	 * 
	 * @param newTab
	 *            the new Tab that opens with the loading screen
	 * @param tabPane
	 *            the container tabpane
	 * @param primaryStage
	 *            the primary stage
	 */
	public LoadingScreenPopUp() {
		super();
		this.initStyle(StageStyle.UNDECORATED);
		this.setResizable(false);
		this.initModality(Modality.APPLICATION_MODAL);
		text = new Text("Loading");

		final Scene scene = new Scene(getContainerStackPane(), 250, 100);

		this.setScene(scene);
		this.setOnCloseRequest(event -> {
			event.consume();
		});
		this.setTitle("Progress");
		this.setResizable(false);
	}

	/**
	 * Creates the container.
	 * 
	 * @return container
	 */
	private StackPane getContainerStackPane() {
		final StackPane stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);
		stackPane.getChildren().setAll(getBorderPane());
		return stackPane;
	}

	/**
	 * Creates a borderpane including the progressbar and a cancel button.
	 * 
	 * @return returns a borderpane with the stackpane and cancel button
	 */
	private HBox getBorderPane() {
		final HBox container = new HBox();
		final Button buttonCancel = new Button("X");
		buttonCancel.setOnAction(event -> {
			// cancel operation
				this.hide();
				System.exit(0);
			});

		buttonCancel.setMinHeight(CANCEL_BUTTON_HEIGHT);
		container.setAlignment(Pos.CENTER);
		container.getChildren().add(getStackPane());
		container.getChildren().add(buttonCancel);
		return container;
	}

	/**
	 * Gets the stackpane with progressbar.
	 * 
	 * @return stackpane with progressbar
	 */
	private StackPane getStackPane() {
		pbs = new ProgressBar(-1.0f);
		pbs.setMinHeight(FIFTY);
		pbs.setMinWidth(TWOHUNDRED);
		final StackPane stackPane = new StackPane();
		stackPane.getChildren().setAll(pbs, text);
		return stackPane;
	}

	/** 50 */
	private static final int FIFTY = 50;
	/** 200 */
	private static final int TWOHUNDRED = 200;
	/** 48 */
	private static final int CANCEL_BUTTON_HEIGHT = 48;

}
