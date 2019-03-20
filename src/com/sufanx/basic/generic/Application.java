package com.sufanx.basic.generic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sufanx.basic.generic.bridge.MyComparator;
import com.sufanx.basic.generic.fruit.Apple;
import com.sufanx.basic.generic.fruit.Fruit;
import com.sufanx.basic.generic.fruit.Red;
import com.sufanx.basic.generic.fruit.RedApple;

public class Application {

    public static void main(String[] args) {
        extendsAndPolymorphism();
        extendsDemo();
        superDemo();
        bridge();
    }

    /**
     * 多态和泛型上边界混合使用
     * <p>
     * 从某中角度看，泛型上边界表现出泛型的多态，但是请注意和类的多态分开
     */
    private static void extendsAndPolymorphism() {
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        LinkedList<Integer> integerLinkedList = new LinkedList<>();
        LinkedList<Double> doubleLinkedList = new LinkedList<>();

        List<? extends Number> list = null;
        list = integerArrayList; // valid
        list = doubleArrayList; // valid
        list = integerLinkedList; // valid
        list = doubleArrayList; // valid
    }

    /**
     * 上边界用法：
     * <p>
     * 1. 引用泛型为指定类型或其子类型的实例；
     * 2. 作为数据仓库，提取数据；
     */
    private static void extendsDemo() {
        List<Fruit> fruits = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();

        // 1. 表示可以引用泛型为 Food 或其子类的容器
        // 单独看这里的声明，我们可以推断出其引用的实例的泛型类型为 Fruit 或者是 Fruit 的子类。
        // 这也是边界的作用，表明实例的泛型范围
        List<? extends Fruit> list = fruits;
        list = apples;

        // 2. 可以从中取出元素，取出的元素推出的类型为上边界
        Fruit food = list.get(0);

        /*
         * 错误用法: 不可往其中存放任何东西
         *
         * List<? extends Food> 的实例可能是 ArrayList<Fruit>、ArrayList<Apple> 等等。
         * 存储的时候（Runtime），并不知道 List<? extends Food> 引用的实例的实际类型，如果往其中存储数据，类型不一定可以匹配，
         * 比如，实际是 ArrayList<Apple>，却想往其中存放 Fruit 实例，这显然是不对的。
         */
        // list.add(new Fruit()); // invalid
        // list.add(new Apple()); // invalid
    }

    /**
     * 下边界用法
     * <p>
     * 1. 引用泛型为指定类型或其父类型的实例；
     * 2. 作为数据仓库，存储数据；
     */
    private static void superDemo() {
        List<Red> fruits = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();

        // 表示可以引用泛型为 RedApple 或其父类的实例
        List<? super RedApple> list = fruits;
        list = apples;

        // 仅能存放最低一级的实例
        list.add(new RedApple());

        /*
         * 不可存放泛型为 Apple 的父类型的实例。
         *
         * List<? super RedApple> 的实例可能是 ArrayList<Apple>、ArrayList<Red> 等等。
         * 存储的时候（Runtime），并不知道 List<? super RedApple> 引用的实例的实际类型，如果往其中存储数据，类型不一定可以匹配，
         * 比如实际是 ArrayList<Red>, 却往其中添加 Apple 实例，显然这是不正确的。
         */
        // list.add(new Apple()); // invalid
        // list.add(new Fruit()); // invalid
    }

    /**
     * 编译时会发生类型擦除，编译器会自动引入一个桥方法（bridge method）
     * @see MyComparator
     */
    private static void bridge() {
        Method[] methods = MyComparator.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isBridge()) {
                System.out.println(method); // public int com.sufanx.basic.generic.bridge.MyComparator.bridge(java.lang.Object,java.lang.Object)
            }
        }
    }
}
