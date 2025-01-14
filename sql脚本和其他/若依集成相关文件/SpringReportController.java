package com.ruoyi.web.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;

@RestController
@RequestMapping("/springReport")
public class SpringReportController extends BaseController {

	@GetMapping("/getTokenData")
	public JSONObject getTokenData(@RequestParam String token) {
		LoginUser loginUser = super.getLoginUser();
		if (loginUser == null) {
			return makeResultData(false, "当前会话已过期或不存在！", null);
		}
		SysUser sysUser = loginUser.getUser();
		JSONObject tokenData = this.userConverter(sysUser);
		tokenData.put("sessionId", loginUser.getToken());
		tokenData.put("isAdmin", sysUser.isAdmin());
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

	private JSONObject userConverter(SysUser user) {
		JSONObject result = new JSONObject();
		result.put("userId", user.getUserId());
		result.put("loginName", user.getUserName());
		result.put("showName", user.getNickName());
		result.put("deptId", user.getDeptId());
		if (user.getDept() != null) {
			Map<String, Object> deptIdDictMap = new HashMap<>(2);
			deptIdDictMap.put("id", user.getDept().getDeptId());
			deptIdDictMap.put("name", user.getDept().getDeptName());
			result.put("deptIdDictMap", deptIdDictMap);
		}
		return result;
	}

}
