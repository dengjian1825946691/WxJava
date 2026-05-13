package me.chanjar.weixin.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatMessageHandler;

/**
 * 关键词匹配消息处理器
 *
 * @author wxjava
 */
@Slf4j
@RequiredArgsConstructor
public class KeywordMatchMessageHandler implements WxChatMessageHandler {

  private final String keyword;
  private final String reply;
  private final boolean fuzzyMatch;

  public KeywordMatchMessageHandler(String keyword, String reply) {
    this(keyword, reply, true);
  }

  @Override
  public Object handle(WxChannelMessage message, String content, String appId) {
    log.debug("关键词匹配处理器: keyword={}, content={}", keyword, content);

    if (content == null) {
      return null;
    }

    boolean matched;
    if (fuzzyMatch) {
      matched = content.contains(keyword);
    } else {
      matched = content.trim().equals(keyword);
    }

    if (matched) {
      log.info("关键词匹配成功，回复: {}", reply);
      return reply;
    }
    return null;
  }

  @Override
  public boolean support(WxChannelMessage message) {
    // 只处理文本消息
    return "text".equals(message.getMsgType());
  }
}
