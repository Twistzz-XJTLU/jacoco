### Jacoco Runner Spring Boot 项目

**用途**：在当前目录下启动一个 Spring Boot 服务，通过 HTTP 接口按照你截图里的 3 步命令，对 `C:/Users/twist/Desktop/project` 这个被测 Spring Boot 项目做 JaCoCo 覆盖率统计。

#### 1. 先准备好必要文件

- **被测项目 jar**：先在 `C:/Users/twist/Desktop/project` 里执行 `mvn clean package`，确保生成：
  - `C:/Users/twist/Desktop/project/target/demo-0.0.1.jar`（名称根据你项目实际为准）
  - `C:/Users/twist/Desktop/project/target/classes`
  - `C:/Users/twist/Desktop/project/src/main/java`
- **jacocoagent.jar / jacococli.jar**：下载 JaCoCo 发行版，把其中的 `jacocoagent.jar` 和 `jacococli.jar` 复制到：
  - `C:/Users/twist/Desktop/jacoco/jacocoagent.jar`
  - `C:/Users/twist/Desktop/jacoco/jacococli.jar`

如路径不同，请修改 `src/main/resources/application.yml` 中的 `jacoco.*` 配置。

#### 2. 启动本项目

在 `C:/Users/twist/Desktop/jacoco` 目录：

```bash
mvn spring-boot:run
```

启动后服务默认端口为 `8081`。

#### 3. 通过接口或前端页面完成覆盖率收集

1. **启动被测系统（带 jacocoagent）**

   ```bash
   curl -X POST http://localhost:8081/jacoco/start
   ```

   这一步等价于：

   ```bash
   java -javaagent:jacocoagent.jar=includes=*,output=tcpserver,port=6300,address=localhost,append=true -jar demo-0.0.1.jar
   ```

2. **手工对被测系统发起测试请求**（比如接口测试、UI 自动化、JMeter 等），让代码跑起来。

3. **dump 覆盖率 exec 文件**

   ```bash
   curl -X POST http://localhost:8081/jacoco/dump
   ```

   这一步等价于：

   ```bash
   java -jar jacococli.jar dump --address 127.0.0.1 --port 6300 --destfile jacoco-demo.exec
   ```

4. **生成 HTML/XML 报告**

   ```bash
   curl -X POST http://localhost:8081/jacoco/report
   ```

   这一步等价于：

   ```bash
   java -jar jacococli.jar report jacoco-demo.exec --classfiles <class目录> --sourcefiles <源码目录> --html html-report --xml report.xml --encoding=utf-8
   ```

生成的 HTML 报告会在：

- `C:/Users/twist/Desktop/jacoco/html-report/index.html`

用浏览器打开即可查看测试覆盖率。

#### 4. 使用前端可视化控制台

你也可以不用手写 curl，直接打开前端页面操作：

- 浏览器访问：`http://localhost:8081/index.html`
- 页面中可以填写：
  - 被测 `jar` 路径（`targetJar`）
  - `class` 目录（`classFiles`）
  - 源码目录（`sourceFiles`）
  - `jacocoagent.jar` 路径（`agentJar`）
  - `jacococli.jar` 路径（`cliJar`）
  - `exec` 输出文件（`execFile`）
  - 报告目录（`reportDir`）
  - Agent 地址、端口（`address` / `port`）
- 如果某个输入框留空，则使用 `application.yml` 中的默认配置。
- 页面上的按钮对应：
  - `① 启动被测系统` → `POST /jacoco/start`
  - `② Dump 覆盖率` → `POST /jacoco/dump`
  - `③ 生成报告` → `POST /jacoco/report`
  - `一键 Dump + 报告` → 先调用 `dump` 再调用 `report`

页面下方的“调用日志”会显示每次请求的 payload 和后端返回的 JSON，方便排查问题。

