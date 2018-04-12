package com.gxb.sdk.models.chain;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/19.
 */

public class GlobalChainParam {

    /**
     * id : 2.0.0
     * parameters : {"current_fees":{"parameters":[[0,{"fee":100000,"price_per_kbyte":1000000}],[1,{"fee":1000000000}],[2,{"fee":0}],[3,{"fee":1000000000}],[4,{}],[5,{"basic_fee":100000,"premium_fee":200000000,"price_per_kbyte":100000}],[6,{"fee":100000,"price_per_kbyte":100000}],[7,{"fee":300000}],[8,{"membership_annual_fee":100000000,"membership_lifetime_fee":200000000}],[9,{"fee":50000000}],[10,{"symbol3":"100000000000","symbol4":"80000000000","long_symbol":"50000000000","price_per_kbyte":10}],[11,{"fee":"10000000000","price_per_kbyte":10}],[12,{"fee":"10000000000"}],[13,{"fee":"10000000000"}],[14,{"fee":"10000000000","price_per_kbyte":100000}],[15,{"fee":"10000000000"}],[16,{"fee":"10000000000"}],[17,{"fee":"10000000000"}],[18,{"fee":"10000000000"}],[19,{"fee":"10000000000"}],[20,{"fee":100000000}],[21,{"fee":2000000}],[22,{"fee":2000000,"price_per_kbyte":10}],[23,{"fee":2000000,"price_per_kbyte":10}],[24,{"fee":100000}],[25,{"fee":100000}],[26,{"fee":100000}],[27,{"fee":2000000,"price_per_kbyte":10}],[28,{"fee":0}],[29,{"fee":1000000000}],[30,{"fee":2000000}],[31,{"fee":100000}],[32,{"fee":100000}],[33,{"fee":2000000}],[34,{"fee":"5000000000"}],[35,{"fee":100000,"price_per_kbyte":10}],[36,{"fee":100000}],[37,{}],[38,{"fee":2000000,"price_per_kbyte":10}],[39,{"fee":500000,"price_per_output":500000}],[40,{"fee":500000,"price_per_output":500000}],[41,{"fee":500000}],[42,{}],[43,{"fee":2000000}],[44,{}],[45,{"fee":0}],[46,{"fee":0}],[47,{"fee":1000000}],[48,{"fee":1000000}],[49,{"fee":1000000}],[50,{"fee":1000000}],[51,{"fee":1000000}],[52,{"fee":1000000}],[53,{"fee":1000000}],[54,{"fee":1000000}],[55,{"fee":100}],[56,{"fee":0}],[57,{"fee":100}],[58,{"fee":0}],[59,{"fee":0}],[60,{"fee":0}],[61,{"fee":1000000}],[62,{"fee":0}],[63,{"fee":1000000}],[64,{"fee":100000}],[65,{"fee":1000000}],[66,{"fee":100000}],[67,{"fee":1000000}],[68,{"fee":100000}],[69,{"fee":100000}],[70,{"fee":0}],[71,{}],[72,{}]],"scale":10000},"block_interval":3,"maintenance_interval":600,"maintenance_skip_slots":3,"committee_proposal_review_period":1800,"maximum_transaction_size":98304,"maximum_block_size":2097152,"maximum_time_until_expiration":86400,"maximum_proposal_lifetime":2419200,"maximum_asset_whitelist_authorities":10,"maximum_asset_feed_publishers":10,"maximum_witness_count":1001,"maximum_committee_count":1001,"maximum_authority_membership":10,"reserve_percent_of_fee":2000,"network_percent_of_fee":2000,"lifetime_referrer_percent_of_fee":3000,"cashback_vesting_period_seconds":7776000,"cashback_vesting_threshold":10000000,"count_non_member_votes":true,"allow_non_member_whitelists":false,"witness_pay_per_block":100000,"worker_budget_per_day":"50000000000","max_predicate_opcode":1,"fee_liquidation_threshold":10000000,"accounts_per_fee_scale":1000,"account_fee_scale_bitshifts":4,"max_authority_depth":2,"extensions":[[6,{"params":[["3",{"lock_days":90,"interest_rate":400,"is_valid":false}],["9",{"lock_days":270,"interest_rate":600,"is_valid":false}],["24",{"lock_days":720,"interest_rate":800,"is_valid":false}]]}]]}
     * next_available_vote_id : 24
     * active_committee_members : ["1.5.7","1.5.10","1.5.1","1.5.0","1.5.5","1.5.2","1.5.4","1.5.6","1.5.8","1.5.3","1.5.9"]
     * active_witnesses : ["1.6.1","1.6.2","1.6.3","1.6.4","1.6.5","1.6.6","1.6.7","1.6.8","1.6.9","1.6.10","1.6.11"]
     */

    private String id;
    private ParametersBean parameters;
    private int next_available_vote_id;
    private List<String> active_committee_members;
    private List<String> active_witnesses;

    public void setId(String id) {
        this.id = id;
    }

    public void setParameters(ParametersBean parameters) {
        this.parameters = parameters;
    }

    public void setNext_available_vote_id(int next_available_vote_id) {
        this.next_available_vote_id = next_available_vote_id;
    }

    public void setActive_committee_members(List<String> active_committee_members) {
        this.active_committee_members = active_committee_members;
    }

    public void setActive_witnesses(List<String> active_witnesses) {
        this.active_witnesses = active_witnesses;
    }

    public String getId() {
        return id;
    }

    public ParametersBean getParameters() {
        return parameters;
    }

    public int getNext_available_vote_id() {
        return next_available_vote_id;
    }

    public List<String> getActive_committee_members() {
        return active_committee_members;
    }

    public List<String> getActive_witnesses() {
        return active_witnesses;
    }

    public static class ParametersBean {
        /**
         * current_fees : {"parameters":[[0,{"fee":100000,"price_per_kbyte":1000000}],[1,{"fee":1000000000}],[2,{"fee":0}],[3,{"fee":1000000000}],[4,{}],[5,{"basic_fee":100000,"premium_fee":200000000,"price_per_kbyte":100000}],[6,{"fee":100000,"price_per_kbyte":100000}],[7,{"fee":300000}],[8,{"membership_annual_fee":100000000,"membership_lifetime_fee":200000000}],[9,{"fee":50000000}],[10,{"symbol3":"100000000000","symbol4":"80000000000","long_symbol":"50000000000","price_per_kbyte":10}],[11,{"fee":"10000000000","price_per_kbyte":10}],[12,{"fee":"10000000000"}],[13,{"fee":"10000000000"}],[14,{"fee":"10000000000","price_per_kbyte":100000}],[15,{"fee":"10000000000"}],[16,{"fee":"10000000000"}],[17,{"fee":"10000000000"}],[18,{"fee":"10000000000"}],[19,{"fee":"10000000000"}],[20,{"fee":100000000}],[21,{"fee":2000000}],[22,{"fee":2000000,"price_per_kbyte":10}],[23,{"fee":2000000,"price_per_kbyte":10}],[24,{"fee":100000}],[25,{"fee":100000}],[26,{"fee":100000}],[27,{"fee":2000000,"price_per_kbyte":10}],[28,{"fee":0}],[29,{"fee":1000000000}],[30,{"fee":2000000}],[31,{"fee":100000}],[32,{"fee":100000}],[33,{"fee":2000000}],[34,{"fee":"5000000000"}],[35,{"fee":100000,"price_per_kbyte":10}],[36,{"fee":100000}],[37,{}],[38,{"fee":2000000,"price_per_kbyte":10}],[39,{"fee":500000,"price_per_output":500000}],[40,{"fee":500000,"price_per_output":500000}],[41,{"fee":500000}],[42,{}],[43,{"fee":2000000}],[44,{}],[45,{"fee":0}],[46,{"fee":0}],[47,{"fee":1000000}],[48,{"fee":1000000}],[49,{"fee":1000000}],[50,{"fee":1000000}],[51,{"fee":1000000}],[52,{"fee":1000000}],[53,{"fee":1000000}],[54,{"fee":1000000}],[55,{"fee":100}],[56,{"fee":0}],[57,{"fee":100}],[58,{"fee":0}],[59,{"fee":0}],[60,{"fee":0}],[61,{"fee":1000000}],[62,{"fee":0}],[63,{"fee":1000000}],[64,{"fee":100000}],[65,{"fee":1000000}],[66,{"fee":100000}],[67,{"fee":1000000}],[68,{"fee":100000}],[69,{"fee":100000}],[70,{"fee":0}],[71,{}],[72,{}]],"scale":10000}
         * block_interval : 3
         * maintenance_interval : 600
         * maintenance_skip_slots : 3
         * committee_proposal_review_period : 1800
         * maximum_transaction_size : 98304
         * maximum_block_size : 2097152
         * maximum_time_until_expiration : 86400
         * maximum_proposal_lifetime : 2419200
         * maximum_asset_whitelist_authorities : 10
         * maximum_asset_feed_publishers : 10
         * maximum_witness_count : 1001
         * maximum_committee_count : 1001
         * maximum_authority_membership : 10
         * reserve_percent_of_fee : 2000
         * network_percent_of_fee : 2000
         * lifetime_referrer_percent_of_fee : 3000
         * cashback_vesting_period_seconds : 7776000
         * cashback_vesting_threshold : 10000000
         * count_non_member_votes : true
         * allow_non_member_whitelists : false
         * witness_pay_per_block : 100000
         * worker_budget_per_day : 50000000000
         * max_predicate_opcode : 1
         * fee_liquidation_threshold : 10000000
         * accounts_per_fee_scale : 1000
         * account_fee_scale_bitshifts : 4
         * max_authority_depth : 2
         * extensions : [[6,{"params":[["3",{"lock_days":90,"interest_rate":400,"is_valid":false}],["9",{"lock_days":270,"interest_rate":600,"is_valid":false}],["24",{"lock_days":720,"interest_rate":800,"is_valid":false}]]}]]
         */

        private CurrentFeesBean current_fees;
        private int block_interval;
        private int maintenance_interval;
        private int maintenance_skip_slots;
        private int committee_proposal_review_period;
        private int maximum_transaction_size;
        private int maximum_block_size;
        private int maximum_time_until_expiration;
        private int maximum_proposal_lifetime;
        private int maximum_asset_whitelist_authorities;
        private int maximum_asset_feed_publishers;
        private int maximum_witness_count;
        private int maximum_committee_count;
        private int maximum_authority_membership;
        private int reserve_percent_of_fee;
        private int network_percent_of_fee;
        private int lifetime_referrer_percent_of_fee;
        private int cashback_vesting_period_seconds;
        private int cashback_vesting_threshold;
        private boolean count_non_member_votes;
        private boolean allow_non_member_whitelists;
        private int witness_pay_per_block;
        private String worker_budget_per_day;
        private int max_predicate_opcode;
        private int fee_liquidation_threshold;
        private int accounts_per_fee_scale;
        private int account_fee_scale_bitshifts;
        private int max_authority_depth;
        private List<List<Integer>> extensions;

        public void setCurrent_fees(CurrentFeesBean current_fees) {
            this.current_fees = current_fees;
        }

        public void setBlock_interval(int block_interval) {
            this.block_interval = block_interval;
        }

        public void setMaintenance_interval(int maintenance_interval) {
            this.maintenance_interval = maintenance_interval;
        }

        public void setMaintenance_skip_slots(int maintenance_skip_slots) {
            this.maintenance_skip_slots = maintenance_skip_slots;
        }

        public void setCommittee_proposal_review_period(int committee_proposal_review_period) {
            this.committee_proposal_review_period = committee_proposal_review_period;
        }

        public void setMaximum_transaction_size(int maximum_transaction_size) {
            this.maximum_transaction_size = maximum_transaction_size;
        }

        public void setMaximum_block_size(int maximum_block_size) {
            this.maximum_block_size = maximum_block_size;
        }

        public void setMaximum_time_until_expiration(int maximum_time_until_expiration) {
            this.maximum_time_until_expiration = maximum_time_until_expiration;
        }

        public void setMaximum_proposal_lifetime(int maximum_proposal_lifetime) {
            this.maximum_proposal_lifetime = maximum_proposal_lifetime;
        }

        public void setMaximum_asset_whitelist_authorities(int maximum_asset_whitelist_authorities) {
            this.maximum_asset_whitelist_authorities = maximum_asset_whitelist_authorities;
        }

        public void setMaximum_asset_feed_publishers(int maximum_asset_feed_publishers) {
            this.maximum_asset_feed_publishers = maximum_asset_feed_publishers;
        }

        public void setMaximum_witness_count(int maximum_witness_count) {
            this.maximum_witness_count = maximum_witness_count;
        }

        public void setMaximum_committee_count(int maximum_committee_count) {
            this.maximum_committee_count = maximum_committee_count;
        }

        public void setMaximum_authority_membership(int maximum_authority_membership) {
            this.maximum_authority_membership = maximum_authority_membership;
        }

        public void setReserve_percent_of_fee(int reserve_percent_of_fee) {
            this.reserve_percent_of_fee = reserve_percent_of_fee;
        }

        public void setNetwork_percent_of_fee(int network_percent_of_fee) {
            this.network_percent_of_fee = network_percent_of_fee;
        }

        public void setLifetime_referrer_percent_of_fee(int lifetime_referrer_percent_of_fee) {
            this.lifetime_referrer_percent_of_fee = lifetime_referrer_percent_of_fee;
        }

        public void setCashback_vesting_period_seconds(int cashback_vesting_period_seconds) {
            this.cashback_vesting_period_seconds = cashback_vesting_period_seconds;
        }

        public void setCashback_vesting_threshold(int cashback_vesting_threshold) {
            this.cashback_vesting_threshold = cashback_vesting_threshold;
        }

        public void setCount_non_member_votes(boolean count_non_member_votes) {
            this.count_non_member_votes = count_non_member_votes;
        }

        public void setAllow_non_member_whitelists(boolean allow_non_member_whitelists) {
            this.allow_non_member_whitelists = allow_non_member_whitelists;
        }

        public void setWitness_pay_per_block(int witness_pay_per_block) {
            this.witness_pay_per_block = witness_pay_per_block;
        }

        public void setWorker_budget_per_day(String worker_budget_per_day) {
            this.worker_budget_per_day = worker_budget_per_day;
        }

        public void setMax_predicate_opcode(int max_predicate_opcode) {
            this.max_predicate_opcode = max_predicate_opcode;
        }

        public void setFee_liquidation_threshold(int fee_liquidation_threshold) {
            this.fee_liquidation_threshold = fee_liquidation_threshold;
        }

        public void setAccounts_per_fee_scale(int accounts_per_fee_scale) {
            this.accounts_per_fee_scale = accounts_per_fee_scale;
        }

        public void setAccount_fee_scale_bitshifts(int account_fee_scale_bitshifts) {
            this.account_fee_scale_bitshifts = account_fee_scale_bitshifts;
        }

        public void setMax_authority_depth(int max_authority_depth) {
            this.max_authority_depth = max_authority_depth;
        }

        public void setExtensions(List<List<Integer>> extensions) {
            this.extensions = extensions;
        }

        public CurrentFeesBean getCurrent_fees() {
            return current_fees;
        }

        public int getBlock_interval() {
            return block_interval;
        }

        public int getMaintenance_interval() {
            return maintenance_interval;
        }

        public int getMaintenance_skip_slots() {
            return maintenance_skip_slots;
        }

        public int getCommittee_proposal_review_period() {
            return committee_proposal_review_period;
        }

        public int getMaximum_transaction_size() {
            return maximum_transaction_size;
        }

        public int getMaximum_block_size() {
            return maximum_block_size;
        }

        public int getMaximum_time_until_expiration() {
            return maximum_time_until_expiration;
        }

        public int getMaximum_proposal_lifetime() {
            return maximum_proposal_lifetime;
        }

        public int getMaximum_asset_whitelist_authorities() {
            return maximum_asset_whitelist_authorities;
        }

        public int getMaximum_asset_feed_publishers() {
            return maximum_asset_feed_publishers;
        }

        public int getMaximum_witness_count() {
            return maximum_witness_count;
        }

        public int getMaximum_committee_count() {
            return maximum_committee_count;
        }

        public int getMaximum_authority_membership() {
            return maximum_authority_membership;
        }

        public int getReserve_percent_of_fee() {
            return reserve_percent_of_fee;
        }

        public int getNetwork_percent_of_fee() {
            return network_percent_of_fee;
        }

        public int getLifetime_referrer_percent_of_fee() {
            return lifetime_referrer_percent_of_fee;
        }

        public int getCashback_vesting_period_seconds() {
            return cashback_vesting_period_seconds;
        }

        public int getCashback_vesting_threshold() {
            return cashback_vesting_threshold;
        }

        public boolean getCount_non_member_votes() {
            return count_non_member_votes;
        }

        public boolean getAllow_non_member_whitelists() {
            return allow_non_member_whitelists;
        }

        public int getWitness_pay_per_block() {
            return witness_pay_per_block;
        }

        public String getWorker_budget_per_day() {
            return worker_budget_per_day;
        }

        public int getMax_predicate_opcode() {
            return max_predicate_opcode;
        }

        public int getFee_liquidation_threshold() {
            return fee_liquidation_threshold;
        }

        public int getAccounts_per_fee_scale() {
            return accounts_per_fee_scale;
        }

        public int getAccount_fee_scale_bitshifts() {
            return account_fee_scale_bitshifts;
        }

        public int getMax_authority_depth() {
            return max_authority_depth;
        }

        public List<List<Integer>> getExtensions() {
            return extensions;
        }

        public static class CurrentFeesBean {
            /**
             * parameters : [[0,{"fee":100000,"price_per_kbyte":1000000}],[1,{"fee":1000000000}],[2,{"fee":0}],[3,{"fee":1000000000}],[4,{}],[5,{"basic_fee":100000,"premium_fee":200000000,"price_per_kbyte":100000}],[6,{"fee":100000,"price_per_kbyte":100000}],[7,{"fee":300000}],[8,{"membership_annual_fee":100000000,"membership_lifetime_fee":200000000}],[9,{"fee":50000000}],[10,{"symbol3":"100000000000","symbol4":"80000000000","long_symbol":"50000000000","price_per_kbyte":10}],[11,{"fee":"10000000000","price_per_kbyte":10}],[12,{"fee":"10000000000"}],[13,{"fee":"10000000000"}],[14,{"fee":"10000000000","price_per_kbyte":100000}],[15,{"fee":"10000000000"}],[16,{"fee":"10000000000"}],[17,{"fee":"10000000000"}],[18,{"fee":"10000000000"}],[19,{"fee":"10000000000"}],[20,{"fee":100000000}],[21,{"fee":2000000}],[22,{"fee":2000000,"price_per_kbyte":10}],[23,{"fee":2000000,"price_per_kbyte":10}],[24,{"fee":100000}],[25,{"fee":100000}],[26,{"fee":100000}],[27,{"fee":2000000,"price_per_kbyte":10}],[28,{"fee":0}],[29,{"fee":1000000000}],[30,{"fee":2000000}],[31,{"fee":100000}],[32,{"fee":100000}],[33,{"fee":2000000}],[34,{"fee":"5000000000"}],[35,{"fee":100000,"price_per_kbyte":10}],[36,{"fee":100000}],[37,{}],[38,{"fee":2000000,"price_per_kbyte":10}],[39,{"fee":500000,"price_per_output":500000}],[40,{"fee":500000,"price_per_output":500000}],[41,{"fee":500000}],[42,{}],[43,{"fee":2000000}],[44,{}],[45,{"fee":0}],[46,{"fee":0}],[47,{"fee":1000000}],[48,{"fee":1000000}],[49,{"fee":1000000}],[50,{"fee":1000000}],[51,{"fee":1000000}],[52,{"fee":1000000}],[53,{"fee":1000000}],[54,{"fee":1000000}],[55,{"fee":100}],[56,{"fee":0}],[57,{"fee":100}],[58,{"fee":0}],[59,{"fee":0}],[60,{"fee":0}],[61,{"fee":1000000}],[62,{"fee":0}],[63,{"fee":1000000}],[64,{"fee":100000}],[65,{"fee":1000000}],[66,{"fee":100000}],[67,{"fee":1000000}],[68,{"fee":100000}],[69,{"fee":100000}],[70,{"fee":0}],[71,{}],[72,{}]]
             * scale : 10000
             */

            private int scale;
            private List<List<Integer>> parameters;

            public void setScale(int scale) {
                this.scale = scale;
            }

            public void setParameters(List<List<Integer>> parameters) {
                this.parameters = parameters;
            }

            public int getScale() {
                return scale;
            }

            public List<List<Integer>> getParameters() {
                return parameters;
            }
        }
    }
}
