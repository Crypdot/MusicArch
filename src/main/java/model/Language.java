package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


import main.Main;

/**
 * Purpose of this class is to set the language of the application
 * @author Kalle
 *
 */

public class Language {
	private ResourceBundle bundle;
	private static Language INSTANCE = null;
	private Properties properties = new Properties();
	private Locale currentLocale;
	
	
	public static Language getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Language();
		}
		return INSTANCE;
	}
	
	private Language() {

		try {
			String current = System.getProperty("user.dir");
			System.out.println("Current working directory in Java : " + current);
			File jarPath=new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			String propertiesPath=jarPath.getParent();
			System.out.println(" propertiesPath-"+propertiesPath+"/MusicArch.properties");
//		    properties.load(new FileInputStream(propertiesPath+"/MusicArch.properties"));
			FileInputStream configStream = new FileInputStream("MusicArch.properties");

//			FileInputStream configStream = new FileInputStream(propertiesPath+"\\classes\\MusicArch.properties");
			System.out.println("Streaming all\n " +configStream.read());
			properties.load(configStream);
			configStream.close();
			
			String language = properties.getProperty("language");
			String country = properties.getProperty("country");
			
			currentLocale = new Locale(language, country);
			Locale.setDefault(currentLocale);
			System.out.println(Locale.getDefault() + " default locale");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			bundle = ResourceBundle.getBundle("TextResources");
			System.out.println("tämä on bundle testi " + bundle.getString("AlbumYearLabel"));
			
		} catch (Exception x) {
			
			System.out.println(x.getMessage());
		}
	}
	
	
//	public ResourceBundle getDefault() {
//		return this.bundle;
//	}
	/**
	 * Setting the country and the language of the application.
	 * @param language Language of the application
	 * @param country Country of the application
	 */
	
	public void setLocale(String language, String country) {
		try {
			Locale.setDefault(new Locale(language, country));
			bundle = ResourceBundle.getBundle("TextResources_" + language + "_" + country);
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	/**
	 * Returning the locale
	 * @return Locale
	 */
	public Locale getLocale() {
		return this.currentLocale;
	}
	
	/**
	 * Setting the default language
	 * @param language
	 */
	
	public void setDefault(Locale language) {
		this.bundle = ResourceBundle.getBundle("TextResources_fi_FI");

	}
	
	public ResourceBundle getBundle() {
		return this.bundle;
	}
	
	public void saveLocale(String language, String country) {
		FileInputStream configStream;
		try {
			configStream = new FileInputStream("src/main/resources/MusicArch.properties");
			properties.load(configStream);
			configStream.close();
			properties.setProperty("language", language);
			properties.setProperty("country", country);
			
			
			currentLocale = new Locale(language, country);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (FileOutputStream output = new FileOutputStream("src/main/resources/MusicArch.properties");
				ObjectOutputStream tiedosto = new ObjectOutputStream(output); )
			{
				properties.store(output, "Description to header");
			
				
				// Kirjoita HashMap objektivirtaan
				
				
				
			} catch (Exception ex) {
				System.out.println("saveLocale -> Fail");
			}
	}

}
