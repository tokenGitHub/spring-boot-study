package com.hanhe.study.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.youzan.open.sdk.client.core.YZClient;

import com.youzan.open.sdk.gen.v1_0_0.api.YouzanLogisticsOrderQuery;
import com.youzan.open.sdk.gen.v1_0_0.api.YouzanTradeDcQueryQuerybyorderno;
import com.youzan.open.sdk.gen.v1_0_0.model.YouzanLogisticsOrderQueryParams;
import com.youzan.open.sdk.gen.v1_0_0.model.YouzanLogisticsOrderQueryResult;
import com.youzan.open.sdk.gen.v1_0_0.model.YouzanTradeDcQueryQuerybyordernoParams;
import com.youzan.open.sdk.gen.v3_0_0.api.*;
import com.youzan.open.sdk.gen.v3_0_0.model.*;
import com.youzan.open.sdk.gen.v4_0_0.api.YouzanTradeGet;
import com.youzan.open.sdk.gen.v4_0_0.api.YouzanTradesSoldGet;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetParams;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradeGetResult;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetParams;
import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetResult;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
/**
 * @ Author     ：
 * @ Date       ：
 * @ Description：有赞sdk调用接口
 * @ Modified By：
 * @Version:
 */

@Log4j
@Service("youzanAPIManager")
public class YouzanAPIManager{
    @Autowired
    private YouzanClient youzanClient;


    /**
     * 关键字查询商品列表
     * @param q
     * @return
     */
    public YouzanItemSearchResult listGoodsByQ(String q) {
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanItemSearchParams youzanItemSearchParams = new YouzanItemSearchParams();

        youzanItemSearchParams.setQ(q);

        YouzanItemSearch youzanItemSearch = new YouzanItemSearch();
        youzanItemSearch.setAPIParams(youzanItemSearchParams);
        YouzanItemSearchResult result = client.invoke(youzanItemSearch);
        return result;
    }

    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    public YouzanItemGetResult GoodsById(Long goodsId){
        YZClient client = youzanClient.getYzClient();//new Sign(appKey, appSecret)
        YouzanItemGetParams youzanItemGetParams = new YouzanItemGetParams();

        youzanItemGetParams.setItemId(goodsId);

        YouzanItemGet youzanItemGet = new YouzanItemGet();
        youzanItemGet.setAPIParams(youzanItemGetParams);
        YouzanItemGetResult result = client.invoke(youzanItemGet);

        return result;
    }

    //获取订单表
    public YouzanTradesSoldGetResult getSoldResult( Long page, String status, String yesterDayStr, String nowDateStr){
        return getSoldResult(page,status,yesterDayStr,nowDateStr,null);
    }

    //获取订单表
    public YouzanTradesSoldGetResult getSoldListResult( Long page, String status, String yesterDayStr, String nowDateStr,String goodsName,long pageSize){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanTradesSoldGetParams youzanTradesSoldGetParams = new YouzanTradesSoldGetParams();
        youzanTradesSoldGetParams.setPageSize(pageSize);
        youzanTradesSoldGetParams.setPageNo(page);
        youzanTradesSoldGetParams.setStatus(status); // WAIT_SELLER_SEND_GOODS WAIT_BUYER_CONFIRM_GOODS TRADE_SUCCESS
        youzanTradesSoldGetParams.setStartCreated(parseToDate(yesterDayStr));
        youzanTradesSoldGetParams.setEndCreated(parseToDate(nowDateStr));
        if (!Strings.isNullOrEmpty(goodsName)){
            youzanTradesSoldGetParams.setGoodsTitle(goodsName);
        }
        YouzanTradesSoldGet youzanTradesSoldGet = new YouzanTradesSoldGet();
        youzanTradesSoldGet.setAPIParams(youzanTradesSoldGetParams);
        YouzanTradesSoldGetResult result = client.invoke(youzanTradesSoldGet);
        return result;
    }

    //获取订单表
    public YouzanTradesSoldGetResult getSoldResult( Long page, String status, String yesterDayStr, String nowDateStr,String goodsName){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanTradesSoldGetParams youzanTradesSoldGetParams = new YouzanTradesSoldGetParams();
        youzanTradesSoldGetParams.setPageSize(50L);
        youzanTradesSoldGetParams.setPageNo(page);
        youzanTradesSoldGetParams.setStatus(status); // WAIT_SELLER_SEND_GOODS WAIT_BUYER_CONFIRM_GOODS TRADE_SUCCESS
        youzanTradesSoldGetParams.setStartCreated(parseToDate(yesterDayStr));
        youzanTradesSoldGetParams.setEndCreated(parseToDate(nowDateStr));
        if (!Strings.isNullOrEmpty(goodsName)){
            youzanTradesSoldGetParams.setGoodsTitle(goodsName);
        }
        YouzanTradesSoldGet youzanTradesSoldGet = new YouzanTradesSoldGet();
        youzanTradesSoldGet.setAPIParams(youzanTradesSoldGetParams);
        YouzanTradesSoldGetResult result = client.invoke(youzanTradesSoldGet);
        return result;
    }
    //获取订单详情
    public YouzanTradeGetResult getSoldDetailsResult(String tid){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanTradeGetParams youzanTradeGetParams = new YouzanTradeGetParams();
        youzanTradeGetParams.setTid(tid);
        YouzanTradeGet youzanTradeGet = new YouzanTradeGet();
        youzanTradeGet.setAPIParams(youzanTradeGetParams);
        YouzanTradeGetResult result = client.invoke(youzanTradeGet);
        return result;
    }
    //根据供货商名称查询商品
    public YouzanItemSearchResult getItemsSearchResult( Long page,Long pageSize,String q){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanItemSearchParams youzanItemSearchParams = new YouzanItemSearchParams();
        youzanItemSearchParams.setQ(q);
        youzanItemSearchParams.setPageNo(page);
        youzanItemSearchParams.setPageSize(pageSize);
        YouzanItemSearch youzanItemSearch = new YouzanItemSearch();
        youzanItemSearch.setAPIParams(youzanItemSearchParams);
        YouzanItemSearchResult result = client.invoke(youzanItemSearch);
        return result;
    }


    //根据供货商名称查询仓库里的商品
    public YouzanItemsInventoryGetResult getItemsInventoryResult( Long page,Long pageSize,String q){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanItemsInventoryGetParams youzanItemsInventoryGetParams = new YouzanItemsInventoryGetParams();

        youzanItemsInventoryGetParams.setPageNo(page);
        youzanItemsInventoryGetParams.setPageSize(pageSize);
        youzanItemsInventoryGetParams.setOrderBy("asc");
        youzanItemsInventoryGetParams.setQ(q);

        YouzanItemsInventoryGet youzanItemsInventoryGet = new YouzanItemsInventoryGet();
        youzanItemsInventoryGet.setAPIParams(youzanItemsInventoryGetParams);
        YouzanItemsInventoryGetResult result = client.invoke(youzanItemsInventoryGet);
        return result;
    }
    //获取单个商品详情
    public YouzanItemGetResult getItemResult(long itemId){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanItemGetParams youzanItemGetParams = new YouzanItemGetParams();

        youzanItemGetParams.setItemId(itemId);

        YouzanItemGet youzanItemGet = new YouzanItemGet();
        youzanItemGet.setAPIParams(youzanItemGetParams);
        YouzanItemGetResult result = client.invoke(youzanItemGet);
        return result;
    }

    //发货详情
    public JSONArray getOrdernoResult(String tid){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanTradeDcQueryQuerybyordernoParams youzanTradeDcQueryQuerybyordernoParams = new YouzanTradeDcQueryQuerybyordernoParams();

        youzanTradeDcQueryQuerybyordernoParams.setKdtId(40436671L);
        youzanTradeDcQueryQuerybyordernoParams.setOrderNo(tid);


        YouzanTradeDcQueryQuerybyorderno youzanTradeDcQueryQuerybyorderno = new YouzanTradeDcQueryQuerybyorderno();
        youzanTradeDcQueryQuerybyorderno.setAPIParams(youzanTradeDcQueryQuerybyordernoParams);
        String result = client.execute(youzanTradeDcQueryQuerybyorderno);
//        YouzanTradeDcQueryQuerybyordernoResult result = client.invoke(youzanTradeDcQueryQuerybyorderno);

        JSONObject resultObject = JSON.parseObject(result);
        JSONArray resultArray = resultObject.getJSONArray("response");

        return resultArray;
    }

    //退款
    public YouzanTradeRefundSearchResult getRefundResult(Long page, String status, long create_time_end, long create_time_start){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanTradeRefundSearchParams youzanTradeRefundSearchParams = new YouzanTradeRefundSearchParams();

        youzanTradeRefundSearchParams.setStatus(status);
        youzanTradeRefundSearchParams.setPageNo(page);
        youzanTradeRefundSearchParams.setPageSize(100L);

        YouzanTradeRefundSearch youzanTradeRefundSearch = new YouzanTradeRefundSearch();
        youzanTradeRefundSearch.setAPIParams(youzanTradeRefundSearchParams);
        YouzanTradeRefundSearchResult result = client.invoke(youzanTradeRefundSearch);
        return result;
    }

    //物流根据订单查物流下每个包裹的详细信息
    public YouzanLogisticsOrderQueryResult getLogisticsOrderResult(String tid){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanLogisticsOrderQueryParams youzanLogisticsOrderQueryParams = new YouzanLogisticsOrderQueryParams();

        youzanLogisticsOrderQueryParams.setKdtId(40436671L);
        youzanLogisticsOrderQueryParams.setTid(tid);
        youzanLogisticsOrderQueryParams.setSourceId(1002L);

        YouzanLogisticsOrderQuery youzanLogisticsOrderQuery = new YouzanLogisticsOrderQuery();
        youzanLogisticsOrderQuery.setAPIParams(youzanLogisticsOrderQueryParams);
        YouzanLogisticsOrderQueryResult result = client.invoke(youzanLogisticsOrderQuery);
        return result;
    }


    //根据有赞fansid获取opendi
    public List<String> getYouzanWeiXinOpenId(Set<String> fansSet, String fansListStr){
//        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        List<String> openidList = Collections.synchronizedList(new ArrayList<>());
        if (fansSet.size()>0){
//            YouzanUsersWeixinFollowerGetsParams youzanUsersWeixinFollowerGetsParams = new YouzanUsersWeixinFollowerGetsParams();
//            youzanUsersWeixinFollowerGetsParams.setFields("fans_id,fans_weixin_openid");
//            youzanUsersWeixinFollowerGetsParams.setFansIds(fansListStr);
//            YouzanUsersWeixinFollowerGets youzanUsersWeixinFollowerGets = new YouzanUsersWeixinFollowerGets();
//            youzanUsersWeixinFollowerGets.setAPIParams(youzanUsersWeixinFollowerGetsParams);
            YouzanUsersWeixinFollowerGetsResult result1 = getYouzanUsersWeixinFollowerGetsResult(fansListStr);

            Stream.of(result1.getUser()).forEach(t -> {
                openidList.add(t.getWeixinOpenid());
            });
        }
        return openidList;
    }

    public Map<String, String> getYouzanWeiXinOpenId(Set<String> fansSet){
        Map<String, String> openidList = new HashMap<>();
        for(String fansListStr : fansSet){
            YouzanUsersWeixinFollowerGetsResult result1 = getYouzanUsersWeixinFollowerGetsResult(fansListStr);
            Stream.of(result1.getUser()).forEach(t -> {
                openidList.put(fansListStr, t.getWeixinOpenid());
            });
        }
        return openidList;
    }

    private YouzanUsersWeixinFollowerGetsResult getYouzanUsersWeixinFollowerGetsResult(String fansListStr){
        YZClient client = youzanClient.getYzClient(); //new Sign(appKey, appSecret)
        YouzanUsersWeixinFollowerGetsParams youzanUsersWeixinFollowerGetsParams = new YouzanUsersWeixinFollowerGetsParams();
        youzanUsersWeixinFollowerGetsParams.setFields("fans_id,fans_weixin_openid");
        youzanUsersWeixinFollowerGetsParams.setFansIds(fansListStr);
        YouzanUsersWeixinFollowerGets youzanUsersWeixinFollowerGets = new YouzanUsersWeixinFollowerGets();
        youzanUsersWeixinFollowerGets.setAPIParams(youzanUsersWeixinFollowerGetsParams);
        YouzanUsersWeixinFollowerGetsResult result1 = client.invoke(youzanUsersWeixinFollowerGets);
        return result1;
    }

    public YouzanUmpCouponTakeResult sendCouponResult(String openId,long coupon){

        YZClient client = youzanClient.getYzClient();
        YouzanUmpCouponTakeParams youzanUmpCouponTakeParams = new YouzanUmpCouponTakeParams();
        youzanUmpCouponTakeParams.setWeixinOpenid(openId);
        youzanUmpCouponTakeParams.setCouponGroupId(coupon);
        YouzanUmpCouponTake youzanUmpCouponTake = new YouzanUmpCouponTake();
        youzanUmpCouponTake.setAPIParams(youzanUmpCouponTakeParams);
        YouzanUmpCouponTakeResult result = client.invoke(youzanUmpCouponTake);
        return result;
    }

    private  Date parseToDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
