package eu.inn.biosign.listener;

public interface IDeviceListener {

	void accept(String signature, String b64Image, Object sender);
	void cancel(Object sender);
}
