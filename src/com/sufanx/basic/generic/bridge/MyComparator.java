package com.sufanx.basic.generic.bridge;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * 在编译时会发生类型擦除，编译后的{@link Comparator}中，泛型被Object替换。
 * MyComparator 实现了{@link Comparator}，所以需要重写 compare(Object a, Object b)，
 * 但是观察重写的方法可以发现，其实我们并没有真正“重写” compare(Object a, Object b)方法，
 * 我们“重写”的是 Comparator 中不存在的方法 compare(Integer a, Integer b)。
 * <p>
 * 不难看出，泛型擦除和继承存在冲突。但是我们的代码并没有报错，并且可以运行，这是为什么呢？
 * 其实在编译代码的时候，编译器引入一个桥方法，用来帮我们调用真正的目标方法{@link MyComparator#compare(Integer, Integer)}，
 * 这个桥方法才是真正的实现重写方法。多态的时候，会先调用桥方法，通过桥方法调用目标方法。
 * 我们可以通过反射或者反编译找到生成的桥方法，也可以在Debug的时候看到桥方法的调用堆栈
 *
 * @see Method#isBridge()
 */
public class MyComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }


    // Bridge Method
    // public int compare(Object a, Object b) {
    //     return compare((Integer)a, (Integer)b);
    // }
}