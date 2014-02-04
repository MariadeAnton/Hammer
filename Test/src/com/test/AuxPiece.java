package com.test;

import java.util.ArrayList;

import com.test.AuxBasicVariables.Paths;
import com.threed.jpct.Object3D;





public class AuxPiece extends Object3D  {

	
		
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private String name="No Name";
		private ArrayList<Paths>paths=new ArrayList<Paths>();
		private boolean touched=false;
		
		
		public AuxPiece(Object3D obj) 
		{
			super(obj);
	
		}

		

		public boolean isTouched() {
			return touched;
		}

		public void setTouched(boolean touched) {
			this.touched = touched;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void addPath(String name,ArrayList<Point3D>path)
		{
			Paths p=AuxBasicVariables.createPaths();
			p.setName(name);
			p.setPathPoints(path);
			paths.add(p);
		}



		public ArrayList<Paths> getPaths() {
			return paths;
		}



		public void setPaths(ArrayList<Paths> paths) {
			this.paths = paths;
		}
		
		
	
		
		
}

