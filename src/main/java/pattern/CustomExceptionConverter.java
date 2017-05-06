package pattern;

import exception.CustomException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

@Plugin(name = "CustomExceptionConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "syscode" })
public final class CustomExceptionConverter extends LogEventPatternConverter {

    private static final Pattern PREFIX = Pattern.compile("^(Caused by: )??" + CustomException.class.getName());

    protected CustomExceptionConverter(String name, String style) {
        super(name, style);
    }

    public static CustomExceptionConverter newInstance(final String[] options) {
        return new CustomExceptionConverter("syscode", "syscode");
    }

    @Override
    public void format(LogEvent event, StringBuilder buffer) {
        final Throwable t = event.getThrown();
        if (t == null) {
            return;
        }
        Queue<String> codeQueue = new LinkedList<>();
        queuingSystemCode(codeQueue, t);
        final StringWriter w = new StringWriter() {
            @Override
            public void write(String str, int off, int len) {
                if (!PREFIX.matcher(str).find()) {
                    super.write(str, off, len);
                    return;
                }
                String suffix = " [SystemCode -> " + codeQueue.poll() + "]";
                super.write(str + suffix, off, len + suffix.length());
            }

        };
        t.printStackTrace(new PrintWriter(w));
        buffer.append(w.toString());
    }

    private void queuingSystemCode(Queue<String> queue, Throwable t) {
        if (t instanceof CustomException) {
            queue.add(((CustomException) t).getCode());
        }
        Throwable cause = t.getCause();
        if (cause == null) {
            return;
        }
        queuingSystemCode(queue, cause);
    }

    @Override
    public boolean handlesThrowable() {
        return true;
    }
}
