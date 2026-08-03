package com.matcharena.grpc.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class GameSessionServiceGrpc {

  private GameSessionServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "match_arena.v1.GameSessionService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PlayerAction,
      com.matcharena.grpc.v1.SessionUpdate> getPlayGameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PlayGame",
      requestType = com.matcharena.grpc.v1.PlayerAction.class,
      responseType = com.matcharena.grpc.v1.SessionUpdate.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PlayerAction,
      com.matcharena.grpc.v1.SessionUpdate> getPlayGameMethod() {
    io.grpc.MethodDescriptor<com.matcharena.grpc.v1.PlayerAction, com.matcharena.grpc.v1.SessionUpdate> getPlayGameMethod;
    if ((getPlayGameMethod = GameSessionServiceGrpc.getPlayGameMethod) == null) {
      synchronized (GameSessionServiceGrpc.class) {
        if ((getPlayGameMethod = GameSessionServiceGrpc.getPlayGameMethod) == null) {
          GameSessionServiceGrpc.getPlayGameMethod = getPlayGameMethod =
              io.grpc.MethodDescriptor.<com.matcharena.grpc.v1.PlayerAction, com.matcharena.grpc.v1.SessionUpdate>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PlayGame"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.PlayerAction.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.matcharena.grpc.v1.SessionUpdate.getDefaultInstance()))
              .setSchemaDescriptor(new GameSessionServiceMethodDescriptorSupplier("PlayGame"))
              .build();
        }
      }
    }
    return getPlayGameMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GameSessionServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceStub>() {
        @java.lang.Override
        public GameSessionServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GameSessionServiceStub(channel, callOptions);
        }
      };
    return GameSessionServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static GameSessionServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceBlockingV2Stub>() {
        @java.lang.Override
        public GameSessionServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GameSessionServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return GameSessionServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GameSessionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceBlockingStub>() {
        @java.lang.Override
        public GameSessionServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GameSessionServiceBlockingStub(channel, callOptions);
        }
      };
    return GameSessionServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GameSessionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GameSessionServiceFutureStub>() {
        @java.lang.Override
        public GameSessionServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GameSessionServiceFutureStub(channel, callOptions);
        }
      };
    return GameSessionServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.PlayerAction> playGame(
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.SessionUpdate> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getPlayGameMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GameSessionService.
   */
  public static abstract class GameSessionServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GameSessionServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GameSessionService.
   */
  public static final class GameSessionServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GameSessionServiceStub> {
    private GameSessionServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GameSessionServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GameSessionServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.PlayerAction> playGame(
        io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.SessionUpdate> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getPlayGameMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GameSessionService.
   */
  public static final class GameSessionServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<GameSessionServiceBlockingV2Stub> {
    private GameSessionServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GameSessionServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GameSessionServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<com.matcharena.grpc.v1.PlayerAction, com.matcharena.grpc.v1.SessionUpdate>
        playGame() {
      return io.grpc.stub.ClientCalls.blockingBidiStreamingCall(
          getChannel(), getPlayGameMethod(), getCallOptions());
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service GameSessionService.
   */
  public static final class GameSessionServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GameSessionServiceBlockingStub> {
    private GameSessionServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GameSessionServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GameSessionServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GameSessionService.
   */
  public static final class GameSessionServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GameSessionServiceFutureStub> {
    private GameSessionServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GameSessionServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GameSessionServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_PLAY_GAME = 0;

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
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PLAY_GAME:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.playGame(
              (io.grpc.stub.StreamObserver<com.matcharena.grpc.v1.SessionUpdate>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getPlayGameMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.matcharena.grpc.v1.PlayerAction,
              com.matcharena.grpc.v1.SessionUpdate>(
                service, METHODID_PLAY_GAME)))
        .build();
  }

  private static abstract class GameSessionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GameSessionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.matcharena.grpc.v1.GameSession.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GameSessionService");
    }
  }

  private static final class GameSessionServiceFileDescriptorSupplier
      extends GameSessionServiceBaseDescriptorSupplier {
    GameSessionServiceFileDescriptorSupplier() {}
  }

  private static final class GameSessionServiceMethodDescriptorSupplier
      extends GameSessionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GameSessionServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (GameSessionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GameSessionServiceFileDescriptorSupplier())
              .addMethod(getPlayGameMethod())
              .build();
        }
      }
    }
    return result;
  }
}
