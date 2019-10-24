package ie.gmit.ds;

import com.google.protobuf.BoolValue;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase{
	
	// as per project spec 
	@Override
	public void validate(ValidateRequest request ,StreamObserver<BoolValue> reponse) {
		
		try {
				boolean correctPassword = Passwords.isExpectedPassword(request.getPassword(), request.getSalt(), request.getHashedPassword());
				
				System.out.print("Valid" + correctPassword);
				
				if(correctPassword == true) {
					
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
	}

}
