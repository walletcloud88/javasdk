package com.wallet.api.example;

import com.wallet.api.utils.HttpUtil;
import com.wallet.api.utils.SignUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public void getAddress() {
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";

        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", version);
        params.put("time", (int) (System.currentTimeMillis() / 1000));
        params.put("lang", lang);
        params.put("coin", "btc");          // support btc\eth\bnb\trx\.....
        params.put("limit", 200);           // default 200
        params.put("sign", SignUtils.sign(params, privateKey));

        Map<String, Object> response = HttpUtil.sendPost(host + "address/getBatch", params);

    }

    public void syncAddress() {
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";

        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", version);
        params.put("time", (int) (System.currentTimeMillis() / 1000));
        params.put("lang", lang);

        params.put("address", "unused address");
        params.put("coin", "address coin");
        params.put("user_id", "address user_id");

        params.put("sign", SignUtils.sign(params, privateKey));

        Map<String, Object> response = HttpUtil.sendPost(host + "address/syncStatus", params);


        // OR you can use batch api

        Map<String, Object> batchParams = new HashMap<>();
        batchParams.put("app_id", appId);
        batchParams.put("version", version);
        batchParams.put("time", (int) (System.currentTimeMillis() / 1000));
        batchParams.put("lang", lang);


        List<Map<String, String>> addressList = new ArrayList<>();
        Map<String, String> address = new HashMap<>();
        address.put("address", "unused address1");
        address.put("coin", "address1 coin");
        address.put("user_id", "address1 user_id");
        addressList.add(address);


        address = new HashMap<>();
        address.put("address", "unused address2");
        address.put("coin", "address2 coin");
        address.put("user_id", "address2 user_id");
        addressList.add(address);


        address = new HashMap<>();
        address.put("address", "unused address3");
        address.put("coin", "address3 coin");
        address.put("user_id", "address3 user_id");
        addressList.add(address);

        batchParams.put("address_list", addressList);
        batchParams.put("sign", SignUtils.sign(batchParams, privateKey));

        Map<String, Object> response2 = HttpUtil.sendPost(host + "address/syncBatchStatus", batchParams);

    }

    public void withdraw() {
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";


        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", version);
        params.put("time", (int) (System.currentTimeMillis() / 1000));
        params.put("lang", lang);

        params.put("user_id", "USER ID");
        params.put("coin", "COIN NAME"); // Token abbreviation as per Wallet agreement，refer to api coin/list
        params.put("amount", "AMOUNT");
        params.put("address", "TO ADDRESS");
        params.put("trade_id", "ORDER ID");

        params.put("sign", SignUtils.sign(params, privateKey));

        Map<String, Object> response = HttpUtil.sendPost(host + "address/getBatch", params);
    }


    public void rechargeCallback(Map requestParams) {
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";

        boolean flag = SignUtils.verify(requestParams, platformPublicKey, requestParams.get("sign").toString());

        if (flag) {

            Map response = new HashMap();
            Map data = new HashMap();
            data.put("success_data", "success");

            response.put("stats", 200);
            response.put("data", data);
            response.put("sign", SignUtils.sign(response, privateKey));

            // do something or not
            // response to request


        } else {
            // do something or not
            // fail request
        }
    }

    public void withdrawCallBack(Map requestParams) {
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";

        boolean flag = SignUtils.verify(requestParams, platformPublicKey, requestParams.get("sign").toString());

        if (flag) {

            Map response = new HashMap();
            Map data = new HashMap();
            data.put("success_data", "success");

            response.put("stats", 200);
            response.put("data", data);
            response.put("sign", SignUtils.sign(response, privateKey));

            // do something or not
            // response to request


        } else {
            // do something or not
            // fail request
        }
    }
}
