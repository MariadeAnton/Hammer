package com.test;

import java.util.ArrayList;

import rajawali.Object3D;

import com.test.AuxBasicVariables.TriplePoint;





public class AuxPiece  {

	
		
		private Object3D obj3D=null;
		private String name="No Name";
		private TriplePoint positionXYZ=AuxBasicVariables.createTriplePoint(0,0,0);
		private ArrayList<TriplePoint> path=new ArrayList<TriplePoint>();
		boolean touched=false;
		
		
		public AuxPiece() 
		{
			super();
			
		}
		
		public AuxPiece(Object3D obj3d) {
			
			super();
			obj3D = obj3d;
		}
		
		public void copyPiece(AuxPiece piece)
		{
			obj3D=piece.getModel3D();
			name=piece.getName();
			path=piece.getPath();
			touched=piece.isTouched();
		}
		public Object3D getModel3D() {
			return obj3D;
		}
		public void setModel3D(Object3D o) {
			obj3D = o;
		}
		public ArrayList<TriplePoint> getPath() {
			return path;
		}
		public void setPath(ArrayList<TriplePoint> path) {
			this.path = path;
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

		public TriplePoint getPositionXYZ() {
			return positionXYZ;
		}

		public void setPositionXYZ(TriplePoint positionXYZ) {
			this.positionXYZ = positionXYZ;
		}
		
		
		
}

