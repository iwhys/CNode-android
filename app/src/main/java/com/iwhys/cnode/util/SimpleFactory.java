package com.iwhys.cnode.util;

import android.os.Bundle;

import com.iwhys.cnode.ui.fragment.BaseFragment;

/**
 * 简单工厂方法
 * Created by devil on 14-7-22.
 */
public class SimpleFactory {

    /**
     * Fragment工厂，通过反射获取Fragment实例
     * @param fragmentName 类名称
     * @param directoryName 多级目录用.分隔，如"column.detail"
     * @param arguments 初始化时传入的参数
     * @return
     */
    public static BaseFragment createFragment(String fragmentName, String directoryName, Bundle arguments){
        StringBuilder path = new StringBuilder();
        path.append(BaseFragment.class.getPackage().getName());
        path.append(".");
        if (directoryName!=null&&directoryName.length()>0){
            //如果存在目录名，则把目录添加到路径中
            path.append(directoryName);
            path.append(".");
        }
        path.append(fragmentName);
        String className = path.toString();
        Class<?> cls = CommonUtils.getClassByString(className);
        if (cls!=null){
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment)cls.newInstance();
                if (arguments!=null){
                    fragment.setArguments(arguments);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }
        return null;
    }

    public static BaseFragment createFragment(String fragmentName, String directoryName){
        return createFragment(fragmentName, directoryName, null);
    }

    public static BaseFragment createFragment(String fragmentName, Bundle arguments){
        return createFragment(fragmentName, null, arguments);
    }

    public static BaseFragment createFragment(String fragmentName){
        return createFragment(fragmentName, null, null);
    }

}
