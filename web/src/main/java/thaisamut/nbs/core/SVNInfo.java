package thaisamut.nbs.core;

import java.util.Properties;

public final class SVNInfo {

    public static final String REPOSITORY;
    public static final String PATH;
    public static final String REVISION;
    public static final String MIXED_REVISIONS;
    public static final String COMMITTED_REVISION;
    public static final String COMMITTED_DATE;
    public static final String STATUS;
    public static final String SPECIAL_STATUS;

    static
    {
        Properties props = new Properties();

        try
        {
            props.load(SVNInfo.class.getResourceAsStream("/revision.properties"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        REPOSITORY = props.getProperty("repository", "UNDEFINED");
        PATH = props.getProperty("path", "UNDEFINED");
        REVISION = props.getProperty("revision", "UNDEFINED");
        MIXED_REVISIONS = props.getProperty("mixedRevisions", "UNDEFINED");
        COMMITTED_REVISION = props.getProperty("committedRevision", "UNDEFINED");
        COMMITTED_DATE = props.getProperty("committedDate", "UNDEFINED");
        STATUS = props.getProperty("status", "UNDEFINED");
        SPECIAL_STATUS = props.getProperty("specialStatus", "UNDEFINED");
    }
}
