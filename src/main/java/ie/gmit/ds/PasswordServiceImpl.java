package ie.gmit.ds;

import com.google.protobuf.BoolValue;

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

	}

}
