package com.sumavision.tetris.cms.aliPush;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;

@Service
public class AliPushService extends BasePush{
	
	/**
	 * 推送通知/消息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午1:25:06
	 * @param title 标题
	 * @param body 内容
	 * @param param 附加参数
	 */
	public void sendMessage(String title, String body, String param) throws Exception{

        PushRequest pushRequest = new PushRequest();
        //安全性比较高的内容建议使用HTTPS
        pushRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        pushRequest.setMethod(MethodType.POST);
        // 推送目标
        pushRequest.setAppKey(appKey);
//        pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送  ALIAS:按别名推送  ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
//        pushRequest.setTargetValue("b58f5172a6b14c8d9afce96e389d16d1"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setTarget("ALL"); //推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
        pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=device, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.


        // 推送配置
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody(body); // 消息的内容

        // 推送配置: iOS
        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
        pushRequest.setIOSSilentNotification(false);//开启静默通知
        pushRequest.setIOSMusic("default"); // iOS通知声音
        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
        
        // 推送配置: Android
        pushRequest.setAndroidNotifyType("NONE");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidOpenType("ACTIVITY"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
        //pushRequest.setAndroidOpenUrl("http://192.165.56.84:8086/cms/resource/article/24/70c5d5e653e84dd288df2189973024b5.html"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
        pushRequest.setAndroidActivity(androidActivity); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        pushRequest.setAndroidMusic("default"); // Android通知音乐
        pushRequest.setAndroidExtParameters(param); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)

        // 推送控制
        Date pushDate = new Date(System.currentTimeMillis()); // 30秒之间的时间点, 也可以设置成你指定固定时间
        String pushTime = ParameterHelper.getISO8601Time(pushDate);
        pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        pushRequest.setExpireTime(expireTime);
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到

        PushResponse pushResponse = client.getAcsResponse(pushRequest);
        System.out.printf("RequestId: %s, MessageID: %s\n",
                pushResponse.getRequestId(), pushResponse.getMessageId());
	}
	
	//发送短信--值周放到tetris-user里
	public void sendSms() throws Exception{
		
		CommonRequest request = new CommonRequest();
//        request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms"); //系统规定参数。取值：SendSms。
        request.putQueryParameter("RegionId", region);
        request.putQueryParameter("PhoneNumbers", "13810728953"); //支持对多个手机号码发送短信，手机号码之间以英文逗号（,）分隔。上限为1000个手机号码
        request.putQueryParameter("SignName", "过来玩"); //短信签名名称。请在控制台签名管理页面签名名称一列查看。
        request.putQueryParameter("TemplateCode", "SMS_131795230"); //短信模板ID。请在控制台模板管理页面模板CODE一列查看。
        JSONObject param = new JSONObject();
        param.put("code", "500202");
        request.putQueryParameter("TemplateParam", param.toJSONString()); //短信模板变量对应的实际值，JSON格式。
        
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
	}
}
