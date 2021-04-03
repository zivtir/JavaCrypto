package rsa.com.crypto.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rsa.com.crypto.sevices.Irepository;
import rsa.com.crypto.sevices.KeyGenerator;
import rsa.com.crypto.sevices.SignatureGenerator;
import sun.security.rsa.RSAPublicKeyImpl;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.List;

@RestController
public class Rsa {

    @Autowired
    KeyGenerator keyGenerator;
    @Autowired
    Irepository repository;
    @Autowired
    SignatureGenerator signatureGenerator;


    @PostMapping(value="/rsa")
    public BigInteger generateKey(){
        return ((RSAPublicKeyImpl) keyGenerator.generateKeyPair().getPublic()).getModulus();
    }

    @GetMapping(value = "hello")
    public String hello(){
        PublicKey publicKey =keyGenerator.generateKeyPair().getPublic();
        String algo =  publicKey.getAlgorithm();
        return "hello Crypto..." + algo;
    }

    @DeleteMapping(value="/rsa/{pKey}")
    public void deleteKey(@PathVariable("pKey") BigInteger pKey){
        KeyPair keyPair= repository.remove(pKey);
        if (keyPair==null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Public key does not exist");
    }

    @GetMapping(value ="/rsa")
    public List<BigInteger> all(){
        return repository.getAllKeys();
    }

    @GetMapping(value = "/repoSize")
    public String repoSize(){
        return "Repo size ->" +   repository.getSize();
    }

    @GetMapping(value = "/signData")
    public String signData(@RequestParam(name = "pKey") BigInteger pKey,@RequestParam(name = "data") String data){
        return signatureGenerator.createDigitalSignature(data,pKey);
    }

    @GetMapping(value = "/verifySignature")
    public boolean verifySignature(@RequestParam(name = "pKey") BigInteger pKey,@RequestParam(name = "data") String data,@RequestParam(name = "digitalSignature") String digitalSignature){
        return signatureGenerator.verifySignature(digitalSignature,pKey,data);
    }
    
    
}
