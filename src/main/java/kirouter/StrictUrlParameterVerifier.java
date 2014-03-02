package kirouter;

import java.util.regex.Pattern;

public class StrictUrlParameterVerifier implements ParamVerifier {
    private Pattern pattern = Pattern.compile("^[a-z0-9/\\-\\.]+$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean ok(String param) {
        return pattern.matcher(param).matches();
    }
}
