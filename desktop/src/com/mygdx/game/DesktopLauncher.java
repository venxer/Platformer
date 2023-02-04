package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Advent");
		config.setForegroundFPS(144);
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new Advent(), config);

		// System.out.println("ARRAY:");
		// int[] arr = Requirements.createArr();
		// System.out.println(Arrays.toString(arr));

		// Requirements.sequentialSearch(arr);
		// System.out.println("Seq Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// arr = Requirements.createArr();
		// Requirements.selectionSort(arr);

		// int random = (int) (Math.random() * 10);
		// Requirements.binarySearch(arr, 0, arr.length, random);
		// System.out.println("Binary Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// arr = Requirements.createArr();
		// Requirements.selectionSort(arr);

		// Requirements.binarySearchIterative(arr, random);
		// System.out.println("Binary Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// System.out.println("ARRAYLIST:");
		// ArrayList<Integer> arrList = Requirements.createArrList();
		// System.out.println(arrList);

		// Requirements.sequentialSearch(arrList);
		// System.out.println("Seq Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// arrList = Requirements.createArrList();
		// Requirements.selectionSort(arrList);

		// int random2 = (int) (Math.random() * 10);
		// Requirements.binarySearch(arrList, 0, arrList.size(), random2);
		// System.out.println("Binary Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// arrList = Requirements.createArrList();
		// Requirements.selectionSort(arrList);

		// Requirements.binarySearchIterative(arrList, random2);
		// System.out.println("Binary Search Count: " + Requirements.getCount());
		// Requirements.resetCount();

		// Requirements.printLetters("Testing123");
		// int[] intArr = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		// System.out.println(Requirements.binarySearch(intArr, 0, intArr.length - 1, 4));

		// ArrayList<Integer> intArrList = new ArrayList<>();
		// for (int x = 0; x < 10; x++) {
		// 	intArrList.add(x);
		// }
		// System.out.println(Requirements.binarySearch(intArrList, 0, intArrList.size() - 1, 4));

		// int[] arr2 = Requirements.createArr();
		// System.out.println(Arrays.toString(arr));
		// Requirements.sort(arr, 0, arr.length - 1);

		// int random3 = (int) (Math.random() * 20);
		// System.out.println(Arrays.toString(arr));
		// System.out.println(random3);

		// System.out.println(Requirements.binarySearch(arr, 0, arr.length - 1, random));
	}
}
