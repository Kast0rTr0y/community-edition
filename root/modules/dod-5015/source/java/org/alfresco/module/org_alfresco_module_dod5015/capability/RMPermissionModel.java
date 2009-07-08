/*
 * Copyright (C) 2005-2009 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.module.org_alfresco_module_dod5015.capability;

/**
 * Capability constants for the RM Permission Model
 * 
 * @author andyh
 */
public interface RMPermissionModel
{
    public static final String DECLARE_RECORDS = "DeclareRecords";

    public static final String VIEW_RECORDS = "ViewRecords";

    public static final String CREATE_MODIFY_DESTROY_FOLDERS = "CreateModifyDestroyFolders";

    public static final String EDIT_RECORD_METADATA = "EditRecordMetadata";

    public static final String EDIT_NON_RECORD_METADATA = "EditNonRecordMetadata";

    public static final String ADD_MODIFY_EVENT_DATES = "AddModifyEventDates";

    public static final String CLOSE_FOLDERS = "CloseFolders";

    public static final String DECLARE_RECORDS_IN_CLOSED_FOLDERS = "DeclareRecordsInClosedFolders";

    public static final String RE_OPEN_FOLDERS = "ReOpenFolders";

    public static final String CYCLE_VITAL_RECORDS = "CycleVitalRecords";

    public static final String PLANNING_REVIEW_CYCLES = "PlanningReviewCycles";

    public static final String UPDATE_TRIGGER_DATES = "UpdateTriggerDates";

    public static final String CREATE_MODIFY_DESTROY_EVENTS = "CreateModifyDestroyEvents";

    public static final String MANAGE_ACCESS_RIGHTS = "ManageAccessRights";

    public static final String MOVE_RECORDS = "MoveRecords";

    public static final String CHANGE_OR_DELETE_REFERENCES = "ChangeOrDeleteReferences";

    public static final String CHANGE_OR_DELETE_LINKS = "ChangeOrDeleteLinks";

    public static final String EDIT_DECLARED_RECORD_METADATA = "EditDeclaredRecordMetadata";

    public static final String MANUALLY_CHANGE_DISPOSITION_DATES = "ManuallyChangeDispositionDates";

    public static final String APPROVE_RECORDS_SCHEDULED_FOR_CUTOFF = "ApproveRecordsScheduledForCutoff";

    public static final String CREATE_MODIFY_RECORDS_IN_CUTOFF_FOLDERS = "CreateModifyRecordsInCutoffFolders";

    public static final String EXTEND_RETENTION_PERIOD_OR_FREEZE = "ExtendRetentionPeriodOrFreeze";

    public static final String UNFREEZE = "Unfreeze";

    public static final String VIEW_UPDATE_REASONS_FOR_FREEZE = "ViewUpdateReasonsForFreeze";

    public static final String DESTROY_RECORDS_SCHEDULED_FOR_DESTRUCTION = "DestroyRecordsScheduledForDestruction";

    public static final String DESTROY_RECORDS = "DestroyRecords";

    public static final String UPDATE_VITAL_RECORD_CYCLE_INFORMATION = "UpdateVitalRecordCycleInformation";

    public static final String UNDECLARE_RECORDS = "UndeclareRecords";

    public static final String DECLARE_AUDIT_AS_RECORD = "DeclareAuditAsRecord";

    public static final String DELETE_AUDIT = "DeleteAudit";

    public static final String CREATE_MODIFY_DESTROY_TIMEFRAMES = "CreateModifyDestroyTimeframes";

    public static final String AUTHORIZE_NOMINATED_TRANSFERS = "AuthorizeNominatedTransfers";

    public static final String EDIT_SELECTION_LISTS = "EditSelectionLists";

    public static final String AUTHORIZE_ALL_TRANSFERS = "AuthorizeAllTransfers";

    public static final String CREATE_MODIFY_DESTROY_FILEPLAN_METADATA = "CreateModifyDestroyFileplanMetadata";

    public static final String CREATE_AND_ASSOCIATE_SELECTION_LISTS = "CreateAndAssociateSelectionLists";

    public static final String ATTACH_RULES_TO_METADATA_PROPERTIES = "AttachRulesToMetadataProperties";

    public static final String CREATE_MODIFY_DESTROY_FILEPLAN_TYPES = "CreateModifyDestroyFileplanTypes";

    public static final String CREATE_MODIFY_DESTROY_RECORD_TYPES = "CreateModifyDestroyRecordTypes";

    public static final String MAKE_OPTIONAL_PARAMETERS_MANDATORY = "MakeOptionalParametersMandatory";

    public static final String MAP_EMAIL_METADATA = "MapEmailMetadata";

    public static final String DELETE_RECORDS = "DeleteRecords";

    public static final String TRIGGER_AN_EVENT = "TriggerAnEvent";

    public static final String CREATE_MODIFY_DESTROY_ROLES = "CreateModifyDestroyRoles";

    public static final String CREATE_MODIFY_DESTROY_USERS_AND_GROUPS = "CreateModifyDestroyUsersAndGroups";

    public static final String PASSWORD_CONTROL = "PasswordControl";

    public static final String ENABLE_DISABLE_AUDIT_BY_TYPES = "EnableDisableAuditByTypes";

    public static final String SELECT_AUDIT_METADATA = "SelectAuditMetadata";

    public static final String DISPLAY_RIGHTS_REPORT = "DisplayRightsReport";

    public static final String ACCESS_AUDIT = "AccessAudit";

    public static final String EXPORT_AUDIT = "ExportAudit";

    public static final String CREATE_MODIFY_DESTROY_REFERENCE_TYPES = "CreateModifyDestroyReferenceTypes";

    public static final String UPDATE_CLASSIFICATION_DATES = "UpdateClassificationDates";

    public static final String CREATE_MODIFY_DESTROY_CLASSIFICATION_GUIDES = "CreateModifyDestroyClassificationGuides";

    public static final String UPGRADE_DOWNGRADE_AND_DECLASSIFY_RECORDS = "UpgradeDowngradeAndDeclassifyRecords";

    public static final String UPDATE_EXEMPTION_CATEGORIES = "UpdateExemptionCategories";

    public static final String MAP_CLASSIFICATION_GUIDE_METADATA = "MapClassificationGuideMetadata";

    public static final String MANAGE_ACCESS_CONTROLS = "ManageAccessControls";
}
