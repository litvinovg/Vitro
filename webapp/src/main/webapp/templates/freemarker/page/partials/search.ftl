<#-- $This file is distributed under the terms of the license in LICENSE$ -->

<div id="searchBlock">
    <form id="searchForm" action="${urls.base}/extendedsearch" accept-charset="UTF-8" method="GET">
        <label for="search">${i18n().search_button}</label>
        <input type="text" name="querytext" id="search" class="search-form-item" value="${querytext!}" size="20" autocapitalize="off" />
        <input class="search-form-submit" name="submit" type="submit"  value="${i18n().search_button}" />
    </form>
</div> <!-- end searchBlock -->
