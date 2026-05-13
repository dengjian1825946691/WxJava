package me.chanjar.weixin.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatMessageHandler;

/**
 * 群欢迎消息处理器
 *
 * @author wxjava
 */
@Slf4j
@RequiredArgsConstructor
public class WelcomeMessageHandler implements WxChatMessageHandler {

  private final String roomWxid;
  private final String welcomeMessage;

  @Override
  public Object handle(WxChannelMessage message, String content, String appId) {
    // 检查是否是群成员加入事件
    if ("member_join".equals(message.getEvent())) {
      log.info("群 [{}] 有新成员加入，发送欢迎消息: {}", roomWxid, welcomeMessage);
      return welcomeMessage;
    }
    return null;
  }

  @Override
  public boolean support(WxChannelMessage message) {
    // 只处理成员加入事件
    return "member_join".equals(message.getEvent())
      && message.getFromUser().equals(roomWxid);
  }
}
