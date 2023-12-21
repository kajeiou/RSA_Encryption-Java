package com.project;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialization of variables
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the message to encrypt: ");
        String msg = scanner.nextLine();
        String encryptedMessage = null;
        String decryptedMessage = null;

        // Definition of values
        BigInteger p = generateRandomPrime();
        BigInteger q = generateRandomPrime();
        BigInteger n = p.multiply(q);

        // Calculation of Euler's totient function: phi(n) = (p - 1) * (q - 1)
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = findSmallestE(m);
        // Verification to ensure "d" is the multiplicative inverse of "e"
        BigInteger d = e.modInverse(m);

        // Display values
        System.out.println("P value: " + p);
        System.out.println("Q value: " + q);
        System.out.println("N value: " + n);
        System.out.println("E value: " + e);
        System.out.println("D value: " + d);

        // Encrypted message - RSA_encrypt()
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);

        StringBuilder encryptedStringBuilder = new StringBuilder();
        for (byte b : msgBytes) {
            BigInteger msgBigInt = new BigInteger(new byte[]{b});
            BigInteger encrypted = msgBigInt.modPow(e, n);
            encryptedStringBuilder.append(encrypted).append(" ");
        }
        encryptedMessage = encryptedStringBuilder.toString().trim();

        System.out.println("Encrypted message: " + encryptedMessage);

        // Decrypted message - RSA_decrypt()
        String[] encryptedParts = encryptedMessage.split(" ");
        StringBuilder decryptedStringBuilder = new StringBuilder();
        for (String part : encryptedParts) {
            BigInteger encryptedBigInt = new BigInteger(part);
            BigInteger decrypted = encryptedBigInt.modPow(d, n);
            decryptedStringBuilder.append((char) decrypted.intValueExact());
        }
        decryptedMessage = decryptedStringBuilder.toString();

        System.out.println("Decrypted message: " + decryptedMessage);

        scanner.close();
    }

    // Function to find the smallest e
    private static BigInteger findSmallestE(BigInteger m) {
        BigInteger e = new BigInteger("2");

        while (e.compareTo(m) < 0 && !e.gcd(m).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }

        if (e.compareTo(m) < 0) {
            return e;
        } else {
            throw new RuntimeException("No suitable value for e found.");
        }
    }

    // Function to generate a random prime number
    private static BigInteger generateRandomPrime() {
        // Use 2048 bits for increased security
        return BigInteger.probablePrime(2048, new SecureRandom());
    }
}
