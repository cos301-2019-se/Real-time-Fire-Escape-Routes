package Builder;

import org.json.JSONObject;

public abstract class Builder {
    JSONObject data;
    Builder(Object data){}
    Object buildPart(){
        return null;
    }
}
