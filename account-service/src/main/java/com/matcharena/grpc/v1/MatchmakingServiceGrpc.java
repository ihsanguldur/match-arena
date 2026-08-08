package com.matcharena.grpc.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 *
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class MatchmakingServiceGrpc {

    public static final java.lang.String SERVICE_NAME = "match_arena.v1.MatchmakingService";
    private static final int METHODID_JOIN_QUEUE = 0;
    private static final int METHODID_LEAVE_QUEUE = 1;
    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.matcharena.grpc.v1.JoinQueueRequest,
            com.matcharena.grpc.v1.JoinQueueUpdate> getJoinQueueMethod;
    private static volatile io.grpc.MethodDescriptor<com.matcharena.grpc.v1.LeaveQueueRequest,
            com.matcharena.grpc.v1.LeaveQueueResponse> getLeaveQueueMethod;
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private MatchmakingServiceGrpc() {
    }

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "JoinQueue",
            requestType = com.matcharena.grpc.v1.JoinQueueRequest.class,
            responseType = com.matcharena.grpc.v1.JoinQueueUpdate.class,
            methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
    public static io.grpc.MethodDescriptor<com.matcharena.grpc.v1.JoinQueueRequest,
            com.matcharena.grpc.v1.JoinQueueUpdate> getJoinQueueMethod() {
        io.grpc.MethodDescriptor<com.matcharena.grpc.v1.JoinQueueRequest, com.matcharena.grpc.v1.JoinQueueUpdate> getJoinQueueMethod;
        if ((getJoinQueueMethod = MatchmakingServiceGrpc.getJoinQueueMethod) == null) {
            synchronized (MatchmakingServiceGrpc.class) {
                if ((getJoinQueueMethod = MatchmakingServiceGrpc.getJoinQueueMethod) == null) {
                    MatchmakingServiceGrpc.getJoinQueueMethod = getJoinQueueMethod =
                            io.grpc.MethodDescriptor.<com.matcharena.grpc.v1.JoinQueueRequest, com.matcharena.grpc.v1.JoinQueueUpdate>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "JoinQueue"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.matcharena.grpc.v1.JoinQueueRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.matcharena.grpc.v1.JoinQueueUpdate.getDefaultInstance()))
                                    .setSchemaDescriptor(new MatchmakingServiceMethodDescriptorSupplier("JoinQueue"))
                                    .build();
                }
            }
        }
        return getJoinQueueMethod;
    }

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "LeaveQueue",
            requestType = com.matcharena.grpc.v1.LeaveQueueRequest.class,
            responseType = com.matcharena.grpc.v1.LeaveQueueResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.matcharena.grpc.v1.LeaveQueueRequest,
            com.matcharena.grpc.v1.LeaveQueueResponse> getLeaveQueueMethod() {
        io.grpc.MethodDescriptor<com.matcharena.grpc.v1.LeaveQueueRequest, com.matcharena.grpc.v1.LeaveQueueResponse> getLeaveQueueMethod;
        if ((getLeaveQueueMethod = MatchmakingServiceGrpc.getLeaveQueueMethod) == null) {
            synchronized (MatchmakingServiceGrpc.class) {
                if ((getLeaveQueueMethod = MatchmakingServiceGrpc.getLeaveQueueMethod) == null) {
                    MatchmakingServiceGrpc.getLeaveQueueMethod = getLeaveQueueMethod =
                            io.grpc.MethodDescriptor.<com.matcharena.grpc.v1.LeaveQueueRequest, com.matcharena.grpc.v1.LeaveQueueResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "LeaveQueue"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.matcharena.grpc.v1.LeaveQueueRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.matcharena.grpc.v1.LeaveQueueResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new MatchmakingServiceMethodDescriptorSupplier("LeaveQueue"))
                                    .build();
                }
            }
        }
        return getLeaveQueueMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static MatchmakingServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceStub>() {
                    @java.lang.Override
                    public MatchmakingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MatchmakingServiceStub(channel, callOptions);
                    }
                };
        return MatchmakingServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports all types of calls on the service
     */
    public static MatchmakingServiceBlockingV2Stub newBlockingV2Stub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceBlockingV2Stub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceBlockingV2Stub>() {
                    @java.lang.Override
                    public MatchmakingServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MatchmakingServiceBlockingV2Stub(channel, callOptions);
                    }
                };
        return MatchmakingServiceBlockingV2Stub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static MatchmakingServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceBlockingStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceBlockingStub>() {
                    @java.lang.Override
                    public MatchmakingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MatchmakingServiceBlockingStub(channel, callOptions);
                    }
                };
        return MatchmakingServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static MatchmakingServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceFutureStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<MatchmakingServiceFutureStub>() {
                    @java.lang.Override
                    public MatchmakingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new MatchmakingServiceFutureStub(channel, callOptions);
                    }
                };
        return MatchmakingServiceFutureStub.newStub(factory, channel);
    }

    public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                        getJoinQueueMethod(),
                        io.grpc.stub.ServerCalls.asyncServerStreamingCall(
                                new MethodHandlers<
                                        com.matcharena.grpc.v1.JoinQueueRequest,
                                        com.matcharena.grpc.v1.JoinQueueUpdate>(
                                        service, METHODID_JOIN_QUEUE)))
                .addMethod(
                        getLeaveQueueMethod(),
                        io.grpc.stub.ServerCalls.asyncUnaryCall(
                                new MethodHandlers<
                                        com.matcharena.grpc.v1.LeaveQueueRequest,
                                        com.matcharena.grpc.v1.LeaveQueueResponse>(
                                        service, METHODID_LEAVE_QUEUE)))
                .build();
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (MatchmakingServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new MatchmakingServiceFileDescriptorSupplier())
                            .addMethod(getJoinQueueMethod())
                            .addMethod(getLeaveQueueMethod())
                            .build();
                }
            }
        }
        return result;
    }

    /**
     *
     */
    public interface AsyncService {

        /**
         *
         */
        default void joinQueue(com.matcharena.grpc.v1.JoinQueueRequest request,
                               io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.JoinQueueUpdate> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getJoinQueueMethod(), responseObserver);
        }

        /**
         *
         */
        default void leaveQueue(com.matcharena.grpc.v1.LeaveQueueRequest request,
                                io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.LeaveQueueResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLeaveQueueMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service MatchmakingService.
     */
    public static abstract class MatchmakingServiceImplBase
            implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return MatchmakingServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service MatchmakingService.
     */
    public static final class MatchmakingServiceStub
            extends io.grpc.stub.AbstractAsyncStub<MatchmakingServiceStub> {
        private MatchmakingServiceStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchmakingServiceStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchmakingServiceStub(channel, callOptions);
        }

        /**
         *
         */
        public void joinQueue(com.matcharena.grpc.v1.JoinQueueRequest request,
                              io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.JoinQueueUpdate> responseObserver) {
            io.grpc.stub.ClientCalls.asyncServerStreamingCall(
                    getChannel().newCall(getJoinQueueMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         *
         */
        public void leaveQueue(com.matcharena.grpc.v1.LeaveQueueRequest request,
                               io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.LeaveQueueResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getLeaveQueueMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service MatchmakingService.
     */
    public static final class MatchmakingServiceBlockingV2Stub
            extends io.grpc.stub.AbstractBlockingStub<MatchmakingServiceBlockingV2Stub> {
        private MatchmakingServiceBlockingV2Stub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchmakingServiceBlockingV2Stub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchmakingServiceBlockingV2Stub(channel, callOptions);
        }

        /**
         *
         */
        @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
        public io.grpc.stub.BlockingClientCall<?, com.matcharena.grpc.v1.JoinQueueUpdate>
        joinQueue(com.matcharena.grpc.v1.JoinQueueRequest request) {
            return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
                    getChannel(), getJoinQueueMethod(), getCallOptions(), request);
        }

        /**
         *
         */
        public com.matcharena.grpc.v1.LeaveQueueResponse leaveQueue(com.matcharena.grpc.v1.LeaveQueueRequest request) throws io.grpc.StatusException {
            return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
                    getChannel(), getLeaveQueueMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do limited synchronous rpc calls to service MatchmakingService.
     */
    public static final class MatchmakingServiceBlockingStub
            extends io.grpc.stub.AbstractBlockingStub<MatchmakingServiceBlockingStub> {
        private MatchmakingServiceBlockingStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchmakingServiceBlockingStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchmakingServiceBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public java.util.Iterator<com.matcharena.grpc.v1.JoinQueueUpdate> joinQueue(
                com.matcharena.grpc.v1.JoinQueueRequest request) {
            return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
                    getChannel(), getJoinQueueMethod(), getCallOptions(), request);
        }

        /**
         *
         */
        public com.matcharena.grpc.v1.LeaveQueueResponse leaveQueue(com.matcharena.grpc.v1.LeaveQueueRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getLeaveQueueMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service MatchmakingService.
     */
    public static final class MatchmakingServiceFutureStub
            extends io.grpc.stub.AbstractFutureStub<MatchmakingServiceFutureStub> {
        private MatchmakingServiceFutureStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchmakingServiceFutureStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchmakingServiceFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<com.matcharena.grpc.v1.LeaveQueueResponse> leaveQueue(
                com.matcharena.grpc.v1.LeaveQueueRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(
                    getChannel().newCall(getLeaveQueueMethod(), getCallOptions()), request);
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final AsyncService serviceImpl;
        private final int methodId;

        MethodHandlers(AsyncService serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_JOIN_QUEUE:
                    serviceImpl.joinQueue((com.matcharena.grpc.v1.JoinQueueRequest) request,
                            (io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.JoinQueueUpdate>) responseObserver);
                    break;
                case METHODID_LEAVE_QUEUE:
                    serviceImpl.leaveQueue((com.matcharena.grpc.v1.LeaveQueueRequest) request,
                            (io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.LeaveQueueResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class MatchmakingServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        MatchmakingServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.matcharena.grpc.v1.Matchmaking.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("MatchmakingService");
        }
    }

    private static final class MatchmakingServiceFileDescriptorSupplier
            extends MatchmakingServiceBaseDescriptorSupplier {
        MatchmakingServiceFileDescriptorSupplier() {
        }
    }

    private static final class MatchmakingServiceMethodDescriptorSupplier
            extends MatchmakingServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final java.lang.String methodName;

        MatchmakingServiceMethodDescriptorSupplier(java.lang.String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }
}
