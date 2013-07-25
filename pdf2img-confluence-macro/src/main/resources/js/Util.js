//Switch between tabs
AJS.tabs.change(AJS.$(tab, e));
//tab - this must be a jQuery object of the anchor for the tab you wish to change to.
//e - Optional. You can also pass in an event if you choose to call this from an event handler.
//Example of changing a tab via JS
//AJS.tabs.change(AJS.$("a[href=#horizontal-third]"));

// Show/Hide dropdown events
$("#myDropdown").on({
	  "aui-dropdown2-show": function() {
	    $dd.addClass("loading");
	    $.get("/actions/myDropdown", function(responseHTML) {
	      $dd.removeClass("loading");
	      $dd.html(responseHTML);
	      $dd.find("a").first().addClass("active"); // Remember to select an option!
	    });
	  },
	  "aui-dropdown2-hide": function(event) {
	    $dd.empty();
	  }
	});