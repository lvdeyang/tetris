//package com.sumavision.bvc.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
///**
// * 业务登录集成<br/>
// * <b>作者:</b>lvdeyang<br/>
// * <b>版本：</b>1.0<br/>
// * <b>日期：</b>2018年10月19日 下午2:13:17
// */
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class BusinessResourceSecurityConfig extends ResourceServerConfigurerAdapter{
//
//	@Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//        
//        	//这里将不需要进行验证的接口配置成例外
//            .antMatchers(
//            		"/api/query/**", 
//            		"/api/**", 
//            		"/web/**", 
//            		"/images/**", 
//            		"/index", 
//            		"/monitor/record/index", 
//            		"/monitor/subtitle/index", 
//            		"/monitor/osd/index", 
//            		"/monitor/record/playback/index", 
//            		"/monitor/record/playback/list/index", 
//            		"/monitor/vod/index", 
//            		"/monitor/live/terminal/index", 
//            		"/monitor/live/monitor/index", 
//            		"/monitor/live/channel/index", 
//            		"/monitor/file/import/index",
//            		"/monitor/live/list/index",
//            		"/monitor/external/static/resource/folder/index",
//            		"/monitor/file/index",
//            		"/userInfo/**", 
//            		"/multiplayerChat/**", 
//            		"/logic/**", 
//            		"/test/**",
//            		"/server/**",
//            		//92点播系统项目例外
//            		"/monitor/api/92/**",
//            		"/router/**",
//            		"/largescreenControlController/**",
//            		"/ts_download_tmp/**",
//            		"/device/group/record/download/**")
//            .permitAll()
//            .anyRequest()
//            .authenticated()
//            .and().headers().frameOptions().disable();
//    }
//	
//}
