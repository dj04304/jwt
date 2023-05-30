package com.jun.jwt.config.jwt;

public interface JwtProperties {
	String SECRET = "jun";
	int EXPIRATION_TIME = 864000000; //10일
	String TOKEN_PREFIX = "Bearer";
	String HEADER_STRING = "Authorization";
}
