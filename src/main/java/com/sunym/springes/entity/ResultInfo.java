package com.sunym.springes.entity;

import lombok.Data;

@Data
public class ResultInfo {
    private String info;

    public ResultInfo() {
    }

    public ResultInfo(String info) {
        this.info = info;
    }
}
