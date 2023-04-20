package edu.cornell.mannlib.vitro.webapp.auth.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePermissions {

    private static final String NS = "java:" + SimplePermission.class.getName() + "#";
    
    private static final Map<String, SimplePermission> simplePermissions = new HashMap<String, SimplePermission>();

    public static final SimplePermission ACCESS_SPECIAL_DATA_MODELS = new SimplePermission( NS + "AccessSpecialDataModels");
    public static final SimplePermission DO_BACK_END_EDITING = new SimplePermission(NS + "DoBackEndEditing");
    public static final SimplePermission DO_FRONT_END_EDITING = new SimplePermission(NS + "DoFrontEndEditing");
    public static final SimplePermission EDIT_ONTOLOGY = new SimplePermission(NS + "EditOntology");
    public static final SimplePermission EDIT_OWN_ACCOUNT = new SimplePermission(NS + "EditOwnAccount");
    public static final SimplePermission EDIT_SITE_INFORMATION = new SimplePermission(NS + "EditSiteInformation");
    public static final SimplePermission ENABLE_DEVELOPER_PANEL = new SimplePermission(NS + "EnableDeveloperPanel");
    public static final SimplePermission LOGIN_DURING_MAINTENANCE = new SimplePermission(NS + "LoginDuringMaintenance");
    public static final SimplePermission MANAGE_MENUS = new SimplePermission(NS + "ManageMenus");
    public static final SimplePermission MANAGE_OWN_PROXIES = new SimplePermission(NS + "ManageOwnProxies");
    public static final SimplePermission MANAGE_PROXIES = new SimplePermission(NS + "ManageProxies");
    public static final SimplePermission MANAGE_SEARCH_INDEX = new SimplePermission(NS + "ManageSearchIndex");
    public static final SimplePermission MANAGE_USER_ACCOUNTS = new SimplePermission(NS + "ManageUserAccounts");
    public static final SimplePermission QUERY_FULL_MODEL = new SimplePermission(NS + "QueryFullModel");
    public static final SimplePermission QUERY_USER_ACCOUNTS_MODEL = new SimplePermission( NS + "QueryUserAccountsModel");
    public static final SimplePermission REFRESH_VISUALIZATION_CACHE = new SimplePermission( NS + "RefreshVisualizationCache");
    public static final SimplePermission SEE_CONFIGURATION = new SimplePermission(NS + "SeeConfiguration");
    public static final SimplePermission SEE_INDVIDUAL_EDITING_PANEL = new SimplePermission( NS + "SeeIndividualEditingPanel");
    public static final SimplePermission SEE_REVISION_INFO = new SimplePermission(NS + "SeeRevisionInfo");
    public static final SimplePermission SEE_SITE_ADMIN_PAGE = new SimplePermission(NS + "SeeSiteAdminPage");
    public static final SimplePermission SEE_STARTUP_STATUS = new SimplePermission(NS + "SeeStartupStatus");
    public static final SimplePermission SEE_VERBOSE_PROPERTY_INFORMATION = new SimplePermission( NS + "SeeVerbosePropertyInformation");
    public static final SimplePermission USE_ADVANCED_DATA_TOOLS_PAGES = new SimplePermission( NS + "UseAdvancedDataToolsPages");
    public static final SimplePermission USE_INDIVIDUAL_CONTROL_PANEL = new SimplePermission( NS + "UseIndividualControlPanel");
    public static final SimplePermission USE_SPARQL_QUERY_PAGE = new SimplePermission(NS + "UseSparqlQueryPage");
    public static final SimplePermission USE_SPARQL_QUERY_API = new SimplePermission(NS + "UseSparqlQueryApi");
    public static final SimplePermission USE_SPARQL_UPDATE_API = new SimplePermission(NS + "UseSparqlUpdateApi");
    
    // ----------------------------------------------------------------------
    // These instances are "catch all" permissions to cover poorly defined
    // groups of actions until better definitions were found. Don't add usages
    // of these, and remove existing usages where possible.
    // ----------------------------------------------------------------------
    
    public static final SimplePermission USE_BASIC_AJAX_CONTROLLERS = new SimplePermission(NS + "UseBasicAjaxControllers");
    public static final SimplePermission USE_MISCELLANEOUS_ADMIN_PAGES = new SimplePermission(NS + "UseMiscellaneousAdminPages");
    public static final SimplePermission USE_MISCELLANEOUS_CURATOR_PAGES = new SimplePermission(NS + "UseMiscellaneousCuratorPages");
    public static final SimplePermission USE_MISCELLANEOUS_PAGES = new SimplePermission(NS + "UseMiscellaneousPages");

    // ----------------------------------------------------------------------
    // These instances are permissions that can be specified for a given page
    // created/managed through page management,
    // e.g. this page is viewable only by admins, this page is viewable to anyone
    // who is logged in, etc.
    // ----------------------------------------------------------------------

    public static final SimplePermission PAGE_VIEWABLE_ADMIN = new SimplePermission(NS + "PageViewableAdmin");
    public static final SimplePermission PAGE_VIEWABLE_CURATOR = new SimplePermission(NS + "PageViewableCurator");
    public static final SimplePermission PAGE_VIEWABLE_LOGGEDIN = new SimplePermission(NS + "PageViewableLoggedIn");
    public static final SimplePermission PAGE_VIEWABLE_EDITOR = new SimplePermission(NS + "PageViewableEditor");
    public static final SimplePermission PAGE_VIEWABLE_PUBLIC = new SimplePermission(NS + "PageViewablePublic");
   
    public static List<SimplePermission> getAllInstances() {
        return new ArrayList<SimplePermission>(simplePermissions.values());
    }
    
    public static void add(SimplePermission permission) {
        simplePermissions.put(permission.getUri(), permission);
    }
    
    public static boolean contains(String uri) {
        return simplePermissions.containsKey(uri);
    }
}
