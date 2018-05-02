package cy.agorise.graphenej.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author inrush
 * @date 2018/5/1.
 */

public class ChainProperties {

    /**
     * id : 2.11.0
     * chain_id : c2af30ef9340ff81fd61654295e98a1ff04b23189748f86727d0b26b40bb0ff4
     * immutable_parameters : {"min_committee_member_count":11,"min_witness_count":11,"num_special_accounts":0,"num_special_assets":0}
     */

    public static final String OBJECT_ID = "2.11.0";

    private String id;
    private String chain_id;
    private ImmutableParameters immutable_parameters;

    public void setId(String id) {
        this.id = id;
    }

    public void setChain_id(String chain_id) {
        this.chain_id = chain_id;
    }

    public void setImmutable_parameters(ImmutableParameters immutable_parameters) {
        this.immutable_parameters = immutable_parameters;
    }

    public String getId() {
        return id;
    }

    public String getChain_id() {
        return chain_id;
    }

    public ImmutableParameters getImmutable_parameters() {
        return immutable_parameters;
    }

    public static class ImmutableParameters {
        /**
         * min_committee_member_count : 11
         * min_witness_count : 11
         * num_special_accounts : 0
         * num_special_assets : 0
         */
        public static final String KEY_MIN_COMMITTEE_MEMBER_COUNT = "min_committee_member_count";
        public static final String KEY_MIN_WITNESS_COUNT = "min_witness_count";
        public static final String KEY_NUM_SPECIAL_ACCOUNTS = "num_special_accounts";
        public static final String KEY_NUM_SPECIAL_ASSETS = "num_special_assets";


        private int min_committee_member_count;
        private int min_witness_count;
        private int num_special_accounts;
        private int num_special_assets;

        public void setMin_committee_member_count(int min_committee_member_count) {
            this.min_committee_member_count = min_committee_member_count;
        }

        public void setMin_witness_count(int min_witness_count) {
            this.min_witness_count = min_witness_count;
        }

        public void setNum_special_accounts(int num_special_accounts) {
            this.num_special_accounts = num_special_accounts;
        }

        public void setNum_special_assets(int num_special_assets) {
            this.num_special_assets = num_special_assets;
        }

        public int getMin_committee_member_count() {
            return min_committee_member_count;
        }

        public int getMin_witness_count() {
            return min_witness_count;
        }

        public int getNum_special_accounts() {
            return num_special_accounts;
        }

        public int getNum_special_assets() {
            return num_special_assets;
        }

        public static class ImmutableParametersDeserializer implements JsonDeserializer<ImmutableParameters> {

            @Override
            public ImmutableParameters deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                ImmutableParameters parameters = new ImmutableParameters();
                parameters.setMin_committee_member_count(jsonObject.get(KEY_MIN_COMMITTEE_MEMBER_COUNT).getAsInt());
                parameters.setMin_witness_count(jsonObject.get(KEY_MIN_WITNESS_COUNT).getAsInt());
                parameters.setNum_special_accounts(jsonObject.get(KEY_NUM_SPECIAL_ACCOUNTS).getAsInt());
                parameters.setNum_special_assets(jsonObject.get(KEY_NUM_SPECIAL_ASSETS).getAsInt());
                return parameters;
            }
        }
    }
}
