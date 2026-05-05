package in.complyease.dto.Business;

public class BusinessResponse {
	private int id;
	private String businessName;
	private String businessGstNumber;

	public BusinessResponse(int id, String businessName, String businessGstNumber) {
		this.id = id;
		this.businessName = businessName;
		this.businessGstNumber = businessGstNumber;
	}

	public int getId() {
		return id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public String getBusinessGstNumber() {
		return businessGstNumber;
	}
}
