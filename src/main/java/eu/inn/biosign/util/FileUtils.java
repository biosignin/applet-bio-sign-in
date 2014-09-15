package eu.inn.biosign.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class FileUtils {
	public static byte[] readFileToByteArray(File file) throws IOException {
		InputStream in = null;
		try {
			in = openInputStream(file);
			return IOUtils.toByteArray(in, file.length());
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static void writeStringToFile(File file, String data)
			throws IOException {
		writeStringToFile(file, data, Charset.defaultCharset(), false);
	}

	public static void writeStringToFile(File file, String data,
			Charset encoding, boolean append) throws IOException {
		OutputStream out = null;
		try {
			out = FileUtils.openOutputStream(file, append);
			write(data, out, encoding);
			out.close(); // don't swallow close Exception if copy completes
							// normally
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void writeByteArrayToFile(File file, byte[] data,
			 boolean append) throws IOException {
		OutputStream out = null;
		try {
			out = FileUtils.openOutputStream(file, append);
			write(data, out);
			out.close(); // don't swallow close Exception if copy completes
							// normally
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void write(String data, OutputStream output, Charset encoding)
			throws IOException {
		if (data != null) {
			output.write(data.getBytes(Charsets.toCharset(encoding)));
		}
	}

	public static void write(byte[] data, OutputStream output)
			throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file
					+ "' does not exist");
		}
		return new FileInputStream(file);
	}

	public static FileOutputStream openOutputStream(File file, boolean append)
			throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file
						+ "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent
							+ "' could not be created");
				}
			}
		}
		return new FileOutputStream(file, append);
	}
}
