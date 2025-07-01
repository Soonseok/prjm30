package com.tech.prjm09.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

		/**
		 * 인수값의 문자열을 SHA-512방식으로 암호화하는 메서드
		 * (단방향 암호화)
		 * @param str 암호화할 문자열 데이터
		 * @return 암호화된 문자열 데이터
		 * @throws NoSuchAlgorithmException 
		 * @throws UnsupportedEncodingException 
		 */
		public static String sha512(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
			// 암호화에 사용할 알고리즘을 지정하여 MessageDigest객체 생성. 예외발생해서 예외처리
			// 알고리즘은 문자열로 지정하는데 다음과 같은 종류가 있다.
			// "MD5", "SHA-256" 등
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			
			// 암호화할 데이터를 byte형 배열로 넣어준다. 예외발생해서 예외처리
			md.update(str.getBytes("utf-8")); 
			
			// md.digest() : 암호화된 데이터를 byte배열로 반환한다.
			 return Base64.getEncoder().encodeToString(md.digest());
		}
		
		/**
		 * 양방향 암호화 중 AES-256 알고리즘으로 암호화하는 메서드
		 * 
		 * @param str 암호화할 문자열
		 * @param key 암호화에 사용할 암호키 문자열(16자 이상)
		 * @return 암호화된 문자열
		 * @throws UnsupportedEncodingException 
		 * @throws NoSuchPaddingException 
		 * @throws NoSuchAlgorithmException 
		 * @throws InvalidAlgorithmParameterException 
		 * @throws InvalidKeyException 
		 * @throws BadPaddingException 
		 * @throws IllegalBlockSizeException 
		 */
		public static String encryptAES256(String str, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			if(key.length()<16) {
				System.out.println("암호키는 16자 이상으로 지정하세요");
				System.out.println("작업을 마칩니다");
				return null;
			}
			byte[] keyBytes = new byte[16];
			System.arraycopy(key.getBytes("utf-8"), 0, keyBytes, 0, keyBytes.length);
			//비밀키 생성 (키에 사용할 byte형 배열과 알고리즘 이름을 지정한다.)
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			// Cipher 객체 생성 및 초기화 (예외처리)
			/*
			 	알고리즘/모드/패딩 (AES/CBC/PKCS5Padding)
			 	- CBC(Cipher Block Chaining Mode) : 동일한 평문 블록과 암호문 블록의 쌍이 발생하지 않도록 이전단계의 암복호화한 결과를 
			 	현 단계에 사용하는 모드를 말한다.
			 	- Padding : CBC에서 작업을할 때 마지막 블록이 블록의 길이가 부족할 때 부족한 부분을 채워넣는 방식을 말한다.
			 */
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// 초기화 벡터값 작성
			// 초기화 벡터(Initial Vector, IV) - 암호문이 패턴화되지 않도록 사용하는 데이터를 말한다.
			// 								  암호화 처리중 첫번째 블록은 암호화할 때 사용되는 값이다.
			String iv = key.substring(0, 16);
			byte[] ivBytes = new byte[16];
			System.arraycopy(iv.getBytes("utf-8"), 0, ivBytes, 0, ivBytes.length);
			// 암호를 옵션 종류에 맞게 초기화한다.
			// 옵션 종류: ENCRYPT_MODE(암호화모드), DECRYP_MODE(복호화모드)
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
			// 암호하할 데이터를 byte형 배열로 공급하여 암호화 작업을 수행한다.
			byte[] encryptBytes = c.doFinal(str.getBytes("utf-8"));
			String enStr = Base64.getEncoder().encodeToString(encryptBytes);
			return enStr;
		}
		/**
		 * 암호화된 데이터를 인수값으로 받아서 원래의 내용으로 복호화하는 메서드
		 * @param str 복원한 암호화된 문자열
		 * @param key 암호키 문자열
		 * @return 복원된 원래의 문자열
		 * @throws UnsupportedEncodingException 
		 * @throws NoSuchPaddingException 
		 * @throws NoSuchAlgorithmException 
		 * @throws InvalidAlgorithmParameterException 
		 * @throws InvalidKeyException 
		 * @throws BadPaddingException 
		 * @throws IllegalBlockSizeException 
		 */
		public static String decryptAES256(String str, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			if(key.length()<16) {
				System.out.println("암호키는 16자 이상으로 지정하세요");
				System.out.println("작업을 마칩니다");
				return null;
			}
			
			byte[] keyBytes = new byte[16];
			System.arraycopy(key.getBytes("utf-8"), 0, keyBytes, 0, keyBytes.length);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			String iv = key.substring(0, 16);
			byte[] ivBytes = new byte[16];
			System.arraycopy(iv.getBytes("utf-8"), 0, ivBytes, 0, ivBytes.length);
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
			// 복원할 암호화된 문자열을 decoding한 byte형 배열을 구한다.
			byte[] byteStr = Base64.getDecoder().decode(str);
			// 암호화된 byte 배열을 원래의 데이터로 복원한 후 문자열로 변환하여 반환한다.
			return new String(c.doFinal(byteStr), "utf-8");
		}
		
}