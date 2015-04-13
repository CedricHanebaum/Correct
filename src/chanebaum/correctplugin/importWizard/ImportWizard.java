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
import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import chanebaum.correctplugin.control.Correct;
import chanebaum.correctplugin.data.Group;

public class ImportWizard extends Wizard implements IImportWizard {

	private File zipfile;
	private ArrayList<Group> groups;

	private SelectFilePage filePage;
	private ConfigureGroupsPage groupPage;

	public ImportWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("File Import Wizard");
		setNeedsProgressMonitor(true);
		filePage = new SelectFilePage("Select File", this);
		groupPage = new ConfigureGroupsPage("Configure Groups", this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		super.addPages();
		addPage(filePage);
		addPage(groupPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		Correct.importFile(zipfile, groups);
		return true;
	}
	
	public void setZipfile(File f) {
		zipfile = f;
	}
	
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}
	
}
