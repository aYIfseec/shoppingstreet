package com.hyk.shoppingstreet.common.model;

import com.hyk.shoppingstreet.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSession {
    private UserAccount userAccount;
    private String token;

}
