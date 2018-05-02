package com.gxb.gxswallet.common;

import io.reactivex.Observable;

/**
 * @author inrush
 * @date 2018/4/25.
 */

public abstract class Task<D, T> {

    /**
     * 任务状态
     */
    private State mState;
    private int mId;
    private String tag;

    /**
     * 返回的数据
     */
    private D mData;

    public String getTag() {
        return tag;
    }


    public interface TaskListener {
        /**
         * 任务完成
         *
         * @param task 任务
         */
        void onFinish(Task task);

        /**
         * 错误捕获
         *
         * @param error Error
         */
        void onError(Error error);
    }

    /**
     * 任务状态
     */
    public enum State {
        /**
         * 初始态,未开始
         */
        INITIAL(0),
        /**
         * 运行中
         */
        RUNNING(0),
        /**
         * 完成
         */
        COMPLETE(0),
        /**
         * 错误状态
         */
        ERROR(0),
        /**
         * 重试
         */
        RETRY(0);

        private int mCount;

        State(int count) {
            mCount = count;
        }

        public int getCount() {
            return mCount;
        }

        public void add() {
            mCount++;
        }
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    public D getData() {
        return mData;
    }

    public int getId() {
        return mId;
    }


    public Task(String tag) {
        setState(State.INITIAL);
        this.mId = generateId();
        this.tag = tag;
    }

    public synchronized Observable<T> run() {
        setState(State.RUNNING);
        return runTask();
    }

    /**
     * 运行任务
     *
     * @return 观察者
     */
    protected abstract Observable<T> runTask();

    protected void taskFinish(D data) {
        setState(State.COMPLETE);
        this.mData = data;
    }

    protected void taskError() {
        setState(State.ERROR);
    }

    private static int ID = 0;

    private static int generateId() {
        return ID++;
    }
}
