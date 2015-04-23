package chanebaum.correctplugin.importWizard;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.osgi.service.prefs.BackingStoreException;

import chanebaum.correctplugin.data.Group;
import chanebaum.correctplugin.data.PrefIO;

public class ConfigureGroupsPage extends WizardPage {
	
	private static final String GROUP = "Group ";

	private List listGroup;
	private List listMember;
	
	private Composite parent;
	
	private ImportWizard wizard;
	private ArrayList<Group> groups = new ArrayList<Group>();
	
	protected ConfigureGroupsPage(String pageName, ImportWizard wizard) {
		super(pageName);
		this.wizard = wizard;
		setTitle(pageName);
		setDescription("Configure your groups and their members");
	}

	@Override
	public void createControl(Composite parent) {
		this.parent = parent;
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		
		Composite compGroup = new Composite(comp, SWT.NONE);
		compGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compGroup.setLayout(new GridLayout(1, false));
		{
			Button buttonGroup = new Button(compGroup, SWT.PUSH);
			buttonGroup.setText("Add Group");
			buttonGroup.addListener(SWT.Selection, new ListenerButtonGroup());
			GridData buttonGroupData = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
			buttonGroupData.heightHint = 20;
			buttonGroup.setLayoutData(buttonGroupData);
			
			listGroup = new List(compGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			listGroup.addSelectionListener(new ListenerGroupSelection());
			GridData listGroupData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			listGroup.setLayoutData(listGroupData);	
		}
		
		Composite compMember = new Composite(comp, SWT.NONE);
		compMember.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compMember.setLayout(new GridLayout(1, false));
		{
			Button buttonMember = new Button(compMember, SWT.PUSH);
			buttonMember.setText("Add Member");
			buttonMember.addListener(SWT.Selection, new ListenerButtonMember());
			GridData buttonMemberData = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
			buttonMemberData.heightHint = 20;
			buttonMember.setLayoutData(buttonMemberData);
			
			listMember = new List(compMember, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			GridData listMembereData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			listMember.setLayoutData(listMembereData);
		}
		
		setControl(comp);
		setPageComplete(false);
		
		loadFromPref();
	}
	
	private void updateGroups() {
		saveToPref();
		wizard.setGroups(groups);
		
		if(groups.size() > 0) setPageComplete(true);
	}
	
	private void loadFromPref() {
		try {
			groups = PrefIO.readConfig();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		for(Group g: groups) {
			listGroup.add(GROUP + g.getGroupID());
		}
		if(listGroup.getItemCount() > 0) {
			listGroup.select(1);
			updateMemberList();
		}
		updateGroups();
	}
	
	private void saveToPref() {
		try {
			PrefIO.writeConfig(groups);
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	private class ListenerButtonGroup implements Listener {

		@Override
		public void handleEvent(Event arg0) {
			GroupIDDialog dialog = new GroupIDDialog(parent.getShell());
			dialog.create();
			if(dialog.open() == Window.OK) {
				listGroup.add(GROUP + dialog.getGroupID());
				groups.add(new Group(dialog.getGroupID()));
			}
			
			updateGroups();
		}
		
	}
	
	private class ListenerButtonMember implements Listener {

		@Override
		public void handleEvent(Event arg0) {
			if(listGroup.getSelectionIndex() == -1) {
				MessageDialog dialog = new MessageDialog(parent.getShell(), "No group selected!", null,
					    "Please select a group", MessageDialog.ERROR, new String[] { "OK" }, 0);
				dialog.open();
			} else {
				Group g = getGroup(listGroup.getSelectionIndex());
				MemberDialog dialog = new MemberDialog(parent.getShell());
				if(dialog.open() == Window.OK) {
					g.addMember(dialog.getName());
					updateMemberList();
				}
			}
			
			updateGroups();
		}
		
	}
	
	private void updateMemberList() {
		Group g = getGroup(listGroup.getSelectionIndex());
		listMember.removeAll();
		ArrayList<String> members = g.getGroupMembers();
		for(String member: members) {
			listMember.add(member);
		}
	}
	
	private Group getGroup(int index) {
		int id = Integer.parseInt(listGroup.getItem(index).substring(GROUP.length()));
		for(Group g: groups) {
			if(g.getGroupID() == id) return g;
		}
		return null;
	}
	
	private class ListenerGroupSelection implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {}

		@Override
		public void widgetSelected(SelectionEvent e) {
			updateMemberList();
		}
		
	}

}
