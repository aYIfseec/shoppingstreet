package com.hyk.shoppingstreet.service;

import com.google.common.collect.Lists;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.dao.ShoppingCartMapper;
import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.model.ShoppingCart;
import com.hyk.shoppingstreet.service.query.CartQuery;
import com.hyk.shoppingstreet.service.vo.CartVO;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class CartService extends AbstractMapperService<Long, ShoppingCart> {

    @Resource
    private IdGenerator idGen;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private CommodityService commodityService;


    public List<CartVO> myList(Long uid) {
        CartQuery query = CartQuery.builder().uid(uid).state(1).build();
        List<ShoppingCart> shoppingCartList = findByQuery(query, "modify_time");

        List<CartVO> res = Lists.newArrayList();
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            return res;
        }

        List<Long> commodityIds = Lists.transform(shoppingCartList, ShoppingCart::getCommodityId);
        List<Commodity> commodityList = commodityService.getListByIds(commodityIds);
        if (CollectionUtils.isEmpty(commodityList)) {
            return res;
        }

        Map<Long, Commodity> commodityMap = commodityList.stream()
            .collect(Collectors.toMap(Commodity::getId, Function.identity(), (k, v) -> k));

        shoppingCartList.forEach(cart -> {
            CartVO cartVO = new CartVO();

            Commodity commodity = commodityMap.get(cart.getCommodityId());
            if (commodity != null) {
                BeanUtils.copyProperties(commodity, cartVO);
                BeanUtils.copyProperties(cart, cartVO);
                res.add(cartVO);
            }
        });

        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean add(Long uid, Long productId, Integer buyNum) {
        CartQuery query = CartQuery.builder().uid(uid).state(1).commodityId(productId).build();
        ShoppingCart shoppingCart = findOneByQuery(query);
        if (shoppingCart != null) {
            shoppingCart.setBuyNum(shoppingCart.getBuyNum() + buyNum);
            if (shoppingCart.getBuyNum() < 0) {
                throw BizException.create(Status.PARAM_ERROR);
            }
            return updateById(shoppingCart) > 0;
        }

        if (buyNum < 1) {
            throw BizException.create(Status.PARAM_ERROR);
        }

        shoppingCart = ShoppingCart.builder()
            .id(idGen.nextId())
            .uid(uid)
            .commodityId(productId)
            .state(1)
            .buyNum(buyNum)
            .build();

        return insert(shoppingCart) > 0;
    }

    public Boolean del(Long shoppingCartId) {
        ShoppingCart shoppingCart = getById(shoppingCartId);
        if (shoppingCart == null) {
            throw BizException.create(Status.PARAM_ERROR);
        }
        shoppingCart.setState(-1);
        return updateById(shoppingCart) > 0;
    }

    public Boolean updateNum(Long shoppingCartId, Integer updateNum) {
        ShoppingCart shoppingCart = getById(shoppingCartId);
        if (shoppingCart == null) {
            throw BizException.create(Status.PARAM_ERROR);
        }
        shoppingCart.setBuyNum(updateNum);
        return updateById(shoppingCart) > 0;
    }
}
