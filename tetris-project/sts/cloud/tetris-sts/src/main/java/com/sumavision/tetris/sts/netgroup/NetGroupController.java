package com.sumavision.tetris.sts.netgroup;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.sts.common.CommonController;
import com.sumavision.tetris.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sumavision.tetris.sts.common.CommonConstants.NetGroupType;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Poemafar on 2019/12/16 13:45
 */
@Controller
@RequestMapping(value = "netGroup")
public class NetGroupController extends CommonController {


    @Autowired
    NetGroupService netGroupService;

    @JsonBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object listNetGroups() {
        List<NetGroupPO> netGroups = netGroupService.findAll();
        return netGroups;
    }


//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ResponseBody
//    public Object createNetGroup( @RequestBody NetGroupPO netGroup) throws Exception {
//        netGroupService.save(netGroup);
//        return netGroup;
//    }

    @JsonBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object createNetGroup(String netName,String info,String netType ) throws Exception {
        NetGroupPO netGroupPO = new NetGroupPO();
        netGroupPO.setNetName(netName);
        netGroupPO.setInfo(info);
        netGroupPO.setNetType(NetGroupType.valueOf(netType));
        netGroupService.save(netGroupPO);
        return netGroupPO;
    }

    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public Object delete(@PathVariable Long id) throws Exception{
        netGroupService.delete(id);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object editNetGroup(@PathVariable Long id,String netName,String info,String netType ) throws Exception {
        NetGroupPO netGroupPO = netGroupService.findOne(id);
        netGroupPO.setNetName(netName);
        netGroupPO.setInfo(info);
        netGroupPO.setNetType(NetGroupType.valueOf(netType));
        netGroupPO.setUpdateTime(new Date());
        netGroupService.save(netGroupPO);
        return netGroupPO;
    }
}

