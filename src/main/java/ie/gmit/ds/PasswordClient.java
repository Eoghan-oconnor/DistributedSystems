package ie.gmit.ds;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class PasswordClient {

	public static void main(String[] args) {
		PasswordClient client = new PasswordClient("localhost", 50051);
	}

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

	public void validate() {

		StreamObserver<BoolValue> response = new StreamObserver<BoolValue>() {
			@Override
			public void onNext(BoolValue boolValue) {
				System.out.println("**" + boolValue.getValue());
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub

			}
		};

		try {
			asyncService.validate(ValidateRequest.newBuilder().setHashedPassword(expectedHash).setSalt(salt)
					.setPassword(test).build(), response);
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return;

	}
	
	public String test = " ";
	public ByteString expectedHash;
	public ByteString salt;
	
	public void hash(int Id, String pwd) {
		
		StreamObserver<HashResponse> reponse = new StreamObserver<HashResponse>() {
			
			@Override
			public void onNext(HashResponse value) {
				// TODO Auto-generated method stub
				
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
			HashRequest hr = HashRequest.newBuilder().setUserID(Id).setPassword(pwd).build();
			asyncService.hash(hr, reponse);
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		return;
	}

}
