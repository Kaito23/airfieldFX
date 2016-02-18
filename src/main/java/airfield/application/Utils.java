package airfield.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import lombok.Getter;

/**
 * Utils for airfield.
 * 
 * @author koetter
 */
public class Utils {

	/** Algorithm DSA or RSA */
	public static String ALGORITHM = "DSA";
	/** SHA1withRSA || SHAwithDSA */
	public static String SIGNATURE_ALGORITHM = "SHAwithDSA";

	/**
	 * Get the bytes of a file.
	 * 
	 * @param file
	 *            the file
	 * @return the bytes of the file
	 * @throws IOException
	 *             TODO
	 */
	public static byte[] getBytesFromFile(final File file) {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[BYTE_BUFFER];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ous != null) {
					ous.close();
				}
				if (ios != null) {
					ios.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ous.toByteArray();
	}

	/**
	 * TODO
	 * 
	 * @param fileList
	 *            TODO
	 * @return TODO
	 * @throws IOException
	 *             TODO
	 */
	public static byte[] getBytesFromArrayList(final ArrayList<File> fileList) throws IOException {
		// TODO besser arrayList<String>?
		byte[] yourBytes;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(fileList);
			yourBytes = bos.toByteArray();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		return yourBytes;
	}

	/**
	 * Adds files recursive to a list from a directory.
	 * 
	 * @param directoryName
	 *            TODO
	 * @param files
	 *            TODO
	 */
	public static void listf(final String directoryName, final ArrayList<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory() && !file.getName().startsWith(".") && !file.getName().equals("sig.properties")) {
				listf(file.getAbsolutePath(), files);
			} else if (file.getName().startsWith(".")) {
				System.out.println("Ignoring " + file.getName());
			} else if(file.getName().equals("sig.properties")) {
				System.out.println("Ignoring sig.properties");
			}
		}
	}

	/**
	 * Get md5 of a file
	 * 
	 * @param file
	 *            TODO
	 * @return TODO
	 * @throws FileNotFoundException
	 *             TODO
	 * @throws IOException
	 *             TODO
	 * @throws NoSuchAlgorithmException
	 *             TODO
	 */
	public static final String md5Files(final File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		FileInputStream fis = new FileInputStream(file);
		String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}

	/** 4096 */
	private static final int BYTE_BUFFER = 4096;
}
