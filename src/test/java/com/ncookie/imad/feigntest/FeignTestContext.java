package com.ncookie.imad.feigntest;

import com.ncookie.imad.global.config.OpenFeignConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ImportAutoConfiguration({
        OpenFeignConfig.class,
//        CustomPropertiesConfig.class,
        FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
})
class FeignTestContext {

}
