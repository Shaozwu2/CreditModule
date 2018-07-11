package com.kooppi.nttca.portal.newportal.dto;

import lombok.Data;

@Data public class NewPortalBaseResponseDto {
		protected String requestId;
		protected String responseCode;
		protected String responseMsg;
		protected UserResponseContent responseContent;
}
