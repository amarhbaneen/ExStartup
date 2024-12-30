package com.example.StartupExercise.AuthConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Utility class for handling JWT operations such as generation, validation, and parsing.
 */

public class JwtUtil {
    private static final String JWT_SECRET = "d43cd4c0caac49dafdd9225cd64fe74698a72090d1d1587663d798b4f1de1cf79818fd37ff778c98f9c79b1944805324697a7eb4c4eb0e3be9886c3c96c7b5e7bbeb0e151b8e43af97ef977bbdfc3aa5644b205a3221ba77e58a1d41a812b94621761f714b546352b853c5b0a16718799d2efa2b4ae0b6dc274690d67f8d82771b484e4c7b61ba810438be4783dc43070629fd8822f497e7ec81cb334b3b734ee19170202723a49262f4e15068014e7e09d0d4b9097d34abc226c5fd2626d3a0ad6341c48bcc9fc6dfdbf476b769a6c5cc4b963ab42d277c218130da4154ae96c9bab8fa1a389ba662e48dc541574dda049590a962bf3af408217175eea2baa3";

    /**
     * Generates a JWT token for the given username.
     * @param username The username to be included in the token.
     * @return The generated JWT token as a String.
     */
    public static String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60*60)).
                signWith(SignatureAlgorithm.HS256,JWT_SECRET).compact();
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public static Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)  // Set the secret key for verification
                .parseClaimsJws(token)  // Parse the JWT token
                .getBody()
                .getExpiration();  // Return the expiration date from the token
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, otherwise false.
     */
    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Check if the token's expiration time is before the current time
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)  // Set the secret key for verification
                .parseClaimsJws(token)  // Parse the JWT token
                .getBody()
                .getSubject();  // Return the subject (username) from the token
    }

    /**
     * Validates the JWT token by comparing the username and checking for expiration.
     *
     * @param token    The JWT token.
     * @param username The username to validate against the token.
     * @return True if the token is valid, otherwise false.
     */
    public static boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));  // Validate by username and expiration date
    }
}
