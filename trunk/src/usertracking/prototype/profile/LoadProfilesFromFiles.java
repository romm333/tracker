package usertracking.prototype.profile;

import java.io.File;

public class LoadProfilesFromFiles extends ProfileCache {

	private static final long serialVersionUID = -4318685451015038353L;

	@Override
	public void loadProfiles() {
		String profileFolder = "profiles";
		
		File folder = new File(profileFolder);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
	}

	@Override
	public ProfileCache getProfiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
