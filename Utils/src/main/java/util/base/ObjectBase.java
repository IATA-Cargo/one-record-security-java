package util.base;

public class ObjectBase {
	
	public ObjectBase fromJson(String data) {
		return Common.fromJson(ObjectBase.class, data);
	}
	
	public String toJson() {
		return Common.toJsonPrettyString(this);
	}
	
}
