package com.fingermidia.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.InputStream;

public class S3 {

    private static AmazonS3 client(String accessKey, String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    protected static boolean sendFile(String accessKey, String secretKey, String file, String path, String bucket, boolean delete) {
        try {
            AmazonS3 s3client = client(accessKey, secretKey);
            try {
                s3client.createBucket(bucket);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return sendFile(s3client, file, path, bucket, delete);

        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return false;
        }

    }

    private static boolean sendFile(AmazonS3 s3client, String file, String path, String bucket, boolean delete) {

        boolean success = false;

        PutObjectRequest objectToPut = new PutObjectRequest(bucket, file, new File(path + file));

        ObjectMetadata meta = new ObjectMetadata();
        objectToPut.setMetadata(meta);

        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated
        objectToPut.setAccessControlList(acl);

        s3client.putObject(objectToPut);

        success = true;

        File f = new File(path + file);
        if (delete) {
            f.delete();
        }

        return success;

    }

    protected static InputStream downloadFile(String accessKey, String secretKey, String pathS3, String file, String bucket) {

        try {

            AmazonS3 s3client = client(accessKey, secretKey);

            GetObjectRequest object = new GetObjectRequest(bucket, pathS3 + "/" + file);

            InputStream in = s3client.getObject(object).getObjectContent();
            return in;

        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return null;
        }

    }

    protected static void deleteFile(String accessKey, String secretKey, String pathS3, String file, String bucket) {

        try {

            AmazonS3 s3client = client(accessKey, secretKey);

            DeleteObjectRequest objectToPut = new DeleteObjectRequest(bucket, pathS3 + file);
            s3client.deleteObject(objectToPut);

        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

    }

}
