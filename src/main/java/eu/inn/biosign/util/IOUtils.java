package eu.inn.biosign.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

	private static final int EOF = -1;

	

	public static byte[] toByteArray(InputStream input, long size)
			throws IOException {

		if (size > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"Size cannot be greater than Integer max value: " + size);
		}

		return toByteArray(input, (int) size);
	}

	public static byte[] toByteArray(InputStream input, int size)
			throws IOException {

		if (size < 0) {
			throw new IllegalArgumentException(
					"Size must be equal or greater than zero: " + size);
		}

		if (size == 0) {
			return new byte[0];
		}

		byte[] data = new byte[size];
		int offset = 0;
		int readed;

		while (offset < size
				&& (readed = input.read(data, offset, size - offset)) != EOF) {
			offset += readed;
		}

		if (offset != size) {
			throw new IOException("Unexpected readed size. current: " + offset
					+ ", excepted: " + size);
		}

		return data;
	}

	public static void closeQuietly(InputStream input) {
		closeQuietly((Closeable) input);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}


}
