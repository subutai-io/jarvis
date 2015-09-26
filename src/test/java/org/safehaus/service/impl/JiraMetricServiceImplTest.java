package org.safehaus.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.service.api.JiraMetricService;


@RunWith( MockitoJUnitRunner.class )
public class JiraMetricServiceImplTest
{
	@Mock
	private Dao dao;

	@InjectMocks
	private JiraMetricService jiraMetricService = new JiraMetricServiceImpl();


	@Test
	public void testInsertJiraMetricIssue()
	{
		JiraMetricIssue jiraMetricIssue = new JiraMetricIssue();
		jiraMetricIssue.setIssueId( -2L );

		jiraMetricService.insertJiraMetricIssue( jiraMetricIssue );

		Mockito.verify( dao ).insert( jiraMetricIssue );
	}


	@Test
	public void testFindJiraMetricIssueById()
	{
		JiraMetricIssue jiraMetricIssue = new JiraMetricIssue();
		jiraMetricIssue.setIssueId( -2L );
		jiraMetricIssue.setAssigneeName( "Test" );

		Mockito.when( dao.findById( JiraMetricIssue.class, -2L ) ).thenReturn( jiraMetricIssue );

		JiraMetricIssue newIssue = jiraMetricService.findJiraMetricIssueById( -2L );

		Assert.assertNotNull( newIssue );
		Assert.assertEquals( (long) newIssue.getIssueId(), -2L );
		Assert.assertEquals( newIssue.getAssigneeName(), "Test" );

		Mockito.verify( dao ).findById( JiraMetricIssue.class, -2L );
	}

	@Test
	public void testFindJiraMetricIssuesByÅssigneeName()
	{
		List<JiraMetricIssue> issueList = new ArrayList<JiraMetricIssue>();
		issueList.add( new JiraMetricIssue() );
		issueList.add( new JiraMetricIssue() );

		Mockito.doReturn( issueList ).when( dao ).findByQuery( Matchers.anyString() );

		List<JiraMetricIssue> newList = jiraMetricService.findJiraMetricIssuesByAssigneeName( "ttest" );

		Assert.assertNotNull( newList );
		Assert.assertTrue( newList.size() > 0 );
		Assert.assertTrue( newList.size() == 2 );

		Mockito.verify( dao ).findByQuery( Matchers.anyString() );
	}


	@Test
	public void testUpdateJiraMetricIssue()
	{
		JiraMetricIssue issue = new JiraMetricIssue();
		issue.setIssueId( -2L );
		jiraMetricService.updateJiraMetricIssue( issue );

		Mockito.verify( dao ).merge( Matchers.any() );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testUpdateJiraMetricIssueException()
	{
		jiraMetricService.updateJiraMetricIssue( null );
	}


	@Test
	public void testDeleteJiraMetricIssue()
	{
		JiraMetricIssue issue = new JiraMetricIssue();
		issue.setIssueId( -2L );

		jiraMetricService.deleteJiraMetricIssue( issue );

		Mockito.verify( dao ).remove( issue );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testDeleteJiraMetricIssueException()
	{
		jiraMetricService.deleteJiraMetricIssue( new JiraMetricIssue() );
	}

}
