package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

// Adapted from Distributed Systems Lab
public class PasswordServer {
	public static void main(String[] args) throws IOException, InterruptedException {

		// Variables
		final PasswordServer passwordServer = new PasswordServer();
		passwordServer.start();
		passwordServer.blockUntilShutdown();
	}

	// Server Set Up
	private Server grpcServer;
	private static final Logger logger = Logger.getLogger(PasswordServer.class.getName());
	private static final int PORT = 50551;
	private String msg = "Password Server started, Listening on " + PORT;

	// Start method that builds and starts the server and prints message to logger
	private void start() throws IOException {

		grpcServer = ServerBuilder.forPort(PORT).addService(new PasswordServiceImpl()).build().start();
		logger.info(msg);

	}

	private void stop() {
		if (grpcServer != null) {
			grpcServer.shutdown();
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (grpcServer != null) {
			grpcServer.awaitTermination();
		}
	}

}
