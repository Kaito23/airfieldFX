package airfield;

import org.apache.log4j.Logger;

import airfield.application.ProgramStarter;
import airfield.application.PropertiesHandler;
import airfield.application.TakeDown;
import airfield.fx.LoadingScreenPopUp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import panda.signer.Generator;
import panda.signer.SignChecker;
import panda.signer.Signer;

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
	/** Logger */
	final static Logger logger = Logger.getLogger(App.class);

	/**
	 * Main
	 * 
	 * @param args
	 *            the overgiven parameters
	 */
	public static final void main(final String[] args) {
		programStarter = new ProgramStarter();
		argsHandler(args);
		final PropertiesHandler ph = new PropertiesHandler();
		local = ph.getFolder();

		try {
			remote = ph.getGit();
			launch(args);
		} catch (final Exception e) {
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
	 * @param updateOnly
	 *            skips the program start
	 */
	private void takeDown(final boolean updateOnly) {

		// Update
		final LoadingScreenPopUp lsPopUp = new LoadingScreenPopUp();
		lsPopUp.show();

		final Thread t = new Thread(() -> {
			final TakeDown installer = new TakeDown(local, remote);
			installer.installOrUpdate();
			Platform.runLater(() -> {
				lsPopUp.hide();

				// Check files by signature
				final SignChecker signChecker = new SignChecker();
				final boolean verify = signChecker.verify("pub", local);
				System.out.println("verify result > " + verify);

				// Start program
				if (!updateOnly) {
					if (verify) {
						programStarter.startProgramm();
					} else {
						System.out.println("ACHTUNG ! Dateien sind nicht valide!");
						logger.error("Files are not valid!");
						final Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("The files are not valid!");
						alert.setContentText("The signing check failed! Your files may be corrupt.");

						alert.showAndWait();
					}
				}
			});
		});
		t.start();
	}

	/**
	 * Handles the overgiven arguments.
	 * 
	 * @param args
	 *            the overgiven arguments
	 */
	private static void argsHandler(final String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
			case "updateonly":
				System.out.println("Only update the program");
				updateOnly = true;
				break;
			case "skipUpdate":
				System.out.println("skip update and start program");
				programStarter.startProgramm();
				System.exit(0);
				break;
			case "generateKeys":
				generateKeys(args);
				System.exit(0);
				break;
			case "sign":
				sign(args);
				System.exit(0);
				break;
			case "testSign":
				testSigning();
				System.exit(0);
				break;
			default:
				System.out.println("No supported argument!");
				logger.error("Unsupported argument overgiven: " + args[0]);
				System.exit(0);
				break;
			}
		}
	}

	/**
	 * Starts the key-generating process.
	 * 
	 * @param args
	 *            the overgiven arguments for the destination folder of the keys
	 */
	private static void generateKeys(final String[] args) {
		System.out.println("generate keypair");
		final Generator generator = new Generator();
		if (args.length == 2) {
			generator.generateKeypair(args[1]);
		} else {
			generator.generateKeypair();
		}
	}

	/**
	 * Starts the signing process.
	 * 
	 * @param args
	 *            the overgiven arguments for a path to the private key
	 */
	private static void sign(final String[] args) {
		System.out.println("sign files");
		final Signer signer = new Signer();
		if (args.length == 2) {
			signer.createSignFile(args[1], "../app/");
		} else {
			System.out.println("Please specify the path to your private key!");
		}
	}

	/** Test the signing */
	private static void testSigning() {
		final SignChecker signChecker = new SignChecker();
		final boolean valid = signChecker.verify("./pub", "../app/");
		if (valid) {
			System.out.println("Files are valid");
		} else {
			System.out.println("!Files are corrupt!");
		}
	}
}
