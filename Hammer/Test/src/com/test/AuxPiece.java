package com.test;

import java.util.ArrayList;

import rajawali.BaseObject3D;

import com.test.AuxBasicVariables.TriplePoint;





public class AuxPiece  {

	
		
		private BaseObject3D obj3D=null;
		private String name;
		private TriplePoint positionXYZ=AuxBasicVariables.createTriplePoint(0,0,0);
		private ArrayList<TriplePoint> path=new ArrayList<TriplePoint>();
		boolean touched=false;
		
		
		public AuxPiece() 
		{
			super();
			
		}
		
		public AuxPiece(BaseObject3D obj3d) {
			
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
		public BaseObject3D getModel3D() {
			return obj3D;
		}
		public void setModel3D(BaseObject3D o) {
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

