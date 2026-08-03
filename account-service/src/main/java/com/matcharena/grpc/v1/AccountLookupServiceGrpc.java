package com.matcharena.grpc.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class AccountLookupServiceGrpc {

  private AccountLookupServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "match_arena.v1.AccountLookupService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PingRequest,
      com.matcharena.grpc.v1.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = com.matcharena.grpc.v1.PingRequest.class,
      responseType = com.matcharena.grpc.v1.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PingRequest,
      com.matcharena.grpc.v1.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PingRequest, com.matcharena.grpc.v1.PingResponse> getPingMethod;
    if ((getPingMethod = AccountLookupServiceGrpc.getPingMethod) == null) {
      synchronized (AccountLookupServiceGrpc.class) {
        if ((getPingMethod = AccountLookupServiceGrpc.getPingMethod) == null) {
          AccountLookupServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<com.matcharena.grpc.v1.PingRequest, com.matcharena.grpc.v1.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AccountLookupServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.matcharena.grpc.v1.ReportMatchResultRequest,
      com.matcharena.grpc.v1.ReportMatchResultResponse> getReportMatchResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReportMatchResult",
      requestType = com.matcharena.grpc.v1.ReportMatchResultRequest.class,
      responseType = com.matcharena.grpc.v1.ReportMatchResultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.matcharena.grpc.v1.ReportMatchResultRequest,
      com.matcharena.grpc.v1.ReportMatchResultResponse> getReportMatchResultMethod() {
    io.grpc.MethodDescriptor<com.matcharena.grpc.v1.ReportMatchResultRequest, com.matcharena.grpc.v1.ReportMatchResultResponse> getReportMatchResultMethod;
    if ((getReportMatchResultMethod = AccountLookupServiceGrpc.getReportMatchResultMethod) == null) {
      synchronized (AccountLookupServiceGrpc.class) {
        if ((getReportMatchResultMethod = AccountLookupServiceGrpc.getReportMatchResultMethod) == null) {
          AccountLookupServiceGrpc.getReportMatchResultMethod = getReportMatchResultMethod =
              io.grpc.MethodDescriptor.<com.matcharena.grpc.v1.ReportMatchResultRequest, com.matcharena.grpc.v1.ReportMatchResultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReportMatchResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.ReportMatchResultRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.ReportMatchResultResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AccountLookupServiceMethodDescriptorSupplier("ReportMatchResult"))
              .build();
        }
      }
    }
    return getReportMatchResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AccountLookupServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceStub>() {
        @java.lang.Override
        public AccountLookupServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccountLookupServiceStub(channel, callOptions);
        }
      };
    return AccountLookupServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static AccountLookupServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceBlockingV2Stub>() {
        @java.lang.Override
        public AccountLookupServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccountLookupServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return AccountLookupServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AccountLookupServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceBlockingStub>() {
        @java.lang.Override
        public AccountLookupServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccountLookupServiceBlockingStub(channel, callOptions);
        }
      };
    return AccountLookupServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AccountLookupServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccountLookupServiceFutureStub>() {
        @java.lang.Override
        public AccountLookupServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccountLookupServiceFutureStub(channel, callOptions);
        }
      };
    return AccountLookupServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void ping(com.matcharena.grpc.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.PingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    default void reportMatchResult(com.matcharena.grpc.v1.ReportMatchResultRequest request,
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.ReportMatchResultResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReportMatchResultMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AccountLookupService.
   */
  public static abstract class AccountLookupServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AccountLookupServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AccountLookupService.
   */
  public static final class AccountLookupServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AccountLookupServiceStub> {
    private AccountLookupServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountLookupServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccountLookupServiceStub(channel, callOptions);
    }

    /**
     */
    public void ping(com.matcharena.grpc.v1.PingRequest request,
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.PingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reportMatchResult(com.matcharena.grpc.v1.ReportMatchResultRequest request,
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.ReportMatchResultResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReportMatchResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AccountLookupService.
   */
  public static final class AccountLookupServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<AccountLookupServiceBlockingV2Stub> {
    private AccountLookupServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountLookupServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccountLookupServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.matcharena.grpc.v1.PingResponse ping(com.matcharena.grpc.v1.PingRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.matcharena.grpc.v1.ReportMatchResultResponse reportMatchResult(com.matcharena.grpc.v1.ReportMatchResultRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getReportMatchResultMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service AccountLookupService.
   */
  public static final class AccountLookupServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AccountLookupServiceBlockingStub> {
    private AccountLookupServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountLookupServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccountLookupServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.matcharena.grpc.v1.PingResponse ping(com.matcharena.grpc.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.matcharena.grpc.v1.ReportMatchResultResponse reportMatchResult(com.matcharena.grpc.v1.ReportMatchResultRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReportMatchResultMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AccountLookupService.
   */
  public static final class AccountLookupServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AccountLookupServiceFutureStub> {
    private AccountLookupServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountLookupServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccountLookupServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.matcharena.grpc.v1.PingResponse> ping(
        com.matcharena.grpc.v1.PingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.matcharena.grpc.v1.ReportMatchResultResponse> reportMatchResult(
        com.matcharena.grpc.v1.ReportMatchResultRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReportMatchResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_REPORT_MATCH_RESULT = 1;

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
        case METHODID_PING:
          serviceImpl.ping((com.matcharena.grpc.v1.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.PingResponse>) responseObserver);
          break;
        case METHODID_REPORT_MATCH_RESULT:
          serviceImpl.reportMatchResult((com.matcharena.grpc.v1.ReportMatchResultRequest) request,
              (io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.ReportMatchResultResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPingMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.matcharena.grpc.v1.PingRequest,
              com.matcharena.grpc.v1.PingResponse>(
                service, METHODID_PING)))
        .addMethod(
          getReportMatchResultMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.matcharena.grpc.v1.ReportMatchResultRequest,
              com.matcharena.grpc.v1.ReportMatchResultResponse>(
                service, METHODID_REPORT_MATCH_RESULT)))
        .build();
  }

  private static abstract class AccountLookupServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AccountLookupServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.matcharena.grpc.v1.Account.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AccountLookupService");
    }
  }

  private static final class AccountLookupServiceFileDescriptorSupplier
      extends AccountLookupServiceBaseDescriptorSupplier {
    AccountLookupServiceFileDescriptorSupplier() {}
  }

  private static final class AccountLookupServiceMethodDescriptorSupplier
      extends AccountLookupServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AccountLookupServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AccountLookupServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AccountLookupServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getReportMatchResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}
