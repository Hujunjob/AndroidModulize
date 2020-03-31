package com.hujun.modulize;

import com.hujun.modulize.api.core.ParameterLoad;

import org.jetbrains.annotations.NotNull;

/**
 * Created by junhu on 2020/3/30
 */
public class XActivity$$Parameter implements ParameterLoad {
    @Override
    public void loadParameter(@NotNull Object target) {
        MainActivity t = (MainActivity)target;
        t.setName(t.getIntent().getStringExtra("name"));
        t.setAge(t.getIntent().getIntExtra("age",t.getAge()));
    }
}
