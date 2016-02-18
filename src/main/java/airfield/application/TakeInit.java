package airfield.application;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * TODO
 * @author koetter
 */
public class TakeInit {

	/**
	 * TODO
	 * @param localPath
	 * @param remotePath
	 * @param password
	 */
	public void loadRepo(final String localPath, final String remotePath, final String password) {
		// TakeDown tdown = new TakeDown(localPath, remotePath);
		// tdown.installOrUpdate();

		SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
			@Override
			protected JSch createDefaultJSch(final FS fs) throws JSchException {
				JSch defaultJSch = super.createDefaultJSch(fs);
				defaultJSch.removeAllIdentity();

				String ppk = "C:/Users/koetter/Documents/privatekey.ppk";
				System.out.println("exists: " + new File(ppk).exists());

				defaultJSch.addIdentity(ppk, password);
				return defaultJSch;
			}

			@Override
			protected void configure(final Host hc, final Session session) {
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
			};
		};

		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(remotePath);
		cloneCommand.setTransportConfigCallback(new TransportConfigCallback() {
			@Override
			public void configure(final Transport transport) {
				SshTransport sshTransport = (SshTransport) transport;
				sshTransport.setSshSessionFactory(sshSessionFactory);
			}
		});

		cloneCommand.setDirectory(new File(localPath));
		try {
			cloneCommand.call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
