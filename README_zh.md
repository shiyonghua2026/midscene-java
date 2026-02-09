# Midscene Java

**Midscene Java** 是一个基于 AI 的自动化 SDK，允许你使用自然语言指令控制 Web 浏览器。它与标准的 Selenium WebDriver（和 Playwright）集成，作为现有测试自动化框架之上的智能代理层。

## 特性

* **自然语言控制**："搜索'耳机'并点击第一个结果。"
* **高级交互**：使用简单命令进行点击、输入、滚动、拖放等操作。
* **多模态理解**：使用截图来理解页面上下文（视觉定位）。
* **智能规划**：自动规划、执行和重试操作。
* **服务层**：用于定位、提取和描述元素的底层 AI 能力。
* **YAML 脚本支持**：执行用 YAML 定义的声明式测试脚本。
* **缓存**：内置缓存以提高性能并降低 API 成本。
* **框架无关**：支持 Selenium 和 Playwright。
* **灵活配置**：支持 OpenAI (GPT-4o) 和 Google Gemini (1.5 Pro) 模型。
* **可视化报告**：生成包含执行轨迹、截图和推理过程的详细 HTML 报告。

## 模块

* **`midscene-core`**：代理的核心。包含 `Agent`、`Service`、`ScriptPlayer` 和核心逻辑。
* **`midscene-web`**：浏览器自动化工具的适配器（Selenium、Playwright）。
* **`midscene-visualizer`**：从执行上下文生成可视化 HTML 报告。

## 安装

将必要的依赖项添加到项目的 `pom.xml`：

```xml
<dependency>
  <groupId>io.github.alstafeev</groupId>
  <artifactId>midscene-web</artifactId>
  <version>0.1.9-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>io.github.alstafeev</groupId>
  <artifactId>midscene-visualizer</artifactId>
  <version>0.1.9-SNAPSHOT</version>
</dependency>
```

## 快速开始（Agent 模式）

Midscene Agent 是与应用程序交互的主要方式。它负责规划和执行。

```java
// 1. 配置
MidsceneConfig config = MidsceneConfig.builder()
    .provider(ModelProvider.GEMINI) // 或 OPENAI
    .apiKey(System.getenv("GEMINI_API_KEY"))
    .modelName("gemini-1.5-pro")
    .build();

// 2. 初始化（Selenium 示例）
WebDriver driver = new ChromeDriver();
SeleniumDriver pageDriver = new SeleniumDriver(driver);
Agent agent = Agent.create(config, pageDriver);

// 3. 交互
agent.aiAction("搜索'耳机'并点击第一个结果");
agent.aiAssert("价格应低于200美元");

// 4. 生成报告
Visualizer.generateReport(agent.getContext(), Paths.get("report.html"));
```

## 高级功能

### 1. 扩展 API 方法

`Agent` 类提供了精确控制的特定方法：

```java
// 交互
agent.aiTap("提交按钮");
agent.aiInput("用户名字段", "admin");
agent.aiScroll(ScrollOptions.down());
agent.aiHover("用户头像图标");

// 断言和等待
agent.aiAssert("登录按钮应该可见");
agent.aiWaitFor("欢迎消息出现");

// 数据查询
String price = agent.aiString("第一个商品的价格是多少？");
boolean isLoggedIn = agent.aiBoolean("用户是否已登录？");
```

### 2. 服务层（底层 AI）

使用 `Service` 类直接执行 AI 任务，无需完整的代理规划：

```java
Service service = new Service(pageDriver, agent.getAiModel());

// 定位元素坐标
LocateResult result = service.locate("蓝色结账按钮");
System.out.println("按钮位置：" + result.getRect());

// 提取数据
ExtractResult<String> price = service.extract("主商品的价格");

// 描述元素
DescribeResult desc = service.describe(100, 200); // 描述 x=100, y=200 位置的元素
```

### 3. YAML 脚本支持

用 YAML 声明式定义测试流程：

```yaml
target:
  url: "https://saucedemo.com"

tasks:
  - name: "登录流程"
    flow:
      - aiAction: "在用户名字段中输入'standard_user'"
      - aiAction: "在密码字段中输入'secret_sauce'"
      - aiAction: "点击登录"
      - aiAssert: "用户应该在库存页面"
      - logScreenshot: "库存页面"
```

用 Java 运行：

```java
ScriptPlayer player = new ScriptPlayer("login_script.yaml", agent);
ScriptResult result = player.run();
```

### 4. 缓存

Midscene 缓存规划结果以加快执行速度并节省 token。

```java
// 缓存默认启用（内存 + 文件）
// 配置缓存行为：
MidsceneConfig config = MidsceneConfig.builder()
    // ...
    .cacheId("my_test_cache") // 持久化缓存文件
    .build();
```

## 支持的驱动

- **Selenium**：`new SeleniumDriver(webDriver)`
- **Playwright**：`new PlaywrightDriver(page)`

## 配置

详细配置选项：

```java
MidsceneConfig config = MidsceneConfig.builder()
    .provider(ModelProvider.OPENAI)
    .apiKey("sk-...")
    .modelName("gpt-4o")
    .baseUrl("https://api.openai.com/v1") // 可选的自定义基础 URL
    .timeoutMs(120000)                    // AI 超时时间
    .build();
```

## 贡献

从源码构建：

```bash
git clone https://github.com/alstafeev/midscene-java.git
cd midscene-java
mvn clean install
```
