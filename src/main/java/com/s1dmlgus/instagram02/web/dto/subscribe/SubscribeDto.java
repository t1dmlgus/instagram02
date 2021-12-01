package com.s1dmlgus.instagram02.web.dto.subscribe;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@AllArgsConstructor
@Data
public class SubscribeDto {

    private BigInteger id;
    private String username;
    private String profileImageUrl;
    private Integer follow;
    private Integer iam;

}
