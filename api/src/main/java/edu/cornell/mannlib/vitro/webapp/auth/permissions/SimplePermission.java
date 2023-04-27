package edu.cornell.mannlib.vitro.webapp.auth.permissions;

public class SimplePermission {

  //Access rules stored here for compatibility reasons

    public static final SimpleAccessRule ACCESS_SPECIAL_DATA_MODELS = new SimpleAccessRule( SimpleAccessRules.NS + "AccessSpecialDataModels");
    public static final SimpleAccessRule DO_BACK_END_EDITING = new SimpleAccessRule(SimpleAccessRules.NS + "DoBackEndEditing");
    public static final SimpleAccessRule DO_FRONT_END_EDITING = new SimpleAccessRule(SimpleAccessRules.NS + "DoFrontEndEditing");
    public static final SimpleAccessRule EDIT_ONTOLOGY = new SimpleAccessRule(SimpleAccessRules.NS + "EditOntology");
    public static final SimpleAccessRule EDIT_OWN_ACCOUNT = new SimpleAccessRule(SimpleAccessRules.NS + "EditOwnAccount");
    public static final SimpleAccessRule EDIT_SITE_INFORMATION = new SimpleAccessRule(SimpleAccessRules.NS + "EditSiteInformation");
    public static final SimpleAccessRule ENABLE_DEVELOPER_PANEL = new SimpleAccessRule(SimpleAccessRules.NS + "EnableDeveloperPanel");
    public static final SimpleAccessRule LOGIN_DURING_MAINTENANCE = new SimpleAccessRule(SimpleAccessRules.NS + "LoginDuringMaintenance");
    public static final SimpleAccessRule MANAGE_MENUS = new SimpleAccessRule(SimpleAccessRules.NS + "ManageMenus");
    public static final SimpleAccessRule MANAGE_OWN_PROXIES = new SimpleAccessRule(SimpleAccessRules.NS + "ManageOwnProxies");
    public static final SimpleAccessRule MANAGE_PROXIES = new SimpleAccessRule(SimpleAccessRules.NS + "ManageProxies");
    public static final SimpleAccessRule MANAGE_SEARCH_INDEX = new SimpleAccessRule(SimpleAccessRules.NS + "ManageSearchIndex");
    public static final SimpleAccessRule MANAGE_USER_ACCOUNTS = new SimpleAccessRule(SimpleAccessRules.NS + "ManageUserAccounts");
    public static final SimpleAccessRule QUERY_FULL_MODEL = new SimpleAccessRule(SimpleAccessRules.NS + "QueryFullModel");
    public static final SimpleAccessRule QUERY_USER_ACCOUNTS_MODEL = new SimpleAccessRule( SimpleAccessRules.NS + "QueryUserAccountsModel");
    public static final SimpleAccessRule REFRESH_VISUALIZATION_CACHE = new SimpleAccessRule( SimpleAccessRules.NS + "RefreshVisualizationCache");
    public static final SimpleAccessRule SEE_CONFIGURATION = new SimpleAccessRule(SimpleAccessRules.NS + "SeeConfiguration");
    public static final SimpleAccessRule SEE_INDVIDUAL_EDITING_PANEL = new SimpleAccessRule( SimpleAccessRules.NS + "SeeIndividualEditingPanel");
    public static final SimpleAccessRule SEE_REVISION_INFO = new SimpleAccessRule(SimpleAccessRules.NS + "SeeRevisionInfo");
    public static final SimpleAccessRule SEE_SITE_ADMIN_PAGE = new SimpleAccessRule(SimpleAccessRules.NS + "SeeSiteAdminPage");
    public static final SimpleAccessRule SEE_STARTUP_STATUS = new SimpleAccessRule(SimpleAccessRules.NS + "SeeStartupStatus");
    public static final SimpleAccessRule SEE_VERBOSE_PROPERTY_INFORMATION = new SimpleAccessRule( SimpleAccessRules.NS + "SeeVerbosePropertyInformation");
    public static final SimpleAccessRule USE_ADVANCED_DATA_TOOLS_PAGES = new SimpleAccessRule( SimpleAccessRules.NS + "UseAdvancedDataToolsPages");
    public static final SimpleAccessRule USE_INDIVIDUAL_CONTROL_PANEL = new SimpleAccessRule( SimpleAccessRules.NS + "UseIndividualControlPanel");
    public static final SimpleAccessRule USE_SPARQL_QUERY_PAGE = new SimpleAccessRule(SimpleAccessRules.NS + "UseSparqlQueryPage");
    public static final SimpleAccessRule USE_SPARQL_QUERY_API = new SimpleAccessRule(SimpleAccessRules.NS + "UseSparqlQueryApi");
    public static final SimpleAccessRule USE_SPARQL_UPDATE_API = new SimpleAccessRule(SimpleAccessRules.NS + "UseSparqlUpdateApi");
    
    // ----------------------------------------------------------------------
    // These instances are "catch all" permissions to cover poorly defined
    // groups of actions until better definitions were found. Don't add usages
    // of these, and remove existing usages where possible.
    // ----------------------------------------------------------------------
    
    public static final SimpleAccessRule USE_BASIC_AJAX_CONTROLLERS = new SimpleAccessRule(SimpleAccessRules.NS + "UseBasicAjaxControllers");
    public static final SimpleAccessRule USE_MISCELLANEOUS_ADMIN_PAGES = new SimpleAccessRule(SimpleAccessRules.NS + "UseMiscellaneousAdminPages");
    public static final SimpleAccessRule USE_MISCELLANEOUS_CURATOR_PAGES = new SimpleAccessRule(SimpleAccessRules.NS + "UseMiscellaneousCuratorPages");
    public static final SimpleAccessRule USE_MISCELLANEOUS_PAGES = new SimpleAccessRule(SimpleAccessRules.NS + "UseMiscellaneousPages");

    // ----------------------------------------------------------------------
    // These instances are permissions that can be specified for a given page
    // created/managed through page management,
    // e.g. this page is viewable only by admins, this page is viewable to anyone
    // who is logged in, etc.
    // ----------------------------------------------------------------------
    
    public static final SimpleAccessRule PAGE_VIEWABLE_ADMIN = new SimpleAccessRule(SimpleAccessRules.NS + "PageViewableAdmin");
    public static final SimpleAccessRule PAGE_VIEWABLE_CURATOR = new SimpleAccessRule(SimpleAccessRules.NS + "PageViewableCurator");
    public static final SimpleAccessRule PAGE_VIEWABLE_LOGGEDIN = new SimpleAccessRule(SimpleAccessRules.NS + "PageViewableLoggedIn");
    public static final SimpleAccessRule PAGE_VIEWABLE_EDITOR = new SimpleAccessRule(SimpleAccessRules.NS + "PageViewableEditor");
    public static final SimpleAccessRule PAGE_VIEWABLE_PUBLIC = new SimpleAccessRule(SimpleAccessRules.NS + "PageViewablePublic");

}
