/*
 */
package airfield.application;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CleanCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author adam-bien.com
 */
public class TakeDown {

	/** The path of the git-repo */
	private final String remotePath;
	/** The path to the directory for the new app */
	private final String localPath;
	/** The Git */
	private Git git;
	/** Logger */
	final static Logger logger = Logger.getLogger(TakeDown.class);

	/**
	 * Initializes with all required information for git.
	 * 
	 * @param localPath
	 *            the path to the folder where the app will be installed
	 * @param remotePath
	 *            the path to the git repository
	 */
	public TakeDown(final String localPath, final String remotePath) {
		this.remotePath = remotePath;
		this.localPath = localPath;
	}

	/** Clones the repo */
	private void initialDownload() {
		try {
			this.git = Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
			System.out.println("+App installed into: " + this.localPath);
		} catch (final GitAPIException ex) {
			System.err.println("--Cannot download files: " + ex.getMessage());
			final Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "Cannot download files: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/** Updates the local working copy */
	private void update() {
		try {
			this.git.revert();
			final CleanCommand setCleanDirectories = this.git.clean().setCleanDirectories(true);
			setCleanDirectories.call();
			setCleanDirectories.call();
			System.out.println("clean directories");
			this.git.clean().call();
			this.git.reset().setMode(ResetCommand.ResetType.HARD).call();
			System.out.println("+Changed and unnecessary files removed");
		} catch (final GitAPIException ex) {
			throw new IllegalStateException("Cannot reset local repository", ex);
		}
		final PullCommand command = this.git.pull();
		try {
			final PullResult pullResult = command.call();
			if (pullResult.isSuccessful()) {
				System.out.println("+Files updated, ready to start!");
			} else {
				System.out.println("--Download was not successful " + pullResult.toString());
				/*
				 * Frame frame = new Frame();
				 * JOptionPane.showMessageDialog(frame,
				 * "Download was not successful " + pullResult.toString(),
				 * "Error", JOptionPane.ERROR_MESSAGE);
				 */
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, an Error Dialog");
				alert.setContentText("Ooops, there was an error!");

				alert.showAndWait();
			}
		} catch (final GitAPIException ex) {
			logger.error(ex);
		}
	}

	/**
	 * Opens the git repo.
	 * 
	 * @return true if the repo can be opened
	 */
	private boolean openLocal() {
		System.out.println("this.localPath = " + this.localPath);
		final File localRepo = new File(this.localPath);
		try {
			this.git = Git.open(localRepo);
		} catch (final IOException ex) {
			System.out.println("-" + ex.getMessage());
			return false;
		}
		System.out.println("+Application already installed at: " + this.localPath);
		return true;
	}

	/** Installes the app or updates it. */
	public final void installOrUpdate() {
		final boolean alreadyInstalled = openLocal();
		if (alreadyInstalled) {
			update();
		} else {
			initialDownload();
		}
	}

}
