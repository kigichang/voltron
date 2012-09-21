package xv.voltron.core;

public interface Option<T> {
	
	public T toValue();
	
	public boolean sameAs(T val);
	
}
