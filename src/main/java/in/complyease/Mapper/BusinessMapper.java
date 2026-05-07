package in.complyease.Mapper;

import in.complyease.dto.business.BusinessResponse;
import in.complyease.entity.Business;

public class BusinessMapper {
	
	public static BusinessResponse mapToBusinessDTO(Business business) {
		return new BusinessResponse(
				business.getBusinessId(),
				business.getBusinessName(),
				business.getBusinessGstNumber(),
				business.getCaAssignmentStatus()
				);
	}

}
