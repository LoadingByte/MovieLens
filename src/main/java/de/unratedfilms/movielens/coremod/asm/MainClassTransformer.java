
package de.unratedfilms.movielens.coremod.asm;

import static de.unratedfilms.movielens.shared.Consts.LOGGER;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import net.minecraft.launchwrapper.IClassTransformer;
import de.unratedfilms.movielens.coremod.asm.transformers.CNT_EntityRenderer;

public class MainClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String obfName, String name, byte[] bytecode) {

        switch (name) {
            case "net.minecraft.client.renderer.EntityRenderer":
                bytecode = applyTransformers(bytecode, new CNT_EntityRenderer());
                break;
        }

        return bytecode;
    }

    private byte[] applyTransformers(byte[] bytecode, ClassNodeTransformer... transformers) {

        // Parse the bytecode into an editable ClassNode object
        ClassReader classReader = new ClassReader(bytecode);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        // Apply the transformers
        for (ClassNodeTransformer transformer : transformers) {
            LOGGER.info("Class transformation running on '{}' with transformer '{}'", classNode.name, transformer.getClass().getName());
            transformer.transform(classNode);
        }

        // Turn the ClassNode object back into bytecode
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

}
