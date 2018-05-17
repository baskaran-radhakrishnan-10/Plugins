package com.equiniti.qa_report.security.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.equiniti.qa_report.exception.api.exception.SecurityException;
import com.equiniti.qa_report.exception.api.faultcode.SecurityFaultCodes;



/**
 * -----------------------------------------------------------------------------
 * The following example implements a class for encrypting and decrypting
 * strings using several Cipher algorithms. The class is created with a key and
 * can be used repeatedly to encrypt and decrypt strings using that key.
 * Some of the more popular algorithms are:
 *      Blowfish
 *      DES
 *      DESede
 *      PBEWithMD5AndDES
 *      PBEWithMD5AndTripleDES
 *      TripleDES
 * 
 * @version 1.0
 * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
 * @author  Baskaran Radhakrishnan
 * -----------------------------------------------------------------------------
 */

public class EncryptionDecryption {

	private Cipher ecipher;

	private Cipher dcipher;

	/**
	 * Constructor used to create this object.  Responsible for setting
	 * and initializing this object's encrypter and decrypter Chipher instances
	 * given a Pass Phrase and algorithm.
	 * @param passPhrase Pass Phrase used to initialize both the encrypter and
	 *                   decrypter instances.
	 * @throws SecurityException 
	 */
	public EncryptionDecryption(String passPhrase) throws SecurityException {

		// 8-bytes Salt
		byte[] salt = {
				(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
				(byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
		};

		// Iteration count
		int iterationCount = 10;

		try {

			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameters to the cipthers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		} catch (InvalidAlgorithmParameterException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_INIT_FAILED_ERROR, e);
		} catch (InvalidKeySpecException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_INIT_FAILED_ERROR, e);
		} catch (NoSuchPaddingException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_INIT_FAILED_ERROR, e);
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_INIT_FAILED_ERROR, e);
		} catch (InvalidKeyException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_INIT_FAILED_ERROR, e);
		}
	}


	/**
	 * Takes a single String as an argument and returns an Encrypted version
	 * of that String.
	 * @param str String to be encrypted
	 * @return <code>String</code> Encrypted version of the provided String
	 * @throws SecurityException 
	 */
	@SuppressWarnings({ "restriction", "hiding" })
	public String encrypt(String str) throws SecurityException {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return new sun.misc.BASE64Encoder().encode(enc);

		} catch (BadPaddingException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_ENCRIPTION_FAILED_ERROR, e);
		} catch (IllegalBlockSizeException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_ENCRIPTION_FAILED_ERROR, e);
		} catch (UnsupportedEncodingException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_ENCRIPTION_FAILED_ERROR, e);
		} catch (IOException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_ENCRIPTION_FAILED_ERROR, e);
		}
	}


	/**
	 * Takes a encrypted String as an argument, decrypts and returns the 
	 * decrypted String.
	 * @param str Encrypted String to be decrypted
	 * @return <code>String</code> Decrypted version of the provided String
	 * @throws SecurityException 
	 */
	@SuppressWarnings("restriction")
	public String decrypt(String str) throws SecurityException {

		try {

			// Decode base64 to get bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");

		} catch (BadPaddingException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_DECRIPTION_FAILED_ERROR, e);
		} catch (IllegalBlockSizeException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_DECRIPTION_FAILED_ERROR, e);
		} catch (UnsupportedEncodingException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_DECRIPTION_FAILED_ERROR, e);
		} catch (IOException e) {
			throw new SecurityException(SecurityFaultCodes.SECURITY_CRYPTO_DECRIPTION_FAILED_ERROR, e);
		}
	}
	
	public static void main(String args[]) throws SecurityException{
		long t1,t2=0L;
		t1=System.nanoTime();
		EncryptionDecryption obj=new EncryptionDecryption("pancredit-team");
		String encripted=obj.encrypt("pancredit");
		System.out.println(encripted);
		System.out.println(obj.decrypt(encripted));
		t2=System.nanoTime();
		System.out.println("Time Taken : "+(t2-t1));
	}
	

}
