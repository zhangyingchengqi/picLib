package com.yc.zuul.filters;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.nio.charset.Charset;
import java.util.Base64;

/*
现在我们要用zuul访问一个 有security保护的服务  这个服务要增加认证信息，那么就必须在其访问之前追加认证的头部操作，
这样的功能需要通过zuul的过滤操作完成
 */
public class AuthorizedRequestFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;   //相当于直接返回   "pre"
    }

    @Override
    public int filterOrder() {   //   order值 越小，过滤器越在前面执行
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*
        HttpHeaders headers=new HttpHeaders();
        String auth="admin:a";   //认证的原始用户名和密码
        byte[] encodeAuth= Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII"))); //加密处理
        String authHeader="Basic "+new String(encodeAuth);
        headers.set("Authorization",authHeader);    //    Http请求头         Authorization: Base xxxxxxxxx
     */
    @Override   //这个用于将敏感信息  admin:a 存入到请求头
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext(); // 获取当前请求的上下文

        String auth = "admin:zy"; // 认证的原始信息
        byte[] encodedAuth = Base64.getEncoder()
                .encode(auth.getBytes(Charset.forName("US-ASCII"))); // 进行一个加密的处理
        String authHeader = "Basic " + new String(encodedAuth);
        currentContext.addZuulRequestHeader("Authorization", authHeader);
        return null;
    }
}

