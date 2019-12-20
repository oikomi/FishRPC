package org.miaohong.fishrpc.core.conf.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.miaohong.fishrpc.core.util.ClassLoaderUtils;
import org.miaohong.fishrpc.core.util.StandardYamlObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class YamlConfig<T> extends AbstractConfig<T> {

    private static final Logger LOG = LoggerFactory.getLogger(YamlConfig.class);
    private final Class<T> clazz;
    private File sourceFile;

    public YamlConfig(String path, Class<T> clazz) {
        super();
        this.clazz = clazz;
        this.sourceFile = new File(
                ClassLoaderUtils.getCurrentClassLoader().
                        getResource(path).getFile());
    }


    @Override
    public T load() {
        try {
            ObjectMapper objectMapper = StandardYamlObjectMapper.MAPPER;
            return objectMapper.readValue(sourceFile, clazz);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

}
