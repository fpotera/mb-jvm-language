package sl;

/**
 * The SL_FILE module contains file handling routines.
 * 
 * @author Iulian Enache
 */
public interface IFile {

	public static final long FileType_Regular = 0; // Regular file
	public static final long FileType_Directory = 1; // Directory file
	public static final long FileType_Fifo = 2; // FIFO file
	public static final long FileType_Block = 3; // Block special file
	public static final long FileType_Character = 4; // Character special file
	public static final long FileType_Other = 5; // Unknown file type

	/**
	 * SL_FILE.$ExceptionName
	 */
	public static final java.lang.String exception_name = "ExceptionName";

	/**
	 * Error - Failed to get the status for a file.
	 */
	public static final java.lang.String error_deletedirectorytreestatdirectory = "Error_DeleteDirectoryTreeStatDirectory";

	/**
	 * Error - The file specified by the path parameter is not a directory.
	 */
	public static final java.lang.String error_deletedirectorytreenotdirectory = "Error_DeleteDirectoryTreeNotDirectory";

	/**
	 * Error - Failed to open a directory.
	 */
	public static final java.lang.String error_deletedirectorytreeopendirectory = "Error_DeleteDirectoryTreeOpenDirectory";

	/**
	 * Error - Failed to read the next entry from a directory.
	 */
	public static final java.lang.String error_deletedirectorytreereaddirectory = "Error_DeleteDirectoryTreeReadDirectory";

	/**
	 * Error - Failed to remove a directory file.
	 */
	public static final java.lang.String error_deletedirectorytreeremovedirectory = "Error_DeleteDirectoryTreeRemoveDirectory";

	/**
	 * Error - Failed to delete a regular file.
	 */
	public static final java.lang.String error_deletedirectorytreedeletefile = "Error_DeleteDirectoryTreeDeleteFile";

	/**
	 * Error - The path parameter does not specify a directory.
	 */
	public static final java.lang.String error_readdirectorynotdirectory = "Error_ReadDirectoryNotDirectory";

	/**
	 * Error - Failed to open the directory specified by path.
	 */
	public static final java.lang.String error_readdirectoryopendirectory = "Error_ReadDirectoryOpenDirectory";

	/**
	 * Error - Error when reading the directory.
	 */
	public static final java.lang.String error_readdirectoryreaddirectory = "Error_ReadDirectoryReadDirectory";

	/**
	 * Error - Failed to get the status for one of the files in the path directory.
	 */
	public static final java.lang.String error_readdirectorystatentry = "Error_ReadDirectoryStatEntry";

	/**
	 * <code><b><i>SL_FILE.DeleteDirectoryTree</i></b></code> - Function that deletes a directory including all the files and directories in it
	 * @param args (path)
	 * 				</br>The <code><b><i>path</i></b></code> parameter is the path name for the directory to delete. It should be a string.
	 * @return a date value.
	 */
	public abstract Object deletedirectorytree(Object... args);

	/**
	 * <code><b><i>SL_FILE.ReadDirectory</i></b></code> - Function that reads the contents of a directory
	 * @param args (path, files)
	 * 				</br>The <code><b><i>path</i></b></code> parameter specifies the name of the directory to read the contents of. It should be a string.
	 * 				</br>The <code><b><i>files</i></b></code> parameter is the output parameter. It should be should be an array of SL_FILE.FileInfo records containing the names, types and sizes of all the files.
	 * 				</br>&emsp;<code>The <b><i>$Name</i></b> field contains the name of the file.</code>
	 * 				</br>&emsp;<code>The <b><i>$Type</i></b> field contains a file type code.</code>
	 * 				</br>&emsp;&emsp;<code>The <b><i>$Type</i></b> field can contain any of the following values:</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Regular</i></b> – Regular file</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Directory</i></b> – Directory file</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Fifo</i></b> – FIFO file</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Block</i></b> – Block special file</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Character</i></b> – Character special file</code>
	 * 				</br>&emsp;&emsp;&emsp;<code><b><i>SL_FILE.$FileType_Other</i></b> – Unknown file type</code>
	 * 				</br>&emsp;<code>The <b><i>$Size</i></b> field contains the file size.</code>
	 * @return an array of SL_FILE.FileInfo records containing the names, types and sizes of all the files.
	 */
	public abstract Object readdirectory(Object... args);

	/**
	 * <code><b><i>SL_FILE.Version</i></b></code> - Function that returns the version number for the SL_FILE module as a string
	 * @return the version number for the SL_FILE module as a string.<code></br>The format of the string is n1.n2.n3.</code>
	 */
	public abstract Object version(Object... args);

	/**
	 * Record containing the name, type and size of a file
	 */
	public class FileInfo {

		private java.lang.String name;
		private long type;
		private long size;

		public FileInfo(java.lang.String name, long type, long size) {
			super();
			this.name = name;
			this.type = type;
			this.size = size;
		}

		public java.lang.String getName() {
			return name;
		}

		public long getType() {
			return type;
		}

		public long getSize() {
			return size;
		}

	}

}
