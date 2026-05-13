package com.binarywang.spring.starter.wxjava.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信聊天配置属性
 *
 * @author wxjava
 */
@Data
@ConfigurationProperties(prefix = "wx.chat")
public class WxChatProperties {

  /**
   * 是否启用
   */
  private boolean enabled = true;

  /**
   * 应用ID
   */
  private String appId;

  /**
   * 应用密钥
   */
  private String appSecret;

  /**
   * 自动回复配置列表
   */
  private List<AutoReplyConfig> autoReplies = new ArrayList<>();

  /**
   * 群欢迎消息配置列表
   */
  private List<WelcomeConfig> welcomeMessages = new ArrayList<>();

  /**
   * 自动回复配置
   */
  @Data
  public static class AutoReplyConfig {
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 回复内容
     */
    private String reply;

    /**
     * 是否模糊匹配
     */
    private boolean fuzzyMatch = true;

    /**
     * 适用于哪些群（空表示所有群）
     */
    private List<String> rooms = new ArrayList<>();
  }

  /**
   * 欢迎消息配置
   */
  @Data
  public static class WelcomeConfig {
    /**
     * 群wxid
     */
    private String roomWxid;

    /**
     * 欢迎消息内容
     */
    private String message;
  }
}
