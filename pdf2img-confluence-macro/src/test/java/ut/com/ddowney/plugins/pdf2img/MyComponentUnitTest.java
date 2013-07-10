package ut.com.ddowney.plugins.pdf2img;

import org.junit.Test;
import org.mockito.Mock;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.SpaceManager;
import com.ddowney.plugins.pdf2img.MyPluginComponent;
import com.ddowney.plugins.pdf2img.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
	@Mock
	private SpaceManager spaceManager;
	@Mock
	private PageManager pageManager;
	@Mock
	private AttachmentManager attachmentManager;
	@Mock
	private PermissionManager permissionManager;
	
	
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null, spaceManager, pageManager, attachmentManager, permissionManager);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}