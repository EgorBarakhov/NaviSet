import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class NaviSet<T> extends AbstractSet<T> implements NavigableSet<T> {
	
	private static final String ERROR_MESSAGE = "error message here";
	private ArrayList<T> 		myCol;
	private int 				size;
	private Comparator<T> 		naviComp;
	
	public NaviSet(ArrayList<T> myCol, Comparator<T> setComp) {
		this.myCol = myCol;
		naviComp = setComp;
		size = myCol.size();
		sort(myCol, setComp);
	}
	
	public NaviSet(Comparator<T> setComp) {
		naviComp = setComp;
		myCol = new ArrayList<>();
		size = 0;
	}
	
	@Override
	public boolean add(T e) {
		if (!(e.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		int count = 0;
		if (size != 0) {
			T insert = null;
			while ((count < size) && (naviComp.compare(e, insert) <= 0)) {
				 insert = myCol.get(count);
				 if (naviComp.compare(e, insert) == 0) return false;
				 count++;
			}
		}
		myCol.add(count, e);
		size++;
		return true;
	}
	
	private Class<?> getType(ArrayList<T> arrayList) {
		Method[] methods = NaviSet.class.getDeclaredMethods();
		Type[] types = methods[1].getGenericParameterTypes();
		ParameterizedType pType = (ParameterizedType) types[0];
		Class<?> cls = (Class<?>) pType.getActualTypeArguments()[0];
		return cls;
	}

	public T remove(int index) {
		if (index >= size) return null;
		T getEl = myCol.get(index);
		myCol.remove(index);
		size--;
		return getEl;
	}
	
	public boolean remove(Object obj) {
		if (myCol.contains(obj)) {
			size--;
			return myCol.remove(obj);
		}
		return false;
	}
			
	@Override
	public Iterator<T> iterator() {
		return new SetIterator<>(myCol);
	}
	
	public void setComp(Comparator<T> setComp) {
		naviComp = setComp;
		sort(myCol, naviComp);
	}

	private void sort(ArrayList<T> myCol, Comparator<T> naviComp) {
		for (int i = 1; i < size; i++) {
			if (naviComp.compare(myCol.get(i), myCol.get(i - 1)) < 0)
				swap(myCol.get(i), myCol.get(i - 1));
		}		
	}

	private void swap(T o1, T o2) {
		T tmp = o1;
		o1 = o2;
		o2 = tmp;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Comparator<? super T> comparator() {
		return naviComp;
	}

	@Override
	public T first() {
		try {
			return myCol.get(0);
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
	}

	@Override
	public T last() {
		try {
			return myCol.get(size - 1);
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
	}

	@Override
	public T lower(T e) {
		if (e == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(e.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		int count = 0;
		try {
			while (naviComp.compare(e, myCol.get(count)) >= 0) {
				count++;
			}
			count--;
			if (naviComp.compare(e, myCol.get(count)) == 0) return myCol.get(count - 1);
			return myCol.get(count);
		} catch (IndexOutOfBoundsException ex) {
			if (count <= 0) return null;
			count--;
			if (naviComp.compare(e, myCol.get(count)) == 0) return myCol.get(count - 1);
			return myCol.get(count);
		}
	}

	@Override
	public T floor(T e) {
		if (e == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(e.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		int count = 0;
		try {
			while (naviComp.compare(e, myCol.get(count)) >= 0) {
				count++;
			}
			if (naviComp.compare(e, myCol.get(count)) == 0) return myCol.get(count);
			return myCol.get(count - 1);
		} catch (IndexOutOfBoundsException ex) {
			if (count == 0) return null;
			return myCol.get(size - 1);
		}
	}

	@Override
	public T ceiling(T e) {
		if (e == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(e.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		int count = 0;
		try {
			while (naviComp.compare(e, myCol.get(count)) >= 0) {
				count++;
			}
			if (naviComp.compare(e, myCol.get(count - 1)) == 0) return myCol.get(count - 1);
			return myCol.get(count);
		} catch (IndexOutOfBoundsException ex) {
			if (count == 0) return myCol.get(0);
			if (naviComp.compare(e, myCol.get(count - 1)) == 0) return myCol.get(count - 1);
			return null;
		}  
	}

	@Override
	public T higher(T e) {
		if (e == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(e.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		int count = 0;
		try {
			while (naviComp.compare(e, myCol.get(count)) >= 0) {
				count++;
			}
			return myCol.get(count);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	@Override
	public T pollFirst() {
		try {
			T getEl = myCol.get(0);
			myCol.remove(0);
			size--;
			return getEl;
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
	}

	@Override
	public T pollLast() {
		try {
			T getEl = myCol.get(size - 1);
			myCol.remove(size - 1);
			size--;
			return getEl;
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
	}

	@Override
	public NavigableSet<T> descendingSet() {
		NavigableSet<T> answer = new NaviSet<>(naviComp);
		for (int i = size - 1; i >= 0; i--) {
			T el = myCol.get(i);
			answer.add(el);
		}
		return answer;
	}

	@Override
	public Iterator<T> descendingIterator() {
		NavigableSet<T> descend = this.descendingSet();
		return descend.iterator();
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		if ((toElement == null) || (fromElement == null)) throw new NullPointerException(ERROR_MESSAGE);
		if (!((toElement.getClass().getName().equals(this.getType(myCol).getClass().getName())) || 
				(fromElement.getClass().getName().equals(this.getType(myCol).getName())))) throw new ClassCastException(ERROR_MESSAGE);
		try {
			if ((naviComp.compare(toElement, myCol.get(0)) < 0) || 
					(naviComp.compare(fromElement, myCol.get(size - 1)) > 0) || 
					(naviComp.compare(fromElement, toElement) < 0)) throw new IllegalArgumentException(ERROR_MESSAGE);
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
		NavigableSet<T> answer = new NaviSet<>(naviComp);
		int count = 0;
		try {
			while (naviComp.compare(fromElement, myCol.get(count)) > 0) {
				count++;
			}
			if ((fromInclusive) && (naviComp.compare(fromElement, myCol.get(count)) == 0)) {
				answer.add(myCol.get(count));
				count++;
			}
			while (naviComp.compare(toElement, myCol.get(count)) > 0) {
				answer.add(myCol.get(count));
				count++;
			}
			if ((toInclusive) && (naviComp.compare(toElement, myCol.get(count)) == 0)) answer.add(myCol.get(count));
		} catch (IndexOutOfBoundsException ex) { }
		return answer;
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		if (toElement == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(toElement.getClass().getName().equals(this.getType(myCol).getClass().getName()))) throw new ClassCastException(ERROR_MESSAGE);
		try {
			if (naviComp.compare(toElement, myCol.get(0)) < 0) throw new IllegalArgumentException(ERROR_MESSAGE);
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
		NavigableSet<T> answer = new NaviSet<>(naviComp);
		int count = 0;
		try {
			while (naviComp.compare(toElement, myCol.get(count)) > 0) {
				answer.add(myCol.get(count));
				count++;
			}
			if ((inclusive) && (naviComp.compare(toElement, myCol.get(count)) == 0)) answer.add(myCol.get(count));
		} catch (IndexOutOfBoundsException ex) { }
		return answer;
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		if (fromElement == null) throw new NullPointerException(ERROR_MESSAGE);
		if (!(fromElement.getClass().getName().equals(this.getType(myCol).getName()))) throw new ClassCastException(ERROR_MESSAGE);
		try {
			if (naviComp.compare(fromElement, myCol.get(size - 1)) > 0) throw new IllegalArgumentException(ERROR_MESSAGE);
		} catch (IndexOutOfBoundsException ex) {
			throw new NoSuchElementException(ERROR_MESSAGE);
		}
		NavigableSet<T> answer = new NaviSet<>(naviComp);
		int count = 0;
		try {
			while (naviComp.compare(fromElement, myCol.get(count)) > 0) {
				count++;
			}
			if ((inclusive) && (naviComp.compare(fromElement, myCol.get(count)) == 0)) answer.add(myCol.get(count));
			count++;
			while (count < size) {
				answer.add(myCol.get(count));
				count++;
			}
		} catch (IndexOutOfBoundsException ex) { }
		return answer;
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return subSet(fromElement, true, toElement, false);
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return headSet(toElement, false);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return tailSet(fromElement, true);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((myCol == null) ? 0 : myCol.hashCode());
		result = prime * result + ((naviComp == null) ? 0 : naviComp.hashCode());
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NaviSet<?> other = (NaviSet<?>) obj;
		if (myCol == null) {
			if (other.myCol != null)
				return false;
		} else if (!myCol.equals(other.myCol))
			return false;
		if (naviComp == null) {
			if (other.naviComp != null)
				return false;
		} else if (!naviComp.equals(other.naviComp))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NaviSet [myCol=" + myCol.toString() + ", size=" + size + ", naviComp=" + naviComp.toString() + "]";
	}
	
}
