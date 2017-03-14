
package de.unratedfilms.movielens.coremod.asm.util;

import java.util.function.Predicate;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class AsmUtils {

    /*
     * Works just like ReflectionHelper.findMethod(), but for ASM method nodes.
     */
    public static MethodNode findMethod(ClassNode classNode, Method... methodDefs) {

        for (Method methodDef : methodDefs) {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(methodDef.getName()) && methodNode.desc.equals(methodDef.getDescriptor())) {
                    return methodNode;
                }
            }
        }

        return null;
    }

    public static AbstractInsnNode searchForward(InsnList insns, Predicate<AbstractInsnNode> cond) {

        return searchForward(insns, null, cond);
    }

    public static AbstractInsnNode searchForward(InsnList insns, AbstractInsnNode start, Predicate<AbstractInsnNode> cond) {

        for (int index = start == null ? 0 : insns.indexOf(start) + 1; index < insns.size(); index++) {
            AbstractInsnNode insn = insns.get(index);
            if (cond.test(insn)) {
                return insn;
            }
        }

        throw new IllegalStateException("Can't find matching instruction; are you using the wrong version of the mod?");
    }

    public static AbstractInsnNode searchBackward(InsnList insns, Predicate<AbstractInsnNode> cond) {

        return searchBackward(insns, null, cond);
    }

    public static AbstractInsnNode searchBackward(InsnList insns, AbstractInsnNode start, Predicate<AbstractInsnNode> cond) {

        for (int index = start == null ? insns.size() - 1 : insns.indexOf(start) - 1; index >= 0; index--) {
            AbstractInsnNode insn = insns.get(index);
            if (cond.test(insn)) {
                return insn;
            }
        }

        throw new IllegalStateException("Can't find matching instruction; are you using the wrong version of the mod?");
    }

    private AsmUtils() {

    }

}
