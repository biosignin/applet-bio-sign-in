package eu.inn.tester;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import eu.inn.biometric.signature.managed.impl.CMSEncryptedBdi;
import eu.inn.biometric.signature.managed.impl.IsoSignatureData;
import eu.inn.examples.GpsCoordinates;

public class Test {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	
	private static String certb64 = "MIIB5TCCAU4CCQCjqUIxhGV9rTANBgkqhkiG9w0BAQUFADA3MQswCQYDVQQGEwJJVDEXMBUGA1UEChMOaW5ub3ZlcnkgUy5wLkExDzANBgNVBAMTBlBvc3RlbDAeFw0xMzEwMTAxMjM0MDRaFw0xNDEwMTAxMjM0MDRaMDcxCzAJBgNVBAYTAklUMRcwFQYDVQQKEw5pbm5vdmVyeSBTLnAuQTEPMA0GA1UEAxMGUG9zdGVsMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXzSDWadYfGIfLSBH0jeoHnrp5zAbqei+OWj1fCl1Md+AnqS+Gcz/943j7m1NkddwDmU4j+qUZExC8oElS8ky6kInwUGsHP9ooWj01aeSCMxOVbZVqG5Kxz1khvTIPZ9LMYK5WDAj2wpN/UOU8iHDVAAYnuzV2YuhAgXPIYZZSTwIDAQABMA0GCSqGSIb3DQEBBQUAA4GBAA8TRwI3pv30Xy6m2K3v42QEsytuAPVcJM47AV0EgC9EBUt5x1YPB+RM4wZejs+tfTtcBBQGTJaSTe5VpdBHob2k11LCDIHFdxJAZlk77o9Vs+/IPuLckLXPe2xj3mvP2LwwLh6m3BPQv5K2nX+pe85Of9xQTnl9LeLI59zN3thB";

	@org.junit.Test
	public void testMap() throws Throwable {
		try {
			IsoSignatureData origIso = new IsoSignatureData();
			origIso.setIsoData(new byte[] { '1' });
			GpsCoordinates coord = new GpsCoordinates(41.1212, 11.2323);
		    coord.setKey("Coord1");
//			origIso.getExtendedDatas().add(new AdditionalHashInfo());
//			origIso.getExtendedDatas().add(new NewData());
//			origIso.getExtendedDatas().add(new AdditionalHashInfo());
			String orig = origIso.toOutput();
			System.out.println(orig);
			System.err.println("-------------");
			IsoSignatureData clonedIso = IsoSignatureData.fromXmlDocument(orig);
			String cloned = clonedIso.toOutput();
			System.out.println(cloned);
			Assert.assertEquals(orig, cloned);

			InputStream inStream = new ByteArrayInputStream(Base64.decode(certb64));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (java.security.cert.X509Certificate) cf.generateCertificate(inStream);
			inStream.close();

			List<X509Certificate> certs = new ArrayList<X509Certificate>();
			certs.add(cert);

			KeyStore keystore = KeyStore.getInstance("PKCS12");
			keystore.load(this.getClass().getClassLoader().getResourceAsStream("cipherbiodata.p12"),
					"11111111".toCharArray());
			PrivateKey key = (PrivateKey) keystore.getKey(keystore.aliases().nextElement(), "11111111".toCharArray());

			CMSEncryptedBdi<IsoSignatureData> enc1 = CMSEncryptedBdi.encrypt(origIso, certs, 128);
			
			enc1.setPrivateKey(key);
			String cifra2 = enc1.toOutput();
			System.out.println(cifra2);
			Assert.assertEquals(orig, enc1.decrypt(IsoSignatureData.class).toOutput());
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
	
	
	@org.junit.Test
	public void testMap2() throws Throwable {
		try {
			IsoSignatureData origIso = new IsoSignatureData();
			origIso.setIsoData(new byte[] { '1' });
			GpsCoordinates coord = new GpsCoordinates(41.1212, 11.2323);
		    coord.setKey("Coord1");
		    origIso.getExtendedDatas().add(coord);
			String orig = origIso.toOutput();
			System.out.println(orig);
			System.err.println("-------------");
			IsoSignatureData clonedIso = IsoSignatureData.fromXmlDocument(orig);
			String cloned = clonedIso.toOutput();
			System.out.println(cloned);
			Assert.assertEquals(orig, cloned);

			InputStream inStream = new ByteArrayInputStream(Base64.decode(certb64));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (java.security.cert.X509Certificate) cf.generateCertificate(inStream);
			inStream.close();

			List<X509Certificate> certs = new ArrayList<X509Certificate>();
			certs.add(cert);

			KeyStore keystore = KeyStore.getInstance("PKCS12");
			keystore.load(this.getClass().getClassLoader().getResourceAsStream("cipherbiodata.p12"),
					"11111111".toCharArray());
			PrivateKey key = (PrivateKey) keystore.getKey(keystore.aliases().nextElement(), "11111111".toCharArray());

			CMSEncryptedBdi<IsoSignatureData> enc1 = CMSEncryptedBdi.encrypt(origIso, certs, 128);
			
			enc1.setPrivateKey(key);
			String cifra2 = enc1.toOutput();
			System.out.println(cifra2);
			Assert.assertEquals(orig, enc1.decrypt(IsoSignatureData.class).toOutput());
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
}
