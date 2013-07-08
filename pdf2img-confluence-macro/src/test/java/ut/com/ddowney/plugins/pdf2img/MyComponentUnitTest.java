package ut.com.ddowney.plugins.pdf2img;

import org.junit.Test;
import com.ddowney.plugins.pdf2img.MyPluginComponent;
import com.ddowney.plugins.pdf2img.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}