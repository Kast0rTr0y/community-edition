/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.share.workflow;

import java.util.List;

import org.alfresco.po.share.ShareLink;
import org.alfresco.po.share.site.document.DocumentAction;
import org.alfresco.po.share.site.document.DocumentDetailsPage;
import org.alfresco.po.share.site.document.DocumentLibraryPage;
import org.alfresco.po.share.workflow.AssignmentPage;
import org.alfresco.po.share.workflow.CloudTaskOrReviewPage;
import org.alfresco.po.share.workflow.DestinationAndAssigneePage;
import org.alfresco.po.share.workflow.KeepContentStrategy;
import org.alfresco.po.share.workflow.Priority;
import org.alfresco.po.share.workflow.SelectContentPage;
import org.alfresco.po.share.workflow.SelectedWorkFlowItem;
import org.alfresco.po.share.workflow.WorkFlowPage;
import org.alfresco.share.util.AbstractWorkflow;
import org.alfresco.share.util.ShareUser;
import org.alfresco.share.util.ShareUserSitePage;
import org.alfresco.share.util.ShareUserWorkFlow;
import org.alfresco.share.util.api.CreateUserAPI;
import org.alfresco.test.FailedTestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

/**
 * @author Bogdan.Bocancea
 */

@Listeners(FailedTestListener.class)
public class HybridCreateWorkfloFormTests extends AbstractWorkflow
{
    private String testDomain;

    /**
     * Class includes: Tests from TestLink in Area: Workflow
     */
    @Override
    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception
    {
        super.setup();
        testName = this.getClass().getSimpleName();
        testDomain = DOMAIN_HYBRID;
    }

    public void createCloudAccount(String testName) throws Exception
    {
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String[] userInfo1 = new String[] { user1 };
        String cloudUser = getUserNameForDomain(testName + "cloudUser", testDomain);
        String[] cloudUserInfo1 = new String[] { cloudUser };
        String opSite = getSiteName(testName) + "-OP";
        String fileName1 = getFileName(testName) + ".txt";

        // Create User1 (On-premise)
        CreateUserAPI.CreateActivateUser(drone, ADMIN_USERNAME, userInfo1);

        // Create User1 (Cloud)
        CreateUserAPI.CreateActivateUser(hybridDrone, ADMIN_USERNAME, cloudUserInfo1);
        CreateUserAPI.upgradeCloudAccount(hybridDrone, ADMIN_USERNAME, testDomain, "1000");

        // Login to User1, set up the cloud sync
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);
        signInToAlfrescoInTheCloud(drone, cloudUser, DEFAULT_PASSWORD);

        // create Site
        ShareUser.createSite(drone, opSite, SITE_VISIBILITY_PUBLIC);

        // upload a file
        String[] fileInfo1 = { fileName1, DOCLIB };
        ShareUser.uploadFileInFolder(drone, fileInfo1).render();
        ShareUser.logout(drone);

    }

    /**
     * AONE-15600:Form - Items
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15600() throws Exception
    {
        String testName = getTestName() + System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);

        createCloudAccount(testName);
        
        // Login as OP user
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        // Start Simple Cloud Task Workflow
        CloudTaskOrReviewPage cloudTaskOrReviewPage = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();

        // ---- Step 1 ----
        // --- Step action ---
        // Verify the Items section.
        // --- Expected results ---
        /**
         * The section contains the following controls:
         * - After completion drop-down list
         * - Lock on-premise content check-box
         * - Selected Items section
         * - Add button
         * - Remove All button
         */
        Assert.assertTrue(cloudTaskOrReviewPage.isAfterCompletionDropdownPresent(), "Completion drop-down list is not present");
        Assert.assertTrue(cloudTaskOrReviewPage.isRemoveAllButtonPresent(), "'Remove All' button is not presented");
        Assert.assertTrue(cloudTaskOrReviewPage.isNoItemsSelectedMessagePresent(), "Selected Items section doesn't contain 'No Items Selected' message");
        Assert.assertFalse(cloudTaskOrReviewPage.isLockOnPremiseSelected(), "Is lock on Premise selected by default");
        Assert.assertTrue(cloudTaskOrReviewPage.isAddButtonPresent(), "Add button is not present");

        // ---- Step 2 ----
        // --- Step action ---
        // Verify After completion drop-down list.
        // --- Expected results ---
        /**
         * The list contains the following values:
         * - 'Keep content synced on cloud',
         * - 'Keep content on cloud and remove sync',
         * - 'Delete content on cloud and remove sync'.
         */
        List<String> afterComplete = cloudTaskOrReviewPage.getAfterCompletionOptions();
        Assert.assertTrue(afterComplete.contains(KeepContentStrategy.KEEPCONTENTREMOVESYNC.getStrategy()),
                "Keep content on cloud and remove sync option is not displayed");
        Assert.assertTrue(afterComplete.contains(KeepContentStrategy.DELETECONTENT.getStrategy()),
                "Delete content on cloud and remove sync option is not displayed");
        Assert.assertTrue(afterComplete.contains(KeepContentStrategy.KEEPCONTENT.getStrategy()), "Keep content synced on cloud option is not displayed");

        // ---- Step 3 ----
        // --- Step action ---
        // Choose any value in After completion drop-down list.
        // --- Expected results ---
        // The value is chosen successfully.
        cloudTaskOrReviewPage.selectAfterCompleteDropDown(KeepContentStrategy.KEEPCONTENT);
        Assert.assertTrue(cloudTaskOrReviewPage.isAfterCompletionSelected(KeepContentStrategy.KEEPCONTENT),
                "Keep content synced on cloud option cannot be selected");

        // ---- Step 4 ----
        // --- Step action ---
        // Verify Lock on-premise content check-box.
        // --- Expected results ---
        // The check-box is unchecked by default.
        Assert.assertFalse(cloudTaskOrReviewPage.isLockOnPremiseSelected(), "Is lock on Premise selected by default");

        // ---- Step 5 ----
        // --- Step action ---
        // Try to check Lock on-premise content check-box.
        // --- Expected results ---
        // The check-box is checked successfully.
        cloudTaskOrReviewPage.selectLockOnPremiseCheckbox(true);
        Assert.assertTrue(cloudTaskOrReviewPage.isLockOnPremiseSelected(), "Is lock on Premise cannot be set to true");

        // ---- Step 6 ----
        // --- Step action ---
        // Verify Selected Items section.
        // --- Expected results ---
        // 'No items selected' message is displayed by default.
        Assert.assertTrue(cloudTaskOrReviewPage.isNoItemsSelectedMessagePresent(), "'No Items Selected' message' is not displayed");

        // ---- Step 7 ----
        // --- Step action ---
        // Verify the Add and Remove All buttons.
        // --- Expected results ---
        // Add button is enabled, Remove All button is disabled.
        Assert.assertTrue(cloudTaskOrReviewPage.isAddButtonPresent(), "Add button is not active or not displayed");
        Assert.assertFalse(cloudTaskOrReviewPage.isRemoveAllButtonEnabled(), "Remove All button is active");

        ShareUser.logout(drone);
    }

 
    /**
     * AONE-15601:Form - Items - Select Items
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15601() throws Exception
    {
        String testName = getTestName() + System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String opSite = getSiteName(testName) + "-OP";
        String fileName1 = getFileName(testName) + ".txt";

        createCloudAccount(testName);
        
        // Login as OP user
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        // ---- Step 1----
        // --- Step action ---
        // Click on Add button.
        // --- Expected results ---
        // Select window is opened. Company Home directory is opened by default.
        WorkFlowPage workflow = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();
        SelectContentPage selectPage = workflow.clickAddItems();

        // ---- Step 2 ----
        // --- Step action ---
        // Verify the available controls.
        // --- Expected results ---
        /**
         * The section contains the following controls:
         * - Folder Up picker (disabled when root directory is chosen)
         * - Navigator (contains parent directory structure)
         * - Available Items section (Directories from Company Home are displayed)
         * - Selected Items section ('No items selected' when no items are chosen)
         * - OK button
         * - Cancel button
         */
        Assert.assertFalse(selectPage.isFolderUpButtonEnabled(), "Folder Up button is enabled by default");
        Assert.assertEquals(selectPage.getNoItemsSelected(), "No items selected", "'No items selected' text is not displayed when no items are chosen");
        Assert.assertTrue(selectPage.isOkButtonPresent());
        Assert.assertTrue(selectPage.isCancelButtonPresent());
        Assert.assertTrue(selectPage.isCompanyHomeButtonPresent(), "Company button is not displayed");
        List<String> elements = selectPage.getDirectoriesLeftPanel();
        Assert.assertTrue(
                elements.contains("Data Dictionary") && elements.contains("Guest Home") && elements.contains("Imap Attachments")
                        && elements.contains("IMAP Home") && elements.contains("Shared") && elements.contains("Sites") && elements.contains("User Homes"),
                "Default directories from Company Home are NOT displayed");

        // ---- Step 3 ----
        // --- Step action ---
        // Open any directory, e.g. Sites
        // --- Expected results ---
        // The directory is opened. Folder Up picker becomes enabled.
        selectPage.clickElementOnLeftPanel("Sites");
        Assert.assertTrue(selectPage.isFolderUpButtonEnabled(), "Folder Up is not active after selection child object");

        // ---- Step 4 ----
        // --- Step action ---
        // Click on Folder Up picker.
        // --- Expected results ---
        // Company Home directory is opened again.
        selectPage.clickFolderUpButton();
        Assert.assertTrue(selectPage.isCompanyHomeButtonPresent(), "Company Home directory is not displayed after using Up button");

        // ---- Step 5 ----
        // --- Step action ---
        // Open any directory, e.g. Sites.
        // --- Expected results ---
        // The directory is opened.

        selectPage.clickElementOnLeftPanel("Sites");

        // ---- Step 6 ----
        // --- Step action ---
        // Open Company Home using Navigator.
        // --- Expected results ---
        // Company Home directory is opened again.
        selectPage.navigateToRootDir();
        Assert.assertTrue(selectPage.isCompanyHomeButtonPresent(), "Company Home is not displayed after navigation");

        // ---- Step 7 ----
        // ---- Step 8 ----
        // ---- Step 9 ----
        // --- Step action ---
        // Navigate to the directory where the added in the pre-condition document is located.
        // Click on Add icon.
        // --- Expected results ----
        // The directory is opened.
        // The document appears in the Selected Items section. Add icon disappears for the added document. Remove icon is present for the document which is
        // present in the Selected Items section.
        selectPage.addItemFromSite(fileName1, opSite);
        Assert.assertFalse(selectPage.isAddIconPresent(fileName1), "Add icon is not disabled after adding the file to workflow");
        List<String> addedItems = selectPage.getAddedItems();
        Assert.assertTrue(addedItems.contains(fileName1), "Added File is not displayed for Added section");
        Assert.assertTrue(selectPage.isRemoveIconPresent(fileName1), "Remove button is not displayed for added content");

        // ---- Step 10 ----
        // --- Step action ---
        // Click on Remove icon.
        // --- Expected results ----
        // The document disappears from the Selected Items section. Add icon appears for the removed document in the Available Items section.
        selectPage.removeItem(fileName1);
        addedItems = selectPage.getAddedItems();
        Assert.assertFalse(addedItems.contains(fileName1), "File is still displayed for added section after removing.");
        Assert.assertTrue(selectPage.isAddIconPresent(fileName1), "Add button for the file is not displayed after removing.");

        // ---- Step 11 ----
        // ---- Step action ----
        // Repeat step 9.
        // ---- Expected results -----
        // The document appears in the Selected Items section. Add icon disappears for the added document. Remove icon is present for the document which is
        // present in the Selected Items section.
        selectPage.addItemFromSite(fileName1, opSite);
        addedItems = selectPage.getAddedItems();
        Assert.assertTrue(addedItems.contains(fileName1), "Added File is not displayed for Added section");
        Assert.assertTrue(selectPage.isRemoveIconPresent(fileName1), "Remove icon is not displayed for Added file");
        Assert.assertFalse(selectPage.isAddIconPresent(fileName1), "Add icon is not disabled after adding the file to workflow");

        // ---- Step 12 ----
        // --- Step action ---
        // Click on Close button.
        // --- Expected results ----
        // The window is closed. No data was changed in Selected Items section - no document was added.
        selectPage.selectCloseButton();
        CloudTaskOrReviewPage cloudTaskOrReviewPage = new CloudTaskOrReviewPage(drone);
        Assert.assertTrue(cloudTaskOrReviewPage.isNoItemsSelectedMessagePresent(), "Content has been added after closing Select window");

        // ---- Step 13 ----
        // ---- Step action ---
        // Verify the Remove All button.
        // ---- Expected results ----
        // The button is disabled.
        Assert.assertFalse(cloudTaskOrReviewPage.isRemoveAllButtonEnabled(), "Button is enabled after closing Select window");

        // ---- Step 14 ----
        // --- Step action ---
        // Repeat steps 1-9.
        // ---- Expected results ----
        // The latest opened directory is opened in the window (step 2). The previously chosen document is displayed in the Selected Items section (step 2). The
        // document is chosen.
        selectPage = workflow.clickAddItems();
        selectPage.removeItem(fileName1);
        selectPage.addItemFromSite(fileName1, opSite);

        // ---- Step 15 ----
        // ---- Step action ----
        // Click on Cancel button.
        // ---- Expected results ----
        // The window is closed. No data was changed in Selected Items section - no document was added.
        selectPage.selectCancelButton();
        Assert.assertTrue(cloudTaskOrReviewPage.isNoItemsSelectedMessagePresent(), "Content has been added after canceling Select window");

        // ---- Step 16 ----
        // ---- Step action ----
        // Verify the Remove All button.
        // ----- Expected results ----
        // The button is disabled.
        Assert.assertFalse(cloudTaskOrReviewPage.isRemoveAllButtonEnabled(), "Button is enabled after canceling Select window");

        // ---- Step 17 ----
        // --- Step action ---
        // Repeat steps 1-9.
        // ---- Expected results ----
        // The latest opened directory is opened in the window (step 2). The previously chosen document is displayed in the Selected Items section (step 2). The
        // document is chosen.
        selectPage = workflow.clickAddItems();
        selectPage.removeItem(fileName1);
        selectPage.addItemFromSite(fileName1, opSite);

        // ---- Step 18 ----
        // --- Step action ---
        // Click on OK button.
        // ---- Expected results ----
        // The window is closed. The data was changed in Selected Items section - the document was added.
        selectPage.selectOKButton();

        List<SelectedWorkFlowItem> selectedWorkFlowItems = workflow.getSelectedItem(fileName1);
        String itemName = selectedWorkFlowItems.get(0).getItemName();
        Assert.assertEquals(itemName, fileName1, "File was not added to workflow.");

        // ---- Step 19 ----
        // --- Step action ---
        // Verify the Remove All button.
        // ---- Expected results ----
        // The button is enabled.
        Assert.assertTrue(cloudTaskOrReviewPage.isRemoveAllButtonEnabled(), "Remove all button is still disabled after addition file");
    }

    /**
     * AONE-15602:Form - Items - Items were selected
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15602() throws Exception
    {
        String testName = getTestName()+ System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String opSite = getSiteName(testName) + "-OP";
        String fileName1 = getFileName(testName) + ".txt";
        String fileName2 = "second" + getFileName(testName) + ".txt";

        createCloudAccount(testName);

        // login
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        // start workflow
        WorkFlowPage workflow = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();
        SelectContentPage selectPage = workflow.clickAddItems();

        // add new document
        selectPage.addItemFromSite(fileName1, opSite);
        selectPage.selectOKButton();

        // ---- Step 1 ----
        // --- Step action ---
        // Verify the information and controls available for the Added document.
        // ---- Expected results ----
        /**
         * The section contains the following information and controls:
         * - Document's thumbnail placeholder (depends on mimetype)
         * - Document's name (a link to Document Details page)
         * - Document's description
         * - Document's modified date
         * - View More Actions button
         * - Remove button
         */
        CloudTaskOrReviewPage cloudTaskOrReviewPage = new CloudTaskOrReviewPage(drone);
        List<SelectedWorkFlowItem> selectedWorkFlowItems = workflow.getSelectedItem(fileName1);
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy");
        Calendar cal = Calendar.getInstance();
        String current_date = dateFormat.format(cal.getTime());
        String itemName = selectedWorkFlowItems.get(0).getItemName();
        ShareLink link = selectedWorkFlowItems.get(0).getItemNameLink();
        String url = link.getHref();

        Assert.assertEquals(itemName, fileName1, "File with incorrect name has been added");
        Assert.assertTrue(cloudTaskOrReviewPage.getItemDate(fileName1).contains(current_date), "Item Date is not correct for added file");
        Assert.assertTrue(selectedWorkFlowItems.get(0).getDescription().contains("None"), "Description has been added to file");

        Assert.assertTrue(selectedWorkFlowItems.get(0).isRemoveLinkPresent(), "'Remove link is not displayed for the file");
        Assert.assertTrue(selectedWorkFlowItems.get(0).isViewMoreActionsPresent(), "View more action is not displayed for the file");

        String handle1 = drone.getWindowHandle();
        // ---- Step 2 ----
        // --- Step action ---
        // Click on the document's name to open the page in a separate tab/window (e.g. via RBC > Open Link in a new tab).
        // ---- Expected results ----
        // The document details page in opened. All data is displayed correctly. No information about Sync or Workflows is present - the document is now not a
        // part of workflow and is not yet synced to Cloud.
       drone.createNewTab();
        drone.navigateTo(url);
        DocumentDetailsPage docDetails = drone.getCurrentPage().render();
        Assert.assertTrue(docDetails.isDocumentActionPresent(DocumentAction.START_WORKFLOW), "Start workflow is not present");
        Assert.assertTrue(docDetails.getContentTitle().contains(fileName1), "Title of new tab doesn't contain info about file");
        drone.closeTab();
       // drone.find(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");

        drone.switchToWindow(handle1);

        // ---- Step 3 -----
        // --- Step action ---
        // Click on the View More Actions button to open the page in a separate tab/window (e.g. via mouse scroll button).
        // ---- Expected results ----
        // The document details page in opened. All data is displayed correctly. No information about Sync or Workflows is present - the document is now not a
        // part of workflow and is not yet synced to Cloud.
        cloudTaskOrReviewPage.selectViewMoreActionsBtn(fileName1).render();
        drone.getCurrentPage().render();
        Assert.assertTrue(docDetails.isDocumentActionPresent(DocumentAction.START_WORKFLOW), "Start workflow is not present");
        Assert.assertTrue(docDetails.getContentTitle().contains(fileName1), "Title of new tab doesn't contain info about file (via View More Actions Button)");

        workflow = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();
        selectPage = workflow.clickAddItems();

        // add new document
        selectPage.addItemFromSite(fileName1, opSite);
        selectPage.selectOKButton();

        // ---- Step 4 ----
        // --- Step action ---
        // Click on the Remove button.
        // ---- Expected results ----
        // The document is removed from the Items section.
        cloudTaskOrReviewPage.selecRemoveBtn(fileName1);
        Assert.assertTrue(cloudTaskOrReviewPage.isNoItemsSelectedMessagePresent(), "Failed is not removed from workFlow");

        // ---- Step 5 ----
        // --- Step action ---
        // The documents are selected.
        // ---- Expected results ----
        // The documents are selected.
        String[] fileInfo2 = { fileName2, DOCLIB };

        ShareUser.openSiteDashboard(drone, opSite);
        @SuppressWarnings("unused")
        DocumentLibraryPage documentLibraryPage = ShareUser.uploadFileInFolder(drone, fileInfo2).render();

        workflow = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();
        selectPage = workflow.clickAddItems();
        selectPage.addItemFromSite(fileName1, opSite);
        selectPage.selectOKButton();

        selectPage = workflow.clickAddItems();
        selectPage.addItemFromSite(fileName2, opSite);
        selectPage.selectOKButton();
        selectedWorkFlowItems = workflow.getSelectedItem(fileName1);
        itemName = selectedWorkFlowItems.get(0).getItemName();
        Assert.assertEquals(itemName, fileName1, "First file is not added to workFlow");
        selectedWorkFlowItems = workflow.getSelectedItem(fileName2);
        itemName = selectedWorkFlowItems.get(0).getItemName();
        Assert.assertEquals(itemName, fileName2, "Second file is not added to workFlow");

        // ---- Step 6 -----
        // --- Step action ---
        // Click on Remove action for any of the added document.
        // ---- Expected results ----
        // The document is removed from the Items section. All other previously added documents are still present.
        cloudTaskOrReviewPage.selecRemoveBtn(fileName1);
        Assert.assertFalse(cloudTaskOrReviewPage.isItemAdded(fileName1), "First file is not removed from workFlow");
        Assert.assertTrue(cloudTaskOrReviewPage.isItemAdded(fileName2), "Second file is removed during deletion first file");

        // ---- Step 7 ----
        // --- Step action ---
        // Click on Remove All button.
        // ---- Expected results ----
        // All other previously added documents are removed.
        cloudTaskOrReviewPage.selectRemoveAllButton();
        Assert.assertFalse(cloudTaskOrReviewPage.isItemAdded(fileName2), "Remove All button doesn't work");
    }

    /**
     * AONE-15603:Negative case - Destination is not specified
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15603() throws Exception
    {
        String testName = getTestName() + System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String opSite = getSiteName(testName) + "-OP";
        String fileName1 = getFileName(testName) + ".txt";
        String dueDate = getDueDateString();

        createCloudAccount(testName);

        // Login as OP user
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        ShareUser.openSiteDocumentLibraryFromSearch(drone, opSite);

        // Start Simple Cloud Task Workflow
        CloudTaskOrReviewPage cloudTaskOrReviewPage = ShareUserWorkFlow.startWorkFlowFromDocumentLibraryPage(drone, fileName1).render();

        // ---- Step 1 ----
        // --- Step action ---
        // Fill in all the fields except Destination
        // --- Expected results ---
        // Performed correctly.
        cloudTaskOrReviewPage.enterDueDateText(dueDate);
        cloudTaskOrReviewPage.selectLockOnPremiseCheckbox(true);
        cloudTaskOrReviewPage.selectPriorityDropDown(Priority.MEDIUM);

        // ---- Step 2 ----
        // --- Step action ---
        // Click on Start Workflow button
        // --- Expected results ---
        // 'The value cannot be empty.' baloon pop-up is displayed above the Destination section.
        cloudTaskOrReviewPage.clickStartWorkflow();
        Assert.assertTrue(cloudTaskOrReviewPage.isErrorBalloonPresent(), "Baloon error is not displayed when Destination is not specified");
        Assert.assertEquals(cloudTaskOrReviewPage.getErrorBalloonMessage(), "The value cannot be empty.");

        ShareUser.logout(drone);
    }

    /**
     * AONE-15604:Form - Items
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15604() throws Exception
    {
        String testName = getTestName() + System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String cloudUser = getUserNameForDomain(testName + "cloudUser", testDomain);
        String cloudSite = getSiteName(testName) + "CL" + "-3";
        String opSite = getSiteName(testName) + "-OP";
        String fileName1 = getFileName(testName) + ".txt";
        String dueDate = getDueDateString();

        createCloudAccount(testName);

        ShareUser.login(hybridDrone, cloudUser, DEFAULT_PASSWORD);
        ShareUser.createSite(hybridDrone, cloudSite, SITE_VISIBILITY_PUBLIC);
        ShareUser.logout(hybridDrone);

        // Login as OP user
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        ShareUser.openSiteDocumentLibraryFromSearch(drone, opSite);

        // Start Simple Cloud Task Workflow
        CloudTaskOrReviewPage cloudTaskOrReviewPage = ShareUserWorkFlow.startWorkFlowFromDocumentLibraryPage(drone, fileName1).render();

        // ---- Step 1 ----
        // --- Step action ---
        // Fill in all the fields except Assignee/Reviewers.
        // --- Expected results ---
        /**
         * Performed correctly.
         */
        cloudTaskOrReviewPage.selectAfterCompleteDropDown(KeepContentStrategy.KEEPCONTENT);
        cloudTaskOrReviewPage.enterDueDateText(dueDate);
        cloudTaskOrReviewPage.selectLockOnPremiseCheckbox(true);
        cloudTaskOrReviewPage.selectPriorityDropDown(Priority.HIGH);
        DestinationAndAssigneePage destinationAndAssigneePage = cloudTaskOrReviewPage.selectDestinationAndAssigneePage().render();
        destinationAndAssigneePage.selectSite(cloudSite);
        destinationAndAssigneePage.selectSubmitButtonToSync();

        // ---- Step 2 ----
        // --- Step action ---
        // Click on Start Workflow button
        // --- Expected results ---
        // 'The value cannot be empty.' balloon pop-up is displayed above the the Assignee/Reviewers section.
        cloudTaskOrReviewPage.clickStartWorkflow();
        Assert.assertTrue(cloudTaskOrReviewPage.isErrorBalloonPresent(), "Baloon error is not displayed when Assignee is not specified");
        Assert.assertEquals(cloudTaskOrReviewPage.getErrorBalloonMessage(), "The value cannot be empty.");
    }

    /**
     * AONE-15605:Form - Items
     */
    @Test(groups = "Hybrid", enabled = true)
    public void AONE_15605() throws Exception
    {
        String testName = getTestName() + System.currentTimeMillis();
        String user1 = getUserNameForDomain(testName + "opUser", testDomain);
        String cloudUser = getUserNameForDomain(testName + "cloudUser", testDomain);
        String cloudSite = getSiteName(testName) + "CL" + "-2";
        String dueDate = getDueDateString();
        String folderName = getFolderName(testName);

        createCloudAccount(testName);

        ShareUser.login(hybridDrone, cloudUser, DEFAULT_PASSWORD);
        ShareUser.createSite(hybridDrone, cloudSite, SITE_VISIBILITY_PUBLIC);
        ShareUserSitePage.createFolder(hybridDrone, folderName, folderName);
        ShareUser.logout(hybridDrone);

        // Login as OP user
        ShareUser.login(drone, user1, DEFAULT_PASSWORD);

        // Start Simple Cloud Task Workflow
        CloudTaskOrReviewPage cloudTaskOrReviewPage = ShareUserWorkFlow.startCloudReviewTaskWorkFlow(drone).render();

        // ---- Step 1 ----
        // --- Step action ---
        // Fill in all the fields except Items.
        // --- Expected results ---
        // Performed correctly.

        cloudTaskOrReviewPage.selectAfterCompleteDropDown(KeepContentStrategy.KEEPCONTENT);
        Assert.assertTrue(cloudTaskOrReviewPage.isAfterCompletionSelected(KeepContentStrategy.KEEPCONTENT));
        Assert.assertFalse(cloudTaskOrReviewPage.isLockOnPremiseSelected());

        cloudTaskOrReviewPage.selectLockOnPremiseCheckbox(true);
        Assert.assertTrue(cloudTaskOrReviewPage.isLockOnPremiseSelected());

        cloudTaskOrReviewPage.enterDueDateText(dueDate);
        cloudTaskOrReviewPage.selectPriorityDropDown(Priority.HIGH);

        DestinationAndAssigneePage destinationAndAssigneePage = cloudTaskOrReviewPage.selectDestinationAndAssigneePage().render();
        destinationAndAssigneePage.selectSite(cloudSite);
        destinationAndAssigneePage.selectSubmitButtonToSync();
        AssignmentPage assignmentPage = cloudTaskOrReviewPage.selectAssignmentPage().render();
        assignmentPage.selectAssignee(cloudUser);

        // ---- Step 2 ----
        // --- Step action ---
        // Click on Start Workflow button
        // --- Expected results ---
        // Workflow could not be started' dialog with the message '08150481 At least one content item is required to start a Cloud workflow' is displayed.
        cloudTaskOrReviewPage.clickStartWorkflow();
        String error = cloudTaskOrReviewPage.getWorkFlowCouldNotBeStartedPromptMessage();
        Assert.assertTrue(error.contains("At least one content item is required to start a Cloud workflow"), "Error is not displayed when no one file is added");

    }
}
