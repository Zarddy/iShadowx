package club.zarddy.library.util.storage;

public enum StorageType {

    TYPE_AUDIO(DirectoryName.AUDIO_DIRECTORY_NAME),
    TYPE_DATA(DirectoryName.DATA_DIRECTORY_NAME),
    TYPE_FILE(DirectoryName.FILE_DIRECTORY_NAME),
    TYPE_IMAGE(DirectoryName.IMAGE_DIRECTORY_NAME),
    TYPE_LOG(DirectoryName.LOG_DIRECTORY_NAME),
    TYPE_THUMB_IMAGE(DirectoryName.THUMB_DIRECTORY_NAME),
    TYPE_THUMB_VIDEO(DirectoryName.THUMB_DIRECTORY_NAME),
    TYPE_VIDEO(DirectoryName.VIDEO_DIRECTORY_NAME),
    TYPE_TEMP(DirectoryName.TEMP_DIRECTORY_NAME);

    private DirectoryName storageDirectoryName;
    private long storageMinSize;

    public String getStoragePath() {
        return storageDirectoryName.getPath();
    }

    public long getStorageMinSize() {
        return storageMinSize;
    }

    StorageType(DirectoryName dirName) {
        this(dirName, StorageUtils.THRESHOLD_MIN_SPCAE);
    }

    StorageType(DirectoryName dirName, long storageMinSize) {
        this.storageDirectoryName = dirName;
        this.storageMinSize = storageMinSize;
    }

    enum DirectoryName {

        AUDIO_DIRECTORY_NAME("audio/"),
        DATA_DIRECTORY_NAME("data/"),
        FILE_DIRECTORY_NAME("file/"),
        IMAGE_DIRECTORY_NAME("image/"),
        LOG_DIRECTORY_NAME("log/"),
        THUMB_DIRECTORY_NAME("thumb/"),
        VIDEO_DIRECTORY_NAME("video/"),
        TEMP_DIRECTORY_NAME(".temp/");

        private String path;

        public String getPath() {
            return path;
        }

        DirectoryName(String path) {
            this.path = path;
        }
    }
}
