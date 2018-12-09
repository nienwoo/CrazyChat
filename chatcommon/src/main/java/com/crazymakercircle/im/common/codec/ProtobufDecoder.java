package com.crazymakercircle.im.common.codec;

import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码器
 */
public class ProtobufDecoder extends ByteToMessageDecoder
{
    /**
     * 日志
     */
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception
    {
        // 标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 判断包头长度
        if (in.readableBytes() < 2)
        {// 不够包头
            return;
        }

        // 读取传送过来的消息的长度。
        int length = in.readUnsignedShort();

        // 长度如果小于0
        if (length < 0)
        {// 非法数据，关闭连接
            ctx.close();
        }

        if (length > in.readableBytes())
        {// 读到的消息体长度如果小于传送过来的消息长度
            // 重置读取位置
            in.resetReaderIndex();
            return;
        }


        byte[] array;
        int offset;
        if (in.hasArray())
        {
            //堆缓冲
            array = new byte[length];
//            byte[]   readArray =  in.array();
            offset = in.arrayOffset() + in.readerIndex();
            in.getBytes(in.readerIndex(), array, offset, length);
        }
        else
        {
            //直接缓冲
            array = new byte[length];
            offset = 0;
            in.getBytes(in.readerIndex(), array, 0, length);
        }

        // 字节转成对象
        ProtoMsg.Message outmsg =
                ProtoMsg.Message.parseFrom(array);


        if (outmsg != null)
        {
            // 获取业务消息头
            out.add(outmsg);
        }

    }
}
