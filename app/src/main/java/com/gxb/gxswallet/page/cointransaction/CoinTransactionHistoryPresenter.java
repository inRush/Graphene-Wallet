package com.gxb.gxswallet.page.cointransaction;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.sxds.common.presenter.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cy.agorise.graphenej.GrapheneObject;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountHistory;
import cy.agorise.graphenej.api.GetBlock;
import cy.agorise.graphenej.api.GetObjects;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.Block;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.operations.TransferOperation;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class CoinTransactionHistoryPresenter extends BasePresenter<CoinTransactionHistoryContract.View>
        implements CoinTransactionHistoryContract.Presenter {
    CoinTransactionHistoryPresenter(CoinTransactionHistoryContract.View view) {
        super(view);
    }


    @Override
    public void getTransactionHistory(WalletData wallet) {

        new GetAccountHistory(wallet.getAccountId(), 100).call(new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                List<HistoricalTransfer> transfers = (List<HistoricalTransfer>) response.result;

                List<Block> blocks = new ArrayList<>();
                List<HistoricalTransfer> newTransfer = new ArrayList<>();
                for (HistoricalTransfer transfer : transfers) {
                    if (transfer.getOperation() != null) {
                        newTransfer.add(transfer);
                    }
                }
                if (newTransfer.size() == 0) {
                    getView().onGetTransactionHistorySuccess(new ArrayList<>());
                    return;
                }
                for (HistoricalTransfer transfer : newTransfer) {
                    new GetBlock(transfer.getBlockNum()).call(new WitnessResponseListener() {
                        @Override
                        public void onSuccess(WitnessResponse response) {
                            blocks.add((Block) response.result);
                            if (blocks.size() == newTransfer.size()) {
                                convertToTransactionHistoryItem(blocks, wallet);
                            }
                        }

                        @Override
                        public void onError(BaseResponse.Error error) {
                            getView().showError(error.message);
                        }
                    });
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                getView().showError(error.message);
            }
        });
    }

    private void convertToTransactionHistoryItem(List<Block> blocks, WalletData wallet) {
        List<TransactionHistory> histories = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        for (Block block : blocks) {
            TransactionHistory history = new TransactionHistory();
            Transaction transaction = block.getTransactions().get(0);
            TransferOperation operation = (TransferOperation) transaction.getOperations().get(0);
            history.setAmount(operation.getAssetAmount().getAmount().doubleValue() / 100000.0);
            history.setDate(sdf.format(transaction.getBlockData().getExpiration()));
            history.setFee(operation.getFee().getAmount().doubleValue() / 100000.0);
            history.setTransactionIds(block.getTransaction_ids());
            if(operation.getMemo().getNonce() != null){
                history.setMemo(operation.getMemo());
            }
            List<String> accountId = new ArrayList<>();
            if (wallet.getAccountId().equals(operation.getFrom().getObjectId())) {
                history.setType(TransactionHistory.TransactionType.send);
                accountId.add(operation.getFrom().getObjectId());
                accountId.add(operation.getTo().getObjectId());
            } else {
                history.setType(TransactionHistory.TransactionType.receive);
                accountId.add(operation.getFrom().getObjectId());
                accountId.add(operation.getTo().getObjectId());
            }

            new GetObjects(accountId).call(new WitnessResponseListener() {
                @Override
                public void onSuccess(WitnessResponse response) {
                    List<GrapheneObject> objects = (List<GrapheneObject>) response.result;
                    history.setFrom(((UserAccount) objects.get(0)).getName());
                    history.setTo(((UserAccount) objects.get(1)).getName());
                    histories.add(history);
                    if (histories.size() == blocks.size()) {
                        getView().onGetTransactionHistorySuccess(histories);
                    }
                }

                @Override
                public void onError(BaseResponse.Error error) {
                    getView().showError(error.message);
                }
            });

        }
    }
}
