package com.fenwjian.sdcardutil;

public class myCpr<T1 extends Comparable<T1>,T2> implements Updatable{
    	public T1 key;
    	public T2 value;
    	public myCpr(T1 k,T2 v){
    		key=k;value=v;
    	}
    	public int compareTo(myCpr<T1,T2> other) {
    		//if()
    			return this.key.compareTo(other.key);
    		//else
    		//	return 
    	}
    	public String toString(){
    		return key+"_"+value;
    	}
        @Override
        public void Update(Updatable o) {
            value = ((myCpr<T1,T2>)o).value;
        }

        @Override
        public int compareTo(Updatable o) {
            return compareTo((myCpr<T1,T2> )o);
        }

    }