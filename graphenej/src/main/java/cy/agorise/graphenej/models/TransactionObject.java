package cy.agorise.graphenej.models;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.Extensions;
import cy.agorise.graphenej.operations.TransferOperation;

/**
 * @author inrush
 * @date 2018/4/5.
 */

public class TransactionObject {

    /**
     * ref_block_num : 40694
     * ref_block_prefix : 2579623105
     * expiration : 2018-04-04T06:46:37
     * signatures : ["2010b72a8ee2613e7426977040e2da84c3b306cee5994ef62880f80a9944b64d502bf83aa937a81122a740fc74466887624df09bfe3c09f5efc6f9864d57fd5520"]
     */

    private long ref_block_num;
    private long ref_block_prefix;
    private String expiration;
    private List<String> signatures;
    private ArrayList<Extensions> extensions;
    private ArrayList<TransferOperation> operations;

    public ArrayList<Extensions> getExtensions() {
        return extensions;
    }

    public void setExtensions(ArrayList<Extensions> extensions) {
        this.extensions = extensions;
    }

    public ArrayList<TransferOperation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<TransferOperation> operations) {
        this.operations = operations;
    }

    public void setRef_block_num(long ref_block_num) {
        this.ref_block_num = ref_block_num;
    }

    public void setRef_block_prefix(long ref_block_prefix) {
        this.ref_block_prefix = ref_block_prefix;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public long getRef_block_num() {
        return ref_block_num;
    }

    public long getRef_block_prefix() {
        return ref_block_prefix;
    }

    public String getExpiration() {
        return expiration;
    }

    public List<String> getSignatures() {
        return signatures;
    }
}
