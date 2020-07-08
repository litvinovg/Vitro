<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<#-- Template for displaying paged search results -->

<h2 class="searchResultsHeader">
<#escape x as x?html>
    ${i18n().search_results_for} '${querytext}'
    <#if classGroupName?has_content>${i18n().limited_to_type} '${classGroupName}'</#if>
    <#if typeName?has_content>${i18n().limited_to_type} '${typeName}'</#if>
</#escape>
<script type="text/javascript">
	var url = window.location.toString();
	if (url.indexOf("?") == -1){
		var queryText = 'querytext=${querytext}';
	} else {
		var urlArray = url.split("?");
		var queryText = urlArray[1];
	}

	var urlsBase = '${urls.base}';
</script>

	<img id="downloadIcon" src="images/download-icon.png" alt="${i18n().download_results}" title="${i18n().download_results}" />
<#-- <span id="downloadResults" style="float:left"></span>  -->
</h2>

<span id="searchHelp"><a href="${urls.base}/searchHelp" title="${i18n().search_help}">${i18n().not_expected_results}</a></span>
<div class="contentsBrowseGroup">
    <div class="virtualArticleSwitch">
      <label class="switch">Показать виртуальную статью
        <input id="virtualArticleCheck" type="checkbox" checked="false" onclick="showVirtualArticles();">
	    </label>
    </div>

    <#-- Refinement links -->
    <#if classGroupLinks?has_content>
        <div class="searchTOC">
            <h4>${i18n().display_only}</h4>
            <ul>
            <#list classGroupLinks as link>
                <li><a href="${link.url}" title="${i18n().class_group_link}">${link.text}</a><span>(${link.count})</span></li>
            </#list>
            </ul>
        </div>
    </#if>

    <#if classLinks?has_content>
        <div class="searchTOC">
            <#if classGroupName?has_content>
                <h4>${i18n().limit} ${classGroupName} ${i18n().to}</h4>
            <#else>
                <h4>${i18n().limit_to}</h4>
            </#if>
            <ul>
            <#list classLinks as link>
                <li><a href="${link.url}" title="${i18n().class_link}">${link.text}</a><span>(${link.count})</span></li>
            </#list>
            </ul>
        </div>
    </#if>

    <#-- Search results -->
    <ul class="searchhits">
        <#list individuals as individual>
            <li>
            	<@shortView uri=individual.uri viewContext="search" />
            </li>
        </#list>
    </ul>


    <#-- Paging controls -->
    <#if (pagingLinks?size > 0)>
        <div class="searchpages">
            ${i18n().pages}:
            <#if prevPage??><a class="prev" href="${prevPage}" title="${i18n().previous}">${i18n().previous}</a></#if>
            <#list pagingLinks as link>
                <#if link.url??>
                    <a href="${link.url}" title="${i18n().page_link}">${link.text}</a>
                <#else>
                    <span>${link.text}</span> <#-- no link if current page -->
                </#if>
            </#list>
            <#if nextPage??><a class="next" href="${nextPage}" title="${i18n().next_capitalized}">${i18n().next_capitalized}</a></#if>
        </div>
    </#if>
    <br />

    <#-- VIVO OpenSocial Extension by UCSF -->
    <#if openSocial??>
        <#if openSocial.visible>
        <h3>OpenSocial</h3>
            <script type="text/javascript" language="javascript">
                // find the 'Search' gadget(s).
                var searchGadgets = my.findGadgetsAttachingTo("gadgets-search");
                var keyword = '${querytext}';
                // add params to these gadgets
                if (keyword) {
                    for (var i = 0; i < searchGadgets.length; i++) {
                        var searchGadget = searchGadgets[i];
                        searchGadget.additionalParams = searchGadget.additionalParams || {};
                        searchGadget.additionalParams["keyword"] = keyword;
                    }
                }
                else {  // remove these gadgets
                    my.removeGadgets(searchGadgets);
                }
            </script>

            <div id="gadgets-search" class="gadgets-gadget-parent" style="display:inline-block"></div>
        </#if>
    </#if>

</div> <!-- end contentsBrowseGroup -->

<script>
$('input[type=checkbox]').removeAttr('checked');
  function showVirtualArticles(){
    var checkBox = document.getElementById("virtualArticleCheck");
    if (checkBox.checked == true){
      $('.excerptSearchResult').hide();
      $('.virtualArticlePart').show();
    } else {
      $('.excerptSearchResult').show();
      $('.virtualArticlePart').hide();
    }

  }
</script>
<script>
  let workSet = new Set(); 
  let biblioSet = new Set(); 
  var workDivs = $('.virtualArticleWork');
  var biblioDivs = $('.virtualArticleBibliography');
  biblioDivs.each(function() {
   biblioSet.add($(this).html());
  });
  workDivs.each(function() {
   workSet.add($(this).html());
  });
  var workArr = Array.from(workSet);
  workArr.sort();
  var biblioArr = Array.from(biblioSet);
  biblioArr.sort();
  if (workArr.length > 0 ) {
    $('<div class="virtualArticlePart"><button type="button" style="margin-top:16px;border:none;padding: 18px;width: 100%; text-align:left;" class="collapsible">Работы</button><div class="virtualWorks"></div></div>').insertAfter($('.virtualArticlePart').last());
    for (let value of workArr){
      $('.virtualWorks').last().append( '<div class="work"><p>' + value + '</p></div>' );
    }
  }
  if (biblioArr.length > 0 ) {
    $('<div class="virtualArticlePart"><button type="button" style="margin-top:16px;border:none;padding: 18px;width: 100%; text-align:left;" class="collapsible">Литература</button><div class="virtualBibliography"></div></div>').insertAfter($('.virtualArticlePart').last());
    for (let value of biblioArr){
      $('.virtualBibliography').last().append( '<div class="bibliography"><p>' + value + '</p></div>' );
    }
  }
</script>
<script>
  $('.virtualWorks').hide();
  $('.virtualBibliography').hide();
  $('.virtualArticlePart').hide();
  var coll = document.getElementsByClassName("collapsible");
  var i;
  for (i = 0; i < coll.length; i++) {
    coll[i].addEventListener("click", function() {
      this.classList.toggle("active");
      var content = this.nextElementSibling;
      if (content.style.display === "block") {
        content.style.display = "none";
      } else {
        content.style.display = "block";
      }
    });
  }
</script>
${stylesheets.add('<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />',
  				  '<link rel="stylesheet" href="${urls.base}/css/search.css" />',
                  '<link rel="stylesheet" type="text/css" href="${urls.base}/css/jquery_plugins/qtip/jquery.qtip.min.css" />')}

${headScripts.add('<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>',
				  '<script type="text/javascript" src="${urls.base}/js/jquery_plugins/qtip/jquery.qtip.min.js"></script>',
                  '<script type="text/javascript" src="${urls.base}/js/tiny_mce/tiny_mce.js"></script>'
                  )}

${scripts.add('<script type="text/javascript" src="${urls.base}/js/searchDownload.js"></script>')}
