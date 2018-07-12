package com.kooppi.nttca.portal.common.config.file;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class PropertyResolver {

	private Map<String, String> properties = Maps.newHashMap();
	
	@PostConstruct
	private void  init(){
		String configEnv = System.getenv("CONFIG_LOCATION");
		System.out.println("#######CONFIG_LOCATION#####: " + configEnv);
		log.debug("#######CONFIG_LOCATION#####: " + configEnv);
		ConfigurationFiles.PROPERTIES_FILES.forEach(fileName->{
        	Properties p = new Properties();
			try {
				p.load(Files.newBufferedReader(Paths.get(this.getClass().getResource(fileName).toURI())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			properties.putAll(Maps.fromProperties(p));
		});
	}
	
	public Optional<String> getValue(String key){
		return Optional.ofNullable(properties.get(key));
	}
}
