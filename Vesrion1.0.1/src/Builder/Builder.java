package Builder;

import org.json.JSONObject;

public abstract class Builder {
    JSONObject data;
    Builder(Object _data){
        data =  (JSONObject)_data;
    }
    Object buildPart(){
        return null;
    }
}
