package electrodynamics.prefab.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Locale;

import electrodynamics.api.References;
import electrodynamics.api.configuration.BooleanValue;
import electrodynamics.api.configuration.ByteValue;
import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;
import electrodynamics.api.configuration.FloatValue;
import electrodynamics.api.configuration.IntValue;
import electrodynamics.api.configuration.LongValue;
import electrodynamics.api.configuration.StringValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.fml.loading.FMLPaths;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class ConfigurationHandler {
	private static final HashSet<Class<?>> configurationMappings = new HashSet<>();
	// to help keep log spam down
	private static final boolean DEBUG_MODE = false;

	public static void registerConfig(Class<?> configclass) {
		if (configclass.isAnnotationPresent(Configuration.class)) {
			configurationMappings.add(configclass);
		} else {
			new Exception("The class: " + configclass.getName() + " is not annotated by Configuration.class").printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onPreInit(FMLCommonSetupEvent event) {
		load();
	}

	public static void load() {
		for (Class<?> clazz : configurationMappings) {
			Configuration config = clazz.getAnnotation(Configuration.class);
			File file = new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).toFile(), config.name().toLowerCase(Locale.ROOT) + ".txt");
			Field[] declaredFields = clazz.getDeclaredFields();
			try {
				if (!file.exists()) {
					file.createNewFile();
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					for (Field field : declaredFields) {
						if (Modifier.isPublic(field.getModifiers())) {
							String name = field.getName();
							if (field.isAnnotationPresent(IntValue.class)) {
								String comment = field.getAnnotation(IntValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								int value = field.getInt(null);
								writer.write("I:default=" + field.getAnnotation(IntValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(LongValue.class)) {
								String comment = field.getAnnotation(LongValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								long value = field.getLong(null);
								writer.write("L:default=" + field.getAnnotation(LongValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(FloatValue.class)) {
								String comment = field.getAnnotation(FloatValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								float value = field.getFloat(null);
								writer.write("F:default=" + field.getAnnotation(FloatValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(DoubleValue.class)) {
								String comment = field.getAnnotation(DoubleValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								double value = field.getDouble(null);
								writer.write("D:default=" + field.getAnnotation(DoubleValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(StringValue.class)) {
								String comment = field.getAnnotation(StringValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								String value = (String) field.get(null);
								writer.write("S:default=" + field.getAnnotation(StringValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(BooleanValue.class)) {
								String comment = field.getAnnotation(BooleanValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								boolean value = field.getBoolean(null);
								writer.write("T:default=" + field.getAnnotation(BooleanValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(ByteValue.class)) {
								String comment = field.getAnnotation(ByteValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								byte value = field.getByte(null);
								writer.write("B:default=" + field.getAnnotation(ByteValue.class).def() + " -> " + name + "='" + value + "'");
							}
							writer.newLine();
						}
					}
					writer.close();
				} else {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line = reader.readLine();
					HashSet<Field> found = new HashSet<>();
					for (Field field : declaredFields) {
						if (Modifier.isPublic(field.getModifiers())) {
							found.add(field);
						}
					}
					while (line != null) {
						if (!line.startsWith("Comment: ") && !line.startsWith("//") && !line.isEmpty() && line.contains(" -> ")) {
							line = line.substring(10);
							line = line.substring(line.indexOf(" -> "));
							line = line.replace("'", "");
							line = line.substring(4);
							String[] split = line.split("=");
							try {
								Field field = clazz.getDeclaredField(split[0]);
								if (field.isAnnotationPresent(IntValue.class)) {
									field.setInt(null, Integer.parseInt(split[1]));
								}
								if (field.isAnnotationPresent(LongValue.class)) {
									field.setLong(null, Long.parseLong(split[1]));
								}
								if (field.isAnnotationPresent(FloatValue.class)) {
									field.setFloat(null, Float.parseFloat(split[1]));
								}
								if (field.isAnnotationPresent(DoubleValue.class)) {
									field.setDouble(null, Double.parseDouble(split[1]));
								}
								if (field.isAnnotationPresent(StringValue.class)) {
									field.set(null, split[1]);
								}
								if (field.isAnnotationPresent(BooleanValue.class)) {
									field.setBoolean(null, Boolean.parseBoolean(split[1]));
								}
								if (field.isAnnotationPresent(ByteValue.class)) {
									field.setByte(null, Byte.parseByte(split[1]));
								}
								found.remove(field);
							} catch (Exception e) {
								System.out.println("Invalid field found in config file '" + file.getName() + "'");
								System.out.println("Full: " + line);
								System.out.println("Field: " + split[0]);
								System.out.println("Value: " + split[1]);
							}
							if (DEBUG_MODE) {
								System.out.println("Parsed config field '" + split[0] + "' as -> " + split[1]);
							}
						}
						line = reader.readLine();
					}
					reader.close();
					file.delete();
					file.createNewFile();
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					for (Field field : declaredFields) {
						if (Modifier.isPublic(field.getModifiers())) {
							String name = field.getName();
							if (field.isAnnotationPresent(IntValue.class)) {
								String comment = field.getAnnotation(IntValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								int value = field.getInt(null);
								writer.write("I:default=" + field.getAnnotation(IntValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(LongValue.class)) {
								String comment = field.getAnnotation(LongValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								long value = field.getLong(null);
								writer.write("L:default=" + field.getAnnotation(LongValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(FloatValue.class)) {
								String comment = field.getAnnotation(FloatValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								float value = field.getFloat(null);
								writer.write("F:default=" + field.getAnnotation(FloatValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(DoubleValue.class)) {
								String comment = field.getAnnotation(DoubleValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								double value = field.getDouble(null);
								writer.write("D:default=" + field.getAnnotation(DoubleValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(StringValue.class)) {
								String comment = field.getAnnotation(StringValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								String value = (String) field.get(null);
								writer.write("S:default=" + field.getAnnotation(StringValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(BooleanValue.class)) {
								String comment = field.getAnnotation(BooleanValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								boolean value = field.getBoolean(null);
								writer.write("T:default=" + field.getAnnotation(BooleanValue.class).def() + " -> " + name + "='" + value + "'");
							}
							if (field.isAnnotationPresent(ByteValue.class)) {
								String comment = field.getAnnotation(ByteValue.class).comment();
								if (!comment.isEmpty()) {
									writer.write("Comment: '" + comment + "'");
									writer.newLine();
								}
								byte value = field.getByte(null);
								writer.write("B:default=" + field.getAnnotation(ByteValue.class).def() + " -> " + name + "='" + value + "'");
							}
							writer.newLine();
						}
					}
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
