package com.marin.cafeteria.api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerOrdersRequestDTO {

    private int serverId;
    private Timestamp startDate;
    private Timestamp endDate;

}
