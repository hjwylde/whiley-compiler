package wyil.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wyil.lang.ModuleID;
import wyil.lang.PkgID;

/**
 * A WDirectory represents a directory on the file system. Using this, we can
 * list items on the path and see what is there.
 * 
 * @author djp
 * 
 */
public class WDirectory implements WSystem.PackageItem {
	private final PkgID pid;
	private final File dir;
	private final WSystem root;
	private ArrayList<WSystem.Item> contents;	

	public WDirectory(PkgID pid, File dir, WSystem root) {
		this.pid = pid;
		this.dir = dir;		
		this.root = root;
	}

	public PkgID id() {
		return pid;
	}

	public List<WSystem.Item> list() throws IOException {
		if (contents == null) {
			contents = new ArrayList<WSystem.Item>();
			for (File file : dir.listFiles()) {				
				if (file.isDirectory()) {
					// TODO: need to modify filter
					contents.add(new WDirectory(pid.append(file.getName()),
							file, root));
				} else if (file.isFile()) {						
					ModuleReader reader = root.getModuleReader(file
							.getName());
					if (reader != null) {
						// TODO: module name is completely broken?
						contents.add(new WFile(new ModuleID(pid, file
								.getName()), file, reader));
					}
				}
			}
		}
		return Collections.unmodifiableList(contents);
	}

	public void close() throws IOException {
		// not needed
	}

	public void refresh() throws IOException {
		contents = null;
	}
}