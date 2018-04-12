package com.gxb.gxswallet.utils.jdenticon;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class JdenticonWrapper {

    private static JdenticonWrapper instance = new JdenticonWrapper();
    private Object jdenticon;
    private Invocable invocable;

    private JdenticonWrapper() {

        try {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine rhino = scriptEngineManager.getEngineByName("rhino");

            invocable = (Invocable) rhino;
            String script = Resource.jadenticonJs;
            rhino.eval(script);

            jdenticon = rhino.eval("jdenticon");
        } catch (Exception e) {

            throw new RuntimeException("Unable to setup nashorn and jdenticon script.", e);
        }

    }

    static JdenticonWrapper getInstance() {
        return instance;
    }

    String getSvg(Jdenticon jdenticon) {
        try {
            return (String) invocable.invokeMethod(this.jdenticon, "toSvg", jdenticon.getHash(), jdenticon.getSize(), jdenticon.getPadding());
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to create jdenticon svg.", e);
        }

    }
}
