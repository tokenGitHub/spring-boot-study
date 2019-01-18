package com.hanhe.study.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.hanhe.study.utils.YouzanAPIManager;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j
@Controller
@AllArgsConstructor
@RequestMapping("tweetsFlow")
public class TweetsFlowStatisticsController {
    private YouzanAPIManager youzanAPIManager;
    @ResponseBody
    @GetMapping("doTask")
    public Object doTask() throws Exception {
        double count = 0;
//        String dateStart = "2019-01-03 00:00:00";
//        String daetEnd   = "2019-01-04 00:00:00";  //上上周


        String dateStart = "2018-12-30 00:00:00";  //上上上周
        String daetEnd   = "2018-12-31 00:00:00";

//        String dateStart = "2019-01-25 00:00:00";   //上上上上周
//        String daetEnd   = "2019-01-26 00:00:00";

//        String dateStart = "2019-01-18 00:00:00";   //上上上上上周
//        String daetEnd   = "2019-01-19 00:00:00";

        Map<String, Double> totalMap = new HashMap<>();
        String[] statuses = {"WAIT_SELLER_SEND_GOODS", "TRADE_SUCCESS", "WAIT_BUYER_CONFIRM_GOODS", "WAIT_BUYER_PAY", "TRADE_REFUND"};
        Set<String> userSet = FileController.getWechatIdSet("/var/root/Desktop/2019-01-03SqlResult.csv");
//        Set<String> userSet = FileController.getWechatIdSet("/var/root/Desktop/allDate.csv");

        log.info("calculate start ================================");

        totalMap.put("totalYouzan", 0d);
        totalMap.put("totalUser", Double.valueOf(userSet.size()));
        for (String status : statuses) {
            Map<String, YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail> orderInfoMap = getSoldTidMap(dateStart, daetEnd, status);
            Map<String,String> userInfoMap  = youzanAPIManager.getYouzanWeiXinOpenId(orderInfoMap.keySet());

            Double val = totalMap.get("totalYouzan");
            totalMap.put("totalYouzan", val + orderInfoMap.keySet().size());
            for (String key : userInfoMap.keySet()) {

                String opendId = userInfoMap.get(key);
                Long fansId = Long.parseLong(key);
                log.info("key : " + key);

                YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail detail = orderInfoMap.get(fansId.toString());
                log.info("openid : " + opendId);

                if (userSet.contains(opendId)) {
                    totalMap.put("status_count", count++);
                    replace("status_" + status, detail, totalMap);
                    replace("status_", totalMap, detail);
                }
                replace( status, detail, totalMap);
                replace("", totalMap, detail);
            }
        }
        log.info(" calculate end ================================");

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(totalMap);


        return FileController.jsonStrToArr(jsonObject);
    }

    private void replace(String prefix, Map<String, Double> totalMap, YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail detail){
        Double paymentSum = totalMap.get(prefix + "paymentSum");
        setValue(prefix + "paymentSum", paymentSum, detail.getPayment(), totalMap);
    }

    private void replace(String key, YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail detail, Map<String, Double> totalMap){
        Double value = totalMap.get(key);
        setValue(key, value, detail.getPayment(), totalMap);
    }

    public void setValue(String key, Double oldValue, Float newValue, Map<String, Double> totalMap){
        if(oldValue == null) {
            totalMap.put(key, Double.valueOf(newValue));
        }else{
            totalMap.put(key, oldValue + newValue);
        }
    }

    private Map<String, YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail> getSoldTidMap(
            String yesterDayStr, String nowDateStr, String status) {

        String lastSevenDateStr = yesterDayStr;
        Map<String, YouzanTradesSoldGetResult.StructurizationTradePayInfoDetail> resultMap = new HashMap<>();

        log.info("========== get order info start ==========");
        YouzanTradesSoldGetResult result;
        long page = 1L;
        log.info(" status : " + status);
        do {
            log.info(" page : " + page);
            result = youzanAPIManager.getSoldListResult(page, status, lastSevenDateStr, nowDateStr, null, 100L);
            Arrays.stream(result.getFullOrderInfoList()).forEach(t -> {
                YouzanTradesSoldGetResult.StructurizationTradeOrderInfo orderInfo = t.getFullOrderInfo();
                resultMap.put(orderInfo.getBuyerInfo().getFansId() + "", orderInfo.getPayInfo());
                log.info(" fansId : " + orderInfo.getBuyerInfo().getFansId());
            });

            page++;
        } while (result.getFullOrderInfoList().length > 0);

        log.info("========== get order info end ===========");
        return resultMap;
    }
}
