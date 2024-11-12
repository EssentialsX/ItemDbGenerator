package io.github.essentialsx.itemdbgenerator;

import com.google.gson.Gson;
import io.github.essentialsx.itemdbgenerator.providers.util.AnnotationUtil;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;

import java.io.FileWriter;
import java.util.Set;

public class AnnotationGenerator {
  public static void main(String[] args) {
    final Set<Material> experimentalMaterials = AnnotationUtil.getExperimentalEnums(Material.class, Material::valueOf);
    final Set<PotionType> experimentalPotions = AnnotationUtil.getExperimentalEnums(PotionType.class, PotionType::valueOf);

    final Gson gson = new Gson();
    final String materialsJson = gson.toJson(experimentalMaterials);
    final String potionsJson = gson.toJson(experimentalPotions);

    try (final FileWriter materialsWriter = new FileWriter("src/main/resources/experimental_materials.json")) {
      materialsWriter.write(materialsJson);
    } catch (Exception e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
    }

    try (final FileWriter potionsWriter = new FileWriter("src/main/resources/experimental_potions.json")) {
      potionsWriter.write(potionsJson);
    } catch (Exception e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
    }
  }
}
