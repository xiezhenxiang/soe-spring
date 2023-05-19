package cn.soe.boot.autoconfigure.web.actuator;

import cn.soe.boot.autoconfigure.web.SoeWebConfiguration;
import cn.soe.boot.core.util.SoeUtils;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author xiezhenxiang 2023/5/6
 **/
@RestControllerEndpoint(id = "version")
public class SoeControllerEndPoint {

    @GetMapping
    public String appVersion() {
        return SoeUtils.getEnvProperty("app.version", String.class);
    }
}
