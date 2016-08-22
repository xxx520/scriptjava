/* Copyright 2016 Dimitry Ivanov (cr@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package scriptjava.buildins;

import scriptjava.ProcessRedirect;
import scriptjava.Value;
import scriptjava.ValueMutable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class Exec {

    public static String exec(String command) {

        final Value<String> result = new ValueMutable<>();
        final Runtime runtime = Runtime.getRuntime();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        PrintStream printStream = null;
        Process process = null;
        try {

            printStream = new PrintStream(out, true, "UTF-8");
            process = runtime.exec(command);
            ProcessRedirect.redirect(printStream, process.getInputStream(), null);
            ProcessRedirect.redirect(printStream, process.getErrorStream(), null);
            printStream.flush();
            result.set(out.toString("UTF-8"));

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (printStream != null) {
                printStream.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result.get();
    }

    private Exec() {}
}