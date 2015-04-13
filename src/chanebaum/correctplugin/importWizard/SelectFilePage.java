/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package chanebaum.correctplugin.importWizard;

import java.io.File;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class SelectFilePage extends WizardPage {
	
	private ImportWizard wizard;
	
	private FileFieldEditor editor;
	private Composite container;
	
	public SelectFilePage(String pageName, ImportWizard wizard) {
		super(pageName);
		this.wizard = wizard;
		setTitle(pageName);
		setDescription("Import a file from the local file system into the workspace");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    
	    editor = new FileFieldEditor("fileSelect","Select File: ",container);
	    String[] extensions = new String[] { "*.zip" };
		editor.setFileExtensions(extensions);
		editor.getTextControl(container).addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				checkFile(editor);
			}

		});
		
	    // required to avoid an error in the system
	    setControl(container);
	    setPageComplete(false);

		
		//		Composite fileSelectionArea = new Composite(parent, SWT.NONE);
//		GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL
//				| GridData.FILL_HORIZONTAL);
//		fileSelectionArea.setLayoutData(fileSelectionData);
//
//		GridLayout fileSelectionLayout = new GridLayout();
//		fileSelectionLayout.numColumns = 3;
//		fileSelectionLayout.makeColumnsEqualWidth = false;
//		fileSelectionLayout.marginWidth = 0;
//		fileSelectionLayout.marginHeight = 0;
//		fileSelectionArea.setLayout(fileSelectionLayout);
//		
//		editor = new FileFieldEditor("fileSelect","Select File: ",fileSelectionArea); //NON-NLS-1 //NON-NLS-2
//		editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener(){
//			public void modifyText(ModifyEvent e) {
//				
//			}
//		});
//		String[] extensions = new String[] { "*.zip" }; //NON-NLS-1
//		editor.setFileExtensions(extensions);
//		fileSelectionArea.moveAbove(null);
	}
	
	private void checkFile(FileFieldEditor editor) {
		File f = new File(editor.getTextControl(container).getText());
		if(f.exists()) {
			wizard.setZipfile(f);
			setPageComplete(true);
		}
	}
}
