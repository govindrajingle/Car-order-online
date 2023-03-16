package com.solutionstouch.tesla.navigation;

import android.content.ClipData;
import android.content.Context;

import com.solutionstouch.tesla.R;

import java.util.ArrayList;
import java.util.List;

public class CustomDataProvider {

    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;
    private static List<BaseItem> mMenu = new ArrayList<>();
    Context context;

    public static List<BaseItem> getInitialItems() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();


     //   rootMenu.add(new ClipData.Item("Home", R.drawable.ic_category));
        rootMenu.add(new GroupItem("SEDAN", R.drawable.sedan));
        rootMenu.add(new GroupItem("COUPE", R.drawable.coupe));
        rootMenu.add(new GroupItem("SPORT CAR", R.drawable.sportcar));
        rootMenu.add(new GroupItem("SUV", R.drawable.suv));
        rootMenu.add(new GroupItem("TRUCK", R.drawable.truck));
        rootMenu.add(new GroupItem("EXIT", R.drawable.logout));
        return rootMenu;
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();
        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }


        switch (level) {
            case LEVEL_1:
                switch (menuItem.toUpperCase()) {

                        case "SEDAN":
                                result = getListCategory();
                                break;
                        case "COUPE":
                                result = getListAssignments();
                                break;
                        case "SPORT CAR":
                                result = getListSportCars();
                                break;
                        case "SUV":
                                result = getListSuv();
                                break;
                        case "TRUCK":
                                result = getListTruck();
                                break;
                }
                break;
        }
        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    private static List<BaseItem> getListCategory() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Model S"));
        list.add(new Item("Model A"));
        list.add(new Item("Model B"));


        return list;
    }

    private static List<BaseItem> getListAssignments() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Model 3"));
        list.add(new Item("Model C"));
        list.add(new Item("Model D"));
        return list;
    }


    private static List<BaseItem> getListSportCars() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Model X"));
        list.add(new Item("Model H"));
        list.add(new Item("Model G"));
        return list;
    }

    private static List<BaseItem> getListSuv() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Model Y"));
        list.add(new Item("Model H"));
        list.add(new Item("Model I"));
        return list;
    }

    private static List<BaseItem> getListTruck() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("CyberTruck"));
        return list;
    }
}
