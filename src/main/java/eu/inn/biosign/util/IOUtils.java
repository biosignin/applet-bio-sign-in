package eu.inn.biosign.util;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * IOUtils.java is part of BioSignIn project
 * %%
 * Copyright (C) 2014 Innovery SpA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
