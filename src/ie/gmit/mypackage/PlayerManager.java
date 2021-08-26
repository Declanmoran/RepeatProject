package ie.gmit.mypackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Create Player ArrayList
		private List<Player> playerList = null;
		
		//Constructor
		public PlayerManager (){
			playerList = new ArrayList<Player>();
		}
		
		//Player Add Method
		public boolean addPlayer (Player playerObject) {
			// Loop over all Players and check if already on List
			for (Player player : playerList) { // For each player in the playerList
				if (player.getPlayerName().equals(playerObject.getPlayerName())) {
					System.out.println("Player NOT Added to Player List. Player already on List!");
					return false;
				}
			}
			
			return playerList.add(playerObject);
		}
		
		
		//Player Remove Method
		public boolean removePlayer (Player playerObject) {
			return playerList.remove(playerObject);
		}
		
		public Player deletePlayerByAge(int number) {
			try {
				return playerList.remove(number - 1);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("There are " + playerList.size()
						+ "players on the list. Please pick a number from 1 to " + playerList.size());
			}
			return null;
		}
		
		public int findTotalPlayers() {
			// returns the current number of Players in the ArrayList
			return playerList.size();
		}
		
		public String listAllPlayerss() {
			// Create a StringBuilder object
			StringBuilder sb = new StringBuilder();
			int counter = 1;

			sb.append(String.format("%-20s%-20s%-20s%-20s%-20s\n", "Name", "Club", "Nationality", "Age"));
			sb.append(String.format("===============================================================\n"));

			// sb.append("Name\t\t\t\t\tClub\t\t\tNationality\t\t\tAge\n");
			// sb.append("----------------------------------------------------------\n");
			for (Player player : this.playerList) {
				sb.append(counter + ": " + player.findAllFieldValuesInCSVFormat().replace(",", "\t\t") + "\n");
				// sb.append(String.format("%-20s%-20\n",counter,
				// player.findAllFieldValuesInCSVFormat().replace(",", "\t\t") + "\n"));
				counter++;
			}
			
			return sb.toString();
		}
		
		public void loadPlayersFromCSVFile(File playerCSVFile) {
			FileReader playerCSVFileReader = null;
			BufferedReader bufferedPlayerCSVFileReader = null;
			String bufferData = null; // Used to store lines of data we read from the buffer

			// Create a file reader
			try {
				playerCSVFileReader = new FileReader(playerCSVFile);
				// Add a buffer to the file reader
				bufferedPlayerCSVFileReader = new BufferedReader(playerCSVFileReader);
				// Read first line of file and discard it. It contains column headers.
				bufferedPlayerCSVFileReader.readLine();

				while ((bufferData = bufferedPlayerCSVFileReader.readLine()) != null) {
					// System.out.println(bufferData);
					String[] playerFieldValues = bufferData.split(",");
					// System.out.println(Arrays.toString(playerFieldValues));
					Player newPlayer = new Player(playerFieldValues[0], playerFieldValues[1], playerFieldValues[2],
							Integer.parseInt(playerFieldValues[3]));
					this.addPlayer(newPlayer); // Add player to the playerList
				}
				System.out.println("Loaded Player List from CSV file successfully!");
			} catch (FileNotFoundException fnfExc) {
				fnfExc.printStackTrace();
			} catch (IOException IOExc) {
				IOExc.printStackTrace();
			} finally {
				try {
					if (playerCSVFileReader != null) {
						// Flushes buffer, which transfers buffer data to the file, then closes buffer.
						bufferedPlayerCSVFileReader.close();
						// Close the file reader stream
						playerCSVFileReader.close();
					}
				} catch (IOException IOExc) {
					IOExc.printStackTrace();
				} // End catch
			} // End finally
		} // End load method
		
		public void savePlayersToCSVFile(File playerDBFile) {
			FileWriter playerFileWriterStream = null;
			BufferedWriter bufferedplayerFileWriterStream = null;
			try {
				playerFileWriterStream = new FileWriter(playerDBFile);
				bufferedplayerFileWriterStream = new BufferedWriter(playerFileWriterStream);
				bufferedplayerFileWriterStream.write("Name,Club,Nationality,Age" + "\n");

				// Write out player data from playerList to buffer and flush it to CSV file
				for (Player playerObject : playerList) {
					bufferedplayerFileWriterStream.write(playerObject.getPlayerName() + "," + playerObject.getPlayerName()
							+ "," + playerObject.getclub() + "," + playerObject.getage() + "\n");
					// bufferedplayerFileWriterStream.write(playerObject.findAllFieldValuesInCSVFormat()
					// + "\n");
					bufferedplayerFileWriterStream.flush(); // Flushes buffer which transfers buffer data to the file
				}
				System.out.println("Saved Players List to CSV file successfully!");
			} catch (FileNotFoundException fnfExc) {
				fnfExc.printStackTrace();
			} catch (IOException IOExc) {
				IOExc.printStackTrace();
			} finally {
				try {
					if (playerFileWriterStream != null) {
						// Close buffer
						bufferedplayerFileWriterStream.close();
						// Close file writer
						playerFileWriterStream.close();
					}
				} catch (IOException IOExc) {
					IOExc.printStackTrace();
				} // End catch
			} // End finally
		} // End Save method

		public PlayerManager loadPlayerManagerObjectFromFile(File playerObjectsFile) {
			FileInputStream fis = null;
			ObjectInputStream ois = null;
			PlayerManager sm = null;
			try {
				fis = new FileInputStream(playerObjectsFile);
				ois = new ObjectInputStream(fis);
				sm = (PlayerManager) ois.readObject();
			} catch (FileNotFoundException e) {
				System.out.println("What just happened...");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (ois != null) {
						// Close ObjectOutputStream
						ois.close();
					}
					if (fis != null) {
						// Close FileOutputStream
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} // End catch
			} // End finally
			if (sm == null) {
			}
			return sm; // Returns null if no object is read in.
		}

		public void savePlayerManagerObjectToFile(File playerObjectsFile) {
			FileOutputStream fos = null;
			ObjectOutputStream oos = null;
			try {
				fos = new FileOutputStream(playerObjectsFile);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (oos != null) {
						// Close ObjectOutputStream
						oos.close();
					}
					if (fos != null) {
						// Close FileOutputStream
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} // End catch
			} // End finally

		}

	
}//End of class
