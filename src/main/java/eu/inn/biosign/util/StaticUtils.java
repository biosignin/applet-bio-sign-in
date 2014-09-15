package eu.inn.biosign.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.inn.biosign.DeviceManager;

public class StaticUtils {
	public StaticUtils() {
	}

	public static String[] getResourceListing(Class<?> clazz, String path) {

		if (!path.endsWith("/"))
			path += "/";
		try {
			String ret = new String(getResourceUsingFileStreams(clazz.getClassLoader().getResourceAsStream(
					path + "list")));
			return StringUtils.split(ret, "\r\n");
		} catch (Exception ex) {
			System.out.println("cannot list directory " + path);
			ex.printStackTrace();
			return new String[0];
		}
	}

	public static byte[] getResourceUsingFileStreams(InputStream source) {

		ByteArrayOutputStream output = null;
		try {

			output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = source.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			output.flush();
			return output.toByteArray();
		} catch (Exception ex) {
			return null;
		} finally {
			try {
				source.close();
				output.close();
			} catch (Exception ex) {
			}
		}
	}

	private static void copyFileUsingFileStreams(InputStream source, File dest) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = source.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			try {
				source.close();
				output.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static File copy(String destFolder, String destFileName, String internalPath, boolean overWrite) {
		try {
			File dest = new File(System.getProperty("user.home") + "\\" + destFolder);
			if (!dest.exists()) {
				if(!dest.mkdirs()) {
					dest = new File("/tmp/" + destFolder);
					if (!dest.exists()) {
						if(!dest.mkdirs()) {
							throw new Exception("No matter how hard I try, I'm unable to create this dir: "+ dest.getAbsolutePath());
						}
					}
				}
			}
			File dest2 = new File(dest.getAbsolutePath() + System.getProperty("file.separator") + destFileName);
			if (dest2.exists() && overWrite) {
				dest2.delete();
				Thread.sleep(100);
			}
			if (!dest2.exists()) {
				InputStream nativeSrc = DeviceManager.class.getClassLoader().getResourceAsStream(internalPath);
				copyFileUsingFileStreams(nativeSrc, dest2);
				Thread.sleep(100);
			}
			return dest2;
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Unable to copy " + internalPath);
			return null;
		}
	}

	public static boolean loadNative(String destFolder, String destFileName, String internalPath, boolean overWrite) {
		try {
			File dest2 = copy(destFolder, destFileName, internalPath, overWrite);
			if (dest2 == null)
				return false;
			System.load(dest2.getAbsolutePath());
			return true;
		} catch (final Error e) {
			e.printStackTrace();
			System.out.println("Unable to load " + internalPath);
			return false;
		}
	}

}
