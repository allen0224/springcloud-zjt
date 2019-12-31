package com.zjt.netty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @program: springcloud-zjt *
 * @description: *
 * @author: zhangjitong *
 * @create: 2019-12-30 18:14
 **/

public class WebsocketChatServerInitializer extends
        ChannelInitializer<SocketChannel> {    //1

    @Override
    public void initChannel(SocketChannel ch) throws Exception {//2
        ChannelPipeline pipeline = ch.pipeline();

        //将请求和应答消息编码或者解码为HTTP消息，
        pipeline.addLast(new HttpServerCodec());
        //将http消息的多个部分组合成一条完整的http消息
        pipeline.addLast(new HttpObjectAggregator(64*1024));
        //向客户端发送H5文件，主要用于支持浏览器和服务端进行websocket通信
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpRequestHandler("/ws"));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler());

    }
}
