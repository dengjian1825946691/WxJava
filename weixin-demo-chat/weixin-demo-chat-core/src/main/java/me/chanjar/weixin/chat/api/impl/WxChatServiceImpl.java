package me.chanjar.weixin.chat.api.impl;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatMessageHandler;
import me.chanjar.weixin.chat.api.WxChatService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信聊天服务实现
 *
 * @author wxjava
 */
@Slf4j
public class WxChatServiceImpl implements WxChatService {

  /**
   * 消息处理器列表
   */
  private final List<WxChatMessageHandler> handlers = new ArrayList<>();

  /**
   * 自动回复映射 (keyword -> reply)
   */
  private final Map<String, String> autoReplies = new ConcurrentHashMap<>();

  /**
   * 欢迎消息映射 (roomWxid -> welcomeMsg)
   */
  private final Map<String, String> welcomeMessages = new ConcurrentHashMap<>();

  /**
   * 群列表缓存
   */
  private final Set<String> roomSet = new HashSet<>();

  @Override
  public boolean sendTextMessage(String toUser, String content, String appId) {
    try {
      log.info("【发送消息】类型: 文本, 目标: [{}], 内容: {}", toUser, content);
      // TODO: 调用微信API发送消息
      // 这里需要集成实际的微信Channel SDK发送逻辑
      return true;
    } catch (Exception e) {
      log.error("发送文本消息失败", e);
      return false;
    }
  }

  @Override
  public boolean sendImageMessage(String toUser, String mediaId, String appId) {
    try {
      log.info("【发送消息】类型: 图片, 目标: [{}], mediaId: {}", toUser, mediaId);
      // TODO: 调用微信API发送图片消息
      return true;
    } catch (Exception e) {
      log.error("发送图片消息失败", e);
      return false;
    }
  }

  @Override
  public boolean sendLinkMessage(String toUser, String title, String content, String url, String appId) {
    try {
      log.info("【发送消息】类型: 链接, 目标: [{}], 标题: {}", toUser, title);
      // TODO: 调用微信API发送链接消息
      return true;
    } catch (Exception e) {
      log.error("发送链接消息失败", e);
      return false;
    }
  }

  @Override
  public List<String> getContacts(String appId) {
    try {
      log.info("【获取联系人】");
      // TODO: 调用微信API获取联系人列表
      return new ArrayList<>(roomSet);
    } catch (Exception e) {
      log.error("获取联系人列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  public List<String> getRoomMembers(String roomWxid, String appId) {
    try {
      log.info("【获取群成员】群: {}", roomWxid);
      // TODO: 调用微信API获取群成员列表
      return Collections.emptyList();
    } catch (Exception e) {
      log.error("获取群成员列表失败", e);
      return Collections.emptyList();
    }
  }

  @Override
  public Object processMessage(WxChannelMessage message, String content, String appId) {
    log.info("【处理消息】type={}, from={}, content={}",
             message.getMsgType(), message.getFromUser(), content);

    // 1. 检查是否是群消息
    boolean isRoomMessage = isRoomMessage(message);

    // 2. 如果是群消息且有欢迎消息配置，检查是否是新成员加入
    if (isRoomMessage) {
      handleRoomWelcome(message, appId);
    }

    // 3. 如果是群内消息且有自动回复内容，检查是否匹配关键字
    if (isRoomMessage) {
      String autoReply = checkAutoReply(content);
      if (autoReply != null) {
        log.info("【自动回复】匹配关键字，回复: {}", autoReply);
        return autoReply;
      }
    }

    // 4. 分发给消息处理器
    for (WxChatMessageHandler handler : handlers) {
      if (handler.support(message)) {
        Object result = handler.handle(message, content, appId);
        if (result != null) {
          return result;
        }
      }
    }

    return null;
  }

  /**
   * 判断是否是群消息
   */
  private boolean isRoomMessage(WxChannelMessage message) {
    String fromUser = message.getFromUser();
    // 群wxid通常以 @chatroom 结尾
    return fromUser != null && fromUser.contains("@chatroom");
  }

  /**
   * 处理群欢迎消息
   */
  private void handleRoomWelcome(WxChannelMessage message, String appId) {
    String roomWxid = message.getFromUser();
    String welcomeMsg = welcomeMessages.get(roomWxid);

    if (welcomeMsg != null && "member_join".equals(message.getEvent())) {
      // 新成员加入，发送欢迎消息
      log.info("【欢迎消息】检测到新成员加入群 [{}]，发送欢迎消息: {}", roomWxid, welcomeMsg);
      sendTextMessage(roomWxid, welcomeMsg, appId);
    }
  }

  /**
   * 检查自动回复
   */
  private String checkAutoReply(String content) {
    if (content == null || content.isEmpty()) {
      return null;
    }

    // 精确匹配
    String reply = autoReplies.get(content.trim());
    if (reply != null) {
      return reply;
    }

    // 模糊匹配（包含关键字）
    for (Map.Entry<String, String> entry : autoReplies.entrySet()) {
      if (content.contains(entry.getKey())) {
        return entry.getValue();
      }
    }

    return null;
  }

  @Override
  public void addHandler(WxChatMessageHandler handler) {
    if (handler != null) {
      handlers.add(handler);
    }
  }

  @Override
  public void setAutoReply(String keyword, String reply) {
    if (keyword != null && reply != null) {
      autoReplies.put(keyword, reply);
      log.info("【配置自动回复】关键字=[{}] -> 回复=[{}]", keyword, reply);
    }
  }

  @Override
  public void setWelcomeMessage(String roomWxid, String welcomeMsg) {
    if (roomWxid != null && welcomeMsg != null) {
      welcomeMessages.put(roomWxid, welcomeMsg);
      roomSet.add(roomWxid);
      log.info("【配置欢迎消息】群=[{}] -> 消息=[{}]", roomWxid, welcomeMsg);
    }
  }
}
