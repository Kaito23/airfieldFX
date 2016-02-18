package airfield.application;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

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

	/**
	 * 
	 */
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
		URL url1 = getClass().getResource("");
		String ur = url1.toString();
		ur = ur.substring(SUBSTRING_BEGIN_INDEX);
		String[] truepath = ur.split("/*.jar");
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
			System.out.println("Your OS is not support!!");
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Your OS is not support!!", "Error", JOptionPane.ERROR_MESSAGE);
		}

		System.exit(0);

	}

	/** Starts the program batch script */
	private void startBatch() {
		try {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			String command = "cmd /c start " + path + "app\\start.bat";
			System.out.println("command: " + command);
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Starts the programs mac shell script */
	private void startMacShell() {
		try {
			new File(path + "app/mac.sh").setExecutable(true);
			new ProcessBuilder(path + "app/mac.sh").start();
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Starts the programs shell script */
	private void startShell() {
		try {
			new File(path + "app/start.sh").setExecutable(true);
			new ProcessBuilder(path + "app/start.sh").start();
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
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

	/** 9 */
	private static final int SUBSTRING_BEGIN_INDEX = 9;
}
