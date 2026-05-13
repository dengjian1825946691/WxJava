package com.binarywang.spring.starter.wxjava.chat.demo;

import com.binarywang.spring.starter.wxjava.chat.config.WxChatProperties;
import com.binarywang.spring.starter.wxjava.chat.service.WxChatTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * ============================================================
 * 微信聊天示例应用 - 可直接运行的主启动类
 * ============================================================
 *
 * 使用方法：
 * 1. 修改 src/main/resources/application.yml 中的配置
 *    - wx.chat.app-id: 你的微信Channel AppID
 *    - wx.chat.app-secret: 你的微信Channel AppSecret
 * 2. 运行 main 方法启动应用
 *
 * 功能：
 * - 发送消息给好友或群
 * - 自动回复（关键字匹配）
 * - 群欢迎消息
 * ============================================================
 *
 * @author wxjava
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(WxChatProperties.class)
public class WxChatDemoApplication {

  public static void main(String[] args) {
    log.info("========================================");
    log.info("   正在启动微信聊天示例应用...");
    log.info("========================================");
    try {
      SpringApplication.run(WxChatDemoApplication.class, args);
    } catch (Exception e) {
      log.error("应用启动失败", e);
      System.exit(1);
    }
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx, WxChatTemplate wxChatTemplate,
                                             WxChatProperties wxChatProperties) {
    return args -> {
      log.info("");
      log.info("========================================");
      log.info("   微信聊天示例应用启动成功！");
      log.info("========================================");
      log.info("");

      // 打印配置信息
      printConfiguration(wxChatProperties);

      // 演示发送消息
      demonstrateSendMessage();

      // 演示自动回复配置
      demonstrateAutoReply(wxChatTemplate);

      // 演示欢迎消息配置
      demonstrateWelcomeMessage(wxChatTemplate, wxChatProperties);

      // 显示使用说明
      printUsage();

      log.info("========================================");
      log.info("   配置完成，应用正在运行...");
      log.info("========================================");
      log.info("");
      log.info("【提示】按 Ctrl+C 可安全停止应用");
      log.info("");
    };
  }

  private void printConfiguration(WxChatProperties wxChatProperties) {
    log.info("【配置信息】");
    log.info("------------------------------------------------");
    log.info("应用ID: {}", wxChatProperties.getAppId());
    log.info("自动回复数量: {}", wxChatProperties.getAutoReplies().size());
    log.info("欢迎消息数量: {}", wxChatProperties.getWelcomeMessages().size());
    log.info("");
  }

  private void demonstrateSendMessage() {
    log.info("【功能演示1】发送消息");
    log.info("------------------------------------------------");

    String roomWxid = "test_room@chatroom";
    String friendWxid = "friend_wxid";

    log.info("可通过 wxChatTemplate 发送消息：");
    log.info("  // 发送文本消息给群");
    log.info("  wxChatTemplate.sendTextToRoom(\"{}\", \"消息内容\");", roomWxid);
    log.info("");
    log.info("  // 发送文本消息给好友");
    log.info("  wxChatTemplate.sendTextToFriend(\"{}\", \"消息内容\");", friendWxid);
    log.info("");
    log.info("  // 发送图片消息");
    log.info("  wxChatTemplate.sendImage(\"wxid\", \"media_id\");");
    log.info("");
    log.info("  // 发送链接消息");
    log.info("  wxChatTemplate.sendLink(\"wxid\", \"标题\", \"描述\", \"http://xxx.com\");");
    log.info("");
  }

  private void demonstrateAutoReply(WxChatTemplate wxChatTemplate) {
    log.info("【功能演示2】自动回复配置");
    log.info("------------------------------------------------");

    // 动态添加自动回复
    wxChatTemplate.setAutoReply("你好", "你好！很高兴见到你！有什么可以帮助的吗？");
    wxChatTemplate.setAutoReply("帮助", "" +
      "欢迎使用微信聊天机器人！\n" +
      "发送以下指令：\n" +
      "  1. 你好 - 打招呼\n" +
      "  2. 帮助 - 显示此帮助\n" +
      "  3. 天气 - 查询天气\n" +
      "  4. 时间 - 获取时间");

    log.info("已配置以下自动回复（根据配置文件）：");
    log.info("");
  }

  private void demonstrateWelcomeMessage(WxChatTemplate wxChatTemplate, WxChatProperties wxChatProperties) {
    log.info("【功能演示3】群欢迎消息");
    log.info("------------------------------------------------");

    // 动态添加欢迎消息
    wxChatTemplate.setWelcomeMessage("family_group@chatroom", "欢迎家人加入！🎉");
    wxChatTemplate.setWelcomeMessage("work_group@chatroom", "欢迎新成员！请自我介绍一下~");

    log.info("已配置以下群欢迎消息：");
    log.info("");
  }

  private void printUsage() {
    log.info("【使用说明】");
    log.info("------------------------------------------------");
    log.info("1. 消息推送：");
    log.info("   注入 WxChatTemplate 或 WxChatService 使用");
    log.info("");
    log.info("2. 自动回复：");
    log.info("   在 application.yml 中配置 auto-replies");
    log.info("   或通过 wxChatTemplate.setAutoReply(keyword, reply) 动态配置");
    log.info("");
    log.info("3. 欢迎消息：");
    log.info("   在 application.yml 中配置 welcome-messages");
    log.info("   或通过 wxChatTemplate.setWelcomeMessage(roomId, msg) 动态配置");
    log.info("");
    log.info("4. 示例代码：");
    log.info("   @Service");
    log.info("   public class MyService {");
    log.info("       @Autowired");
    log.info("       private WxChatTemplate wxChatTemplate;");
    log.info("");
    log.info("       public void send() {");
    log.info("           wxChatTemplate.sendTextToRoom(\"群ID@chatroom\", \"消息\");");
    log.info("       }");
    log.info("   }");
    log.info("");
  }
}
