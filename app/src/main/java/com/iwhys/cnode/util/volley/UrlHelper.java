package com.iwhys.cnode.util.volley;

import java.util.Map;

/**
 * 网络数据请求URL辅助类
 * Created by devil on 15/4/1.
 */
public class UrlHelper {

    public final static String HOST = "https://cnodejs.org";
    public final static String API = "/api/v1";
    public final static String TOPICS = HOST + API + "/topics";
    public final static String TOPIC = HOST + API + "/topic";
    public final static String USER = HOST + API + "/user";
    public final static String ACCESS_TOKEN = HOST + API + "/accesstoken";
    public final static String REPLY_SUFFIX = "/replies";

    /**
     * 主题列表url
     * @param params 参数
     */
    public static String getTopicsUrl(Map<String, Object> params){
        return resolve(TOPICS, params);
    }

    /**
     * 授权验证url
     */
    public static String getOauthUrl(){
        return ACCESS_TOKEN;
    }

    /**
     * 单个主题url
     * @param id 主题id
     */
    public static String getTopicUrl(String id){
        return UrlHelper.resolve(TOPIC, id);
    }

    /**
     * 回复url
     * @param id 主题id
     */
    public static String getReplyUrl(String id){
        return resolve(getTopicUrl(id), REPLY_SUFFIX);
    }

    //拼接url路径
    public static String resolve(String host, String path){
        StringBuilder builder = new StringBuilder(host);
        if (path.startsWith("/")&&host.endsWith("/")){
            path = path.substring(1);
        } else if (!path.startsWith("/")&&!host.endsWith("/")){
            builder.append("/");
        }
        builder.append(path);
        return builder.toString();
    }

    //拼接参数
    public static String resolve(String host, Map<String, Object> params){
        StringBuilder builder = new StringBuilder(host);
        if (!params.isEmpty()){
            builder.append("?");
            for (String key:params.keySet()){
                if (!builder.toString().endsWith("?")){
                    builder.append("&");
                }
                builder.append(key);
                builder.append("=");
                builder.append(params.get(key));
            }
        }
        return builder.toString();
    }
}
