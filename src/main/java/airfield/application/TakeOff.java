/*
 */
package airfield.application;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 *
 * @author adam-bien.com
 */
public class TakeOff {

	/** The Git */
	private Git git;

	/**
	 * Initializes with all required information for git.
	 * 
	 * @param localPath
	 *            the path to the folder where the app will be installed
	 * @param remotePath
	 *            the path to the git repository
	 */
	public TakeOff(final String localPath, final String remotePath) {
		File repoDir = new File(localPath);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(repoDir)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            System.out.println("Having repository: " + repository.getDirectory());

            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.getRef("refs/heads/master");
            System.out.println("Ref of refs/heads/master: " + head);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void commit() {
		try {
			this.git.commit().setMessage("test").call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Updates the local working copy */
	private void push() {
		//this.git.add() TODO
		try {
			this.git.push().call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Installes the app or updates it. */
	public final void upload() {
		commit();
		push();
	}

}
