package rsa.com.crypto.sevices;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.List;

public interface Irepository {
    void addKeyPair(KeyPair keyPair);
    int getSize();

    KeyPair remove(BigInteger pKey);

    List<BigInteger> getAllKeys();
    KeyPair getKeyPair(BigInteger pKey);
}
