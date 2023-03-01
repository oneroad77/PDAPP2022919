package com.example.pdapp2022919.net;

import androidx.annotation.NonNull;

import com.flyn.fc_message.base.RawMessageCodec;
import com.flyn.fc_message.message.LoginMessagePD;
import com.flyn.fc_message.message.RegisterMessagePD;
import com.flyn.fc_message.message.UUIDMessage;
import com.flyn.fc_message.secure.AesCodec;
import com.flyn.fc_message.secure.RsaCodec;

import java.net.InetSocketAddress;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class Client {

    public static final UUID FAILED_UUID = new UUID(0, 0);

    private static final String host = "140.135.101.71";
    private static final RSAPublicKey publicKey;
    private static final SecretKeySpec aesKeySpec;
    private static final IvParameterSpec ivSpec;

    private static UUID uuid = new UUID(0, 563178952L);

    static {
        Dotenv dotenv = Dotenv.configure()
                .directory("/assets")
                .filename("env")
                .load();
        RSAPublicKey pubKey = null;
        SecretKeySpec aesKey = null;
        IvParameterSpec ivPar = null;
        byte[] pub = decodeHex(Objects.requireNonNull(dotenv.get("PUBLIC_KEY")));
        byte[] aes = decodeHex(Objects.requireNonNull(dotenv.get("SECRET_KEY")));
        byte[] iv = decodeHex(Objects.requireNonNull(dotenv.get("IV_PARAMETER")));
        try {
            pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pub));
            aesKey = new SecretKeySpec(aes, "AES");
            ivPar = new IvParameterSpec(iv);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        publicKey = pubKey;
        aesKeySpec = aesKey;
        ivSpec = ivPar;
    }

    private static byte[] decodeHex(String s) {
        byte[] data = new byte[s.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (Character.digit(s.charAt(i * 2), 16) << 4);
            data[i] |= Character.digit(s.charAt(i * 2 + 1), 16);
        }
        return data;
    }

    public static UUID getUuid() {
        return uuid;
    }

    public static void login(int id, String name, CallbackUUID callback) {
        setupClient(new ChannelInboundHandlerAdapter() {

            @Override
            public void channelActive(@NonNull ChannelHandlerContext ctx) {
                LoginMessagePD msg = new LoginMessagePD(id, name);
                ctx.writeAndFlush(LoginMessagePD.Companion.encoder(ctx, msg));
            }

            @Override
            public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
                if (msg instanceof UUIDMessage) {
                    UUID uuid = ((UUIDMessage) msg).getUuid();
                    if (uuid.equals(FAILED_UUID)) {
                        callback.failed();
                    }
                    else {
                        Client.uuid = uuid;
                        callback.succeed();
                    }
                }
            }
        });
    }

    public static void register(int id, String name, CallbackUUID callback) {
        setupClient(new ChannelInboundHandlerAdapter() {

            @Override
            public void channelActive(@NonNull ChannelHandlerContext ctx) {
                RegisterMessagePD msg = new RegisterMessagePD(id, name);
                ctx.writeAndFlush(RegisterMessagePD.Companion.encoder(ctx, msg));
            }

            @Override
            public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) {
                if (msg instanceof UUIDMessage) {
                    UUID uuid = ((UUIDMessage) msg).getUuid();
                    if (uuid.equals(FAILED_UUID)) {
                        callback.failed();
                    }
                    else {
                        Client.uuid = uuid;
                        callback.succeed();
                    }
                }
            }

        });
    }

    private static void setupClient(ChannelInboundHandlerAdapter handler) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture future = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        protected void initChannel(@NonNull NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10240, 0, 2, 0, 2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new AesCodec(aesKeySpec, ivSpec));
                            ch.pipeline().addLast(new RawMessageCodec());
                            ch.pipeline().addLast(new RsaCodec(publicKey, null));
                            ch.pipeline().addLast(handler);
                        }

                    })
                    .connect(new InetSocketAddress(host, 8787))
                    .sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
