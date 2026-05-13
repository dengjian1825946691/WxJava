package me.chanjar.weixin.chat.api;

import me.chanjar.weixin.channel.message.WxChannelMessage;

/**
 * 微信聊天消息处理器接口
 *
 * @author wxjava
 */
public interface WxChatMessageHandler {

  /**
   * 处理接收到的微信消息
   *
   * @param message 微信消息
   * @param content 消息内容（解密后）
   * @param appId   应用ID
   * @return 处理结果
   */
  Object handle(WxChannelMessage message, String content, String appId);

  /**
   * 判断是否支持处理此消息
   *
   * @param message 微信消息
   * @return 是否支持
   */
  boolean support(WxChannelMessage message);
}
