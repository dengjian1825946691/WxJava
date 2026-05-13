package me.chanjar.weixin.chat.api;

import me.chanjar.weixin.channel.message.WxChannelMessage;

import java.util.List;

/**
 * 微信聊天服务接口
 *
 * @author wxjava
 */
public interface WxChatService {

  /**
   * 发送文本消息给好友或群
   *
   * @param toUser  接收人wxid（好友wxid或群wxid）
   * @param content 消息内容
   * @param appId  应用ID
   * @return 是否发送成功
   */
  boolean sendTextMessage(String toUser, String content, String appId);

  /**
   * 发送图片消息给好友或群
   *
   * @param toUser  接收人wxid（好友wxid或群wxid）
   * @param mediaId 图片media_id
   * @param appId   应用ID
   * @return 是否发送成功
   */
  boolean sendImageMessage(String toUser, String mediaId, String appId);

  /**
   * 发送链接消息给好友或群
   *
   * @param toUser    接收人wxid
   * @param title     链接标题
   * @param content   链接内容
   * @param url       链接地址
   * @param appId     应用ID
   * @return 是否发送成功
   */
  boolean sendLinkMessage(String toUser, String title, String content, String url, String appId);

  /**
   * 获取联系人列表（好友和群）
   *
   * @param appId 应用ID
   * @return 联系人列表
   */
  List<String> getContacts(String appId);

  /**
   * 获取群成员列表
   *
   * @param roomWxid 群wxid
   * @param appId    应用ID
   * @return 群成员wxid列表
   */
  List<String> getRoomMembers(String roomWxid, String appId);

  /**
   * 处理收到的消息
   *
   * @param message 消息对象
   * @param content 原始内容
   * @param appId   应用ID
   * @return 处理结果
   */
  Object processMessage(WxChannelMessage message, String content, String appId);

  /**
   * 添加消息处理器
   *
   * @param handler 消息处理器
   */
  void addHandler(WxChatMessageHandler handler);

  /**
   * 设置自动回复消息
   *
   * @param keyword 关键字
   * @param reply   回复内容
   */
  void setAutoReply(String keyword, String reply);

  /**
   * 设置欢迎消息
   *
   * @param roomWxid  群wxid
   * @param welcomeMsg 欢迎消息
   */
  void setWelcomeMessage(String roomWxid, String welcomeMsg);
}
