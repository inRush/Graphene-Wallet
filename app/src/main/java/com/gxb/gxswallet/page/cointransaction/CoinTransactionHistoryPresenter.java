package com.gxb.gxswallet.page.cointransaction;

import com.gxb.gxswallet.base.Task;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.gxb.gxswallet.services.rpc.RpcTask;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.sxds.common.presenter.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetAccountHistory;
import cy.agorise.graphenej.api.GetBlock;
import cy.agorise.graphenej.api.GetObjects;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.Block;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.operations.TransferOperation;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
    public void getTransactionHistory(WalletData wallet, AssetData asset) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        WebSocketService service = WebSocketServicePool.getInstance().getService(asset.getName());
        List<Block> blocks = new ArrayList<>();
        final String[] accountId = new String[1];
        List<TransactionHistory> histories = new ArrayList<>();

        new RpcTask(new GetAccountByName(service, wallet.getName()), asset.getName())
                .run().flatMap(rpcTask -> {
            if (rpcTask.getData().result instanceof AccountProperties) {
                accountId[0] = ((AccountProperties) rpcTask.getData().result).id;
                return new RpcTask(new GetAccountHistory(service, accountId[0], 100), asset.getName()).run();
            }
            return Observable.empty();
        }).flatMap(rpcTask -> {
            if (rpcTask.getData().result instanceof List) {
                List<HistoricalTransfer> transfers = (List<HistoricalTransfer>) rpcTask.getData().result;
                // 筛选出有用的交易历史
                transfers = filterTransfer(transfers);
                if (transfers.size() == 0) {
                    // 没有数据,直接结束
                    return Observable.empty();
                } else {
                    RpcTask[] rpcTasks = new RpcTask[transfers.size()];
                    for (int i = 0; i < transfers.size(); i++) {
                        rpcTasks[i] = new RpcTask(new GetBlock(service, transfers.get(i).getBlockNum()), asset.getName());
                    }
                    return Observable.fromArray(rpcTasks)
                            .flatMap(Task::run);
                }
            }
            return Observable.empty();
        }).flatMap(rpcTask -> {
            if (rpcTask.getData().result instanceof Block) {
                List<String> ids = new ArrayList<>();
                Block block = (Block) rpcTask.getData().result;
                Transaction transaction = block.getTransactions().get(0);
                TransferOperation operation = (TransferOperation) transaction.getOperations().get(0);
                TransactionHistory history = new TransactionHistory();
                if (accountId[0].equals(operation.getFrom().getObjectId())) {
                    history.setType(TransactionHistory.TransactionType.send);
                    ids.add(operation.getFrom().getObjectId());
                    ids.add(operation.getTo().getObjectId());
                } else {
                    history.setType(TransactionHistory.TransactionType.receive);
                    ids.add(operation.getFrom().getObjectId());
                    ids.add(operation.getTo().getObjectId());
                }
                int index = histories.size();
                blocks.add(block);
                histories.add(history);
                return new RpcTask(new GetObjects(service, ids), String.valueOf(index)).run();
            }
            return Observable.empty();
        }).flatMap((Function<RpcTask, ObservableSource<RpcTask>>) rpcTask -> {
            if (rpcTask.getData().result instanceof List) {
                List<UserAccount> accounts = (List<UserAccount>) rpcTask.getData().result;
                int index = Integer.parseInt(rpcTask.getTag());
                TransactionHistory history = histories.get(index);
                Block block = blocks.get(index);
                Transaction transaction = block.getTransactions().get(0);
                TransferOperation operation = (TransferOperation) transaction.getOperations().get(0);
                history.setFrom(accounts.get(0).getName());
                history.setTo(accounts.get(1).getName());
                history.setAmount(operation.getAssetAmount().getAmount().doubleValue() / 100000.0);
                history.setDate(sdf.format(transaction.getBlockData().getExpiration()));
                history.setFee(operation.getFee().getAmount().doubleValue() / 100000.0);
                history.setTransactionIds(block.getTransaction_ids());
                if (operation.getMemo().getNonce() != null) {
                    history.setMemo(operation.getMemo());
                }
            }
            return Observable.empty();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RpcTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(RpcTask rpcTask) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                        getView().onGetTransactionHistorySuccess(histories);
                    }

                    @Override
                    public void onComplete() {
                        getView().onGetTransactionHistorySuccess(histories);
                    }
                });
    }

    private List<HistoricalTransfer> filterTransfer(List<HistoricalTransfer> transfers) {
        List<HistoricalTransfer> newTransfer = new ArrayList<>();
        for (HistoricalTransfer transfer : transfers) {
            if (transfer.getOperation() != null) {
                newTransfer.add(transfer);
            }
        }
        return newTransfer;
    }
}
