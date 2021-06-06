package com.netty.HttpNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

@Slf4j
public class HttpServer {
    public static void main(String[] args) {

        NioEventLoopGroup boos =new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boos,work);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                   // ch.pipeline().addLast(new LoggingHandler());
                    ch.pipeline().addLast(new HttpRequestDecoder());
                    ch.pipeline().addLast(new HttpResponseEncoder());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {//可以指定处理类型的数据 HttpRequest
                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                            //获取请求的信息
                            log.debug(msg.uri());

                            //返回响应
                            DefaultFullHttpResponse response =new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);

                            byte[] bytes = "<h1>HELLO WORLD</h1>".getBytes();
                            response.headers().setInt(CONTENT_LENGTH,bytes.length);
                            response.content().writeBytes(bytes);
                            //写入到channel
                            ctx.writeAndFlush(response);
                        }
                    });
//                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//                        @Override
//                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                            log.debug("{}",msg.getClass());
//                            if(msg instanceof HttpRequest){
//                                log.debug(((HttpRequest) msg).uri());
//                                DefaultFullHttpResponse response =new DefaultFullHttpResponse(((HttpRequest) msg).protocolVersion(), HttpResponseStatus.OK);
//                                byte[] bytes = "<h3>HELLO WORLD</h3>".getBytes();
//                                response.headers().setInt(CONTENT_LENGTH,bytes.length);
//                                response.content().writeBytes(bytes);
//                                ctx.writeAndFlush(response);
//
//                            }else if(msg instanceof HttpContent){
//
//                            }
//                        }
//                    });
                }
            });

         ChannelFuture channelFuture= serverBootstrap.bind(8080).sync();
         channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boos.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
