package org.myorg.netty.WebSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.myorg.netty.HttpRequestHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;
    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    //重写userEventTriggered()方法以处理自定义事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("ctx:" + ctx);
        //如果该事件表示握手成功，则从该channelpipeline中移除httpRequesthandler,因为将不会接收到任何http消息了
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);
            //通知所有已经连接的websocket客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame("client"+ctx.channel()+"joined"));
            //将新的websocket channel添加到channelGroup中，以便它可以接收到所有的消息
            group.add(ctx.channel());
            System.out.println("kkkk");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //增加消息的引用计数，并将它写到channelGroup中所有已经连接的客户端
        group.writeAndFlush(msg.retain());
    }
}
