package wybs.lang;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import wyfs.lang.Content;
import wyfs.lang.Path;

public interface Build {

	/**
	 * <p>
	 * Represents a top-level entity responsible for managing everything related
	 * to a given build. A build project provides a global "namespace" where
	 * named objects (e.g. source files, binary files) reside and/or are
	 * created. A build project also contains one or more build rules which
	 * determines how source files are transformed.
	 * </p>
	 * <p>
	 * For a given set of source files, a build project defines (in an abstract
	 * sense) a "build" --- that is, a specific plan of construction starting
	 * from one or more source files and producing one or more binary files.
	 * This is abstract because, in the normal course of events, the build is
	 * only known "after the fact"; that is, once all binary files are
	 * generated. This is necessary because it can be difficult to predict ahead
	 * of time what binary files will be generated from a given source file.
	 * </p>
	 * <p>
	 * Build projects have the opportunity to record the dependencies created
	 * during a build. That is, a binary file depends on those source file(s)
	 * required to build it. Recording this information is necessary if one
	 * wants to perform an incremental (re)compilation. That is, using such
	 * dependency information, one can avoid recompiling all source files from
	 * scratch.
	 * </p>
	 * <p>
	 * Finally, build projects may choose to record other information (e.g.
	 * timing and other statistical information) and/or employ different
	 * techniques (e.g. parallel builds).
	 * </p>
	 * 
	 * @author David J. Pearce
	 * 
	 */
	public interface Project {

		/**
		 * Check whether or not a given entry is contained in this root;
		 * 
		 * @param entry
		 * @return
		 */
		public boolean contains(Path.Entry<?> entry) throws IOException;

		/**
		 * Check whether or not a given entry and content-type is contained in
		 * this root.
		 * 
		 * @throws IOException
		 *             --- in case of some I/O failure.
		 */
		public boolean exists(Path.ID id, Content.Type<?> ct)
				throws IOException;

		/**
		 * Get the entry corresponding to a given ID and content type. If no
		 * such entry exists, return null.
		 * 
		 * @param id
		 *            --- id of module to lookup.
		 * @throws IOException
		 *             --- in case of some I/O failure.
		 */
		public <T> Path.Entry<T> get(Path.ID id, Content.Type<T> ct)
				throws IOException;

		/**
		 * Get all objects matching a given content filter stored in this root.
		 * In the case of no matches, an empty list is returned.
		 * 
		 * @throws IOException
		 *             --- in case of some I/O failure.
		 * 
		 * @param ct
		 * @return
		 */
		public <T> List<Path.Entry<T>> get(Content.Filter<T> ct)
				throws IOException;

		/**
		 * Identify all entries matching a given content filter stored in this
		 * root. In the case of no matches, an empty set is returned.
		 * 
		 * @throws IOException
		 *             --- in case of some I/O failure.
		 * 
		 * @param filter
		 *            --- filter to match entries with.
		 * @return
		 */
		public <T> Set<Path.ID> match(Content.Filter<T> filter)
				throws IOException;
	}

	/**
	 * <p>
	 * A build rule is an abstraction describing how a set of one or more source
	 * files should be compiled. Each build rule is associated with a builder
	 * responsible for compiling matching files, a destination root and a
	 * mechanism for "matching source" files. For example, we could view a build
	 * rule like this:
	 * </p>
	 * 
	 * <pre>
	 * WhileyCompiler :: src/:whiley/lang/*.whiley => bin/
	 * </pre>
	 * 
	 * <p>
	 * Here, the builder is the <code>WhileyCompiler</code>, whilst the
	 * destination root is "bin/". Source files are taken from the root "src/"
	 * matching the regex "whiley/lang/*.whiley".
	 * </p>
	 * 
	 * <p>
	 * Different build rules are free to implement the "matching" mechanism as
	 * they wish. Typically, one wants a generic way to describe a group of
	 * source files using wildcards (often called the "includes"). Occasionally,
	 * one also wants a way to exclude one or more files (oftern called the
	 * "excludes").
	 * </p>
	 * 
	 * @author David J. Pearce
	 * 
	 */
	public interface Rule {

		/**
		 * <p>
		 * Apply this rule to a given compilation group, producing a set of
		 * generated or modified files. This set may be empty if the rule does
		 * not match against any source file in the group.
		 * </p>
		 * 
		 * @param The
		 *            set of files currently being compiled.
		 * @return The set of files generated by this rule (which may be empty,
		 *         but cannot be <code>null</code>).
		 * @throws IOException
		 */
		public Set<Path.Entry<?>> apply(Collection<? extends Path.Entry<?>> group)
				throws IOException;
	}
}
