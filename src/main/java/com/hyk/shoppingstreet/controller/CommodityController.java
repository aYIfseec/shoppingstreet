package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.model.UserSession;
import com.hyk.shoppingstreet.common.utils.ArgumentCheckUtil;
import com.hyk.shoppingstreet.common.utils.RedisUtil.TokenType;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.service.CommodityService;
import com.hyk.shoppingstreet.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/commodity")
public class CommodityController {

    @Resource
    private CommodityService commodityService;

    @PostMapping("/queryOnePage")
    public ReturnMsg<List<Commodity>> queryOnePage(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "pageNo", required = true) Integer pageNo,
        @RequestParam(value = "pageSize", required = true) Integer pageSize
    ) {
        List<Commodity> res = commodityService.queryOnePage(name, pageNo, pageSize);

        return ReturnMsg.createWithData(res);
    }

}
