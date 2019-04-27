package com.hyk.shoppingstreet.service;

import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.dao.AddressMapper;
import com.hyk.shoppingstreet.model.Address;
import com.hyk.shoppingstreet.service.query.AddressQuery;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressService extends AbstractMapperService<Long, Address> {

  @Resource
  private IdGenerator idGen;
  @Resource
  private AddressMapper addressMapper;


  public List<Address> myList(Long uid) {
    AddressQuery query = AddressQuery.builder().uid(uid).stateGte(1).build();
    List<Address> res = findByQuery(query);
    return res;
  }

  public Boolean save(Address address) {

    if (address.getId() == null) {
      address.setId(idGen.nextId());
      insert(address);
      return true;
    }

    Address oldAddress = getById(address.getId());
    if (oldAddress == null) {
      throw BizException.create(Status.PARAM_ERROR);
    }

    address.setModifyTime(new Date());
    updateById(address);
    return true;
  }

  public Boolean del(Long addressId, Long uid) {
    AddressQuery query = AddressQuery.builder().addressId(addressId).uid(uid).build();
    Address address = findOneByQuery(query);
    if (address == null) {
      throw BizException.create(Status.PARAM_ERROR);
    }
    address.setState(-1);
    address.setModifyTime(new Date());
    return updateById(address) > 0;
  }

}
