package com.example.jacocorunner.web;

import com.example.jacocorunner.service.JacocoService;
import com.example.jacocorunner.web.dto.JacocoConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jacoco")
public class JacocoController {

    private final JacocoService jacocoService;
    private Process runningProcess;

    public JacocoController(JacocoService jacocoService) {
        this.jacocoService = jacocoService;
    }

    /**
     * 启动被测应用（挂载 jacocoagent）。
     * 如果请求体为空或字段为空，则使用 application.yml 中的默认配置。
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start(@RequestBody(required = false) JacocoConfig config) throws Exception {
        Map<String, Object> body = new HashMap<>();
        if (runningProcess != null && runningProcess.isAlive()) {
            body.put("status", "OK");
            body.put("message", "Target already running.");
            return ResponseEntity.ok(body);
        }
        runningProcess = jacocoService.startTargetWithAgent(config);
        body.put("status", "OK");
        body.put("message", "Target started with JaCoCo agent.");
        return ResponseEntity.ok(body);
    }

    /**
     * dump 覆盖率数据到 exec 文件。
     */
    @PostMapping("/dump")
    public ResponseEntity<Map<String, Object>> dump(@RequestBody(required = false) JacocoConfig config) throws Exception {
        jacocoService.dumpExec(config);
        Map<String, Object> body = new HashMap<>();
        body.put("status", "OK");
        body.put("message", "Dump exec finished.");
        return ResponseEntity.ok(body);
    }

    /**
     * 基于 exec 生成 HTML/XML 报告。
     */
    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> report(@RequestBody(required = false) JacocoConfig config) throws Exception {
        jacocoService.generateReport(config);
        Map<String, Object> body = new HashMap<>();
        body.put("status", "OK");
        body.put("message", "Report generated.");
        return ResponseEntity.ok(body);
    }
}

