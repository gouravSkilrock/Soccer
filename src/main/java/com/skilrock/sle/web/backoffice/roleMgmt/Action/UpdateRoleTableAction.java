package com.skilrock.sle.web.backoffice.roleMgmt.Action;

import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.roleMgmt.controllerImpl.UpdateRoleTableControllerImpl;

public class UpdateRoleTableAction extends BaseActionWeb{
	private static final long serialVersionUID = 1L;

	public UpdateRoleTableAction() {
		super(UpdateRoleTableAction.class.getName());
	}

	public String updateRoleTable(){
		try {
				UpdateRoleTableControllerImpl controllerImpl = new UpdateRoleTableControllerImpl();
				controllerImpl.updateRoleTableWithPriviledge(request.getRemoteAddr());
		} catch (GenericException ge) {
			logger.info("ErrorCode:"+ge.getErrorCode()+" ErrorMessage:"+ge.getErrorMessage());
			return ERROR;
		} catch (Exception e) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return ERROR;
		}
		return SUCCESS;
	}
}