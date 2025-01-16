package com.orangeforms.webadmin.upms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "SpringReport验证token接口")
@Slf4j
@RestController
@RequestMapping("/admin/springReport")
public class SpringReportController {
	@GetMapping("/getTokenData")
	public JSONObject getTokenData(@RequestParam String token) {
		JSONObject tokenData = new JSONObject();
		tokenData.put("token", token);
		return this.makeResultData(true, null, tokenData);
	}

	private JSONObject makeResultData(boolean success, String errorMsg, Object data) {
		JSONObject result = new JSONObject();
		result.put("success", success);
		result.put("errorMessage", errorMsg);
		result.put("data", data);
		return result;
	}
}
