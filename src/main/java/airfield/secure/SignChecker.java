package airfield.secure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

import airfield.application.Utils;

/**
 * Checks all files in app dir so no corrupt files are in it.
 * 
 * @author koetter
 */
public class SignChecker {

	/** Path to the publickey */
	private String publickeyPath;

	/**
	 * Verifies the files.
	 * 
	 * @param keyFolderPath
	 *            TODO
	 * @param local
	 *            the path to the app directory
	 * @return true if files are correct
	 */
	public final boolean verify(final String keyFolderPath, final String local) {
		this.publickeyPath = keyFolderPath;
		
		System.out.println("++ publicKEy: " + publickeyPath);

		boolean filesVerified = false;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String propertiesPath = local + File.separator + "sig.properties";
			input = new FileInputStream(propertiesPath);
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			String propertySignature = prop.getProperty("Signature");
			System.out.println("propertySignature > "+propertySignature);

			ArrayList<File> fileList = new ArrayList<File>();
			Utils.listf(local, fileList);

			byte[] bytesFromArrayList = Utils.getBytesFromArrayList(fileList);
			filesVerified = verify(bytesFromArrayList, propertySignature);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filesVerified;
	}

	/**
	 * Verifies the files.
	 * 
	 * @param data
	 *            the data of all files from a filelist
	 * @param signature
	 *            the signature from properties
	 * @return true if files are verified correct
	 * @throws SignatureException
	 *             TODO
	 */
	private boolean verify(final byte[] data, final String signature) {
		boolean filesVerified = false;
		try {
			Signature sign = Signature.getInstance(Utils.SIGNATURE_ALGORITHM);
			PublicKeyReader publicKeyReader = new PublicKeyReader();
			PublicKey publicKey = publicKeyReader.get(publickeyPath);
			sign.initVerify(publicKey);
			sign.update(data);
			filesVerified = sign.verify(Base64.decodeBase64(signature.getBytes("UTF-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return filesVerified;
	}

}
