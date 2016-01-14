package airfield;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

public class ProgramStarter {

	private static String OS = System.getProperty("os.name").toLowerCase();

	private String path;

	public ProgramStarter() {
		path = path();
		System.out.println("____" + path);
	}

	public String path() {
		URL url1 = getClass().getResource("");
		String ur = url1.toString();
		ur = ur.substring(9);
		String truepath[] = ur.split("/*.jar");
		truepath[0] = truepath[0].replaceAll("%20", " ");
		truepath[0] = truepath[0].replaceAll("airfield/airfield", "");
		return truepath[0];
	}// This methos will work on Windows and Linux as well.

	/** Starts the programm depending on os */
	public void startProgramm() {
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

		// List cmdAndArgs = Arrays.asList({"cmd", "/c", "upsert.bat"});
		// File dir = new File(local);
		//
		// ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
		// pb.directory(new File(dir));
		// Process p = pb.start();
	}

	/** Starts the program batch script */
	private void startBatch() {
		try {
			Runtime.getRuntime().exec("cmd /c start " + path + "app\\start.bat");
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Starts the programs mac shell script */
	private void startMacShell() {
		try {
			new File(path+"app/mac.sh").setExecutable(true);
			new ProcessBuilder(path + "app/mac.sh").start();
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Starts the programs shell script */
	private void startShell() {
		try {
			new File(path+"app/start.sh").setExecutable(true);
			new ProcessBuilder(path + "app/start.sh").start();
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Check if os is windows */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/** Check if os is mac */
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/** Check if os is unix */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
}
