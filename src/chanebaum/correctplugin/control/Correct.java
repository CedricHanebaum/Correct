package chanebaum.correctplugin.control;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import chanebaum.correctplugin.data.Group;

public class Correct {

	public static void importFile(File zipfile, ArrayList<Group> groups) {
		// Create Project
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("U" + String.format("%02d", getFileNumber(zipfile)));
		IJavaProject javaProject = null;
		IPackageFragmentRoot srcFolder = null;
		
		try {
			project.create(progressMonitor);
			project.open(progressMonitor);
		
			
			//set the java project nature
		    IProjectDescription description = project.getDescription();
		    description.setNatureIds(new String[] { JavaCore.NATURE_ID});
		    project.setDescription(description, null);
		 
		    //create java project
		    javaProject = JavaCore.create(project);
		 
		    //add bin/ouput folder
		    IFolder binFolder = project.getFolder("bin");
		    binFolder.create(false, true, null);
		    javaProject.setOutputLocation(binFolder.getFullPath(), null);
		 
		    //add libs to project class path
		    List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		    IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		    LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		    for (LibraryLocation element : locations) {
		        entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		    }
		     
		    javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
		 
		    //create source folder
		    IFolder folder = project.getFolder("src");
		    folder.create(false, true, null);
		    srcFolder = javaProject.getPackageFragmentRoot(folder);
		 
		    IPackageFragmentRoot srcRoot = javaProject.getPackageFragmentRoot(folder);
		    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		    newEntries[oldEntries.length] = JavaCore.newSourceEntry(srcRoot.getPath());
		    javaProject.setRawClasspath(newEntries, null);
		 
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		// End Create Project

		File tmpDir = extractFile(zipfile);
		
		for(Group g: groups) {
			IPackageFragment fragment = null;
			try {
				fragment = srcFolder.createPackageFragment("g" + g.getGroupID(), true, null);
			} catch (JavaModelException e) { e.printStackTrace(); }
			
			File f = g.findHomework(tmpDir);
			if(f != null) {
				File workspaceDir = root.getLocation().toFile();
				ZipExtractor.extractFolder(f, workspaceDir.toString() + fragment.getPath().toString());
				injectPackage(new File(workspaceDir.toString() + fragment.getPath().toString()), "g" + g.getGroupID());
			}
		}
		
		deleteDir(tmpDir);
		
	}
	
	private static void injectPackage(File dir, String packageName) {
		for(File f: dir.listFiles()) {
			if(f.getName().endsWith(".java")) {
				RandomAccessFile file;
				try {
					file = new RandomAccessFile(f.getAbsolutePath(), "rws");
				    byte[] text = new byte[(int) file.length()];
				    file.readFully(text);
				    file.seek(0);
				    file.writeBytes("package " + packageName + ";\n");
				    file.write(text);
				    file.close();
				} catch (IOException e) { e.printStackTrace(); }
			}
		}
	}
	
	private static File extractFile(File zipfile) {
		File workdir = new File("U" + String.format("%02d", getFileNumber(zipfile)));
		ZipExtractor.extractFolder(zipfile, workdir.getName());
		return workdir;
	}
	
	private static int getFileNumber(File f) {
		String numberS = f.getName().substring(12, 14);
		return Integer.parseInt(numberS);
	}
	
	public static void deleteDir(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteDir(c);
		}
		if (!f.delete())
			throw new RuntimeException("Failed to delete file: " + f);
	}
	
}
