package rsa.com.crypto.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Service
public class KeyGenerator {

    @Autowired
    Irepository repository;

    private KeyPairGenerator keyPairGenerator;
    private String cryptoSystem = "RSA";

    public KeyPair generateKeyPair() {
        if (keyPairGenerator==null){
            try {
                keyPairGenerator = KeyPairGenerator.getInstance(cryptoSystem);
                keyPairGenerator.initialize(2048);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No such algorithm " + cryptoSystem);
            }
        }

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        repository.addKeyPair(keyPair);
        return keyPair;
    }

    public int getRepoSize() {
        return repository.getSize();
    }
}
