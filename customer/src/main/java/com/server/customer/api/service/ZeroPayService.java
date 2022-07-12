package com.server.customer.api.service;

import net.sf.json.JSONObject;

public interface ZeroPayService {
    JSONObject preparePayment() throws Exception;
}
