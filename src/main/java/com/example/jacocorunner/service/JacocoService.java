package com.example.jacocorunner.service;

import com.example.jacocorunner.config.JacocoProperties;
import com.example.jacocorunner.web.dto.JacocoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JacocoService {

    private static final Logger log = LoggerFactory.getLogger(JacocoService.class);

    private final JacocoProperties properties;

    public JacocoService(JacocoProperties properties) {
        this.properties = properties;
    }

    public Process startTargetWithAgent() throws Exception {
        return startTargetWithAgent(null);
    }

    /**
     * 启动被测系统 jar，并挂载 jacocoagent。
     */
    public Process startTargetWithAgent(JacocoConfig config) throws Exception {
        String agentJar = valueOrDefault(config != null ? config.getAgentJar() : null, properties.getAgentJar());
        String targetJar = valueOrDefault(config != null ? config.getTargetJar() : null, properties.getTargetJar());
        String address = valueOrDefault(config != null ? config.getAddress() : null, properties.getAddress());
        int port = intOrDefault(config != null ? config.getPort() : null, properties.getPort());

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add(String.format("-javaagent:%s=includes=*,output=tcpserver,port=%d,address=%s,append=true",
                agentJar, port, address));
        command.add("-jar");
        command.add(targetJar);

        log.info("Start target process with JaCoCo agent: {}", command);

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(new File(new File(targetJar).getParent()));
        builder.inheritIO();
        return builder.start();
    }

    public void dumpExec() throws Exception {
        dumpExec(null);
    }

    /**
     * 使用 jacococli.jar 执行 dump 命令生成 exec 文件。
     */
    public void dumpExec(JacocoConfig config) throws Exception {
        String address = valueOrDefault(config != null ? config.getAddress() : null, properties.getAddress());
        int port = intOrDefault(config != null ? config.getPort() : null, properties.getPort());
        String execFile = resolveExecFile(config != null ? config.getExecFile() : null);
        String cliJar = valueOrDefault(config != null ? config.getCliJar() : null, properties.getCliJar());

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(cliJar);
        command.add("dump");
        command.add("--address");
        command.add(address);
        command.add("--port");
        command.add(String.valueOf(port));
        command.add("--destfile");
        command.add(execFile);

        log.info("Run JaCoCo CLI dump: {}", command);
        runProcess(command, new File("."));
    }

    public void generateReport() throws Exception {
        generateReport(null);
    }

    /**
     * 使用 jacococli.jar 执行 report 命令生成覆盖率报告。
     */
    public void generateReport(JacocoConfig config) throws Exception {
        String execFile = resolveExecFile(config != null ? config.getExecFile() : null);
        String classFiles = valueOrDefault(config != null ? config.getClassFiles() : null, properties.getClassFiles());
        String sourceFiles = valueOrDefault(config != null ? config.getSourceFiles() : null, properties.getSourceFiles());
        String reportDirPath = valueOrDefault(config != null ? config.getReportDir() : null, properties.getReportDir());
        String cliJar = valueOrDefault(config != null ? config.getCliJar() : null, properties.getCliJar());

        File reportDir = new File(reportDirPath);
        if (!reportDir.exists() && !reportDir.mkdirs()) {
            throw new IllegalStateException("Cannot create report directory: " + reportDir.getAbsolutePath());
        }

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(cliJar);
        command.add("report");
        command.add(execFile);
        command.add("--classfiles");
        command.add(classFiles);
        command.add("--sourcefiles");
        command.add(sourceFiles);
        command.add("--html");
        command.add(reportDir.getAbsolutePath());
        command.add("--xml");
        command.add("report.xml");
        command.add("--encoding");
        command.add("utf-8");

        log.info("Run JaCoCo CLI report: {}", command);
        runProcess(command, new File("."));
    }

    private void runProcess(List<String> command, File workingDir) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        builder.inheritIO();
        Process process = builder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Command failed with exit code " + exitCode + ": " + String.join(" ", command));
        }
    }

    private String valueOrDefault(String candidate, String defaultValue) {
        if (candidate == null) {
            return defaultValue;
        }
        if (candidate.trim().isEmpty()) {
            return defaultValue;
        }
        return candidate;
    }

    private int intOrDefault(Integer candidate, int defaultValue) {
        return candidate != null ? candidate : defaultValue;
    }

    /**
     * 将前端传入的 execFile 解析成真正的 exec 文件路径：
     * - 为空：使用配置中的默认值（可能是相对路径，如 jacoco-demo.exec）。
     * - 以 .exec 结尾：认为已经是完整文件路径，直接使用。
     * - 其他情况：认为是目录，在目录下拼接默认文件名（如 jacoco-demo.exec）。
     */
    private String resolveExecFile(String candidate) {
        String defaultPath = properties.getExecFile();
        if (candidate == null || candidate.trim().isEmpty()) {
            return defaultPath;
        }
        String trimmed = candidate.trim();
        String lower = trimmed.toLowerCase();
        if (lower.endsWith(".exec")) {
            return trimmed;
        }
        File dir = new File(trimmed);
        String defaultFileName = new File(defaultPath).getName();
        return new File(dir, defaultFileName).getPath();
    }
}

