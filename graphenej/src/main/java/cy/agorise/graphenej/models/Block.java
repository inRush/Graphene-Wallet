package cy.agorise.graphenej.models;

import java.util.List;

import cy.agorise.graphenej.Transaction;

public class Block {
    private String previous;
    private String timestamp;
    private String witness;
    private String transaction_merkle_root;
    private Object[] extensions;
    private String witness_signature;
    private List<String> transaction_ids;
    private List<Transaction> transactions;

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getWitness() {
        return witness;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }

    public String getTransaction_merkle_root() {
        return transaction_merkle_root;
    }

    public void setTransaction_merkle_root(String transaction_merkle_root) {
        this.transaction_merkle_root = transaction_merkle_root;
    }

    public List<String> getTransaction_ids() {
        return transaction_ids;
    }

    public void setTransaction_ids(List<String> transaction_ids) {
        this.transaction_ids = transaction_ids;
    }

    public Object[] getExtensions() {
        return extensions;
    }

    public void setExtensions(Object[] extensions) {
        this.extensions = extensions;
    }

    public String getWitness_signature() {
        return witness_signature;
    }

    public void setWitness_signature(String witness_signature) {
        this.witness_signature = witness_signature;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
