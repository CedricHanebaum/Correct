package chanebaum.correctplugin.importWizard;

import java.util.Scanner;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GroupIDDialog extends TitleAreaDialog {

	private Text textGroupID;
	private int groupID;

	public GroupIDDialog(Shell parentShell) {
		super(parentShell);
		setTitle("GroupID");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createGroupID(container);

		return area;
	}

	private void createGroupID(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Group ID");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		textGroupID = new Text(container, SWT.BORDER);
		textGroupID.setLayoutData(dataFirstName);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		if (checkInput()) {
			saveInput();
			super.okPressed();
		} else {
			MessageDialog dialog = new MessageDialog(this.getShell(), "No Integer!", null,
				    "Please insert an Integer", MessageDialog.ERROR, new String[] { "OK" }, 0);
			dialog.open();
		}
	}

	private boolean checkInput() {
		Scanner sc = new Scanner(textGroupID.getText().trim());
		final int radix = 10;
		if(!sc.hasNextInt(radix)) {
			sc.close();
			return false;
		}
		sc.nextInt(radix);
		boolean ret = !sc.hasNext();
		sc.close();
		return ret;
	}

	private void saveInput() {
		groupID = Integer.parseInt(textGroupID.getText());
	}

	public int getGroupID() {
		return groupID;
	}

}
