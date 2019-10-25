package ie.gmit.ds;

import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PasswordClient {
	
	// logger used to log message for application component
	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncService;
	
	public PasswordClient(String host, int port) {
		
		channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
		
		syncService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncService = PasswordServiceGrpc.newStub(channel);
	}
	
	

}
