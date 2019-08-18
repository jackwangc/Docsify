import javafx.scene.chart.PieChart.Data;

/**
 * 数组中的逆序对
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组，求出这个数组中的逆序对的总数。
 * 归并排序活用
 */
class Solution {
    public void solution(int[] arr) {
        
    }
    public void sort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) << 1;
            sort(arr, left, mid);
            sort(arr, mid + 1, right);
            merge(arr, left, mid, right);;
        }
    }
    public void merge(int[] arr, int left, int center, int right) {
        int[] tempArr = new int[arr.length];
        int mid = center + 1;
        int third = left;
        int temp = left;
        while (left <= center && mid <= right) {
            if (arr[left] <= arr[right]) {
                tempArr[third++] = arr[left++];
                // 统计逆序对的个数
                // inte ++
            } else {
                tempArr[third++] = arr[mid++];
            }
        }
        while (mid <= right) {
            tempArr[third++] = arr[mid++];
        }
        while (left <= center) {
            tempArr[third++] = arr[left++];
        }
        while (temp <= right) {
            arr[temp] = tempArr[temp++];
        }
    }
}
