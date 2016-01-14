package airfield;

import java.awt.Frame;
import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import com.airhacks.airfield.TakeDown;

/**
 *
 * @author airhacks.com
 */
public class App extends Application {

	private static String local;
	private static String remote;
	private static ProgramStarter programStarter;

	/** Main */
	public final static void main(String args[]) {
		programStarter = new ProgramStarter();

		if (args.length > 0) {
			for (String string : args) {
				if (string.equals("skip")) {
					System.out.println("skip");
					programStarter.startProgramm();
					System.exit(0);
				}

				if (string.equals("updateonly")) {
					System.out.println("TODO!");
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoadingScreenPopUp lsPopUp = new LoadingScreenPopUp();
		lsPopUp.show();

		Thread t = new Thread(() -> {
			TakeDown installer = new TakeDown(local, remote);
			installer.installOrUpdate();
			Platform.runLater(() -> {
				lsPopUp.hide();
				programStarter.startProgramm();
			});
		});

		t.start();
	}

}
