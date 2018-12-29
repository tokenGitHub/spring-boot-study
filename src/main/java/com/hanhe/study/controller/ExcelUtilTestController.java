package com.hanhe.study.controller;

import cn.edu.hfut.dmic.webcollector.example.TutorialCrawler;
import com.hanhe.study.asyncTask.DoSpiderTask;
import com.hanhe.study.asyncTask.TestTask;
import com.hanhe.study.asyncTask.spiders.FirstSpider;
import com.hanhe.study.domain.ExcelTestDomain;
import com.hanhe.study.utils.ExcelUtil;
import com.hanhe.study.utils.FilterInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("test")
@AllArgsConstructor
public class ExcelUtilTestController {
    private TestTask task;
    private DoSpiderTask spiderTask;

    @GetMapping("getExcel")
    public void getTestExcel(HttpServletResponse response){
        List<ExcelTestDomain> list = new ArrayList<>();
        for(int i = 0 ;i < 10 ;i++){
            ExcelTestDomain testDomain = new ExcelTestDomain();
            testDomain.setId(i);
            testDomain.setAge(i * 10);
            testDomain.setName(randomName());
            testDomain.setCreateDate(new Date(System.currentTimeMillis()));
            testDomain.setStatus(i % 3);
            list.add(testDomain);
        }
        String columns[][] = {
            {"id", "age", "name", "createDate", "status"},  //第一行为属性名
            {"编号", "年龄", "姓名", "创建日期", "状态"}       //第二行为显示名  一一对应
        };
        ExcelUtil.outputDetailExcel(columns, list, response, "测试数据", (column, data) -> {
            if("status".equals(column)){
                switch (data.toString()){
                    case "0": return "小学";
                    case "1": return "初中";
                    case "2": return "高中";
                }
            }
            return data.toString();
        });
    }

    private String randomName(){
        Random random = new Random();
        int len = random.nextInt(15) + 5;
        String ret = "";
        for(int i = 0;i < len;i++){
            ret += (char)(random.nextInt(26) + 'A');
        }
        return ret;
    }

    @ResponseBody
    @GetMapping("async")
    public String testAsync() throws Exception{
        List<Future<Long>> taskFutures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for(int i = 0 ;i < 500 ;i++) {
            taskFutures.add(task.doTask(i + "Thread  " )) ;
            log.info("  " +i);
        }

        task.taskReturnValueMange(taskFutures,start);
        return "ok";
    }

    @ResponseBody
    @GetMapping("toString")
    public String toStringTest() throws Exception{
        String ret = "";

        List<Object> list = new ArrayList<>();
        list.add(new String());
        list.add(new Integer(0));
        list.add(new Long(0));
        list.add(new ArrayList<>());
        list.add(new HashMap<>());
        list.add(new Object());
        list.add(new Hashtable<>());
        list.add(new ExcelTestDomain());

        for(int i = 0;i < list.size();i++){
            ret += FilterInterface.toString(list.get(i)) + " <br/> <br/>";
        }
        return ret;
    }

    @ResponseBody
    @GetMapping("doTask")
    public String doTask(){
        spiderTask.doBaiduTask();

//        String string = "<!doctype html>\n" +
//                "          <li class=\"hdline0\"> <i class=\"dot\"></i> <strong> <a href=\"http://m.news.cctv.com/2018/12/28/ARTIgt3BAxKKCZdSsXi7aJoP181228.shtml\" target=\"_blank\" class=\"a3\" mon=\"ct=1&amp;a=1&amp;c=top&amp;pn=0\">习近平出访欧洲拉美并出席G20峰会纪实</a></strong> </li> \n" +
//                "          <li class=\"hdline1\"> <i class=\"dot\"></i> <strong> <a href=\"http://news.youth.cn/sz/201812/t20181229_11829487.htm\" target=\"_blank\" mon=\"r=1\">十八届三中全会五周年了</a> </strong> </li> \n" +
//                "          <li class=\"hdline2\"> <i class=\"dot\"></i> <strong> <a href=\"http://news.cctv.com/2018/12/28/ARTImXn7bFSxwHvjvkMTO12W181228.shtml\" target=\"_blank\" class=\"a3\" mon=\"ct=1&amp;a=1&amp;c=top&amp;pn=1\">“千万工程”的浙江实践</a></strong> </li> \n" +
//                "          <li class=\"hdline3\"> <i class=\"dot\"></i> <strong> <a href=\"http://news.cctv.com/2018/12/26/ARTIwsXfc2duvm9zPx08hFVr181226.shtml\" target=\"_blank\" mon=\"r=1\">2019中国民营经济怎么走？中央这样定调</a> </strong> </li> \n" +
//                "          <li class=\"hdline4\"> <i class=\"dot\"></i> <strong> <a href=\"https://world.chinadaily.com.cn/a/201812/28/WS5c25dec6a3106072a9033089.html\" target=\"_blank\" class=\"a3\" mon=\"ct=1&amp;a=1&amp;c=top&amp;pn=2\">北斗拥抱全球 虹云飞天 外媒赞中国卫星技术</a></strong> </li> \n" +
//                "          <li class=\"hdline5\"> <i class=\"dot\"></i> <strong> <a href=\"http://news.cctv.com/2018/12/28/ARTISLeGIOOP2hEFZJK2ICyU181228.shtml\" target=\"_blank\" mon=\"r=1\">这位中国空军“王牌旅长”被授予“时代楷模”称号</a> </strong> </li> \n" +
//                "         <li class=\"bold-item\"> <span class=\"dot\"></span> <a href=\"http://baijiahao.baidu.com/s?id=1621112941531586624\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=1\" target=\"_blank\">快看!2019年这些新规将落地,你的工资还能涨!</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621112841928552019\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=2\" target=\"_blank\">2019年省级两会时间表出炉,多地强调严肃会风会纪</a></li> \n" +
//                "         <li> <a href=\"http://www.chinanews.com/cj/2018/12-29/8715276.shtml\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=3\" target=\"_blank\">两部门：年终奖2022年前单独缴纳个税 </a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621080338162919041\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=4\" target=\"_blank\">元旦假期天气出炉:寒冷将贯穿整个假期北上可滑雪南下需防寒</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621138282507574175\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=5\" target=\"_blank\">12306网站用户信息泄露?铁总回应:网传信息不实</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621122678442834602\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=6\" target=\"_blank\">个税申报实用帖来啦!手把手教您怎么报税、怎么享受优惠</a></li> \n" +
//                "         <li class=\"bold-item\"> <span class=\"dot\"></span> <a href=\"http://baijiahao.baidu.com/s?id=1621141203840254427\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=7\" target=\"_blank\">警惕!这种新型毒品已瞄准了00后,皮肤一碰就沾毒</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621084394697896596\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=8\" target=\"_blank\">海关总署：允许美国大米输华</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621121056144406744\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=9\" target=\"_blank\">中国官方驳斥\"韩国雾霾来自中国\",韩媒却在坚持甩锅</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621120052869930923\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=10\" target=\"_blank\">2018中国经济表情:大变局之下的喜怒哀乐</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621145778589510184\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=11\" target=\"_blank\">海外中国公民护照政策将有这些大调整!明年1月正式实施</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621100578697377263\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=12\" target=\"_blank\">美国总统特朗普又出“新瓜” ,被曝开假证明逃兵役</a></li> \n" +
//                "         <li class=\"bold-item\"> <span class=\"dot\"></span> <a href=\"http://baijiahao.baidu.com/s?id=1621139301237826484\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=13\" target=\"_blank\">麻烦大了!特朗普这一趟去伊拉克,不仅仅泄密那么简单</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621141534918792645\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=14\" target=\"_blank\">美国政府关门风波:特朗普取消休假计划,博物馆将停业</a></li> \n" +
//                "         <li> <a href=\"http://www.bjnews.com.cn/world/2018/12/29/535000.html\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=15\" target=\"_blank\">印尼海啸伤者增至7202,巽他海峡海域可能再发海啸</a></li> \n" +
//                "         <li> <a href=\"http://www.chinanews.com/gj/2018/12-29/8715411.shtml\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=16\" target=\"_blank\">俄媒:乌克兰解除针对俄罗斯男性的入境禁令 </a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621144020581097763\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=17\" target=\"_blank\">金正恩勉励朝鲜劳动英雄:搞好明年农业再次见面</a></li> \n" +
//                "         <li> <a href=\"http://world.people.com.cn/n1/2018/1229/c1002-30494307.html\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=18\" target=\"_blank\">埃及金字塔附近发生恐怖爆炸,已致4死10伤</a></li> \n" +
//                "         <li class=\"bold-item\"> <span class=\"dot\"></span> <a href=\"http://baijiahao.baidu.com/s?id=1621141512881090378\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=19\" target=\"_blank\">侠客岛:百亿保健帝国权健背后的\"北派传销\"身影</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621148017029992994\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=20\" target=\"_blank\">打脸?权健发起投票请网友站队,91%支持丁香医生</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621142766366119892\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=21\" target=\"_blank\">2020年台湾谁能问鼎?命理师说此人最有“帝王相”</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621084005580770688\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=22\" target=\"_blank\">唐山监狱一服刑人员非正常死亡,两名狱警涉嫌玩忽职守被查</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621116662260321663\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=23\" target=\"_blank\">刘德华流泪宣布中止演唱会一刻,朱丽倩立即站起来为老公打气</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621139555161930226\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=24\" target=\"_blank\">房租上涨致终南山隐士\"还俗\"? 官方回应:清理整治违建</a></li> \n" +
//                "         <li class=\"bold-item\"> <span class=\"dot\"></span> <a href=\"http://baijiahao.baidu.com/s?id=1620995190317103803\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=25\" target=\"_blank\">跨年去哪秀恩爱?2019全球10大跨年目的地出炉</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621107042316608569\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=26\" target=\"_blank\">亚洲第一个支持“同性伴侣关系合法化”的国家出现了</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621147056978365214\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=27\" target=\"_blank\">政府办副主任借“卖画”受贿:四幅画值1万却收300万</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621148453979605043\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=28\" target=\"_blank\">美国向克里米亚派出战机之后 美国:我们被俄方安全拦截了</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621064822337042539\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=29\" target=\"_blank\">600亿骗局!这位80后女版乔布斯,连首富和总统也耍得团团转</a></li> \n" +
//                "         <li> <a href=\"http://baijiahao.baidu.com/s?id=1621147549913325233\" mon=\"ct=1&amp;a=2&amp;c=top&amp;pn=30\" target=\"_blank\">孕妇与7岁自闭症患儿身亡 教育局:已在幼儿园进行心理辅导</a></li> \n" +
//                "         <li class=\"li_0 li_color_0 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E5%AE%A1%E8%AE%AE%E3%80%8A%E4%B8%AD%E5%9B%BD%E5%85%B1%E4%BA%A7%E5%85%9A%E6%94%BF%E6%B3%95%E5%B7%A5%E4%BD%9C%E6%9D%A1%E4%BE%8B%E3%80%8B\" target=\"_blank\" class=\"hotwords_li_a\" title=\"习近平主持中央政治局会议审议《中国共产党政法工作条例》\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=1\">习近平主持<br>中央政治局会议<br>审议《中国共产党<br>政法工作条例》</a> </li> \n" +
//                "         <li class=\"li_1 li_color_1 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E5%9B%9E%E9%A6%962018%20%E9%87%8D%E6%B8%A9%E4%B9%A0%E8%BF%91%E5%B9%B3%E8%BF%9918%E5%8F%A5%E8%AF%9D\" target=\"_blank\" class=\"hotwords_li_a\" title=\"回首2018重温习近平这18句话\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=2\">回首2018<br>重温习近平这18句话</a> </li> \n" +
//                "         <li class=\"li_2 li_color_2 button-slide\"> <a href=\"https://www.baidu.com/s?wd=2019%E7%9C%81%E7%BA%A7%E4%B8%A4%E4%BC%9A%E6%97%B6%E9%97%B4%E8%A1%A8\" target=\"_blank\" class=\"hotwords_li_a\" title=\"2019省级两会时间表\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=3\">2019省级两会<br>时间表</a> </li> \n" +
//                "         <li class=\"li_3 li_color_3 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E5%85%83%E6%97%A6%E4%BA%A4%E9%80%9A%E5%87%BA%E8%A1%8C%E5%AE%89%E5%85%A8%E9%A2%84%E8%AD%A6\" target=\"_blank\" class=\"hotwords_li_a\" title=\"元旦交通出行安全预警\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=4\">元旦交通出行<br>安全预警</a> </li> \n" +
//                "         <li class=\"li_4 li_color_4 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E6%B1%BD%E3%80%81%E6%9F%B4%E6%B2%B9%E4%BB%B7%E6%A0%BC%E9%99%8D%E4%BD%8E\" target=\"_blank\" class=\"hotwords_li_a\" title=\"汽、柴油价格降低\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=5\">汽、柴油<br>价格降低</a> </li> \n" +
//                "         <li class=\"li_5 li_color_5 button-slide\"> <a href=\"https://www.baidu.com/s?wd=2018%E5%9B%BD%E9%99%85%E5%8D%81%E5%A4%A7%E6%96%B0%E9%97%BB\" target=\"_blank\" class=\"hotwords_li_a\" title=\"2018国际十大新闻\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=6\">2018国际<br>十大新闻</a> </li> \n" +
//                "         <li class=\"li_6 li_color_6 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E7%AC%AC%E4%B8%89%E6%9E%B6C919%E5%AE%8C%E6%88%90%E9%A6%96%E9%A3%9E\" target=\"_blank\" class=\"hotwords_li_a\" title=\"第三架C919完成首飞\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=7\">第三架C919<br>完成首飞</a> </li> \n" +
//                "         <li class=\"li_7 li_color_7 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E6%9D%83%E5%81%A5\" target=\"_blank\" class=\"hotwords_li_a\" title=\"权健涉嫌夸大宣传\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=8\">权健涉嫌<br>夸大宣传</a> </li> \n" +
//                "         <li class=\"li_8 li_color_8 button-slide\"> <a href=\"https://www.baidu.com/s?wd=3469%E6%AC%BEAPP%E8%A2%AB%E5%85%B3%E5%81%9C\" target=\"_blank\" class=\"hotwords_li_a\" title=\"3469款APP被关停\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=9\">3469款<br>APP被关停</a> </li> \n" +
//                "         <li class=\"li_9 li_color_9 button-slide\"> <a href=\"https://www.baidu.com/s?wd=%E5%9B%BD%E8%B6%B3%E4%BA%9A%E6%B4%B2%E6%9D%AF23%E4%BA%BA%E5%90%8D%E5%8D%95\" target=\"_blank\" class=\"hotwords_li_a\" title=\"国足亚洲杯23人名单\" mon=\"ct=1&amp;c=top&amp;a=30&amp;pn=10\">国足亚洲杯<br>23人名单</a> </li> \n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621148232595826759\" target=\"_blank\" class=\"item-image\" mon=\"&amp;a=12\" title=\"二十四年互联网大佬往事\" style=\"background-image:url(http://hiphotos.baidu.com/news/crop%3D3%2C0%2C635%2C426%3Bq%3D80%3B/sign=0ded679056b5c9ea76bc59a3e80a9a30/9d82d158ccbf6c81b97c7d73b13eb13532fa40e5.jpg)\"></a> \n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621148232595826759\" target=\"_blank\" class=\"item-title\" title=\"二十四年互联网大佬往事\" mon=\"&amp;a=9\">二十四年互联网大佬往事</a> \n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621148561931070418\" title=\"共享汽车，现在退钱还来得及吗？\" target=\"_blank\" mon=\"&amp;a=12\" class=\"img\" style=\"background-image:url(http://hiphotos.baidu.com/news/crop%3D51%2C0%2C538%2C361%3Bq%3D80%3B/sign=77b3856676899e516cc160547f92e81f/32fa828ba61ea8d3bf56229f9a0a304e251f5830.jpg)\"></a>\n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621148561931070418\" mon=\"&amp;a=9\" class=\"txt\" target=\"_blank\">共享汽车，现在退钱还来得及吗？</a> \n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621149636171018408\" title=\"小扎2018总结惹众怒\" target=\"_blank\" mon=\"&amp;a=12\" class=\"img\" style=\"background-image:url(http://hiphotos.baidu.com/news/crop%3D16%2C0%2C608%2C408%3Bq%3D80%3B/sign=3cf4909da2c379316927dc69d6f58169/960a304e251f95ca95ccc02dc4177f3e6709526b.jpg)\"></a>\n" +
//                "          <a href=\"http://baijiahao.baidu.com/s?id=1621149636171018408\" mon=\"&amp;a=9\" class=\"txt\" target=\"_blank\">小扎2018总结惹众怒</a> \n" +
//                "         <li class=\"bold-item\"><a href=\"http://baijiahao.baidu.com/s?id=1621148017029992994\" target=\"_blank\" mon=\"a=9\">权健发起投票请网友站队 91%支持丁香医生</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621098723264941221\" target=\"_blank\" mon=\"a=9\">揭秘摄像头黑产链：暴露在外的80端口</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621146804135896072\" target=\"_blank\" mon=\"a=9\">2018，互联网消费下沉的一年</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621147050016876106\" target=\"_blank\" mon=\"a=9\">12306脱库疑云：410万数据仅售20美元</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621028947374987975\" target=\"_blank\" mon=\"a=9\">这项投资让苹果今年损失超90亿美元</a></li> \n" +
//                "         <li class=\"bold-item\"><a href=\"http://baijiahao.baidu.com/s?id=1621151117990372996\" target=\"_blank\" mon=\"a=9\">“贺建奎”让基因编辑“背了个大锅”</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621043467215053351\" target=\"_blank\" mon=\"a=9\">富士康拟在印度组装高端iPhone</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621018541086511767\" target=\"_blank\" mon=\"a=9\">阿里系哈啰出行完成新一轮40亿元融资</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621048461249040732\" target=\"_blank\" mon=\"a=9\">专利显示苹果准备打造可折叠iPhone</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621025076439983562\" target=\"_blank\" mon=\"a=9\">共享单车“一地鸡毛” 5G成共享经济下个推手</a></li> \n" +
//                "         <li class=\"bold-item\"><a href=\"http://baijiahao.baidu.com/s?id=1621051589974536514\" target=\"_blank\" mon=\"a=9\">电商法元旦实施 微商代购结束野蛮生长时代</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621005331118698336\" target=\"_blank\" mon=\"a=9\">卖身、离场、坚持、转机：属于智能手机的2018</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621053153134939682\" target=\"_blank\" mon=\"a=9\">奥飞欲出售有妖气部分资产</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621052245840933765\" target=\"_blank\" mon=\"a=9\">锤子摇摇欲坠，谁将是罗永浩的白衣骑士？</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621063114069271890\" target=\"_blank\" mon=\"a=9\">腾讯短视频十四路抗“日”</a></li> \n" +
//                "         <li><a href=\"http://baijiahao.baidu.com/s?id=1621061192607818339\" target=\"_blank\" mon=\"a=9\">FB被指靠PPT审核内容：信息过时、不准确</a></li> \n" +
//                "            <a href=\"http://baijiahao.baidu.com/s?id=1621146804135896072\" target=\"_blank\" class=\"item-image\" mon=\"&amp;a=12\" title=\"2018，互联网消费下沉的一年\" style=\"background-image:url(http://hiphotos.baidu.com/news/crop%3D4%2C0%2C477%2C320%3Bq%3D80%3B/sign=78c8875bbf119313d30ca5f0580c20e7/a71ea8d3fd1f4134bc814ce7281f95cad0c85ef0.jpg)\"></a> \n" +
//                "            <a href=\"http://baijiahao.baidu.com/s?id=1621146804135896072\" target=\"_blank\" class=\"item-title\" title=\"2018，互联网消费下沉的一年\" mon=\"&amp;a=9\">2018，互联网消费下沉的一年</a> \n" +
//                "                          cpOptions_1.data.push({\n" +
//                "          //\"title\": \"中共中央政治局民主生活会 习近平主持并发表讲话\",\n" +
//                "          \"title\": \"中共中央政治局民主生活会 习近平主持并发表讲话\",\n" +
//                "          \"url\": \"http:\\/\\/www.xinhuanet.com\\/politics\\/2018-12\\/26\\/c_1123909688.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=40a42baebede9c82a065fd8f5c8380d2\\/ac6eddc451da81cbeb274c5a5f66d01608243160.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/www.xinhuanet.com\\/politics\\/2018-12\\/26\\/c_1123909688.htm\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 2,\n" +
//                "          //\"title\": \"太行山高速公路主体工程开通\",\n" +
//                "          \"title\": \"太行山高速公路主体工程开通\",\n" +
//                "          \"url\": \"http:\\/\\/photo.china.com.cn\\/2018-12\\/29\\/content_74323340.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=60be32204a10b912b9c1f2fef3fcfcb5\\/f636afc379310a551eb6ab6fba4543a98326109e.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/photo.china.com.cn\\/2018-12\\/29\\/content_74323340.htm\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 3,\n" +
//                "          //\"title\": \"三代“铁警”40年的火车缘\",\n" +
//                "          \"title\": \"三代“铁警”40年的火车缘\",\n" +
//                "          \"url\": \"https:\\/\\/www.chinanews.com\\/tp\\/hd2011\\/2018\\/12-28\\/859639.shtml\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=5ccd5574a4ec8a13121a53e0c7029157\\/37d3d539b6003af33166adfb382ac65c1138b648.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"https:\\/\\/www.chinanews.com\\/tp\\/hd2011\\/2018\\/12-28\\/859639.shtml\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 4,\n" +
//                "          //\"title\": \"哈尔滨:雪博会大型主塑《星河之旅》落成迎客\",\n" +
//                "          \"title\": \"哈尔滨:雪博会大型主塑《星河之旅》落成迎客\",\n" +
//                "          \"url\": \"http:\\/\\/photo.china.com.cn\\/2018-12\\/29\\/content_74323341.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=b6b0a7897f0e0cf3a6f74afb3a47f23d\\/b3b7d0a20cf431aded0d49a24636acaf2fdd9849.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/photo.china.com.cn\\/2018-12\\/29\\/content_74323341.htm\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 5,\n" +
//                "          //\"title\": \"吉林查干湖冬捕启幕 “头鱼”拍出近百万\",\n" +
//                "          \"title\": \"吉林查干湖冬捕启幕 “头鱼”拍出近百万\",\n" +
//                "          \"url\": \"http:\\/\\/picture.youth.cn\\/qtdb\\/201812\\/t20181229_11829304.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=052a325b8f025aafd5327acbcbecab8d\\/6f061d950a7b0208afcd4b7a6fd9f2d3572cc821.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/picture.youth.cn\\/qtdb\\/201812\\/t20181229_11829304.htm\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 6,\n" +
//                "          //\"title\": \"刘家岭壁画墓陈列馆开馆\",\n" +
//                "          \"title\": \"刘家岭壁画墓陈列馆开馆\",\n" +
//                "          \"url\": \"http:\\/\\/www.xinhuanet.com\\/photo\\/2018-12\\/29\\/c_1123921870_2.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=7e9e90b87ef082022b92953f7bfafb8a\\/4d086e061d950a7bd1dd3a5f07d162d9f3d3c945.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/www.xinhuanet.com\\/photo\\/2018-12\\/29\\/c_1123921870_2.htm\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 7,\n" +
//                "          //\"title\": \"非洲企鹅超萌亮相罗马动物园\",\n" +
//                "          \"title\": \"非洲企鹅超萌亮相罗马动物园\",\n" +
//                "          \"url\": \"http:\\/\\/www.chinanews.com\\/tp\\/hd2011\\/2018\\/12-27\\/859535.shtml\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=ff3f1a29bfb7d0a27dc9009dfbee760d\\/ca1349540923dd540445d1f3dc09b3de9c824872.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n" +
//                "        imgList.push({\"url\":\"http:\\/\\/www.chinanews.com\\/tp\\/hd2011\\/2018\\/12-27\\/859535.shtml\"});\n" +
//                "                                cpOptions_1.data.push({\n" +
//                "          \"index\": 8,\n" +
//                "          //\"title\": \"埃及一旅游巴士遭路边炸弹袭击\",\n" +
//                "          \"title\": \"埃及一旅游巴士遭路边炸弹袭击\",\n" +
//                "          \"url\": \"http:\\/\\/www.xinhuanet.com\\/photo\\/2018-12\\/29\\/c_1123921512.htm\",\n" +
//                "          \"imgUrl\": \"https:\\/\\/imgsa.baidu.com\\/news\\/q%3D100\\/sign=eb94fab9c5fcc3ceb2c0cd33a244d6b7\\/78310a55b319ebc41bc100028f26cffc1f1716eb.jpg\",\n" +
//                "          \"abs\": \"\",\n" +
//                "          \"meadia\": \"\"\n" +
//                "        });\n";
//
//        Pattern pattern = Pattern.compile("http://(\\w+.)+");
//        Matcher matcher = pattern.matcher(string);
//        while (matcher.find()){
//            String subStr = matcher.group();
//            String e = matcher.group();
//            //截取出括号中的内容
//            String substring = e.substring(3, e.length()-1);
//            //字符串截取
//            CharSequence subSequence = string.subSequence(matcher.start(0), matcher.end(0));
//            System.out.println("开始位置:"+matcher.start(0)+" 结束位置:"+matcher.end(0));
//            System.out.println(subSequence.toString());
//            System.out.println(e);
//        }
        return " 这就是 return";
    }
}
