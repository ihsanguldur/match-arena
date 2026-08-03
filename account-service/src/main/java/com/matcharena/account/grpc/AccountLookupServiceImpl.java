package com.matcharena.account.grpc;

import com.matcharena.grpc.v1.AccountLookupServiceGrpc;
import com.matcharena.grpc.v1.PingRequest;
import com.matcharena.grpc.v1.PingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AccountLookupServiceImpl extends AccountLookupServiceGrpc.AccountLookupServiceImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder()
                .setMessage("Pong: " + request.getMessage())
                .build());
        responseObserver.onCompleted();
    }
}
