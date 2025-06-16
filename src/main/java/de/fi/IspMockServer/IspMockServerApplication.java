package de.fi.IspMockServer;

import de.fi.IspMockServer.ssl.SSLDisableExample;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IspMockServerApplication {

	public static void main(String[] args) {
		SSLDisableExample.execute();
		SpringApplication.run(IspMockServerApplication.class, args);
	}
}
