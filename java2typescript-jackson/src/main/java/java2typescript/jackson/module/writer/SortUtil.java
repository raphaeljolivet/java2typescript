package java2typescript.jackson.module.writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;

public class SortUtil {

	public static Collection<AbstractNamedType> sortByTypeName(Collection<AbstractNamedType> namedTypes) {
		namedTypes = new ArrayList<AbstractNamedType>(namedTypes);
		Collections.sort((List<AbstractNamedType>) namedTypes, new Comparator<AbstractNamedType>() {
			@Override
			public int compare(AbstractNamedType o1, AbstractNamedType o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return namedTypes;
	}

	public static <V> List<Entry<String, V>> sortEntriesByKey(Collection<Entry<String, V>> entrySet) {
		List<Entry<String, V>> results = new ArrayList<Entry<String, V>>(entrySet);
		Collections.sort(results, new Comparator<Entry<String, V>>() {
			@Override
			public int compare(Entry<String, V> e1, Entry<String, V> e2) {
				return e1.getKey().compareTo(e2.getKey());
			}
		});
		return results;
	}
	
	public static List<String> sort(Collection<String> collection) {
		List<String> list = new ArrayList<String>(collection);
		Collections.sort(list);
		return list;
	}
}
