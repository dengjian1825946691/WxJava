package com.binarywang.spring.starter.wxjava.chat.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.chat.api.WxChatService;
import me.chanjar.weixin.chat.api.impl.WxChatServiceFactory;

import java.util.List;

/**
 * 微信聊天模板类，提供便捷的消息发送方法
 *
 * @author wxjava
 */
@Slf4j
public class WxChatTemplate {

  private final String appId;

  public WxChatTemplate(WxChatService chatService, String appId) {
    this.appId = appId;
    // 注册服务到工厂
    if (chatService != null) {
      WxChatServiceFactory.registerService(appId, chatService);
    }
  }

  /**
   * 获取对应的聊天服务
   */
  private WxChatService getChatService() {
    return WxChatServiceFactory.getService(appId);
  }

  /**
   * 发送文本消息给好友或群
   *
   * @param toUser  接收人wxid
   * @param content 消息内容
   * @return 是否发送成功
   */
  public boolean sendText(String toUser, String content) {
    WxChatService service = getChatService();
    if (service != null) {
      return service.sendTextMessage(toUser, content, appId);
    }
    log.warn("聊天服务未初始化: appId={}", appId);
    return false;
  }

  /**
   * 发送文本消息给好友
   *
   * @param friendWxid 好友wxid
   * @param content    消息内容
   * @return 是否发送成功
   */
  public boolean sendTextToFriend(String friendWxid, String content) {
    return sendText(friendWxid, content);
  }

  /**
   * 发送文本消息给群
   *
   * @param roomWxid 群wxid
   * @param content  消息内容
   * @return 是否发送成功
   */
  public boolean sendTextToRoom(String roomWxid, String content) {
    return sendText(roomWxid, content);
  }

  /**
   * 发送图片消息
   *
   * @param toUser 接收人wxid
   * @param mediaId 图片media_id
   * @return 是否发送成功
   */
  public boolean sendImage(String toUser, String mediaId) {
    WxChatService service = getChatService();
    if (service != null) {
      return service.sendImageMessage(toUser, mediaId, appId);
    }
    log.warn("聊天服务未初始化: appId={}", appId);
    return false;
  }

  /**
   * 发送图片消息给好友
   *
   * @param friendWxid 好友wxid
   * @param mediaId    图片media_id
   * @return 是否发送成功
   */
  public boolean sendImageToFriend(String friendWxid, String mediaId) {
    return sendImage(friendWxid, mediaId);
  }

  /**
   * 发送图片消息给群
   *
   * @param roomWxid 群wxid
   * @param mediaId  图片media_id
   * @return 是否发送成功
   */
  public boolean sendImageToRoom(String roomWxid, String mediaId) {
    return sendImage(roomWxid, mediaId);
  }

  /**
   * 发送链接消息
   *
   * @param toUser  接收人wxid
   * @param title   链接标题
   * @param content 链接描述
   * @param url     链接地址
   * @return 是否发送成功
   */
  public boolean sendLink(String toUser, String title, String content, String url) {
    WxChatService service = getChatService();
    if (service != null) {
      return service.sendLinkMessage(toUser, title, content, url, appId);
    }
    log.warn("聊天服务未初始化: appId={}", appId);
    return false;
  }

  /**
   * 发送链接消息给好友
   *
   * @param friendWxid 好友wxid
   * @param title      链接标题
   * @param content    链接描述
   * @param url        链接地址
   * @return 是否发送成功
   */
  public boolean sendLinkToFriend(String friendWxid, String title, String content, String url) {
    return sendLink(friendWxid, title, content, url);
  }

  /**
   * 发送链接消息给群
   *
   * @param roomWxid 群wxid
   * @param title    链接标题
   * @param content  链接描述
   * @param url      链接地址
   * @return 是否发送成功
   */
  public boolean sendLinkToRoom(String roomWxid, String title, String content, String url) {
    return sendLink(roomWxid, title, content, url);
  }

  /**
   * 获取联系人列表
   *
   * @return 联系人列表
   */
  public List<String> getContacts() {
    WxChatService service = getChatService();
    if (service != null) {
      return service.getContacts(appId);
    }
    log.warn("聊天服务未初始化: appId={}", appId);
    return null;
  }

  /**
   * 获取群成员列表
   *
   * @param roomWxid 群wxid
   * @return 群成员列表
   */
  public List<String> getRoomMembers(String roomWxid) {
    WxChatService service = getChatService();
    if (service != null) {
      return service.getRoomMembers(roomWxid, appId);
    }
    log.warn("聊天服务未初始化: appId={}", appId);
    return null;
  }

  /**
   * 设置自动回复
   *
   * @param keyword 关键字
   * @param reply   回复内容
   */
  public void setAutoReply(String keyword, String reply) {
    WxChatService service = getChatService();
    if (service != null) {
      service.setAutoReply(keyword, reply);
      log.info("【模板】设置自动回复: keyword=[{}], reply=[{}]", keyword, reply);
    } else {
      log.warn("聊天服务未初始化，无法设置自动回复: appId={}", appId);
    }
  }

  /**
   * 设置群欢迎消息
   *
   * @param roomWxid    群wxid
   * @param welcomeMsg  欢迎消息
   */
  public void setWelcomeMessage(String roomWxid, String welcomeMsg) {
    WxChatService service = getChatService();
    if (service != null) {
      service.setWelcomeMessage(roomWxid, welcomeMsg);
      log.info("【模板】设置群欢迎消息: room=[{}], message=[{}]", roomWxid, welcomeMsg);
    } else {
      log.warn("聊天服务未初始化，无法设置欢迎消息: appId={}", appId);
    }
  }
}
