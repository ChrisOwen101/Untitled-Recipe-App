package com.marche.recipely.recipes;

import java.util.ArrayList;


public class Step {

	public String step = null;
	public ArrayList<Integer> timer = null;
	
	public Step(String step, ArrayList<Integer> timer){
		this.step = step;
		this.timer = timer;
	}
}
