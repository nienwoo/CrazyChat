package com.crazymakercircle.chat.common.codec;


import com.crazymakercircle.chat.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码器
 *
 */
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {
	/**
	 * 日志对象
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(ProtobufEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, ByteBuf out)
			throws Exception {

		byte[] bytes = msg.toByteArray();// 将对象转换为byte

		// 加密消息体
		/*ThreeDES des = ctx.channel().attr(AppAttrKeys.ENCRYPT).get();
		byte[] encryptByte = des.encrypt(bytes);*/
		int length = bytes.length;// 读取消息的长度

		ByteBuf buf = Unpooled.buffer(2 + length);
		buf.writeShort(length);// 先将消息长度写入，也就是消息头
		buf.writeBytes(bytes);// 消息体中包含我们要发送的数据
		out.writeBytes(buf);

		LOGGER.debug("send [remote ip:"
				+ ctx.channel().remoteAddress() + "][total length:" + length
				+ "][bare length:" + msg.getSerializedSize() + "]");

	}
	
}
