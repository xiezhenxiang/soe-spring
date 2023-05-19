package cn.soe.boot.autoconfigure.minio;

import cn.soe.util.database.minio.MinioUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import static cn.soe.boot.autoconfigure.minio.MinioProperties.defaultBucket;

/**
 * @author xiezhenxiang 2023/5/9
 */
public class MinioSdk extends MinioUtils {

    public MinioSdk(MinioProperties minioProperties) {
        super(minioProperties);
    }

    public void putObject(String objectName, File file) {
        putObject(defaultBucket(), objectName, file);
    }

    public void putObject(String objectName, InputStream inputStream) {
        putObject(defaultBucket(), objectName, inputStream);
    }

    public InputStream getObject(String objectName) {
        return getObject(defaultBucket(), objectName);
    }

    public void getObject(String objectName, OutputStream out) {
        getObject(defaultBucket(), objectName, out);
    }
}
