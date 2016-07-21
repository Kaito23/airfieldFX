package airfield.application;

import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * Handler for airfieldFX project properties.
 * 
 * @author koetter
 */
public class PropertiesHandler {
	/** The properties */
	private final Properties properties;
	/** Logger */
	final static Logger logger = Logger.getLogger(PropertiesHandler.class);

	/** Default PropertiesHandler */
	public PropertiesHandler() {
		properties = new Properties();
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream("airfield.properties"))) {
			properties.load(stream);
		} catch (final FileNotFoundException e1) {
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Properties nicht gefunden!", "Error", JOptionPane.ERROR_MESSAGE);
			// TODO javafx panel
			logger.error(e1);
		} catch (final IOException e) {
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			// TODO javafx panel
			logger.error(e);
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
			logger.error("Es muss ein gültiger Pfad zu einem Git-Repo angegeben werden!");
			throw new Exception("Es muss ein gültiger Pfad zu einem Git-Repo angegeben werden!");
		}
		return git;
	}
}
