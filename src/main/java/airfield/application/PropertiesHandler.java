package airfield.application;

import java.awt.Frame;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Handler for airfieldFX project properties.
 * 
 * @author koetter
 */
public class PropertiesHandler {
	/** The properties */
	private Properties properties;

	/** Default PropertiesHandler */
	public PropertiesHandler() {
		properties = new Properties();
		BufferedInputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream("airfield.properties"));//airfield/ TODO
			properties.load(stream);
		} catch (FileNotFoundException e1) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Properties nicht gefunden!", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				Frame frame = new Frame();
				JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Get the foldername for the app.
	 * 
	 * @return the foldername
	 */
	public final String getFolder() {
		String folder = properties.getProperty("folder");

		if (folder == null || folder.isEmpty()) {
			folder = "app";
		}
		return folder;
	}

	/**
	 * Get the git http url.
	 * 
	 * @return the git repo url
	 * @throws Exception
	 *             thrown if no repo is declared
	 */
	public final String getGit() throws Exception {
		String git = properties.getProperty("git");
		if (git == null || git.isEmpty()) {
			throw new Exception("Es muss ein gültiger Pfad zu einem Git-Repo angegeben werden!");
		}
		return git;
	}
}
