import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        Channel channel=ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress()+"加入聊天室");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        Channel channel=ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress()+"离开");
        System.out.println("channelGroup size"+channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println(ctx.channel().remoteAddress()+"上线");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        System.out.println(ctx.channel().remoteAddress()+"离线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel=ctx.channel();
        channelGroup.forEach(ch->{
            ch.writeAndFlush(channel.remoteAddress()+":"+msg);
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
