package ie.gmit.mypackage;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Player implements Serializable {
	
	// Add for serialization
		private static final long serialVersionUID = 1L;
	
	//Instance Variables
		private String playerName;
		private String club;
		private String nationality;
		private int age;
		
		//Constructor
		public Player(String playerName) {
			this.playerName = playerName;
		}
		
		public Player(String playerName, String club, String nationality) {
			this(playerName);
			this.club = club;
			this.nationality = nationality;
		}

		public Player(String playerName, String club, String nationality, int age) {
			this(playerName, club, nationality);
			this.age = age;
		}
		
		
		//Getters & Setters
		public String getPlayerName(){
			return playerName;
		}
		
		public void setPlayerName(String playerName){
			this.playerName = playerName;
		}
		
		public String getclub() {
			return club;
		}

		public void setclub(String club) {
			this.club = club;
		}

		public String getnationality() {
			return nationality;
		}

		public void setnationality(String nationality) {
			this.nationality = nationality;
		}

		public int getage() {
			return age;
		}

		public void setage(int age) {
			this.age = age;
		}

		public String findAllFieldValuesInCSVFormat() {
			StringBuilder listOfFields = new StringBuilder();
			
			// determine fields declared in this class only (no fields of superclass)
			Field[] fields = this.getClass().getDeclaredFields();

			// print field names
			for (Field field : fields) {
			//	result.append("  ");
			try {
				// discard static fields
				if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					listOfFields.append( field.get(this) );
					listOfFields.append(",");
			    }	

				} catch (IllegalAccessException ex) {
					System.out.println(ex);
				}
			}
			if( listOfFields.length() > 0 ) {
				listOfFields.setLength( listOfFields.length() - 1 );
			}

			return listOfFields.toString();
		}

}
