package airfield;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import airfield.application.ProgramStarter;
import airfield.application.PropertiesHandler;
import airfield.application.TakeDown;
import airfield.fx.LoadingScreenPopUp;
import airfield.fx.AirfieldView;
import airfield.secure.Generator;

/**
 * Starts the application.
 * 
 * @author airhacks.com
 */
public class App extends Application {
	/** The folder for the app */
	private static String local;
	/** The uri to the git */
	private static String remote;
	/** the programstarter */
	private static ProgramStarter programStarter;

	private boolean updateOnly = false;
	private static boolean takeOff = false;
	private static boolean init = false;

	/**
	 * Main
	 * 
	 * @param args
	 *            the overgiven parameters
	 */
	public static final void main(final String[] args) {
		programStarter = new ProgramStarter();

		if (args.length > 0) {
			for (String string : args) {
				if (string.equals("skipUpdate")) {
					System.out.println("skip update and start program");
					programStarter.startProgramm();
					System.exit(0);
				}

				if (string.equals("updateonly")) {
					System.out.println("Only update the program");
					System.out.println("TODO!"); // TODO
				}
			}
		} else {
			System.out.println("Commit & push program!");
			takeOff = true;
		}

		PropertiesHandler ph = new PropertiesHandler();
		local = ph.getFolder();
		launch(args); // Start javaFX frontend
		/*
		 * TODO try { remote = ph.getGit(); launch(args); } catch (Exception e)
		 * { programStarter.startProgramm(); }
		 */

	}

	/** JavaFX starting point */
	@Override
	public final void start(final Stage primaryStage) throws Exception {
		// takeDown();
		takeOff(primaryStage);
	}

	/**
	 * TODO
	 * 
	 * @param skipProgramStart
	 *            TODO
	 */
	private void takeDown(final boolean skipProgramStart) {

		// Update
		LoadingScreenPopUp lsPopUp = new LoadingScreenPopUp();
		lsPopUp.show();

		Thread t = new Thread(() -> {
			TakeDown installer = new TakeDown(local, remote);
			installer.installOrUpdate();
			Platform.runLater(() -> {
				lsPopUp.hide();

				// Check files by signature
				// TODO

				// Start program
				if (skipProgramStart) {
					programStarter.startProgramm();
				}
			});
		});

		t.start();
	}

	/**
	 * TODO
	 * 
	 * @param stage
	 *            TODO
	 */
	private void takeOff(final Stage stage) {
		System.out.println("takeOff -->");

		// Sign content
		AirfieldView signerView = new AirfieldView();
		signerView.show();

		// Create sign file

		// Commit and push
	}

	private void initialize() {
		takeDown(true);
	}

	/** Test if everything works fine */
	private void test() {
		System.out.println("Future release");
	}

}
