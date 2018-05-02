package com.gxb.gxswallet.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.SparseArray;

import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/4/25.
 */

public class TaskPool extends HandlerThread implements Handler.Callback {

    /**
     * 任务超时时间
     */
    private static final int TIME_OUT = 10000;

    private List<Task> mTaskQueue;
    private SparseArray<Runnable> mTimeoutTask;
    private boolean isOrder = false;
    private int mCurrentTaskIndex = 0;
    private RunTaskListener mListener;
    private Handler mHandler;


    public interface RunTaskListener {
        /**
         * 单个任务完成回调
         *
         * @param task 完成的任务
         * @param next 下一个任务
         */
        void onTaskComplete(Task task, Action next);

        /**
         * 所有任务完成回调
         */
        void onAllTaskComplete();

        /**
         * 任务错误
         *
         * @param task  出错任务
         * @param error 错误
         */
        void onTaskError(Task task, Error error, Action next);

        /**
         * 任务超时
         *
         * @param task  超时任务
         * @param retry 重试
         * @param next  下一个任务
         */
        void onTaskTimeOut(Task task, Action retry, Action next);
    }

    public TaskPool(boolean order) {
        super("TaskPool");
        mTaskQueue = new ArrayList<>();
        mTimeoutTask = new SparseArray<>();
        this.isOrder = order;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(getLooper(),this);
        mHandler.sendEmptyMessage(0x11);
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (isOrder) {
            runOrderTasks();
        } else {
            runDisorderTasks();
        }
        return true;
    }


    private void runDisorderTasks() {
        if (mTaskQueue.size() != 0) {
            for (Task task : mTaskQueue) {
                runTask(task);
            }
        }
    }

    private void runOrderTasks() {
        if (mCurrentTaskIndex >= mTaskQueue.size()) {
            mListener.onAllTaskComplete();
        } else {
            Task task = mTaskQueue.get(mCurrentTaskIndex);
            runTask(task);
        }
    }

    private void runTask(Task task) {
//        task.run(new Task.TaskListener() {
//            @Override
//            public void onFinish(Task task) {
//                finishTask(task);
//                mListener.onTaskComplete(task, () -> {
//                    mCurrentTaskIndex++;
//                    runOrderTasks();
//                });
//            }
//
//            @Override
//            public void onError(Error error) {
//                mListener.onTaskError(task, error, () -> {
//                    removeTimeOutTask(task);
//                    if (isOrder) {
//                        mCurrentTaskIndex++;
//                        runOrderTasks();
//                    }
//                });
//            }
//        });
        Runnable timeOutTask = checkTimeOutTask(task);
        mTimeoutTask.put(task.getId(), timeOutTask);
        mHandler.postDelayed(timeOutTask, TIME_OUT);
    }


    private Runnable checkTimeOutTask(Task task) {
        return () -> mListener.onTaskTimeOut(task, () -> {
            if (isOrder) {
                runOrderTasks();
            } else {
                runTask(task);
            }
        }, () -> {
            if (isOrder) {
                mCurrentTaskIndex++;
                runOrderTasks();
            }
        });
    }


    public TaskPool postTasks(List<Task> tasks, RunTaskListener listener) {
        if (tasks != null) {
            mTaskQueue.addAll(tasks);
            this.mListener = listener;
        }
        return this;
    }

    private void finishTask(Task task) {
        if (task != null) {
            mTaskQueue.remove(task);
            removeTimeOutTask(task);
        }
    }

    private void removeTimeOutTask(Task task) {
        Runnable timeOutTask = mTimeoutTask.get(task.getId());
        if (timeOutTask != null) {
            mHandler.removeCallbacks(timeOutTask);
        }
    }

}
