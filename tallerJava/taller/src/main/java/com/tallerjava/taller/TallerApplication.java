package com.tallerjava.taller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class TallerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TallerApplication.class);

		int preferredPort = 8080;
		Map<String, Object> props = new HashMap<>();

		if (isPortAvailable(preferredPort)) {
			props.put("server.port", String.valueOf(preferredPort));
			System.out.println("[TallerApplication] Puerto " + preferredPort + " libre: usando puerto " + preferredPort);
		} else {
			// Si 8080 está ocupado, usar puerto aleatorio para evitar que la app falle al iniciar
			props.put("server.port", "0");
			System.out.println("[TallerApplication] Puerto " + preferredPort + " ocupado: usando puerto aleatorio (server.port=0)");
		}

		app.setDefaultProperties(props);
		app.run(args);
	}

	private static boolean isPortAvailable(int port) {
		try (ServerSocket socket = new ServerSocket(port)) {
			socket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
