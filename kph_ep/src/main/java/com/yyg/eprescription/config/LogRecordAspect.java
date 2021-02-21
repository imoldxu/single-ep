package com.yyg.eprescription.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * 网络请求日志
 * @author 老徐
 *
 */
@Aspect
@Configuration
public class LogRecordAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);
	 
    // 定义切点Pointcut
    @Pointcut("execution(* com.yyg.eprescription.controller..*.*(..))")
    public void webLog() {
    }
 
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
 
        //String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        //Object[] args = pjp.getArgs();
        String paramStr = "";
        //获取请求参数集合并进行遍历拼接
        if("POST".equals(method) || "PUT".equals(method)){
        	Object[] args = pjp.getArgs();
        	for(int i=0;i<args.length;i++) {
        		 if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
					continue;
				}
        	    if(request.getContentType()=="application/json") {
        	    	paramStr = JSON.toJSONString(args[i]);
        	    }
        	}
        }else if("GET".equals(method)){
        	paramStr = request.getQueryString();;
        }
        
        // result的值就是被拦截方法的返回值
        long startTime = System.currentTimeMillis();
        try {
        	Object result = pjp.proceed();
			logger.info("请求=====>"+method+" "+uri);
			logger.info("请求参数==>:"+paramStr);
			logger.info("响应=====>:" + JSON.toJSONString(result));
			logger.info("耗时=====>"+ (System.currentTimeMillis() - startTime));
			return result;
        }catch (Exception e) {
        	logger.info("请求=====>"+method+" "+uri);
			logger.info("请求参数==>:"+paramStr);
			logger.info("异常====>"+e.getMessage());
			logger.info("耗时=====>"+ (System.currentTimeMillis() - startTime));
			throw e;
		}
       
    }
      
//    @Before("webLog()")
//   public void doBefore(JoinPoint joinPoint) throws Throwable {
//        startTime.set(System.currentTimeMillis());
//
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        // 记录下请求内容
//        Enumeration<String> enums = request.getParameterNames();
//        List<String> params = new ArrayList();
//        while (enums.hasMoreElements()) {
//            String paraName = enums.nextElement();
//            String param = paraName + ":" + request.getParameter(paraName);
//            params.add(param);
//        }
//
//        logger.info("URL: {}, Method: {}, Params: {}, IP: {} ", request.getRequestURL().toString(), request.getMethod(), params.toString(), NetworkUtils.getClientIp(request));
//        logger.info("Class Method: {}, Args: {} ", joinPoint.getSignature().getDeclaringTypeName() + "."  + joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "webLog()")
//    public void doAfterReturning(Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        logger.info("RESPONSE: {} " , ret);
//        //logger.info("SPEND TIME: {} ms", System.currentTimeMillis() - startTime.get());
//    }
}
