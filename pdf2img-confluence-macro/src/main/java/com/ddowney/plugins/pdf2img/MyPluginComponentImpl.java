package com.ddowney.plugins.pdf2img;

import java.util.List;
import java.util.Map;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.sal.api.ApplicationProperties;


public class MyPluginComponentImpl implements MyPluginComponent
{
    private final ApplicationProperties applicationProperties;
	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	
	public MyPluginComponentImpl(ApplicationProperties applicationProperties, SpaceManager spaceManager, PageManager pageManager,
    		AttachmentManager attachmentManager, PermissionManager permissionManager)
    {
        this.applicationProperties = applicationProperties;
        this.spaceManager = spaceManager;
        this.pageManager = pageManager;
        this.attachmentManager = attachmentManager;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "myComponent:" + applicationProperties.getDisplayName();
        }
        return "myComponent";
    }
    
    public String execute(Map<?, ?> params, String body, RenderContext renderContext) {
		
    	Map<String, Object> context = MacroUtils.defaultVelocityContext();
    	
    	Picker p = new Picker(spaceManager, pageManager, attachmentManager);
    	
    	List<Space> spaces = p.getAllSpaces();
    	Map<Space, List<Page>> pages = p.getAllPages(spaces);
    	Map<Page, List<Attachment>> attachments = p.getAllAttachments(pages);
    	
    	context.put("Spaces", spaces);
    	context.put("Pages", pages);
    	context.put("Attachments", attachments);
    	   	
    	
    	return VelocityUtils.getRenderedTemplate("templateContent", context);
    }
}