package airfield.secure;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import airfield.application.Utils;

/**
 * Reader for private keys.
 * 
 * @author koetter
 */
public class PrivateKeyReader {

	/**
	 * Get a private key from a file.
	 * 
	 * @param filename
	 *            the path to the private key file
	 * @return the privatekey from file
	 * @throws Exception TODO
	 */
	public final PrivateKey get(final String filename) throws Exception {
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance(Utils.ALGORITHM);
		return kf.generatePrivate(spec);
	}
}