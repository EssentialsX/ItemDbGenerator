package io.github.essentialsx.itemdbgenerator.providers.item;

import org.bukkit.Material;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides items from the Material enum.
 */
public class MaterialEnumProvider implements ItemProvider {
    @Override
    public Stream<Item> get() {
      final Set<Material> experimentalMaterials = new HashSet<>();

      try {
        ClassReader cr = new ClassReader(Material.class.getName());
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
          @Override
          public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            return new FieldVisitor(Opcodes.ASM9) {
              @Override
              public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                if (descriptor.equals("Lorg/bukkit/MinecraftExperimental;")) {
                  experimentalMaterials.add(Material.valueOf(name));
                }
                return super.visitAnnotation(descriptor, visible);
              }
            };
          }
        }, 0);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      return Arrays.stream(Material.values())
                .filter(mat -> !mat.name().contains("LEGACY"))
                .filter(mat -> !experimentalMaterials.contains(mat))
                .filter(Material::isItem)
                .map(MaterialEnumItem::new);
    }

    public static class MaterialEnumItem extends Item {
        private MaterialEnumItem(Material material) {
            super(material);
        }

        public String getName() {
            return getMaterial().name().toLowerCase();
        }
    }
}
