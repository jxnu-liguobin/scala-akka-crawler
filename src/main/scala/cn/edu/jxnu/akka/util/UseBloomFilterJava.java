package cn.edu.jxnu.akka.util;


import com.gio.bloomfilter.ScalaBloomFilter;

public class UseBloomFilterJava {

    final static int elementCount = 50000;// 测试元素个数
    final static double falsePositiveProbability = 0.001; //假阳性概率

    public static void main(String[] args) {
        ScalaBloomFilter<String> bf = new ScalaBloomFilter<>(falsePositiveProbability, elementCount);
        bf.add("test");
        if (bf.contains("test")) {
            System.out.println("存在元素: test");
            System.out.println("根据公式计算假阳性的预期概率: " + bf.expectedFalsePositiveProbability());
        }
        if (bf.contains("test1")) {
            System.out.println("There was a test1.");
        }
    }
}