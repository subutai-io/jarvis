package jarvis.workflow.plugin.service;


import java.util.Collection;

import com.atlassian.crowd.embedded.api.Group;

import jarvis.workflow.plugin.domain.IssueWrapper;


public interface IPluginSettingsService
{
    //@formatter:off
    String PLUGIN_NAMESPACE     = "JARVIS_PLUGIN.";
    String APPROVING_GROUPS_KEY = "APPROVING_GROUPS";
    //@fomatter:on

    void storeInfo( String key, String value );

    String getInfo( String key );

    void storeInfo( String projectKey, String key, String value );

    Object getInfo( String projectKey, String key );

    void storeInfo( String key, Collection<Group> collection );

    void storeInfo(String key, Object object);

    void storeInfo(IssueWrapper issueWrapper);
}
