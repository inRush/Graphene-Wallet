package com.gxb.sdk.models.chain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/19.
 */

public class CurrentChainParam {

    /**
     * id : 2.1.0
     * head_block_number : 2669946
     * head_block_id : 0028bd7a71014f8ca9d5fb36a2a067240a10a8b3
     * time : 2018-03-19T02:43:57
     * current_witness : 1.6.4
     * next_maintenance_time : 2018-03-19T02:50:00
     * last_budget_time : 2018-03-19T02:40:00
     * witness_budget : 0
     * accounts_registered_this_interval : 0
     * recently_missed_count : 0
     * current_aslot : 2694337
     * recent_slots_filled : 340282366920938463463374607431768211455
     * dynamic_flags : 0
     * last_irreversible_block_num : 2669938
     */

    private String id;
    private int head_block_number;
    private String head_block_id;
    private String time;
    private String current_witness;
    private String next_maintenance_time;
    private String last_budget_time;
    private int witness_budget;
    private int accounts_registered_this_interval;
    private int recently_missed_count;
    private int current_aslot;
    private String recent_slots_filled;
    private int dynamic_flags;
    private int last_irreversible_block_num;

    public void setId(String id) {
        this.id = id;
    }

    public void setHead_block_number(int head_block_number) {
        this.head_block_number = head_block_number;
    }

    public void setHead_block_id(String head_block_id) {
        this.head_block_id = head_block_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCurrent_witness(String current_witness) {
        this.current_witness = current_witness;
    }

    public void setNext_maintenance_time(String next_maintenance_time) {
        this.next_maintenance_time = next_maintenance_time;
    }

    public void setLast_budget_time(String last_budget_time) {
        this.last_budget_time = last_budget_time;
    }

    public void setWitness_budget(int witness_budget) {
        this.witness_budget = witness_budget;
    }

    public void setAccounts_registered_this_interval(int accounts_registered_this_interval) {
        this.accounts_registered_this_interval = accounts_registered_this_interval;
    }

    public void setRecently_missed_count(int recently_missed_count) {
        this.recently_missed_count = recently_missed_count;
    }

    public void setCurrent_aslot(int current_aslot) {
        this.current_aslot = current_aslot;
    }

    public void setRecent_slots_filled(String recent_slots_filled) {
        this.recent_slots_filled = recent_slots_filled;
    }

    public void setDynamic_flags(int dynamic_flags) {
        this.dynamic_flags = dynamic_flags;
    }

    public void setLast_irreversible_block_num(int last_irreversible_block_num) {
        this.last_irreversible_block_num = last_irreversible_block_num;
    }

    public String getId() {
        return id;
    }

    public int getHead_block_number() {
        return head_block_number;
    }

    public String getHead_block_id() {
        return head_block_id;
    }

    public String getTime() {
        return time;
    }

    public String getCurrent_witness() {
        return current_witness;
    }

    public String getNext_maintenance_time() {
        return next_maintenance_time;
    }

    public String getLast_budget_time() {
        return last_budget_time;
    }

    public int getWitness_budget() {
        return witness_budget;
    }

    public int getAccounts_registered_this_interval() {
        return accounts_registered_this_interval;
    }

    public int getRecently_missed_count() {
        return recently_missed_count;
    }

    public int getCurrent_aslot() {
        return current_aslot;
    }

    public String getRecent_slots_filled() {
        return recent_slots_filled;
    }

    public int getDynamic_flags() {
        return dynamic_flags;
    }

    public int getLast_irreversible_block_num() {
        return last_irreversible_block_num;
    }

    public static List<CurrentChainParam> fromJsonToList(String json){
        return new Gson().fromJson(json, new TypeToken<List<CurrentChainParam>>() {
        }.getType());
    }
}
