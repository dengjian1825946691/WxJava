package com.binarywang.spring.starter.wxjava.chat.config;

import com.binarywang.spring.starter.wxjava.chat.service.WxChatTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.chat.api.WxChatService;
import me.chanjar.weixin.chat.api.impl.WxChatServiceImpl;
import me.chanjar.weixin.chat.api.impl.WxChatServiceFactory;
import me.chanjar.weixin.chat.message.WxChatMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 微信聊天自动配置
 *
 * @author wxjava
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "wx.chat", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(WxChatProperties.class)
public class WxChatAutoConfiguration {

  private final WxChatProperties properties;

  /**
   * 创建聊天服务（无外部依赖的简化版本）
   */
  @Bean
  @Primary
  @ConditionalOnMissingBean
  public WxChatService wxChatService() {
    String appId = properties.getAppId();
    log.info("初始化微信聊天服务, appId: {}", appId);

    // 使用简化版实现，不依赖外部微信服务
    WxChatServiceImpl chatService = new WxChatServiceImpl();

    // 配置自动回复
    for (WxChatProperties.AutoReplyConfig config : properties.getAutoReplies()) {
      log.info("配置自动回复: keyword={}, reply={}, fuzzyMatch={}",
               config.getKeyword(), config.getReply(), config.isFuzzyMatch());
      chatService.setAutoReply(config.getKeyword(), config.getReply());
    }

    // 配置欢迎消息
    for (WxChatProperties.WelcomeConfig config : properties.getWelcomeMessages()) {
      log.info("配置群欢迎消息: room={}, message={}", config.getRoomWxid(), config.getMessage());
      chatService.setWelcomeMessage(config.getRoomWxid(), config.getMessage());
    }

    // 注册到工厂
    WxChatServiceFactory.registerService(appId, chatService);

    return chatService;
  }

  /**
   * 创建聊天消息监听器
   */
  @Bean
  @ConditionalOnMissingBean
  public WxChatMessageListener wxChatMessageListener() {
    String appId = properties.getAppId();
    log.info("初始化微信聊天消息监听器, appId: {}", appId);
    WxChatService chatService = WxChatServiceFactory.getService(appId);
    return new WxChatMessageListener(appId, chatService);
  }

  /**
   * 创建聊天模板
   */
  @Bean
  @ConditionalOnMissingBean
  public WxChatTemplate wxChatTemplate() {
    String appId = properties.getAppId();
    log.info("初始化微信聊天模板, appId: {}", appId);
    WxChatService chatService = WxChatServiceFactory.getService(appId);
    return new WxChatTemplate(chatService, appId);
  }
}
