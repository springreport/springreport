package com.springreport.util;

import java.util.Collections;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;

public class SafeAviatorExecutor {
    private static final AviatorEvaluatorInstance INSTANCE;

    static {
    	INSTANCE = AviatorEvaluator.newInstance();
        // 1. 启用沙箱模式（5.4.4+ 会自动移除 io 模块，禁用文件操作）
        INSTANCE.enableSandboxMode();
        INSTANCE.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
        INSTANCE.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
        // 2. 移除反射危险函数（加固）
        INSTANCE.removeFunction("class");
        INSTANCE.removeFunction("new");
        INSTANCE.removeFunction("getDeclaredMethod");
        // 3. 额外显式移除 io 模块（双保险，确保无论版本行为如何，io 都不可用）
        INSTANCE.removeModule("io");
		 // 4. 禁用 NewInstance 特性，防止通过 new java.io.File 等方式操作文件
        INSTANCE.disableFeature(com.googlecode.aviator.Feature.NewInstance);
        // 5. 可选：设置类访问白名单为空，彻底禁止静态字段/方法访问；根据自己的需要扩展
        INSTANCE.setOption(Options.ALLOWED_CLASS_SET, Collections.emptySet());
    }

    /**
     * 安全求值：先编译（可缓存），再执行
     */
    public static Object evaluate(String expression, java.util.Map<String, Object> env) {
        // 编译时开启缓存（第二个参数 true）
        Expression compiled = INSTANCE.compile(expression, true);
        return compiled.execute(env);
    }
    
    /**
     * 安全求值：先编译（可缓存），再执行
     */
    public static Object evaluate(String expression) {
        // 编译时开启缓存（第二个参数 true）
        Expression compiled = INSTANCE.compile(expression, true);
        return compiled.execute(null);
    }
}
