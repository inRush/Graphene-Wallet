package cy.agorise.graphenej.operations;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cy.agorise.graphenej.AccountOptions;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.Authority;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.Extensions;
import cy.agorise.graphenej.OperationType;
import cy.agorise.graphenej.Optional;

/**
 * @author inrush
 * @date 2018/4/5.
 */

public class AccountOperation extends BaseOperation {
    public static final String KEY_NAME = "name";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_FEE = "fee";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_EXTENSIONS = "extensions";

    private AssetAmount fee;
    private String name;
    private Optional<Authority> owner;
    private Optional<Authority> active;
    private Optional<AccountOptions> options;

    /**
     * Account update operation constructor.
     *
     * @param name    User name to update. Can't be null.
     * @param owner   Owner authority to set. Can be null.
     * @param active  Active authority to set. Can be null.
     * @param options Active authority to set. Can be null.
     * @param fee     The fee to pay. Can be null.
     */
    public AccountOperation(String name, Authority owner, Authority active, AccountOptions options, AssetAmount fee) {
        super(OperationType.ACCOUNT_UPDATE_OPERATION);
        this.fee = fee;
        this.name = name;
        this.owner = new Optional<>(owner);
        this.active = new Optional<>(active);
        this.options = new Optional<>(options);
        extensions = new Extensions();
    }

    public AccountOperation(String name, Authority owner, Authority active, AccountOptions options) {
        this(name, owner, active, options, new AssetAmount(UnsignedLong.valueOf(0), new Asset("1.3.1")));
    }

    @Override
    public void setFee(AssetAmount fee) {
        this.fee = fee;
    }

    public void setOwner(Authority owner) {
        this.owner = new Optional<>(owner);
    }

    public void setActive(Authority active) {
        this.active = new Optional<>(active);
    }

    public void setAccountOptions(AccountOptions options) {
        this.options = new Optional<>(options);
    }

    public AssetAmount getFee() {
        return fee;
    }

    public String getName() {
        return name;
    }

    public Optional<Authority> getOwner() {
        return owner;
    }

    public Optional<Authority> getActive() {
        return active;
    }

    public Optional<AccountOptions> getOptions() {
        return options;
    }

    @Override
    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public JsonElement toJsonObject() {
        JsonArray array = new JsonArray();
        array.add(this.getId());

        JsonObject accountUpdate = new JsonObject();
        accountUpdate.add(KEY_FEE, fee.toJsonObject());
        accountUpdate.addProperty(KEY_NAME, name);
        if (owner.isSet()) {
            accountUpdate.add(KEY_OWNER, owner.toJsonObject());
        }
        if (active.isSet()) {
            accountUpdate.add(KEY_ACTIVE, active.toJsonObject());
        }
        if (options.isSet()) {
            accountUpdate.add(KEY_OPTIONS, options.toJsonObject());
        }
        accountUpdate.add(KEY_EXTENSIONS, extensions.toJsonObject());
        array.add(accountUpdate);
        return array;
    }

    @Override
    public byte[] toBytes() {
        byte[] feeBytes = fee.toBytes();
        byte[] accountBytes = name.getBytes();
        byte[] ownerBytes = owner.toBytes();
        byte[] activeBytes = active.toBytes();
        byte[] optionsBytes = options.toBytes();
        byte[] extensionBytes = extensions.toBytes();
        return Bytes.concat(feeBytes, accountBytes, ownerBytes, activeBytes, optionsBytes, extensionBytes);
    }


    public static class AccountDeserializer implements JsonDeserializer<AccountOperation> {

        @Override
        public AccountOperation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                // This block is used just to check if we are in the first step of the deserialization
                // when we are dealing with an array.
                JsonArray serializedTransfer = json.getAsJsonArray();
                if (serializedTransfer.get(0).getAsInt() != OperationType.ACCOUNT_CREATE_OPERATION.ordinal()) {
                    // If the operation type does not correspond to a transfer operation, we return null
                    return null;
                } else {
                    // Calling itself recursively, this is only done once, so there will be no problems.
                    return context.deserialize(serializedTransfer.get(1), AccountOperation.class);
                }
            } else {
                // This block is called in the second recursion and takes care of deserializing the
                // transfer data itself.
                JsonObject jsonObject = json.getAsJsonObject();

                String name = jsonObject.get(KEY_NAME).getAsString();
                Authority owner = context.deserialize(jsonObject.get(KEY_OWNER), Authority.class);
                Authority active = context.deserialize(jsonObject.get(KEY_ACTIVE), Authority.class);
                AccountOptions options = context.deserialize(jsonObject.get(KEY_OPTIONS), AccountOptions.class);
                AssetAmount fee = context.deserialize(jsonObject.get(KEY_FEE), AssetAmount.class);

                AccountOperation operation = new AccountOperation(name, owner, active, options, fee);

                return operation;
            }
        }
    }
}
