package ie.gmit.ds;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class PasswordClient {

	
	// logger used to log message for application component
	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());

	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncService;

	public PasswordClient(String host, int port) {

		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		syncService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public String test = " ";
	public ByteString expectedHash;
	public ByteString salt;

	public void validate() {
		// Debugs
		System.out.println("In Validate Method");


		StreamObserver<BoolValue> response = new StreamObserver<BoolValue>() {

			@Override
			public void onNext(BoolValue boolValue) {
				//logger.info("*");
				//logger.info("onNext Method");
				if (boolValue.getValue()) {
					logger.info("Login success");
					//System.out.println(boolValue.getValue());
				} else {
					logger.info("Invalid");
				}
			}

			@Override
			public void onError(Throwable t) {
				logger.info("Error" + t.getLocalizedMessage());

			}

			@Override
			public void onCompleted() {
				logger.info("completed");
			}
		};

		try {
			asyncService.validate(ValidateRequest.newBuilder().setHashedPassword(expectedHash).setSalt(salt)
					.setPassword(test).build(), response);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return;

	}

	

	public void hash(int Id, String pwd) {
		
		System.out.println("In hash method");

		StreamObserver<HashResponse> reponse = new StreamObserver<HashResponse>() {

			@Override
			public void onNext(HashResponse value) {
				System.out.println(value.getUserID());
				salt = value.getSalt();
				expectedHash = value.getHashedPassword();

			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub

			}
		};

		try {
			logger.info("Getting items");
			HashRequest hr = HashRequest.newBuilder().setUserID(Id).setPassword(pwd).build();
			asyncService.hash(hr, reponse);
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		return;
	}
	
	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 50051);
		Scanner console = new Scanner(System.in);
		int Id;
		String pwd;
		String quit = "";

		while (!quit.equalsIgnoreCase("Y")) {
			System.out.println("********GRPC-CLIENT********");
			System.out.println("\nEnter Id: ");
			Id = console.nextInt();
			System.out.println("Enter Password: \n");
			pwd = console.next();
			System.out.println("Enter test: \n");
			client.test = console.next();

			ByteString pass = ByteString.copyFrom(pwd.getBytes());

			try {

				client.hash(Id, pwd);
				client.validate();

				System.out.print("Press Y to quit and N to Continue: ");
				quit = console.next();
				if (quit.equalsIgnoreCase("Y")) {
					System.exit(0);
				}

			} finally {
				Thread.currentThread().join();
			}

		}
	}


}
