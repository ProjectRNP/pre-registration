package io.mosip.registration.processor.core.constant;

public enum APIAuthorityList {

	PACKETRECEIVER(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER"}),
	
	PACKETSYNC(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER" }),
	
	REGISTRATIONSTATUS(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER" }),
	
	MANUALVERIFICTION(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER" }),
	
	PRINTSTAGE(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER" }),
	
	CONNECTORSTAGE(new String[] { "REGISTRATION_ ADMIN", "REGISTRATION_SUPERVISOR", "REGISTRATION_OFFICER" });
	
	private final String[] list;

	private APIAuthorityList(String[] list) {
		this.list = list;
	}

	public String[] getList(){
		return this.list;
	}
}