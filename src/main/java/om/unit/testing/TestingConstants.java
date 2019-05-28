package om.unit.testing;

public enum TestingConstants {

	CREATED_BY("Mockito-test");

	private String value;

	TestingConstants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
