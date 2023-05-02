package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.AccessOperation;
import edu.cornell.mannlib.vitro.webapp.auth.requestedAction.SimpleAuthorizationRequest;

public class SimplePermission {

  //Access rules stored here for compatibility reasons

    public static final SimplePermission ACCESS_SPECIAL_DATA_MODELS = new SimplePermission( SimpleAccessRules.NS + "AccessSpecialDataModels");
    public static final SimplePermission DO_BACK_END_EDITING = new SimplePermission(SimpleAccessRules.NS + "DoBackEndEditing");
    public static final SimplePermission DO_FRONT_END_EDITING = new SimplePermission(SimpleAccessRules.NS + "DoFrontEndEditing");
    public static final SimplePermission EDIT_ONTOLOGY = new SimplePermission(SimpleAccessRules.NS + "EditOntology");
    public static final SimplePermission EDIT_OWN_ACCOUNT = new SimplePermission(SimpleAccessRules.NS + "EditOwnAccount");
    public static final SimplePermission EDIT_SITE_INFORMATION = new SimplePermission(SimpleAccessRules.NS + "EditSiteInformation");
    public static final SimplePermission ENABLE_DEVELOPER_PANEL = new SimplePermission(SimpleAccessRules.NS + "EnableDeveloperPanel");
    public static final SimplePermission LOGIN_DURING_MAINTENANCE = new SimplePermission(SimpleAccessRules.NS + "LoginDuringMaintenance");
    public static final SimplePermission MANAGE_MENUS = new SimplePermission(SimpleAccessRules.NS + "ManageMenus");
    public static final SimplePermission MANAGE_OWN_PROXIES = new SimplePermission(SimpleAccessRules.NS + "ManageOwnProxies");
    public static final SimplePermission MANAGE_PROXIES = new SimplePermission(SimpleAccessRules.NS + "ManageProxies");
    public static final SimplePermission MANAGE_SEARCH_INDEX = new SimplePermission(SimpleAccessRules.NS + "ManageSearchIndex");
    public static final SimplePermission MANAGE_USER_ACCOUNTS = new SimplePermission(SimpleAccessRules.NS + "ManageUserAccounts");
    public static final SimplePermission QUERY_FULL_MODEL = new SimplePermission(SimpleAccessRules.NS + "QueryFullModel");
    public static final SimplePermission QUERY_USER_ACCOUNTS_MODEL = new SimplePermission( SimpleAccessRules.NS + "QueryUserAccountsModel");
    public static final SimplePermission REFRESH_VISUALIZATION_CACHE = new SimplePermission( SimpleAccessRules.NS + "RefreshVisualizationCache");
    public static final SimplePermission SEE_CONFIGURATION = new SimplePermission(SimpleAccessRules.NS + "SeeConfiguration");
    public static final SimplePermission SEE_INDVIDUAL_EDITING_PANEL = new SimplePermission( SimpleAccessRules.NS + "SeeIndividualEditingPanel");
    public static final SimplePermission SEE_REVISION_INFO = new SimplePermission(SimpleAccessRules.NS + "SeeRevisionInfo");
    public static final SimplePermission SEE_SITE_ADMIN_PAGE = new SimplePermission(SimpleAccessRules.NS + "SeeSiteAdminPage");
    public static final SimplePermission SEE_STARTUP_STATUS = new SimplePermission(SimpleAccessRules.NS + "SeeStartupStatus");
    public static final SimplePermission SEE_VERBOSE_PROPERTY_INFORMATION = new SimplePermission( SimpleAccessRules.NS + "SeeVerbosePropertyInformation");
    public static final SimplePermission USE_ADVANCED_DATA_TOOLS_PAGES = new SimplePermission( SimpleAccessRules.NS + "UseAdvancedDataToolsPages");
    public static final SimplePermission USE_INDIVIDUAL_CONTROL_PANEL = new SimplePermission( SimpleAccessRules.NS + "UseIndividualControlPanel");
    public static final SimplePermission USE_SPARQL_QUERY_PAGE = new SimplePermission(SimpleAccessRules.NS + "UseSparqlQueryPage");
    public static final SimplePermission USE_SPARQL_QUERY_API = new SimplePermission(SimpleAccessRules.NS + "UseSparqlQueryApi");
    public static final SimplePermission USE_SPARQL_UPDATE_API = new SimplePermission(SimpleAccessRules.NS + "UseSparqlUpdateApi");
    
    // ----------------------------------------------------------------------
    // These instances are "catch all" permissions to cover poorly defined
    // groups of actions until better definitions were found. Don't add usages
    // of these, and remove existing usages where possible.
    // ----------------------------------------------------------------------
    
    public static final SimplePermission USE_BASIC_AJAX_CONTROLLERS = new SimplePermission(SimpleAccessRules.NS + "UseBasicAjaxControllers");
    public static final SimplePermission USE_MISCELLANEOUS_ADMIN_PAGES = new SimplePermission(SimpleAccessRules.NS + "UseMiscellaneousAdminPages");
    public static final SimplePermission USE_MISCELLANEOUS_CURATOR_PAGES = new SimplePermission(SimpleAccessRules.NS + "UseMiscellaneousCuratorPages");
    public static final SimplePermission USE_MISCELLANEOUS_PAGES = new SimplePermission(SimpleAccessRules.NS + "UseMiscellaneousPages");

    // ----------------------------------------------------------------------
    // These instances are permissions that can be specified for a given page
    // created/managed through page management,
    // e.g. this page is viewable only by admins, this page is viewable to anyone
    // who is logged in, etc.
    // ----------------------------------------------------------------------
    
    public static final SimplePermission PAGE_VIEWABLE_ADMIN = new SimplePermission(SimpleAccessRules.NS + "PageViewableAdmin");
    public static final SimplePermission PAGE_VIEWABLE_CURATOR = new SimplePermission(SimpleAccessRules.NS + "PageViewableCurator");
    public static final SimplePermission PAGE_VIEWABLE_LOGGEDIN = new SimplePermission(SimpleAccessRules.NS + "PageViewableLoggedIn");
    public static final SimplePermission PAGE_VIEWABLE_EDITOR = new SimplePermission(SimpleAccessRules.NS + "PageViewableEditor");
    public static final SimplePermission PAGE_VIEWABLE_PUBLIC = new SimplePermission(SimpleAccessRules.NS + "PageViewablePublic");
    
    
    public SimpleAuthorizationRequest ACTION;
    private SimpleAccessRule accessRule;

    private SimplePermission(String uri) {
        this.ACTION = new SimpleAuthorizationRequest(uri, AccessOperation.EXECUTE);
        accessRule = new SimpleAccessRule(uri);
        SimpleAccessRules.add(accessRule);
    }
    
    public SimpleAccessRule getAccessRule() {
        return accessRule;
    }
}
