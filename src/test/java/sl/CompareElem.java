package sl;

import java.util.Comparator;

public class CompareElem implements Comparator<Elem> {

	@Override
	public int compare(Elem obj1, Elem obj2) {
		int result = obj1.getName().compareTo(obj2.getName());
//		int result = obj1.getOther().compareTo(obj2.getOther());
		if (result == 0) {
//			return obj1.getName().compareTo(obj2.getName());
			return obj1.getOther().compareTo(obj2.getOther());
		} else {
			return result;
		}
	}

}
