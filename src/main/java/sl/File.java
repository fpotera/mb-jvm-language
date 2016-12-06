package sl;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import resources.ApplicationProperties;

/**
 * The SL_FILE module contains file handling routines.
 * 
 * @author Iulian Enache
 */
public class File implements IFile {

	private static File instance;

	private File() {
		super();
	}

	public static File getModule() {
		if (instance == null) {
			synchronized (File.class) {
				if (instance == null) {
					instance = new File();
				}
			}
		}
		return instance;
	}

	@Override
	public Object deletedirectorytree(Object... args) {
		Path path = FileSystems.getDefault().getPath((java.lang.String) args[0]);
		try {
			deleteFileOrFolder(path);
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object readdirectory(Object... args) {
		List<FileInfo> infos = null;
		Path directoryPath = Paths.get((java.lang.String) args[0]);
		List<Path> directoryContents = listDirectoryContents(directoryPath);
		for (Path path : directoryContents) {
			BasicFileAttributes fileAttributes = null;
			try {
				fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
				if (infos == null) {
					infos = new ArrayList<FileInfo>();
				}
				if (fileAttributes.isDirectory()) {
					FileInfo info = new FileInfo(path.getFileName().toString(), IFile.FileType_Directory,
							fileAttributes.size());
					infos.add(info);
				} else if (fileAttributes.isRegularFile()) {
					FileInfo info = new FileInfo(path.getFileName().toString(), IFile.FileType_Regular, fileAttributes.size());
					infos.add(info);
				} else {
					FileInfo info = new FileInfo(path.getFileName().toString(), IFile.FileType_Other, fileAttributes.size());
					infos.add(info);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return infos.toArray(new FileInfo[infos.size()]);
	}

	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_FILEversion();
	}
	
	private void deleteFileOrFolder(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.deleteIfExists(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
				if (e != null) {
					e.printStackTrace();
					return FileVisitResult.TERMINATE;
				}
				Files.deleteIfExists(dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				e.printStackTrace();
				return FileVisitResult.TERMINATE;
			}
		});
	}

	private List<Path> listDirectoryContents(Path path) {
		List<Path> files = new ArrayList<Path>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
			for (Path directoryPath : directoryStream) {
				files.add(directoryPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

}
