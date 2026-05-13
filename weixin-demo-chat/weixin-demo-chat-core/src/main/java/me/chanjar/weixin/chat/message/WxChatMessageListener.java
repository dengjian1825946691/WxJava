package me.chanjar.weixin.chat.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatService;

/**
 * 微信聊天消息监听器
 *
 * @author wxjava
 */
@Slf4j
@RequiredArgsConstructor
public class WxChatMessageListener {

  private final String appId;
  private final WxChatService chatService;

  /**
   * 处理接收到的消息
   *
   * @param message 消息对象
   * @param content 消息内容
   */
  public void onMessage(WxChannelMessage message, String content) {
    try {
      log.info("【消息监听】处理消息: msgType={}, from={}", message.getMsgType(), message.getFromUser());

      // 使用聊天服务处理消息
      Object result = chatService.processMessage(message, content, appId);

      // 如果有自动回复结果，发送回复
      if (result != null) {
        String reply = result.toString();
        String toUser = message.getFromUser();
        log.info("【消息监听】发送自动回复给 [{}]: {}", toUser, reply);
        chatService.sendTextMessage(toUser, reply, appId);
      }
    } catch (Exception e) {
      log.error("【消息监听】处理消息失败", e);
    }
  }

  /**
   * 处理接收到的消息（静态方法，用于Webhook回调）
   *
   * @param message 消息对象
   * @param content 消息内容
   * @param appId   应用ID
   * @param chatService 聊天服务
   */
  public static void process(WxChannelMessage message, String content, String appId, WxChatService chatService) {
    try {
      log.info("【静态处理】处理消息: msgType={}, from={}", message.getMsgType(), message.getFromUser());

      // 使用聊天服务处理消息
      Object result = chatService.processMessage(message, content, appId);

      // 如果有自动回复结果，发送回复
      if (result != null) {
        String reply = result.toString();
        String toUser = message.getFromUser();
        log.info("【静态处理】发送自动回复给 [{}]: {}", toUser, reply);
        chatService.sendTextMessage(toUser, reply, appId);
      }
    } catch (Exception e) {
      log.error("【静态处理】处理消息失败", e);
    }
  }

  /**
   * 获取聊天服务
   *
   * @return 聊天服务实例
   */
  public WxChatService getChatService() {
    return chatService;
  }
}
