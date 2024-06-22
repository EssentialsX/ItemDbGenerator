package io.github.essentialsx.itemdbgenerator.providers.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class AnnotationUtil {
  private AnnotationUtil() {
  }

  public static <T extends Enum<T>> Set<T> getExperimentalEnums(Class<T> enumClass, Function<String, T> valueOf) {
    final Set<T> experimentalEnums = new HashSet<>();

    if (true) {
      return experimentalEnums;
    }

    try {
      ClassReader cr = new ClassReader(enumClass.getName());
      cr.accept(new ClassVisitor(Opcodes.ASM9) {
        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
          return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
              if (descriptor.equals("Lorg/bukkit/MinecraftExperimental;")) {
                experimentalEnums.add(valueOf.apply(name));
              }
              return super.visitAnnotation(descriptor, visible);
            }
          };
        }
      }, 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return experimentalEnums;
  }
}
