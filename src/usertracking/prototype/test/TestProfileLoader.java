package usertracking.prototype.test;

import static org.junit.Assert.*;

import org.junit.Test;

import usertracking.prototype.profile.ProfileDataFromFiles;
import usertracking.prototype.profile.ProfileCache;

public class TestProfileLoader {

	@Test
	public void test() {
		ProfileCache cache = new ProfileDataFromFiles();
		cache.loadProfiles();
		
		
		
		assertEquals(cache.size(), 2.0, 0.0);
	}

}
