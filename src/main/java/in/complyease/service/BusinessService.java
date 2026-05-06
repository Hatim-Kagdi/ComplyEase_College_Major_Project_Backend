package in.complyease.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.Mapper.BusinessMapper;
import in.complyease.dto.Business.*;
import in.complyease.entity.*;
import in.complyease.repository.*;

@Service
public class BusinessService {
	@Autowired private BusinessRepository businessRepository;

	@Autowired private UserRepository userRepository;

	@Transactional
	public BusinessResponse createBusiness(BusinessRequest request, String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Business business = new Business();
		business.setBusinessName(request.getBusinessName());
		business.setBusinessGstNumber(request.getBusinessGstNumber());
		business.setUser(user);

		Business saved = businessRepository.save(business);

		return BusinessMapper.mapToBusinessDTO(saved);
	}

	public List<BusinessResponse> getUserBusinesses(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return businessRepository.findByUser(user).stream()
				.map(b -> BusinessMapper.mapToBusinessDTO(b))
				.collect(Collectors.toList());
	}

	public BusinessResponse getBusinessById(int id) {
	    Business b = businessRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Business not found"));

	    return BusinessMapper.mapToBusinessDTO(b);
	}

	@Transactional
	public void deleteBusiness(int id) {
	    Business b = businessRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Business not found"));

	    businessRepository.delete(b); // soft delete triggers here
	}
	
	public BusinessResponse updateBusiness(int id, BusinessRequest request, String email) {

	    // 1. Get logged-in user
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // 2. Get business
	    Business business = businessRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Business not found"));

	    // 3. SECURITY CHECK
	    if (!business.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("You are not allowed to update this business");
	    }

	    // 4. Update fields
	    business.setBusinessName(request.getBusinessName());
	    business.setBusinessGstNumber(request.getBusinessGstNumber());

	    // 5. Save updated entity
	    Business updated = businessRepository.save(business);

	    // 6. Return response
	    return BusinessMapper.mapToBusinessDTO(updated);
	}
}
