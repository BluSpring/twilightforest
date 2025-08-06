package twilightforest.fabric;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class TFEarlyRiser implements Runnable {
	@Override
	public void run() {
		var mappingResolver = FabricLoader.getInstance().getMappingResolver();

		{
			var biomeSpecialEffectsMapped =
				mappingResolver.mapClassName("intermediary", "net.minecraft.class_4763").replace(".", "/");
			var grassColorModifierMapped =
				mappingResolver.mapClassName("intermediary", "net.minecraft.class_4763$class_5486").replace(".", "/");
			var biomeInjectionName =
				"twilightforest/fabric/GrassColorModifierExtension";
			var colorModifierName = "twilightforest/fabric/ColorModifier";
			var modifyColor = mappingResolver.mapMethodName(
				"intermediary",
				"net.minecraft.class_4763$class_5486",
				"method_30823",
				"(DDI)I"
			);

			ClassTinkerers.addTransformation(grassColorModifierMapped, classNode -> {
				classNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_ENUM; // why the fuck is this needed????

				// Use a delegate for modifyColor
				// Expected code:
					/*
					public int modifyColor(double x, double z, int grassColor) {
						return this.kilt$getDelegate().modifyGrassColor(x, z, grassColor);
					}
					 */
				{
					var originalModifyColorMethod = classNode.methods.stream().filter(it -> it.name.equals(modifyColor)).findFirst().orElseThrow();
					classNode.methods.removeIf(it -> it.name.equals(modifyColor));

					var modifyColorMethod = classNode.visitMethod(Opcodes.ACC_PUBLIC, originalModifyColorMethod.name, originalModifyColorMethod.desc, originalModifyColorMethod.signature, originalModifyColorMethod.exceptions.toArray(new String[0]));

					modifyColorMethod.visitCode();

					var label0 = new Label();
					var label1 = new Label();

					modifyColorMethod.visitLabel(label0);
					modifyColorMethod.visitVarInsn(Opcodes.ALOAD, 0);
					modifyColorMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, grassColorModifierMapped, "tf$getDelegate", "()Ltwilightforest/fabric/ColorModifier;", false);
					modifyColorMethod.visitVarInsn(Opcodes.DLOAD, 1);
					modifyColorMethod.visitVarInsn(Opcodes.DLOAD, 3);
					modifyColorMethod.visitVarInsn(Opcodes.ILOAD, 5);
					modifyColorMethod.visitMethodInsn(Opcodes.INVOKEINTERFACE, "twilightforest/fabric/ColorModifier", "modifyGrassColor", "(DDI)I", true);
					modifyColorMethod.visitInsn(Opcodes.IRETURN);

					modifyColorMethod.visitLabel(label1);
					modifyColorMethod.visitLocalVariable("this", "L" + grassColorModifierMapped + ";", null, label0, label1, 0);
					modifyColorMethod.visitLocalVariable("x", "D", null, label0, label1, 1);
					modifyColorMethod.visitLocalVariable("z", "D", null, label0, label1, 3);
					modifyColorMethod.visitLocalVariable("grassColor", "I", null, label0, label1, 5);

					modifyColorMethod.visitMaxs(6, 6);
					modifyColorMethod.visitEnd();
				}
			});
		}
	}
}
