import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: asarawgi
 * Date: 10/21/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultHashMap<K,V> extends HashMap<K,V> {
    protected V defaultValue;
    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    @Override
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? this.defaultValue : v;
    }
}


