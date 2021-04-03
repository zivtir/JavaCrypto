package rsa.com.crypto.sevices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Service
public class SignatureGenerator {

    @Autowired
    Irepository repository;
    private String unicodeFormat = "UTF-8";

    Signature signature;

    {
        try {
            signature = Signature.getInstance("SHA256WithRSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Algorithm issue with SHA256WithRSA");
        }
    }

    public String createDigitalSignature(String data, BigInteger pKey){
        KeyPair keyPair = getKeyPair(pKey);

        SecureRandom secureRandom = new SecureRandom();
        try {
            signature.initSign(keyPair.getPrivate(), secureRandom);
            byte[] byteData = data.getBytes(unicodeFormat);
            signature.update(byteData);
            byte[] digitalSignature = signature.sign();
            String base64Encoded = Base64.getEncoder().encodeToString(digitalSignature);
            String urlEncoded = URLEncoder.encode(base64Encoded, String.valueOf(StandardCharsets.UTF_8));

            return urlEncoded;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unsupported Encoding " + unicodeFormat);
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Signature Exception" );
        }catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Key Exception" );
        }
    }

    private KeyPair getKeyPair(BigInteger pKey) {
        KeyPair keyPair = repository.getKeyPair(pKey);
        if (keyPair == null){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Public key does not exist");
        }
        return keyPair;
    }

    public boolean verifySignature(String digitalSignature,BigInteger pKey,String data){
        try {
            KeyPair keyPair = getKeyPair(pKey);
            byte[] byteData;
            signature.initVerify(keyPair.getPublic());
            byteData = data.getBytes(unicodeFormat);
            signature.update(byteData);
            byte[] bytesSignature = new byte[0];
            try{
                bytesSignature = Base64.getDecoder().decode(digitalSignature.getBytes());
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Digital signature is corrupted");
            }
            return signature.verify(bytesSignature);
        }catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid key");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Encoding issue");
        }catch (SignatureException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Signature exception");
        }
    }
}
