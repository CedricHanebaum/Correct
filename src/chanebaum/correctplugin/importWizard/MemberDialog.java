package chanebaum.correctplugin.importWizard;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MemberDialog extends TitleAreaDialog {

	private Text textMemberFirstName;
	private Text textMemberLastName;
	private Text textNameControl;
	
	private String firstName;
	private String lastName;

	public MemberDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createFirstName(container);
		createLastName(container);
		createNameControl(container);

		return area;
	}

	private void createNameControl(Composite container) {
		Label lbtControl = new Label(container, SWT.NONE);
		lbtControl.setText("Control");

		GridData dataControl = new GridData();
		dataControl.grabExcessHorizontalSpace = true;
		dataControl.horizontalAlignment = GridData.FILL;

		textNameControl = new Text(container, SWT.BORDER);
		textNameControl.setLayoutData(dataControl);
		textNameControl.setEditable(false);
	}

	private void createFirstName(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("FirstName");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		textMemberFirstName = new Text(container, SWT.BORDER);
		textMemberFirstName.setLayoutData(dataFirstName);
		textMemberFirstName.addModifyListener(new NameModifyListener());
	}

	private void createLastName(Composite container) {
		Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Last Name");

		GridData dataLastName = new GridData();
		dataLastName.grabExcessHorizontalSpace = true;
		dataLastName.horizontalAlignment = GridData.FILL;

		textMemberLastName = new Text(container, SWT.BORDER);
		textMemberLastName.setLayoutData(dataLastName);
		textMemberLastName.addModifyListener(new NameModifyListener());
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private void saveInput() {
		firstName = textMemberFirstName.getText();
		lastName = textMemberLastName.getText();
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getName() {
		return lastName + "_" + firstName;
	}
	
	private class NameModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			saveInput();
			textNameControl.setText(getName());
		}
		
	}
	
}
