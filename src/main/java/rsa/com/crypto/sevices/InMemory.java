package rsa.com.crypto.sevices;

import org.springframework.stereotype.Service;
import sun.security.rsa.RSAPublicKeyImpl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.*;

@Service
public class InMemory implements Irepository {

    HashMap<BigInteger, KeyPair> keyPairs = new HashMap<>();

    @Override
    public void addKeyPair(KeyPair keyPair) {
        keyPairs.put(((RSAPublicKeyImpl)keyPair.getPublic()).getModulus(),keyPair);
    }

    @Override
    public int getSize() {
        return keyPairs.size();
    }

    @Override
    public KeyPair remove(BigInteger pKey) {
        return keyPairs.remove(pKey);
    }

    @Override
    public List<BigInteger> getAllKeys() {
        Iterator it = keyPairs.entrySet().iterator();
        List<BigInteger> ret =  new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ret.add((BigInteger) pair.getKey());
        }
        return ret;
    }

    @Override
    public KeyPair getKeyPair(BigInteger pKey){
        return keyPairs.get(pKey);
    }
}
