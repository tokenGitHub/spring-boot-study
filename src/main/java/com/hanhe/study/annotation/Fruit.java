package com.hanhe.study.annotation;

public class Fruit {
    private String name;

    @FruitColor(fruitColor = FruitColor.Color.BLUE)
    private FruitColor.Color color;

    @FruitName("apple")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
