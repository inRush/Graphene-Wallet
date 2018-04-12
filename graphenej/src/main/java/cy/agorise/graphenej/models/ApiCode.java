package cy.agorise.graphenej.models;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class ApiCode {
    private int dataseId;
    private int networkBroadcastId;
    private int historyId;
    private int cryptoId;

    public int getDataseId() {
        return dataseId;
    }

    public void setDataseId(int dataseId) {
        this.dataseId = dataseId;
    }

    public int getNetworkBroadcastId() {
        return networkBroadcastId;
    }

    public void setNetworkBroadcastId(int networkBroadcastId) {
        this.networkBroadcastId = networkBroadcastId;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(int cryptoId) {
        this.cryptoId = cryptoId;
    }
}
