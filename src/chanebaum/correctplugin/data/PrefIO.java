package chanebaum.correctplugin.data;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class PrefIO {
	
	private static String GROUPID = "node.group";
	private static String MEMBERCOUNT = "group.membercount";
	private static String MEMBER = "group.member";
	
	public static void writeConfig(ArrayList<Group> groups) throws BackingStoreException {
		IEclipsePreferences pref = getPreferences();
		pref.clear();
		for(Group g: groups) {
			Preferences prefG = pref.node(GROUPID + g.getGroupID());
			
			ArrayList<String> members = g.getGroupMembers();
			prefG.putInt(MEMBERCOUNT, members.size());
			for(int i = 0; i < members.size(); ++i) {
				prefG.put(MEMBER + i, members.get(i));
			}
		}
		pref.flush();
	}
	
	public static ArrayList<Group> readConfig() throws BackingStoreException {
		ArrayList<Group> groups = new ArrayList<Group>();
		IEclipsePreferences pref = getPreferences();
		
		for(String child: pref.childrenNames()) {
			Preferences prefG = pref.node(child);
			Group g = new Group(Integer.parseInt(child.substring(GROUPID.length())));
			for(int i = 0; i < prefG.getInt(MEMBERCOUNT, 0); ++i) {
				g.addMember(prefG.get(MEMBER + i, "default"));
			}
			groups.add(g);
		}
		
		return groups;
	}
	
	private static IEclipsePreferences getPreferences() {
		return InstanceScope.INSTANCE.getNode("chanebaum.correct");
	}
	
}
