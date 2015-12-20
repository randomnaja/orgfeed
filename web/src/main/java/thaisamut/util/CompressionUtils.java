package thaisamut.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class CompressionUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(CompressionUtils.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private CompressionUtils() { }

    public static long gunzip(InputStream in, OutputStream out)
            throws IOException
    {
        GZIPInputStream gzip = new GZIPInputStream(in);

        try
        {
            return IOUtils.copyLarge(gzip, out);
        }
        finally
        {
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(out);
        }
    }

    public static String gunzip(byte[] stream, String charset)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            gunzip(stream, baos);

            return new String(baos.toByteArray(), charset);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static long gunzip(byte[] in, OutputStream out)
            throws IOException
    {
        return gunzip(new ByteArrayInputStream(in), out);
    }

    public static long gunzip(byte[] in, FileObject out)
            throws IOException
    {
        out.createFile();

        FileContent content = out.getContent();
        OutputStream o = content.getOutputStream();

        try
        {
            return gunzip(in, o);
        }
        finally
        {
            content.close();
        }
    }

    public static long gunzip(String base64, OutputStream out)
            throws IOException
    {
        return gunzip(decodeBase64(base64), out);
    }

    public static long gzip(String text, OutputStream out)
            throws IOException
    {
        return gzip(text, DEFAULT_CHARSET, out);
    }

    public static long gzip(String text, String charset, OutputStream out)
            throws IOException
    {
        return isEmpty(text)
                ? 0L
                : gzip(new ByteArrayInputStream(text.getBytes(charset)), out);
    }

    public static long gzip(InputStream in, OutputStream out)
            throws IOException
    {
        GZIPOutputStream zout = new GZIPOutputStream(out);

        try
        {
            return IOUtils.copyLarge(in, zout);
        }
        finally
        {
            zout.flush();
            zout.finish();
            IOUtils.closeQuietly(zout);
            IOUtils.closeQuietly(in);
        }
    }

    public static byte[] gzip(String text)
            throws IOException
    {
        return gzip(text, DEFAULT_CHARSET);
    }

    public static byte[] gzip(String text, String charset)
            throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        gzip(text, charset, stream);

        return stream.toByteArray();
    }

    public static String decompress(String base64)
            throws Exception
    {
        return decompress(base64, DEFAULT_CHARSET);
    }

    public static String gunzip(byte[] gzipped)
            throws Exception
    {
        return gunzip(gzipped, DEFAULT_CHARSET);
    }

    public static String decompress(String base64, String charset)
            throws Exception
    {
        if (null == base64)
        {
            return null;
        }
        else if (EMPTY.equals(base64))
        {
            return EMPTY;
        }
        else
        {
            return gunzip(decodeBase64(base64), charset);
        }
    }

    public static String compress(String text, String charset)
            throws Exception
    {
        if (null == text)
        {
            return null;
        }
        else if (EMPTY.equals(text))
        {
            return EMPTY;
        }
        else
        {
            return encodeBase64String(gzip(text, charset));
        }
    }
}
