/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package plugins.Interdex.serl;

import plugins.Interdex.serl.Serialiser.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;

/**
** DOCUMENT
**
** @author infinity0
*/
public class CollectionPacker<K, T extends Collection>
extends Packer<K, T>
implements MapSerialiser<K, T> {

	final protected static Comparator<Collection> BinElementComparator = new Comparator<Collection>() {
		public int compare(Collection c1, Collection c2) {
			if (c1 == c2) { return 0; }
			int d = c2.size() - c1.size();
			// this is a bit of a hack but is needed since Tree* treats two objects
			// as "equal" if their "compare" returns 0
			if (d != 0) { return d; }
			int h = c2.hashCode() - c1.hashCode();
			// on the off chance that the hashCodes are equal but the objects are not,
			// test the string representations of them...
			return (h != 0)? h: (c2.equals(c1))? 0: c2.toString().compareTo(c1.toString());
		}
	};

	public CollectionPacker(int c, Class<? extends T> cc, IterableSerialiser<Map<K, T>> s) {
		super(c, BinElementComparator, cc, s);
	}

	/*========================================================================
	  abstract public class Packer
	 ========================================================================*/

	@Override protected T newPartitionOf(Iterator it, int max) {
		T el = newElement();
		for (int j=0; j<max; ++j) {
			el.add(it.next());
		}
		return el;
	}

	@Override protected Iterable iterableOf(T element) {
		return element;
	}

	@Override protected void setMetaAfterPack(Map<String, Object> meta, T element, int binindex) {
		if (!meta.containsKey(Field_BINS)) { meta.put(Field_BINS, new ArrayList<Integer>()); }
		((List)meta.get(Field_BINS)).add(binindex);
	}

	@Override protected int sizeOf(T element) {
		return element.size();
	}

}
