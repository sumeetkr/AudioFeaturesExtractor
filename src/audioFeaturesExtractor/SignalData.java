package audioFeaturesExtractor;

public class SignalData {
	private double amplitude;
	private String beaconId;

	public SignalData(double amplitude, String beaconId) {
		this.amplitude = amplitude;
		this.beaconId = beaconId;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public String getBeaconId() {
		return beaconId;
	}
}