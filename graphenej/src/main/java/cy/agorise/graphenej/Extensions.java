package cy.agorise.graphenej;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import cy.agorise.graphenej.interfaces.ByteSerializable;
import cy.agorise.graphenej.interfaces.JsonSerializable;

/**
 *
 * @author nelson
 * @date 11/9/16
 */
public class Extensions implements JsonSerializable, ByteSerializable {
    public static final String KEY_EXTENSIONS = "extensions";

    private ArrayList<JsonSerializable> extensions;

    public Extensions(){
        extensions = new ArrayList<>();
    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public JsonElement toJsonObject() {
        JsonArray array = new JsonArray();
        for(JsonSerializable o : extensions) {
            array.add(o.toJsonObject());
        }
        return array;
    }

    @Override
    public byte[] toBytes() {
        return new byte[1];
    }

    public int size(){
        return extensions.size();
    }
}
