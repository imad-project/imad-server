package com.ncookie.imad.global.aws;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aws")
public class LoadbalancerHealthController {
    @GetMapping("")
    public ApiResponse<?> successHealthCheck() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.LOADBALANCER_HEALTH_CHECK_SUCCESS);
    }
}
