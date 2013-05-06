package net.x11.patch;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;


/**
 * Created with IntelliJ IDEA.
 * User: mzheludkov
 * Date: 5/6/13
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinuxX11PatchAgent implements ClassFileTransformer {

    public static final String XNET_PROTOCOL = "XNETProtocol";
    public static final String GET_WM_NAME = "getWMName";
    private static String wmName;

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        wmName = agentArgument;
        instrumentation.addTransformer(new LinuxX11PatchAgent());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return className.contains(XNET_PROTOCOL) ? doClass(className, classBeingRedefined, classfileBuffer) : classfileBuffer;
    }

    private byte[] doClass(String name, Class clazz, byte[] b) {
        System.out.println("modufy! "+name);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            CtMethod m = cl.getDeclaredMethod(GET_WM_NAME);
            m.setBody("{ return \""+wmName+"\"; }");
            b = cl.toBytecode();
        } catch (Exception e) {
            System.err.println("Could not instrument  " + name + ",  exception : " + e.getMessage());
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }
}
