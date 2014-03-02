package kirouter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SinatraRouteParser {
    private List<String> keys = new ArrayList<String>();
    private Pattern pattern;
    private static Pattern segmentRegex = Pattern.compile("((:\\w+)|\\*)");

    SinatraRouteParser(final String route) {
        pattern = compileRoutePattern(route);
    }

    public Map<String, Object> parse(String path, ParamVerifier paramVerifier) {
        Map<String, Object> ret = null;
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            ret = new HashMap<String, Object>();
            for (int i = 0; i < matcher.groupCount(); i++) {
                String match = matcher.group(i + 1);
                if (paramVerifier != null && !paramVerifier.ok(match)) {
                    return null;
                }
                String key = keys.get(i);
                append(ret, key, match);
            }
        }
        return ret;
    }

    private Pattern compileRoutePattern(String route) {
        String routeWithoutFirstSlash = route.substring(1);
        List<String> segments = new ArrayList<String>();
        for (String segment : routeWithoutFirstSlash.split("/")) {
            Matcher match = segmentRegex.matcher(segment);
            if (match.matches()) {
                String firstMatch = match.group(1);
                if (firstMatch.equals("*")) {
                    keys.add("splat");
                    segment = "(.*)";
                } else {
                    keys.add(firstMatch.substring(1));
                    segment = "([^\\/?#]+)";
                }
            } else {
                segment = segment.replace(".", "\\.");
            }
            segments.add(segment);
        }
        return Pattern.compile("^/" + join(segments, "/") + "$");
    }

    private String join(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list) {
            if (first)
                first = false;
            else
                sb.append(separator);
            sb.append(item);
        }
        return sb.toString();
    }

    private void append(Map map, String key, String value) {
        Object old = map.get(key);
        if (old != null) {
            if (old instanceof ArrayList) {
                ((ArrayList) old).add(value);
            } else {
                map.put(key, Arrays.asList(old, value));
            }
        } else {
            map.put(key, value);
        }
    }

}
