package com.example.jacocorunner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jacoco")
public class JacocoProperties {

    /**
     * 被测系统 jar 包路径，例如：C:/Users/twist/Desktop/project/target/demo-0.0.1.jar
     */
    private String targetJar;

    /**
     * 被测系统编译后的 class 目录，例如：C:/Users/twist/Desktop/project/target/classes
     */
    private String classFiles;

    /**
     * 被测系统源码目录，例如：C:/Users/twist/Desktop/project/src/main/java
     */
    private String sourceFiles;

    /**
     * jacocoagent.jar 的路径
     */
    private String agentJar;

    /**
     * jacococli.jar 的路径
     */
    private String cliJar;

    /**
     * 覆盖率 exec 文件输出路径
     */
    private String execFile = "jacoco-demo.exec";

    /**
     * 覆盖率报告输出目录（HTML）
     */
    private String reportDir = "html-report";

    /**
     * tcpserver 模式监听地址
     */
    private String address = "127.0.0.1";

    /**
     * tcpserver 模式端口
     */
    private int port = 6300;

    public String getTargetJar() {
        return targetJar;
    }

    public void setTargetJar(String targetJar) {
        this.targetJar = targetJar;
    }

    public String getClassFiles() {
        return classFiles;
    }

    public void setClassFiles(String classFiles) {
        this.classFiles = classFiles;
    }

    public String getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(String sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public String getAgentJar() {
        return agentJar;
    }

    public void setAgentJar(String agentJar) {
        this.agentJar = agentJar;
    }

    public String getCliJar() {
        return cliJar;
    }

    public void setCliJar(String cliJar) {
        this.cliJar = cliJar;
    }

    public String getExecFile() {
        return execFile;
    }

    public void setExecFile(String execFile) {
        this.execFile = execFile;
    }

    public String getReportDir() {
        return reportDir;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

