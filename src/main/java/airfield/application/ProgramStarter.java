package airfield.application;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Programmstarter starts the app by using a batch- or shellscript depending on
 * the users os.
 * 
 * @author koetter
 */
public class ProgramStarter {
	/** The os the user is running */
	private static final String OS = System.getProperty("os.name").toLowerCase();
	/** Path to the running jar. */
	private String path;
	/** 9 */
	private static final int SUBSTRING_BEGIN_INDEX = 9;
	/** Logger */
	final static Logger logger = Logger.getLogger(ProgramStarter.class);

	/** Initializes the path. */
	public ProgramStarter() {
		path = getPath();
		System.out.println("path: " + path);
	}

	/**
	 * Get the path to the running jar.
	 * 
	 * @return current execution path
	 */
	private String getPath() {
		final URL url1 = getClass().getResource("");
		String ur = url1.toString();
		ur = ur.substring(SUBSTRING_BEGIN_INDEX);
		final String[] truepath = ur.split("/*.jar");
		truepath[0] = truepath[0].replaceAll("%20", " ");
		truepath[0] = truepath[0].replaceAll("airfield/airfield", "");
		return truepath[0];
	}

	/** Starts the programm depending on os */
	public final void startProgramm() {
		if (isWindows()) {
			System.out.println("This is Windows");
			startBatch();
		} else if (isMac()) {
			System.out.println("This is Mac");
			startMacShell();
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
			startShell();
		} else {
			System.out.println("Your OS is not support!");
			final Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("OS not support");
			alert.setHeaderText("Your OS is not support");
			alert.setContentText("It seems like we don't support your OS, sorry for that. "
					+ "Check the airfieldFX github page and leave a comment with the folling information: OS=" + OS);
			alert.showAndWait();
			logger.error(
					"Check final the airfieldFX github final page and leave final a comment with final the folling information: OS="
							+ OS);
		}

		System.exit(0);

	}

	/** Starts the program batch script */
	private void startBatch() {
		try {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			final String command = "cmd /c start " + path + "app\\start.bat";
			System.out.println("command: " + command);
			Runtime.getRuntime().exec(command);
		} catch (final IOException e) {
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Starts the programs mac shell script */
	private void startMacShell() {
		try {
			new File(path + "app/mac.sh").setExecutable(true);
			new ProcessBuilder(path + "app/mac.sh").start();
		} catch (final IOException e) {
			// TODO javafx panel
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			logger.error(e);
		}
	}

	/** Starts the programs shell script */
	private void startShell() {
		try {
			new File(path + "app/start.sh").setExecutable(true);
			new ProcessBuilder(path + "app/start.sh").start();
		} catch (final IOException e) {
			// TODO javafx panel
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			logger.error(e);
		}
	}

	/**
	 * Check if os is windows.
	 * 
	 * @return true if the users os is windows
	 */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/**
	 * Check if os is mac.
	 * 
	 * @return true if the users os is mac
	 */
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/**
	 * Check if os is unix.
	 * 
	 * @return true if the users os is linux or unix
	 */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
}
