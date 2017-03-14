
package de.unratedfilms.movielens.coremod.asm.transformers;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import de.unratedfilms.movielens.coremod.asm.ClassNodeTransformer;
import de.unratedfilms.movielens.coremod.asm.util.AsmUtils;
import de.unratedfilms.movielens.fmlmod.hooks.RenderHooks;

public class CNT_EntityRenderer implements ClassNodeTransformer {

    private static final String HOOKS_CLASS_NAME       = Type.getInternalName(RenderHooks.class);

    private static final Method ADJUST_VIEWPORT_HOOK   = Method.getMethod("void adjustViewport ()");
    private static final Method GET_ASPECT_RATIO_HOOK  = Method.getMethod("float getAspectRatio ()");
    private static final Method RENDER_BLACK_BARS_HOOK = Method.getMethod("void renderBlackBars ()");

    @Override
    public void transform(ClassNode classNode) {

        adjustCallTo_glViewport(classNode);
        adjustCallsTo_gluPerspective(classNode);
        insertCallTo_renderBlackBars(classNode);
    }

    private void adjustCallTo_glViewport(ClassNode classNode) {

        // Find method to inject into
        MethodNode renderWorld = AsmUtils.findMethod(classNode, Method.getMethod("void renderWorld (float, long)"));

        // Find the call to glViewport()
        MethodInsnNode callInsn = (MethodInsnNode) AsmUtils.searchForward(renderWorld.instructions,
                insn -> insn.getOpcode() == INVOKESTATIC && ((MethodInsnNode) insn).name.equals("glViewport"));

        InsnList toInjectInsns = new InsnList();
        // Instructions to get rid of the four arguments for glViewport()
        toInjectInsns.add(new InsnNode(POP2));
        toInjectInsns.add(new InsnNode(POP2));
        // Instruction to invoke the custom viewport adjustment hook
        toInjectInsns.add(new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS_NAME, ADJUST_VIEWPORT_HOOK.getName(), ADJUST_VIEWPORT_HOOK.getDescriptor(), false));

        // Actually inject the new instructions in place of the old call to glViewport()
        renderWorld.instructions.insert(callInsn, toInjectInsns);
        renderWorld.instructions.remove(callInsn);
    }

    private void adjustCallsTo_gluPerspective(ClassNode classNode) {

        // Find methods to inject into
        MethodNode setupCameraTransform = AsmUtils.findMethod(classNode, Method.getMethod("void setupCameraTransform (float, int)"));
        MethodNode renderHand = AsmUtils.findMethod(classNode, Method.getMethod("void renderHand (float, int)"));

        // Transform the gluPerspective() calls in those methods
        transformMethodWith_gluPerspective(setupCameraTransform);
        transformMethodWith_gluPerspective(renderHand);
    }

    private void transformMethodWith_gluPerspective(MethodNode method) {

        // Find the call to gluPerspective()
        MethodInsnNode callInsn = (MethodInsnNode) AsmUtils.searchForward(method.instructions,
                insn -> insn.getOpcode() == INVOKESTATIC && ((MethodInsnNode) insn).name.equals("gluPerspective"));

        // Find the instruction which computes the aspect ratio
        AbstractInsnNode fdivInsn = AsmUtils.searchBackward(method.instructions, callInsn,
                insn -> insn.getOpcode() == FDIV);

        InsnList toInjectInsns = new InsnList();
        // Instruction to get rid of the original aspect ratio (result of FDIV)
        toInjectInsns.add(new InsnNode(POP));
        // Instruction to push the new aspect ratio onto the stack
        toInjectInsns.add(new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS_NAME, GET_ASPECT_RATIO_HOOK.getName(), GET_ASPECT_RATIO_HOOK.getDescriptor(), false));

        // Actually inject the new instructions after the old FDIV computation
        method.instructions.insert(fdivInsn, toInjectInsns);
    }

    private void insertCallTo_renderBlackBars(ClassNode classNode) {

        // Find method to inject into
        MethodNode updateCameraAndRender = AsmUtils.findMethod(classNode, Method.getMethod("void updateCameraAndRender (float)"));

        // Find injection point, directly in front of the GUI overlay rendering call
        // Note that we can't just render with the rest of the GUI overlay since that would disappear as soon as the user presses F1
        LdcInsnNode guiLdc = (LdcInsnNode) AsmUtils.searchForward(updateCameraAndRender.instructions,
                insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst.equals("gui"));
        MethodInsnNode endStartGuiSection = (MethodInsnNode) AsmUtils.searchForward(updateCameraAndRender.instructions, guiLdc,
                insn -> insn.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) insn).name.equals("endStartSection"));

        // Inject the render black bars hook before any other GUI overlays are rendered
        updateCameraAndRender.instructions.insert(endStartGuiSection,
                new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS_NAME, RENDER_BLACK_BARS_HOOK.getName(), RENDER_BLACK_BARS_HOOK.getDescriptor(), false));
    }

}
