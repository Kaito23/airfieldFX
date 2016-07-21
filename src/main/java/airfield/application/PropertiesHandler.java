package airfield.application;

import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
	private final Properties properties;

	/** Default PropertiesHandler */
	public PropertiesHandler() {
		properties = new Properties();
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream("airfield.properties"))) {
			properties.load(stream);
		} catch (final FileNotFoundException e1) {
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Properties nicht gefunden!", "Error", JOptionPane.ERROR_MESSAGE);
			// TODO javafx panel
		} catch (final IOException e) {
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			// TODO javafx panel
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
		final String git = properties.getProperty("git");
		if (git == null || git.isEmpty()) {
			throw new Exception("Es muss ein gültiger Pfad zu einem Git-Repo angegeben werden!");
			// TODO
		}
		return git;
	}
}
