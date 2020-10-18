package uit.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Component
public class RelayTokenFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(RelayTokenFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();

        HttpServletRequest request = context.getRequest();

        context.addZuulRequestHeader("Authorization",request.getHeader("Authorization"));

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }
}
