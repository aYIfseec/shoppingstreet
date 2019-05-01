package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.controller.request.AddressSaveRequest;
import com.hyk.shoppingstreet.model.Address;
import com.hyk.shoppingstreet.service.AddressService;
import com.hyk.shoppingstreet.service.vo.AddressVO;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */

@RestController
@RequestMapping(value = "/address")
public class AddressController {

  @Resource
  private AddressService addressService;

  @PostMapping("/list")
  public ReturnMsg<List<AddressVO>> myList(
  ) {

    Long uid = UserSessionThreadLocal.getUserSession().getUid();

    List<AddressVO> res = addressService.myList(uid);

    return ReturnMsg.createWithData(res);
  }


  @PostMapping("/save")
  public ReturnMsg<Boolean> save(
      @RequestBody @Valid AddressSaveRequest request
  ) {

    Address address = new Address();
    BeanUtils.copyProperties(request, address);

    Long uid = UserSessionThreadLocal.getUserSession().getUid();
    address.setBindObject(uid);

    Boolean res = addressService.save(address);

    return ReturnMsg.createWithoutTotalCount(res);
  }


  @PostMapping("/del")
  public ReturnMsg<Boolean> del(
      @RequestParam(value = "addressId", required = true) Long addressId
  ) {
    Long uid = UserSessionThreadLocal.getUserSession().getUid();
    Boolean res = addressService.del(addressId, uid);

    return ReturnMsg.createWithoutTotalCount(res);
  }

}
