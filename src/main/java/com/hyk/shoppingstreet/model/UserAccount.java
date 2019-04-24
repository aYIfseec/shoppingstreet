package com.hyk.shoppingstreet.model;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    @Id
    private Long uid;

    private String phone;

    private String pwd;

    private String salt;

    private String username;

    private String email;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    // user_account columns
    public static class column {
        public static final String uid = "uid";

        public static final String phone = "phone";

        public static final String pwd = "pwd";

        public static final String salt = "salt";

        public static final String username = "username";

        public static final String email = "email";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";
    }
}