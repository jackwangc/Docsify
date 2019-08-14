/**
 * 不用除法，乘法实现除法
 */

// 求 12/2 的商，转换成加法，即：
// 12 = 2 + 2 + 2 + 2 + 2 + 2
// 共累加6次，即商为6。

// 通过观察发现，每次都是累加同一个数字，能不能减少加法运算，快速逼近12？答案对除数进行移位操作。

// 轮次  |     移位        |    商
// 1        4 (2 <<= 1)       2 (1 <<= 1)
// 2        8 (4 <<= 1)       4 (2 <<= 1)
// 3        16 (8 <<= 1) 退出循环

// 经过两轮循环，除数为8，商为4，余数为12 - 8 = 4。然后继续使用相同的方法计算4/2的商。

// 轮次  |     移位    |    商
// 1       4 (2 <<= 1)     2 (1 <<= 1)
// 2       8 (4 <<= 1)  退出循环

// 则12/2商为 4 + 2 = 6。通过此算法，共减少了3次加法运算。
// 除法的本质，被除数里面包含多少个这样的数
public class Solution {
    public int divide(int dividend, int divisor) {
        long a = Math.abs((long)dividend);
        
        // ref : http://blog.csdn.net/kenden23/article/details/16986763
        // Note: 在这里必须先取long再abs，否则int的最小值abs后也是原值
        long b = Math.abs((long)divisor);
        
        int ret = 0;
        // 这里必须是= 因为相等时也可以减
        while (a >= b) {
            // cnt 减去的个数
            // 判断条件，不能再减的时候，还原 除数的值重新循环
            for (long deduce = b, cnt = 1; a >= deduce; deduce <<= 1, cnt <<= 1) {
                // 余数减去
                a -= deduce;
                // 统计减去的个数
                ret += cnt;
            }
        }
        
        // 获取符号位。根据除数跟被除数的关系来定
        return (dividend > 0) ^ (divisor > 0) ? -ret: ret;
    }
}

/**
 *  两数相乘
 *  整数n乘以2k == n << k
 *  k0×2^0+k1×2^1+...+km×2m的形式。（其中：ki∈{0, 1}, i∈{0, 1, ... , m}, 32位int型m = 30, 64位int型m = 62）
 */