package airfield.secure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Properties;

import lombok.Setter;

import org.apache.commons.codec.binary.Base64;

import airfield.application.Utils;

/**
 * Signer for applications. Creates the sig.properties and verifies the
 * downloaded content.
 * 
 * @author koetter
 */
public class Signer {

	private String appPath;
	@Setter //TODO DO BETTER ...
	private String privateKeyPath;
	private String publickeyPath;

	/**
	 * Creates a sig.properties file in the app dir.
	 * 
	 * @param local
	 *            the path to the app dir
	 * @param privatekeyFolderPath
	 *            TODO
	 * @param publickeyFolderPath TODO
	 */
	public final void createSignFile(final String privatekeyFolderPath, final String publickeyFolderPath, final String local) {
		System.out.println("publickeyfolderpath > "+ publickeyFolderPath);
		System.out.println("privatekeyFolderPath > "+ privatekeyFolderPath);
		this.appPath = local;
		this.privateKeyPath = privatekeyFolderPath;
		this.publickeyPath = publickeyFolderPath;
		
		System.out.println("create sigfile");

		Properties prop = new Properties();
		OutputStream output = null;
		ArrayList<File> fileList = null;
		try {
			String sigFilePath = local + "/sig.properties";
			output = new FileOutputStream(sigFilePath);

			fileList = new ArrayList<File>();
			Utils.listf(local, fileList);

			// set the properties value
			for (File file : fileList) {
				String fileName = file.getName();
				byte[] bytesFromFile = Utils.getBytesFromFile(file);
				String signature = createSignature(bytesFromFile).toString();
				prop.setProperty(fileName, signature);
			}

			byte[] byteList = Utils.getBytesFromArrayList(fileList);
			String signature = createSignature(byteList);
			prop.setProperty("Signature", signature);

			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get the signature of a file.
	 * 
	 * @param file
	 *            the file as byte array
	 * @return the signature to the overgiven file
	 */
	final String createSignature(final byte[] file) {
		byte[] byteArraySignature = null;
		try {
			PrivateKeyReader privateKeyReader = new PrivateKeyReader();
			PrivateKey privateKey = privateKeyReader.get(privateKeyPath);
			Signature signature = Signature.getInstance(Utils.SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(file, 0, file.length);
			byteArraySignature = signature.sign();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] encryptedByteValue = Base64.encodeBase64(byteArraySignature);
		String returner;
		returner = new String(encryptedByteValue, DEFAULT_CHARSET);
		return returner;
	}

	/** Default charset utf-8 */
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

}
