package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.model.ShoppingCart;
import com.hyk.shoppingstreet.service.CartService;
import com.hyk.shoppingstreet.service.vo.CartVO;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.jvm.hotspot.utilities.Assert;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping("/list")
    public ReturnMsg<List<CartVO>> myList(
    ) {

        Long uid = UserSessionThreadLocal.getUserSession().getUid();

        List<CartVO> res = cartService.myList(uid);

        return ReturnMsg.createWithData(res);
    }


    @PostMapping("/add")
    public ReturnMsg<Boolean> add(
        @RequestParam(value = "productId", required = true) Long productId,
        @RequestParam(value = "buyNum", required = true) Integer buyNum
    ) {

        if (buyNum < 1) {
            throw BizException.create(Status.PARAM_ERROR);
        }

        Long uid = UserSessionThreadLocal.getUserSession().getUid();

        Boolean res = cartService.add(uid, productId, buyNum);

        return ReturnMsg.createWithoutTotalCount(res);
    }


    @PostMapping("/updateNum")
    public ReturnMsg<Boolean> updateNum(
        @RequestParam(value = "shoppingCartId", required = true) Long shoppingCartId,
        @RequestParam(value = "updateNum", required = true) Integer updateNum
    ) {

        if (updateNum < 1) {
            throw BizException.create(Status.PARAM_ERROR);
        }

        Boolean res = cartService.updateNum(shoppingCartId, updateNum);

        return ReturnMsg.createWithoutTotalCount(res);
    }

    @PostMapping("/del")
    public ReturnMsg<Boolean> del(
        @RequestParam(value = "shoppingCartId", required = true) Long shoppingCartId
    ) {
        Boolean res = cartService.del(shoppingCartId);

        return ReturnMsg.createWithoutTotalCount(res);
    }

}
