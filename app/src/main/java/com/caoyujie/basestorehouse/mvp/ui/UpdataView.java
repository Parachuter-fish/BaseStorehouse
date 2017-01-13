package com.caoyujie.basestorehouse.mvp.ui;

import java.util.List;

/**
 * Created by caoyujie on 17/1/8.
 * 更新数据mvp ,view接口
 */

public interface UpdataView<T> {
    void upData(List<T> datas);
}
