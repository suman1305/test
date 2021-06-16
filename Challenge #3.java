package com.kpmg.sse.assignment;

import java.util.HashMap;
import java.util.Map;

public class SearchDemo {

    public static void main(String[] args) {
        Search cache = new SearchCache();
        cache.push("{“a”:{“b”:{“c”:”d”}}}");
        System.out.println(cache.pop("a/b/c"));
    }

}

/**
 * The Search interface will define specifications to push and pop elements from
 * the cache.
 */
interface Search {

    /**
     * This method will help to push the nestedJson Parsable data to cache, this
     * will throw exception if the string contains invalid json.
     * 
     * @param nestedJsonData
     */
    void push(String nestedJsonData);

    /**
     * This method will pop the value, if present, in the cache with the given
     * hierarchy of keys
     * 
     * @param key
     * @return
     */
    String pop(String key);
}

/**
 * This class will cache the data based on the hierarchy of keys in the input
 * json
 */
class SearchCache implements Search {

    // Constant size cahce to store the json as key value pair in hierarchy.
    private static final Map<String, Map<String, Map<String, String>>> CACHE = new HashMap<>(1);

    // Dummy interface which will suport json and key value pair parsing,
    // implementation of this interface is not provided to limit the scope
    private Parser jsonParser;

    /**
     * This method will parse the input json and retrun three keys and the value
     * hidden in the nested JSON which is supplied
     * 
     * @param input
     * @return
     */
    @Override
    public void push(String nestedJsonData) {
        Map<String, String> keyValues = jsonParser.parseJson(nestedJsonData);
        String value = keyValues.get("VALUE");
        String key3 = keyValues.get("KEY3");
        String key2 = keyValues.get("KEY2");
        String key1 = keyValues.get("KEY1");
        Map<String, String> keyValuePair3 = new HashMap<>(1);
        keyValuePair3.put(key3, value);
        Map<String, Map<String, String>> keyValuePair2 = new HashMap<>(1);
        keyValuePair2.put(key2, keyValuePair3);
        CACHE.put(key1, keyValuePair2);
    }

    /**
     * This method will parse the input key and return the three keys supplied in
     * the input string
     * 
     * @param input
     * @return
     */
    @Override
    public String pop(String key) {
        Map<String, String> keys = jsonParser.parseKey(key);
        String key3 = keys.get("KEY3");
        String key2 = keys.get("KEY2");
        String key1 = keys.get("KEY1");

        Map<String, Map<String, String>> keyValuePair2 = CACHE.get(key1);
        if (null != keyValuePair2) {
            Map<String, String> keyValuePair3 = keyValuePair2.get(key2);
            if (null != keyValuePair3) {
                return keyValuePair3.get(key3);
            }
        }
        return null;
    }

}

/**
 * This interface will define specifications to parse various inputs.
 */
interface Parser {
    /**
     * This method will parse the input json and retrun three keys and the value
     * hidden in the nested JSON which is supplied
     * 
     * @param input
     * @return
     */
    Map<String, String> parseJson(String input);

    /**
     * This method will parse the input key and return the three keys supplied in
     * the input string
     * 
     * @param input
     * @return
     */
    Map<String, String> parseKey(String input);
}
