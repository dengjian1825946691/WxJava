# 微信聊天示例项目 (weixin-demo-chat)

基于 WxJava 的微信聊天示例项目，实现了给指定微信好友/微信群推送消息，以及群内自动回复功能。

## 功能特性

- **消息推送**：支持向指定好友或群发送文本、图片、链接等消息
- **自动回复**：支持关键字匹配自动回复
- **欢迎消息**：支持群新成员加入时自动发送欢迎消息
- **消息监听**：支持监听和处理各类微信消息

## 模块结构

```
weixin-demo-chat/
├── weixin-demo-chat-core/          # 核心模块
│   └── src/main/java/
│       └── me.chanjar.weixin.chat/
│           ├── api/                 # API接口定义
│           │   ├── WxChatService.java           # 聊天服务接口
│           │   └── WxChatMessageHandler.java     # 消息处理器接口
│           ├── api/impl/           # API实现
│           │   ├── WxChatServiceImpl.java        # 聊天服务实现
│           │   └── WxChatServiceFactory.java    # 服务工厂
│           ├── handler/            # 消息处理器
│           │   ├── AutoReplyMessageHandler.java          # 自动回复处理器
│           │   ├── KeywordMatchMessageHandler.java       # 关键字匹配处理器
│           │   └── WelcomeMessageHandler.java           # 欢迎消息处理器
│           └── message/            # 消息相关
│               └── WxChatMessageListener.java           # 消息监听器
│
└── weixin-demo-chat-spring-boot-starter/   # Spring Boot Starter
    └── src/main/
        ├── java/
        │   └── com/binarywang/spring/starter/wxjava/chat/
        │       ├── config/                  # 配置类
        │       │   ├── WxChatAutoConfiguration.java    # 自动配置
        │       │   └── WxChatProperties.java            # 配置属性
        │       ├── demo/                     # 示例应用
        │       │   └── WxChatDemoApplication.java       # 启动类
        │       └── service/                  # 服务模板
        │           └── WxChatTemplate.java               # 消息发送模板
        └── resources/
            ├── application.yml               # 配置示例
            └── META-INF/
                └── spring.factories          # Spring配置
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.github.binarywang</groupId>
    <artifactId>weixin-demo-chat-spring-boot-starter</artifactId>
    <version>4.8.3.B</version>
</dependency>
```

### 2. 配置 application.yml

```yaml
wx:
  chat:
    enabled: true
    app-id: your_app_id
    app-secret: your_app_secret

    # 自动回复配置
    auto-replies:
      - keyword: "你好"
        reply: "你好！很高兴见到你！"
        fuzzy-match: false
      - keyword: "帮助"
        reply: "你可以发送以下指令：\n1. 你好\n2. 帮助\n3. 天气"
        fuzzy-match: true

    # 群欢迎消息配置
    welcome-messages:
      - room-wxid: "your_room_wxid@chatroom"
        message: "欢迎新朋友加入！"
```

### 3. 使用示例

#### 发送消息

```java
@Service
@RequiredArgsConstructor
public class YourService {

    private final WxChatTemplate wxChatTemplate;

    public void sendMessage() {
        // 发送文本消息给群
        wxChatTemplate.sendTextToRoom("群wxid@chatroom", "这是一条测试消息");

        // 发送文本消息给好友
        wxChatTemplate.sendTextToFriend("好友wxid", "你好！");

        // 发送图片消息
        wxChatTemplate.sendImageToRoom("群wxid@chatroom", "media_id");

        // 发送链接消息
        wxChatTemplate.sendLinkToRoom("群wxid@chatroom", "标题", "描述", "https://example.com");
    }
}
```

#### 动态设置自动回复

```java
@Service
@RequiredArgsConstructor
public class YourService {

    private final WxChatTemplate wxChatTemplate;

    public void setupAutoReply() {
        // 设置关键字回复
        wxChatTemplate.setAutoReply("你好", "你好！很高兴见到你！");
        wxChatTemplate.setAutoReply("天气", "今天天气晴朗！");
    }
}
```

#### 设置群欢迎消息

```java
@Service
@RequiredArgsConstructor
public class YourService {

    private final WxChatTemplate wxChatTemplate;

    public void setupWelcomeMessage() {
        // 设置群欢迎消息
        wxChatTemplate.setWelcomeMessage("群wxid@chatroom", "欢迎新朋友加入！请自我介绍一下~");
    }
}
```

#### 使用消息监听器

```java
@Configuration
public class WxChatConfig {

    @Bean
    public WxChatMessageListener wxChatMessageListener(WxChannelService wxChannelService,
                                                        WxChatTemplate wxChatTemplate) {
        WxChatMessageListener listener = new WxChatMessageListener(wxChannelService, "your_app_id");

        // 添加自定义消息处理器
        listener.getRouter().rule()
            .msgType("text")
            .content("测试")
            .handler((message, content, context, service, sessionManager, exceptionHandler) -> {
                // 处理收到的测试消息
                wxChatTemplate.sendTextToRoom(message.getFromUser(), "收到测试消息！");
                return null;
            })
            .end();

        return listener;
    }
}
```

## 配置说明

### WxChatProperties 配置项

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| enabled | boolean | true | 是否启用 |
| app-id | String | - | 微信应用ID |
| app-secret | String | - | 微信应用密钥 |

### AutoReplyConfig 配置项

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| keyword | String | - | 关键字 |
| reply | String | - | 回复内容 |
| fuzzy-match | boolean | true | 是否模糊匹配 |
| rooms | List | [] | 适用的群列表（空表示所有群） |

### WelcomeConfig 配置项

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| room-wxid | String | - | 群wxid |
| message | String | - | 欢迎消息内容 |

## 注意事项

1. **消息发送频率**：请注意微信对消息发送频率的限制，避免过度发送
2. **群消息识别**：群wxid通常以 `@chatroom` 结尾
3. **关键字匹配**：模糊匹配会对消息内容进行包含检查
4. **欢迎消息**：仅在检测到成员加入事件时触发

## License

基于 WxJava 项目，采用 Apache License 2.0 开源协议。
