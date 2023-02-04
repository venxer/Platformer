package com.mygdx.game;

import java.util.ArrayList;

public class Requirements {
    static int[] createArr() {
        int[] arr = new int[1000];
        for (int x = 0; x < arr.length; x++) {
            arr[x] = (int) (Math.random() * 1000);
        }
        return arr;
    }

    public static void merge(int arr[], int l, int m, int r) {
        // Determine sizes of two subarrays to be merged.
        int n1 = m - l + 1;
        int n2 = r - m;
        // Create temporary arrays.
        int left[] = new int[n1];
        int right[] = new int[n2];
        // Copy data to temporary arrays.
        for (int i = 0; i < n1; i++)
            left[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            right[j] = arr[m + 1 + j];
        // Merge the temporary arrays in ascending order.
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }
        // Copy remaining elements of the left array if any.
        while (i < n1) {
            arr[k] = left[i];
            i++;
            k++;
        }
        // Copy remaining elements of the right array if any.
        while (j < n2) {
            arr[k] = right[j];
            j++;
            k++;
        }
    }

    public static void sort(int arr[], int l, int r) {
        if (l < r) {
            // Find the middle.
            int m = (l + r) / 2;
            // Split the array recursively.
            sort(arr, l, m);
            sort(arr, m + 1, r);
            // Merge the sorted halves.
            merge(arr, l, m, r);
        }
    }

    static void printLetters(String word) {
        if (word.length() > 0) {
            printLetters(word.substring(0, word.length() - 1));
            System.out.println(word.substring(word.length() - 1));
        }
    }

    private static int count = 0;

    public static ArrayList<Integer> createArrList() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            arr.add((int) (Math.random() * 10));
        }
        return arr;
    }

    public static int sequentialSearch(int[] arr) {
        int find = (int) (Math.random() * 10);
        for (int x = 0; x < arr.length; x++) {
            count++;
            if (arr[x] == find) {
                return x;
            }
        }
        return -1;
    }

    public static int sequentialSearch(ArrayList<Integer> arr) {
        int find = (int) (Math.random() * 10);
        for (int x = 0; x < arr.size(); x++) {
            count++;
            if (arr.get(x) == find) {
                return x;
            }
        }
        return -1;
    }

    public static int[] insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    public static ArrayList<Integer> insertionSort(ArrayList<Integer> arr) {
        for (int i = 1; i < arr.size(); i++) {
            int key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j) > key) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, key);
        }
        return arr;
    }

    public static int[] selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }
        }
        return arr;
    }

    public static ArrayList<Integer> selectionSort(ArrayList<Integer> arr) {
        for (int i = 0; i < arr.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.size(); j++) {
                if (arr.get(j) < arr.get(minIndex)) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                int temp = arr.get(minIndex);
                arr.set(minIndex, arr.get(i));
                arr.set(i, temp);
            }
        }
        return arr;
    }

    public static int binarySearch(int[] arr, int left, int right, int x) {
        count++;
        if (right >= left) {
            int mid = (left + right) / 2;
            if (arr[mid] == x) {
                return mid;
            } else if (arr[mid] > x) {
                return binarySearch(arr, left, mid - 1, x);
            } else {
                return binarySearch(arr, mid + 1, right, x);
            }
        }
        return -1;
    }

    public static int binarySearch(ArrayList<Integer> myList, int low, int high, int target) {
        count++;
        int mid = (high + low) / 2;
        if (target < myList.get(mid)) {
            return binarySearch(myList, low, mid - 1, target);
        } else if (target > myList.get(mid)) {
            return binarySearch(myList, mid + 1, high, target);
        } else if (myList.get(mid).equals(target)) {
            return mid;
        }
        return -1;
    }

    public static int binarySearchIterative(int[] arr, int find) {
        int low = 0;
        int high = arr.length - 1;
        while (high >= low) {
            count++;
            int mid = (low + high) / 2;
            if (arr[mid] == find) {
                return mid;
            } else if (arr[mid] > find) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public static int binarySearchIterative(ArrayList<Integer> arr, int find) {
        int low = 0;
        int high = arr.size() - 1;
        while (high >= low) {
            count++;
            int mid = (low + high) / 2;
            if (arr.get(mid) == find) {
                return mid;
            } else if (arr.get(mid) > find) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public static void resetCount() {
        count = 0;
    }

    public static int getCount() {
        return count;
    }
}
