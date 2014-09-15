package eu.inn.biosign.util;

import java.nio.charset.Charset;

public class Charsets {
	 public static Charset toCharset(Charset charset) {
	        return charset == null ? Charset.defaultCharset() : charset;
	    }
}
