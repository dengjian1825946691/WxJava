package me.chanjar.weixin.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatMessageHandler;

/**
 * 自动回复消息处理器
 *
 * @author wxjava
 */
@Slf4j
@RequiredArgsConstructor
public class AutoReplyMessageHandler implements WxChatMessageHandler {

  private final String keyword;
  private final String reply;

  @Override
  public Object handle(WxChannelMessage message, String content, String appId) {
    log.info("自动回复消息处理器处理: keyword={}, content={}", keyword, content);
    if (content != null && content.contains(keyword)) {
      return reply;
    }
    return null;
  }

  @Override
  public boolean support(WxChannelMessage message) {
    // 所有消息都支持，由handle方法判断是否匹配
    return true;
  }
}
