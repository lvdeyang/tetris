package com.sumavision.tetris.device.netgroup;

import com.sumavision.tetris.business.common.enumeration.NetGroupType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Poemafar on 2019/12/16 13:45
 */
@RestController
@RequestMapping(value = "netGroup")
public class NetGroupController {


    @Autowired
    NetGroupService netGroupService;

    @JsonBody
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Object listNetGroups() throws Exception {
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
    public Object createNetGroup(@RequestParam String netName,@RequestParam String info,@RequestParam String netType ) throws Exception {
        NetGroupPO netGroupPO = new NetGroupPO();
        netGroupPO.setNetName(netName);
        netGroupPO.setInfo(info);
        netGroupPO.setNetType(NetGroupType.valueOf(netType));
        netGroupService.save(netGroupPO);
        return netGroupPO;
    }

    @JsonBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object delete(@RequestParam Long id) throws Exception{
        netGroupService.delete(id);
        return null;
    }

    @JsonBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Object editNetGroup(@RequestParam Long id,@RequestParam String netName,@RequestParam String info,@RequestParam String netType ) throws Exception {
        NetGroupPO netGroupPO = netGroupService.findOne(id);
        netGroupPO.setNetName(netName);
        netGroupPO.setInfo(info);
        netGroupPO.setNetType(NetGroupType.valueOf(netType));
        netGroupPO.setUpdateTime(new Date());
        netGroupService.save(netGroupPO);
        return netGroupPO;
    }

    @JsonBody
    @RequestMapping(value = "/beUsedByNetGroupId",method = RequestMethod.POST)
    public Object beUsedByNetGroupId(@RequestParam Long id) throws Exception{
       return netGroupService.beUsedByNetGroupId(id);
    }
}

