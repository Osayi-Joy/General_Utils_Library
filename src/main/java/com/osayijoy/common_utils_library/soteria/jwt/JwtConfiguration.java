package com.osayijoy.common_utils_library.soteria.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.osayijoy.common_utils_library.soteria.config.SoteriaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

	private final SoteriaConfig soteriaConfig;
	
	@Bean
	public KeyStore keyStore() {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(soteriaConfig.getJwtKeyStorePath());
			keyStore.load(resourceAsStream, soteriaConfig.getJwtKeyStorePassword().toCharArray());
			return keyStore;
		} catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Unable to load keystore: {}", soteriaConfig.getJwtKeyStorePath(), e);
		}
		
		throw new IllegalArgumentException("Unable to load keystore");
	}
	
	@Bean
	public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
		try {
			Key key = keyStore.getKey(soteriaConfig.getJwtKeyAlias(), soteriaConfig.getJwtKeyStorePassword().toCharArray());
			if (key instanceof RSAPrivateKey rsaPrivateKey) {
				return rsaPrivateKey;
			}
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Unable to load private key from keystore: {}", soteriaConfig.getJwtKeyStorePath(), e);
		}
		
		throw new IllegalArgumentException("Unable to load private key");
	}
	
	@Bean
	public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
		try {
			Certificate certificate = keyStore.getCertificate(soteriaConfig.getJwtKeyAlias());
			PublicKey publicKey = certificate.getPublicKey();
			
			if (publicKey instanceof RSAPublicKey rsaPublicKey) {
				return rsaPublicKey;
			}
		} catch (KeyStoreException e) {
			log.error("Unable to load private key from keystore: {}", soteriaConfig.getJwtKeyStorePath(), e);
		}
		
		throw new IllegalArgumentException("Unable to load RSA public key");
	}
	
	@Bean
	public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
		return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
	}
}
