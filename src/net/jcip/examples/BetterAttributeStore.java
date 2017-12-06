package net.jcip.examples;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * BetterAttributeStore
 * <p/>
 * Reducing lock duration
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this")
    //also can use Hashtable,SynchronizedMap,ConcurrentHashMap to avoid use 'synchronized' block
    private final Map<String, String>
            attributes = new HashMap<String, String>();

    public boolean userLocationMatches(String name, String regexp) {
        String key = "users." + name + ".location";
        String location;
        synchronized (this) {
            location = attributes.get(key);
        }
        return location != null && Pattern.matches(regexp, location);
    }
}
