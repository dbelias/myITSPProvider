package support;



public class ComboBoxItem {
	String key;
	int value;
	public ComboBoxItem(String k, int v){
		value=v;
		key=k;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString(){
	return key;
	}
	
	

}
