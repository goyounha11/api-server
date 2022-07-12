package com.server.customer.api;

import com.server.customer.api.service.ZeroPayService;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zero")
public class ZeroApi {

    private final ZeroPayService zeroPayService;

    @PostMapping("/test")
    public JSONObject zeroapi(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return zeroPayService.preparePayment();
    }
}
