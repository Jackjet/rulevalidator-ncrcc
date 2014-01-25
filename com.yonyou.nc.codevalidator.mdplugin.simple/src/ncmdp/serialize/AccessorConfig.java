package ncmdp.serialize;

import ncmdp.model.Accessor;

public class AccessorConfig {
	public static Accessor[] loadAccessor() {
		return StereoTypeConfig.getAccessors();
	}

}
