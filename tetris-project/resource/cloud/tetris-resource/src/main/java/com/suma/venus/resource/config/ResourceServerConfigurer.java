package com.suma.venus.resource.config;

/**
 * Created by forezp on 2017/5/31.
 */

/**   
 * @ClassName:  ResourceServerConfigurer   
 * @Description:Resource服务配置  
 * @author: 
 * @date:   2018年8月2日 下午5:12:43   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */  
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//        		/**添加例外接口，外部访问例外接口不需要携带token信息**/
//                .antMatchers("/api/**"/*对外开放接口*/,"/**/import"/**上传文件*/,"/**/export"/**上传文件*/,"/druid/**","/vue/**").permitAll()
//                .anyRequest().authenticated()
//                .and().cors()
//                .and().csrf().disable();
//    }
//
//}

