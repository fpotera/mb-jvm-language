package sl;

import java.nio.file.Paths;

import resources.ApplicationProperties;

public class Path implements IPath {

	private static Path instance;

	private Path() {
		super();
	}

	public static Path getModule() {
		if (instance == null) {
			synchronized (Path.class) {
				if (instance == null) {
					instance = new Path();
				}
			}
		}
		return instance;
	}

	@Override
	public Object filter(Object... args) {
		java.nio.file.Path path = (java.nio.file.Path) Paths.get((java.lang.String) args[0]);
		return path.normalize().toString();
	}

	@Override
	public Object compare(Object... args) {
		java.nio.file.Path path1 = (java.nio.file.Path) Paths.get((java.lang.String) args[0]);
		java.nio.file.Path path2 = (java.nio.file.Path) Paths.get((java.lang.String) args[1]);
		if (path1.equals(path2)) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object comparefilter(Object... args) {
		java.nio.file.Path path1 = (java.nio.file.Path) Paths.get((java.lang.String) args[0]);
		java.nio.file.Path path2 = (java.nio.file.Path) Paths.get((java.lang.String) args[1]);
		if (path1.normalize().equals(path2.normalize())) {
			return Long.valueOf(1L);
		} else {
			return Long.valueOf(0L);
		}
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_PATHversion();
	}

}
