
package com.example.jacocorunner.web.dto;

/**
 * 前端可选传入的 JaCoCo 配置，用于覆盖 application.yml 中的默认值。
 */
public class JacocoConfig {

    private String targetJar;
    private String classFiles;
    private String sourceFiles;
    private String agentJar;
    private String cliJar;
    private String execFile;
    private String reportDir;
    private String address;
    private Integer port;

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}

