package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	// as per project spec
	@Override
	public void validate(ValidateRequest request, StreamObserver<BoolValue> response) {

		try {
			boolean correctPassword = Passwords.isExpectedPassword(request.getPassword(), request.getSalt(),
					request.getHashedPassword());

			System.out.print("Valid" + correctPassword);

			if (correctPassword == true) {
				response.onNext(BoolValue.newBuilder().setValue(true).build());
				response.onCompleted();
			} else {
				response.onNext(BoolValue.newBuilder().setValue(false).build());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end validate 
	
	@Override
	public void hash(HashRequest resquest, StreamObserver<HashResponse> response) {
		try {
			
			byte[] salt = Passwords.getNextSalt();
			byte[] hashPassword = Passwords.hash(resquest.getPassword().toCharArray(), salt);
			
			ByteString saltByteString = ByteString.copyFrom(salt);
			ByteString hashPasswordByteString = ByteString.copyFrom(hashPassword);
			
			System.out.println(saltByteString);
			System.out.println(hashPasswordByteString);
			
			response.onNext(HashResponse.newBuilder().setUserID(resquest.getUserID()).setHashedPassword(hashPasswordByteString).setSalt(saltByteString).build());
			response.onCompleted();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

}
