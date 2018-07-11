package com.kooppi.nttca.portal.newportal.dto;

import java.util.List;

import lombok.Data;

@Data public class UserResponseContent {
    
	//for get newportal user by login use
    private Integer organizationUid;

    //for get all newportal user
    private List<CustomerItemDto> customerItems;
}
