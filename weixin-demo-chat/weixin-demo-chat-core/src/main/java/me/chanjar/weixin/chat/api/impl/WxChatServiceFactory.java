package me.chanjar.weixin.chat.api.impl;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.channel.message.WxChannelMessage;
import me.chanjar.weixin.chat.api.WxChatMessageHandler;
import me.chanjar.weixin.chat.api.WxChatService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信聊天服务工厂
 *
 * @author wxjava
 */
@Slf4j
public class WxChatServiceFactory {

  private static final Map<String, WxChatService> SERVICE_MAP = new ConcurrentHashMap<>();

  /**
   * 注册聊天服务实例
   *
   * @param appId 应用ID
   * @param service 聊天服务实例
   */
  public static void registerService(String appId, WxChatService service) {
    if (appId != null && service != null) {
      SERVICE_MAP.put(appId, service);
      log.info("注册聊天服务: appId={}", appId);
    }
  }

  /**
   * 获取聊天服务实例
   *
   * @param appId 应用ID
   * @return 聊天服务实例
   */
  public static WxChatService getService(String appId) {
    return SERVICE_MAP.get(appId);
  }

  /**
   * 获取或创建聊天服务实例（带WxChannelService）
   *
   * @param appId          应用ID
   * @param channelService 微信Channel服务
   * @return 聊天服务实例
   */
  public static WxChatService getService(String appId, Object channelService) {
    return SERVICE_MAP.computeIfAbsent(appId, k -> new WxChatServiceImpl());
  }

  /**
   * 注册消息处理器
   *
   * @param appId   应用ID
   * @param handler 消息处理器
   */
  public static void registerHandler(String appId, WxChatMessageHandler handler) {
    WxChatService service = SERVICE_MAP.get(appId);
    if (service != null) {
      service.addHandler(handler);
    }
  }

  /**
   * 设置自动回复
   *
   * @param appId   应用ID
   * @param keyword 关键字
   * @param reply   回复内容
   */
  public static void setAutoReply(String appId, String keyword, String reply) {
    WxChatService service = SERVICE_MAP.get(appId);
    if (service != null) {
      service.setAutoReply(keyword, reply);
    }
  }

  /**
   * 设置欢迎消息
   *
   * @param appId       应用ID
   * @param roomWxid    群wxid
   * @param welcomeMsg  欢迎消息
   */
  public static void setWelcomeMessage(String appId, String roomWxid, String welcomeMsg) {
    WxChatService service = SERVICE_MAP.get(appId);
    if (service != null) {
      service.setWelcomeMessage(roomWxid, welcomeMsg);
    }
  }

  /**
   * 处理消息
   *
   * @param appId   应用ID
   * @param message 消息
   * @param content 原始内容
   * @return 处理结果
   */
  public static Object processMessage(String appId, WxChannelMessage message, String content) {
    WxChatService service = SERVICE_MAP.get(appId);
    if (service != null) {
      return service.processMessage(message, content, appId);
    }
    return null;
  }

  /**
   * 移除服务实例
   *
   * @param appId 应用ID
   */
  public static void removeService(String appId) {
    SERVICE_MAP.remove(appId);
  }

  /**
   * 获取所有服务实例
   *
   * @return 服务实例映射
   */
  public static Map<String, WxChatService> getAllServices() {
    return Collections.unmodifiableMap(SERVICE_MAP);
  }
}
