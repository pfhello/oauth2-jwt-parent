package cn.itcast.zuul.filter;

import cn.itcast.auth.result.CodeMsg;
import cn.itcast.auth.result.Result;
import cn.itcast.auth.util.JsonUtils;
import cn.itcast.zuul.service.AuthService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //int值来定义过滤器的执行顺序，数值越小优先级越高
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        // 该过滤器需要执行
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = authService.getToken(request);
        if (token == null) {
            render();
            return null;
        }
        long explire = authService.getExplire(token);
        if (explire <= 0) {
            render();
            return null;
        }
        String jwt = authService.getJwtFromHeader(request);
        if (jwt == null) {
            render();
            return null;
        }
        return null;
    }

    //返回错误信息
    private void render() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false);//拒绝访问
        requestContext.setResponseStatusCode(403);//设置状态码
        Result result = Result.fail(CodeMsg.USER_NOT_LOGIN);
        String json = JsonUtils.objectToJson(result);
        requestContext.setResponseBody(json);
        response.setContentType("application/json;charset=UTF-8");
    }
}
