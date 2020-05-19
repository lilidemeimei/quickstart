package org.myorg.netty.netty1;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
//ChannelInboundHandlerAdapter这个类提供了ChannelInboundHandler的默认实现，
//ChannelInboundHandler是接口，用来定义响应入站事件的方法
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {//对于每个传入的消息都要调用
        ByteBuf in = (ByteBuf) msg;
        System.out.println(
                "Servrer recived: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(in);
//        super.channelRead(ctx, msg);
    }

    @Override
    //通知ChannelInboundHandler最后一次对channelRead()的调用是当前批量读取中的最后一条消息
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //消息在调用writeAndFlush()方法时被释放
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    //在读取操作期间，有异常抛出时会调用
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); //关闭channel
//        super.exceptionCaught(ctx, cause);
    }
}
