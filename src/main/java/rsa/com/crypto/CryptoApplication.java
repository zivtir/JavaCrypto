package rsa.com.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(CryptoApplication.class, args);
		}
		catch (Exception d){
			System.out.println("On main method: " + d.toString());
			System.out.println(d);
		}
	}

}
