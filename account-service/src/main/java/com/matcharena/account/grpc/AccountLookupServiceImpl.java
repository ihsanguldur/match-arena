package com.matcharena.account.grpc;

import com.matcharena.account.match.MatchResultService;
import com.matcharena.grpc.v1.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;

@GrpcService
public class AccountLookupServiceImpl extends AccountLookupServiceGrpc.AccountLookupServiceImplBase {

    private final MatchResultService matchResultService;

    public AccountLookupServiceImpl(MatchResultService matchResultService) {
        super();
        this.matchResultService = matchResultService;
    }

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        responseObserver.onNext(PingResponse.newBuilder()
                .setMessage("Pong: " + request.getMessage())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void reportMatchResult(ReportMatchResultRequest request, StreamObserver<ReportMatchResultResponse> responseObserver) {
        Instant playedAt = Instant.ofEpochSecond(request.getPlayedAt().getSeconds(), request.getPlayedAt().getNanos());

        boolean success = matchResultService.reportResult(request.getSessionId(), request.getParticipantsList(), playedAt);

        responseObserver.onNext(ReportMatchResultResponse.newBuilder()
                .setSuccess(success)
                .build());
        responseObserver.onCompleted();
    }
}
