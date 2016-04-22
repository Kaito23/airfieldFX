package airfield;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import panda.signer.SignChecker;
import airfield.application.ProgramStarter;
import airfield.application.PropertiesHandler;
import airfield.application.TakeDown;
import airfield.fx.LoadingScreenPopUp;

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
	/** only update the app */
	private static boolean updateOnly = false;

	/**
	 * Main
	 * 
	 * @param args the overgiven parameters
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
					updateOnly = true;
				}
			}
		}

		PropertiesHandler ph = new PropertiesHandler();
		local = ph.getFolder();

		try {
			remote = ph.getGit();
			launch(args);
		} catch (Exception e) {
			programStarter.startProgramm();
		}

	}

	/** JavaFX starting point */
	@Override
	public final void start(final Stage primaryStage) throws Exception {
		takeDown(updateOnly);
	}

	/**
	 * Updates the app and starts it.
	 * 
	 * @param updateOnly skips the program start
	 */
	private void takeDown(final boolean updateOnly) {

		// Update
		LoadingScreenPopUp lsPopUp = new LoadingScreenPopUp();
		lsPopUp.show();

		Thread t = new Thread(() -> {
			TakeDown installer = new TakeDown(local, remote);
			installer.installOrUpdate();
			Platform.runLater(() -> {
				lsPopUp.hide();

				// Check files by signature
				SignChecker signChecker = new SignChecker();
				boolean verify = signChecker.verify("pub", local);
				System.out.println("verify result > " + verify);

				// Start program
				if (!updateOnly) {
					if (verify) {
						programStarter.startProgramm();
					} else {
						System.out.println("ACHTUNG ! Dateien sind nicht valide!");
						// TODO DO logging
					}
				}
			});
		});

		t.start();
	}
}
