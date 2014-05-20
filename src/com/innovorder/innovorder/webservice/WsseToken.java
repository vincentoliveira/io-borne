package com.innovorder.innovorder.webservice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.util.Base64;

public class WsseToken {
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_WSSE = "X-WSSE";

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	private String username;
	private String password;
	private String nonce;
	private String createdAt;
	private String digest;

	public WsseToken(String username, String password) {
		this.username = username;
		this.password = password;
		this.createdAt = generateTimestamp();
		this.nonce = generateNonce();
		this.digest = generateDigest();
	}

	private String generateTimestamp() {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}
	
	private String generateNonce() {
		SecureRandom random = new SecureRandom();
		byte seed[] = random.generateSeed(10);
		return bytesToHex(seed);
	}
	
	public static String bytesToHex(byte[] bytes) {
	 final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	 char[] hexChars = new char[bytes.length * 2];
	 int v;
	 for ( int j = 0; j < bytes.length; j++ ) {
	 v = bytes[j] & 0xFF;
	 hexChars[j * 2] = hexArray[v >>> 4];
	 hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	 }
	 return new String(hexChars);
	}

	private String generateDigest() {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			StringBuilder sb = new StringBuilder();
			sb.append(this.nonce);
			sb.append(this.createdAt);
			sb.append(this.password);
			byte sha[] = md.digest(sb.toString().getBytes());
			digest = Base64.encodeToString(sha,Base64.NO_WRAP);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return digest;
	}

	public String getWsseHeader() {
		StringBuilder header = new StringBuilder();
		header.append("UsernameToken Username=\"");
		header.append(this.username);
		header.append("\", PasswordDigest=\"");
		header.append(this.digest);
		header.append("\", Nonce=\"");
		header.append(Base64.encodeToString(this.nonce.getBytes(), Base64.NO_WRAP));
		header.append("\", Created=\"");
		header.append(this.createdAt);
		header.append("\"");
		return header.toString();
	}
	
	public String getAuthorizationHeader() {
		return "WSSE profile=\"UsernameToken\"";
	}
	
}
